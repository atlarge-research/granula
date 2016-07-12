function envView(job, envTab) {

    if(job.env) {
        var cardRow1 = $('<div class="card-group"></div>');


        var startTime = job.meta.startTime;
        var endTime = job.meta.endTime;

        var interval = conf.interval;
        while((endTime - startTime) / interval > 300) {
            interval = interval * 10;
            if(interval >= 10000) {
                break;
            }
        }

        var tabAttrs = envTabAttrs(job.env, startTime, endTime, interval, "env");
        envTabMap = list2Map(tabAttrs, "id");

        var startTime = job.meta.startTime;
        var endTime = job.meta.endTime;

        envTab.append(envTabs(startTime, endTime, "env", envTabMap));
        envTab.append(cardRow1);
    } else {
        
    }
}


function envTabAttrs(env, startTime, endTime, interval, tabGroupId) {

    var dataList = env;

    var cpuConf = {title:"Cpu Time", xLabel:"Execution Time (s)", yLabel:"Usage (cpu sec/s)", interval:interval};
    var cpuData = chartData(filterMetricDataList(dataList, "cpu", interval), startTime, endTime, interval, 1, true);
    var cpuChart = {id: "cputime", name: "CPU", conf: cpuConf, data: cpuData};

    var cpuTab = {id: tabGroupId+ "-cpu", name: "CPU", charts: [cpuChart]};


    var memRssConf = {title:"Memory RSS", xLabel:"Execution Time (s)", yLabel:"Usage (GB)", interval:interval};
    var memRssData = chartData(filterMetricDataList(dataList, "mem-rss", interval), startTime, endTime, interval, 1000000, false);
    var memRssChart = {id: "memRss", name: "Memory", conf: memRssConf, data: memRssData};

    var memSwapConf = {title:"Swap", xLabel:"Execution Time (s)", yLabel:"Usage (GB)", interval:interval};
    var memSwapData = chartData(filterMetricDataList(dataList, "mem-swap", interval), startTime, endTime, interval, 1000000, false);
    var memSwapChart = {id: "memSwap", name: "Swap", conf: memSwapConf, data: memSwapData};

    var memTab = {id: tabGroupId + "-mem", name: "Memory", charts: [memRssChart, memSwapChart]};


    // var netEthSndConf = {title:"Network Send - Ethernet", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    // var netEthSndData = chartData(filterMetricDataList(dataList, "net-eth-snd", interval), startTime, endTime, interval, 1000000, true);
    // var netEthSndChart = {id: "netEthSnd", name: "Network-Snd", conf: netEthSndConf, data: netEthSndData};
    //
    // var netEthRecConf = {title:"Network Receive - Ethernet", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    // var netEthRecData = chartData(filterMetricDataList(dataList, "net-eth-rec", interval), startTime, endTime, interval, 1000000, true);
    // var netEthRecChart = {id: "netEthRec", name: "Network-Rec", conf: netEthRecConf, data: netEthRecData};
    //
    //
    // var netIbSndConf = {title:"Network Send - Infiniband", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    // var netIbSndData = chartData(filterMetricDataList(dataList, "net-ib-snd", interval), startTime, endTime, interval, 1000000, true);
    // var netIbSndChart = {id: "netIbSnd", name: "Network-Snd", conf: netIbSndConf, data: netIbSndData};
    //
    // var netIbRecConf = {title:"Network Receive - Infiniband", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    // var netIbRecData = chartData(filterMetricDataList(dataList, "net-ib-rec", interval), startTime, endTime, interval, 1000000, true);
    // var netIbRecChart = {id: "netIbRec", name: "Network-Rec", conf: netIbRecConf, data: netIbRecData};
    //
    // var netTab = {id: tabGroupId+ "-net", name: "Network", charts: [netEthSndChart, netEthRecChart, netIbSndChart, netIbRecChart]};


    var netSndConf = {title:"Network Send", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    var netSndData = chartData(filterMetricDataList(dataList, "net-snd", interval), startTime, endTime, interval, 1000000, true);
    var netSndChart = {id: "netIbSnd", name: "Network-Snd", conf: netSndConf, data: netSndData};

    var netRecConf = {title:"Network Receive", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    var netRecData = chartData(filterMetricDataList(dataList, "net-rec", interval), startTime, endTime, interval, 1000000, true);
    var netRecChart = {id: "netIbRec", name: "Network-Rec", conf: netRecConf, data: netRecData};

    var netTab = {id: tabGroupId+ "-net", name: "Network", charts: [netSndChart, netRecChart]};

    var stgRcharConf = {title:"Disk Read ", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    var stgRcharData = chartData(filterMetricDataList(dataList, "dsk-rchar", interval), startTime, endTime, interval, 1000000, true);
    var stgRcharChart = {id: "dskRchar", name: "Disk-Read", conf: stgRcharConf, data: stgRcharData};

    var stgWcharConf = {title:"Disk Write ", xLabel:"Execution Time (s)", yLabel:"Volume (MB/s)", interval:interval};
    var stgWcharData = chartData(filterMetricDataList(dataList, "dsk-wchar", interval), startTime, endTime, interval, 1000000, true);
    var stgWcharChart = {id: "dskWchar", name: "Disk-Write", conf: stgWcharConf, data: stgWcharData};

    var stgTab = {id: tabGroupId+ "-stg", name: "Storage", charts: [stgRcharChart, stgWcharChart]};

    return [cpuTab, memTab, netTab, stgTab];
}

function envTabs(startTime, endTime, tabGroupId, tabAttrMap) {


    var tabDiv = $('<div role="tabpanel">');
    var tabList = $('<ul class="nav nav-tabs" role="tablist" />');
    var tabContent = $('<div class="tab-content" />');

    tabDiv.append(tabList);
    tabDiv.append(tabContent);


    tabList.append('<li class="nav-item">' +
        '<a class="nav-link" data-toggle="tab" href="#'+tabGroupId+'-summary-tab" role="tab">All</a>' +
        '</li>');

    var content = $('<div class="tab-pane active" id="'+tabGroupId+'-summary-tab" role="tabpanel"></div>');
    tabContent.append(content);
    content.append(envSummaryCard(startTime, endTime));

    for(var tabId in tabAttrMap) {

        var tabAttr = tabAttrMap[tabId];
        tabList.append('<li class="nav-item">' +
            '<a class="nav-link" data-toggle="tab" tab-id="'+tabAttr.id+'" href="#'+tabGroupId+'-'+tabAttr.id+'-tab" role="tab">'+tabAttr.name+'</a>' +
            '</li>');

        var content = $('<div class="tab-pane " id="'+tabGroupId+'-'+tabAttr.id+'-tab" role="tabpanel"></div>');

        tabAttr.charts.forEach(function (chart) {
            content.append(areaChartCard(tabGroupId+'-'+chart.id+'-chart', tabAttr));
        });

        tabContent.append(content);
    }

    tabDiv.find('[data-toggle = "tab"]').on('shown.bs.tab', function (e) {
        try{
            var tabId = $(e.target).attr('tab-id');

            if(tabId != null) {
                var thismetric = tabAttrMap[tabId];
                thismetric.charts.forEach(function (chart) {
                    areaChart('#'+tabGroupId+'-'+chart.id+"-chart", chart.conf, chart.data);
                })
            }
        }catch(err){
            logging("" + err);
        }
    });

    tabDiv.find('ul').insertAfter(tabDiv.find('.tab-content'));

    return tabDiv;
}


function envSummaryCard(startTime, endTime) {
    var card = $('<div class="card" />');
    var table = divTable();
    table.append(caption(""));


    var headerCard = divCard(11);
    var headerTable = divTable();
    headerTable.append($('<tr>' +
        '<td class="col-md-3">Name</td><td class="col-md-2">Measured value</td>' +
        '<td class="col-md-2">Theoretical Peak</td><td class="col-md-3">Percentage</td>' +
        '</tr>'));

    var cardRow0 = $('<div class="card-group"></div>');
    table.append(cardRow0);
    cardRow0.append(divCard(1).append(""));
    cardRow0.append(headerCard.append(headerTable));

    var cardRow1 = $('<div class="card-group"></div>');
    var cardRow2 = $('<div class="card-group"></div>');
    var cardRow3 = $('<div class="card-group"></div>');

    cardRow1.append(divCard(1).append("CPU"));
    cardRow1.append(cpuSummaryCard(startTime, endTime));
    cardRow2.append(divCard(1).append("Memory"));
    cardRow2.append(memSummaryCard(startTime, endTime));
    cardRow3.append(divCard(1).append("Network"));
    cardRow3.append(networkSummaryCard(startTime, endTime));

    table.append(cardRow1);
    table.append(cardRow2);
    table.append(cardRow3);
    card.append(table);
    return card;
}


function cpuSummaryCard(startTime, endTime) {

    var cpuMetricDataList = filterMetricDataList(job.env, "cpu", conf.interval);
    var aggMetricValues = aggregatedMetricValues(cpuMetricDataList);
    var cpuUsage = diffMetricValues(aggMetricValues, startTime, endTime);
    var cpuMax = cpuMetricDataList.length * (endTime - startTime) / 1000 * 32;

    var card = divCard(11);
    var table = divTable();
    card.append(table);
    table.append(resTableRow("CpuTime (all nodes, all cores)", cpuUsage,  "s", cpuMax));

    return card;
}


function memSummaryCard(startTime, endTime) {



    var memMetricDataList = filterMetricDataList(job.env, "mem", conf.interval);
    var aggMetricValues = aggregatedMetricValues(memMetricDataList);
    var memAvgAll = avgMethod(extractValues(aggMetricValues, startTime, endTime)) / 1000000;
    var memMaxAll = maxMethod(extractValues(aggMetricValues, startTime, endTime)) / 1000000;
    var memMaxNode = memMetricDataList
        .map(function (metricData) {
            return maxMethod(extractValues(metricData.data, startTime, endTime)) / 1000000;
        }).reduce(function (a, b) {
            return (a > b) ? a : b;
        });

    var memPeakAll = memMetricDataList.length * 64;
    var memPeakNode = 64;

    var card = divCard(11);
    var table = divTable();
    card.append(table);
    table.append(resTableRow("MemAvg (all nodes)", memAvgAll,  "GB", memPeakAll));
    table.append(resTableRow("MemMax (all nodes)", memMaxAll,  "GB", memPeakAll));
    table.append(resTableRow("MemMax (any nodes)", memMaxNode,  "GB", memPeakNode));

    return card;

}



function networkSummaryCard(startTime, endTime) {

    var netIbSndMetricDataList = filterMetricDataList(job.env, "net-ib-snd", conf.interval);
    var aggMetricValues = aggregatedMetricValues(netIbSndMetricDataList);
    var netIBSnd = diffMetricValues(aggMetricValues, startTime, endTime) / Math.pow(1000, 3);
    var netIBPeak = netIbSndMetricDataList.length * (endTime - startTime) / 1000 * 1.25;

    var aggIbRecValues = aggregatedMetricValues(filterMetricDataList(job.env, "net-ib-rec", 1000));
    var netIBRec = diffMetricValues(aggIbRecValues, startTime, endTime) / Math.pow(1000, 3);

    var card = divCard(11);
    var table = divTable();
    card.append(table);
    table.append(resTableRow("NetVol (SEND, all nodes)", netIBSnd,  "GB", netIBPeak));
    table.append(resTableRow("NetVol (REC, all nodes)", netIBRec,  "GB", netIBPeak));

    return card;

}



function areaChartCard(chartName, tabAttr) {
    var chartCard = $('<div class="card" />');
    var chart = $('<div class="card-block" ><svg id="'+chartName+'"  viewBox="0 0 1000 270" height="300" width="100%"></svg></div>');
    // var text = "interval = " +  tabAttr.chartAttr.interval;
    chartCard.append(chart);
    // chartCard.append(text);

    return chartCard;
}
