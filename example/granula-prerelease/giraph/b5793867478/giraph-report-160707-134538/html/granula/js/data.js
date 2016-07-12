var conf = {};
conf.interval = 100;


envTabMap = {};
operEnvTabMap = {};

function convertData(jobdata) {
    var metric = new Object();
    metric.nodeId = jobdata.key.split("/")[0];
    metric.processId = jobdata.key.split("/")[1];
    metric.type = jobdata.key.split("/")[2].split("_")[0];
    metric.interval = jobdata.key.split("/")[2].split("_")[1].split("ms")[0];
    metric.data = jobdata;
    return metric;
}


function reduceData(jobdata) {
    var newdata = {};
    newdata.key = jobdata.key;
    newdata.values = jobdata.values.filter(function (dp) {
        return dp[0] >= job.meta.startTime && dp[0] <= job.meta.endTime;
    });
    return newdata;
};

function demagnitude(jobdata, magnitude) {
    var newdata = {};
    newdata.key = jobdata.key;
    newdata.values = jobdata.values.map(function (dp) {
        return [dp[0], dp[1] / magnitude];
    });
    return newdata;
};

//-------------------new-----------------------------
function metricData(jobdata, magnitude) {
    var metricData = {};
    metricData.entity = jobdata.key.split("/")[0] + "/" + jobdata.key.split("/")[1];
    metricData.type = jobdata.key.split("/")[2].split("_")[0];
    metricData.interval = jobdata.key.split("/")[2].split("_")[1].split("ms")[0];
    metricData.data =  kvList2Map(jobdata.values);
    metricData.magnitude = magnitude;
    return metricData;
}


function filterMetricDataList(dataList, type, interval) {
    return dataList.
        filter(function (metricData) {
        if(interval) {
            return metricData.type.indexOf(type) > -1
                && metricData.interval == interval
        } else {
            return metricData.type.indexOf(type) > -1
        }
    });
}

function diffMetricValues(metricValues, startTime, endTime) {
    var count = 0;
    var minTimestamp = Number.MAX_VALUE;
    var maxTimestamp = Number.MIN_VALUE;

    for (var timestamp in metricValues) {
        minTimestamp = (timestamp < minTimestamp && timestamp > startTime) ? timestamp : minTimestamp;
        maxTimestamp = (timestamp > maxTimestamp && timestamp < endTime) ? timestamp : maxTimestamp;
        count++;
    }

    return (count > 0) ? parseFloat(metricValues[maxTimestamp]) - parseFloat(metricValues[minTimestamp]) : null;

}

function extractValues(oValues, startTime, endTime) {

    var nValues = {};

    for (var timestamp in oValues) {
        if((timestamp < endTime && timestamp > startTime)) {
            nValues[timestamp] = oValues[timestamp];
        }
    }
    return nValues;
}

function avgMethod(values) {
    var sum = 0;
    var count = 0;

    for (var timestamp in values) {
            sum += parseFloat(values[timestamp]);
            count++;
    }


    return (count > 0) ? (sum / count) : null;
}

function maxMethod(values) {
    var count = 0;
    var value = Number.MIN_VALUE;

    for (var timestamp in values) {
        value = (parseFloat(values[timestamp]) > parseFloat(value)) ? values[timestamp] : value;
        count++;
    }
    return (count > 0) ? value : null;
}

function minMethod(values) {
    var count = 0;
    var value = Number.MAX_VALUE;

    for (var timestamp in values) {
        value = (parseFloat(values[timestamp]) < parseFloat(value)) ? values[timestamp] : value;
        count++;
    }
    return (count > 0) ? value : null;
}



function aggregatedMetricValues(metricDataList) {
    var aggValues = {};
    var aggCount = {};

    metricDataList.forEach(function (metricData) {
        for (var timestamp in metricData.data) {
            if(aggValues[timestamp]) {
                aggValues[timestamp] += parseFloat(metricData.data[timestamp]);
                aggCount[timestamp] += 1;
            } else {
                aggValues[timestamp] = parseFloat(metricData.data[timestamp]);
                aggCount[timestamp] = 1;
            }
        }
    });

    var realAggValues = {}
    for (var timestamp in aggCount) {
        if (aggCount[timestamp] == metricDataList.length) {
            realAggValues[timestamp] = aggValues[timestamp];
        }
    }

    return realAggValues;
}


function countedValues(metricDataList) {
    var aggCount = {};

    metricDataList.forEach(function (metricData) {
        for (var timestamp in metricData.data) {
            if(aggCount[timestamp]) {
                aggCount[timestamp] += 1;
            } else {
                aggCount[timestamp] = 1;
            }
        }
    });

    return aggCount;
}


function reduceDatapoints(dataList, startTime, endTime) {
    return dataList.map(function (completeData) {
        var metricData = {};
        metricData.entity = completeData.entity;
        metricData.type = completeData.type;
        metricData.interval = completeData.interval;
        metricData.data = {};
        for (var timestamp in completeData.data) {
            if(timestamp > startTime && timestamp < endTime) {
                metricData.data[timestamp] = completeData.data[timestamp];
            }
        }
        return metricData;
    });
}

function chartData(dataList, startTime, endTime, interval, magnitude, cumulative) {

    var aggValues = countedValues(dataList);
    return dataList.map(function (metricData) {
        var values = {};

        for (var timestamp in metricData.data) {
            if(timestamp > startTime && timestamp < endTime) {
                values[timestamp] = metricData.data[timestamp];
            }
        }
        // values = rmAbsence(values, startTime, endTime, interval, aggValues);
        values = (cumulative) ? decumulative2(values) : values;
        values = fillGap(values, startTime, endTime, interval, aggValues);
        values = setRange(values, startTime, endTime);

        values = map2KVList(values).map(function (value) {
            return [value[0] - startTime, value[1] * metricData.magnitude / magnitude];
        });

        return {key:metricData.entity, values:values};
    });
}


function rmAbsence(gValues, startTime, endTime, interval, aggValues) {
    var fValues = {};

    var start = parseInt(startTime) + (interval - startTime % interval);
    var end = parseInt(endTime) - endTime % interval;

    for (var i = start; i <= end; i = i + interval) {
        if(aggValues[i]) {
            fValues[i] = gValues[i];
        }
    }
    return fValues;
};


function fillGap(gValues, startTime, endTime, interval, countedValues) {
    var fValues = {};

    var start = parseInt(startTime) + (interval - startTime % interval);
    var end = parseInt(endTime) - endTime % interval;

    for (var i = start; i <= end; i = i + interval) {
        if(gValues[i]) {
            fValues[i] = gValues[i];
        } else {
            if(countedValues[i] >= 1) {
                fValues[i] = 0.0;
            }
        }
    }
    return fValues;
};

function setRange(gValues, startTime, endTime) {
    var fValues = {};

    var minTimestamp = Number.MAX_VALUE;
    var maxTimestamp = Number.MIN_VALUE;
    var count = 0;

    for (var i in gValues) {
        minTimestamp = Math.min(minTimestamp, i);
        maxTimestamp = Math.max(maxTimestamp, i);
        count++;
    }

    fValues[startTime] = (gValues[startTime]) ? gValues[startTime] : 0.0; // improve later by interpolation

    if(count > 0) {
        fValues[minTimestamp - 1] = 0.0;
    }

    for (var i in gValues) {
        fValues[i] = gValues[i];
    }

    if(count > 0) {
        fValues[maxTimestamp + 1] = 0.0;
    }

    fValues[endTime] = (gValues[endTime]) ? gValues[endTime] : 0.0; // improve later by interpolation

    return fValues;
}

function decumulative2(cValues) {
    var dValues = {};

    var diff;
    var currValue;
    var prevValue;
    var prevTimestamp;
    for (var timestamp in cValues) {
        currValue = cValues[timestamp];

        diff = (prevValue) ? (currValue - prevValue) / (timestamp - prevTimestamp) * 1000.0: 0;
        dValues[timestamp] = diff;
        prevValue = currValue;
        prevTimestamp = timestamp;
    }
    return dValues;
}


//-------------------new-----------------------------




function decumulative(data) {
    var newdata = {};

    newdata.key = data.key;

    var newValues =  [];
    var oldValues = data.values;
    newValues[0] = [oldValues[0][0], 0];
    for (var i = 1; i < oldValues.length; i++) {
        var dff = (oldValues[i][1] - oldValues[i - 1][1])
            / (oldValues[i][0] - oldValues[i - 1][0]) * 1000;
        newValues[i] = [oldValues[i][0], dff];
    }

    newdata.values = newValues;
    return newdata;
}


function list2Map(list, id) {
    var map = {};
    list.forEach(function (item) {
        map[item[id]] = item;
    });
    return map;
}

function kvList2Map(list, id) {
    var map = {};
    list.forEach(function (item) {
        map[item[0]] = item[1];
    });
    return map;
}


function map2List(map) {
    var list = [];
    for (var item in map) {
        list.push(map[item]);
    }
    return list;
}

function map2KVList(map) {
    var list = [];
    for (var item in map) {
        list.push([item, map[item]]);
    }
    return list;
}