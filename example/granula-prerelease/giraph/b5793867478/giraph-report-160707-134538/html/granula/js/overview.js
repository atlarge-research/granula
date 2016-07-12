
function jobOverview(job, overviewTab) {

    if(job.meta) {
        var cardRow1 = $('<div class="card-group"></div>');
        var cardRow3 = $('<div class="card-group"></div>');
        var cardRow2 = $('<div class="card-group"></div>');
        cardRow1.append(descriptionCard(job));
        cardRow1.append(parameterCard(job));

        cardRow2.append(durationCard(job));
        cardRow2.append(breakDownTableCard(job));

        if(job.system) {
            cardRow3.append(arcCard(job));
            cardRow3.append(emptyCard(job));
        }

        overviewTab.append(cardRow1);
        overviewTab.append(cardRow2);
        overviewTab.append(cardRow3);
    } else {
        var errCard = errorCard();
        var text = $('<p>'+"Execution data not available for this job. Verify if meta-arc.js exists"+'</p>');
        errCard.append(text);

        overviewTab.append(errCard);
    }

}

function arcCard(job) {

    var arcCard = $('<div class="card"></div>');

    var operationSize = 0;
    var infoSize = 0;
    var actorSize = 0;
    var missionSize = 0;
    for (var i in job.system.operations) { operationSize++; }
    for (var i in job.system.infos) { infoSize++; }
    for (var i in job.system.actors) { actorSize++; }
    for (var i in job.system.missions) { missionSize++; }

    var countTable = divTable();
    countTable.append($(caption("Job Archive")));
    countTable.append(tableRow("#Operations", operationSize));
    countTable.append(tableRow("#Actors", actorSize));
    countTable.append(tableRow("#Missions", missionSize));
    countTable.append(tableRow("#Infos", infoSize));

    arcCard.append(countTable);

    return arcCard;
}

function descriptionCard(job) {

    var descriptionCard = $('<div class="card"></div>');

    var descriptionTable = divTable();
    var description = job.meta.description;
    descriptionTable.append($(caption("Job Description")));
    descriptionTable.append($('<p>'+description+'</p>'));
    descriptionCard.append(descriptionTable);

    return descriptionCard;
}


function parameterCard(job) {



    var parameterCard = $('<div class="card"></div>');
    //
    // var startTime = 0;
    // var endTime = 0;
    // job.system.infoIds.forEach(function (id) {
    //     var info = job.system.infos[id];
    //     if(info.name == "StartTime") {
    //         startTime = info.value;
    //     }
    //     if(info.name == "EndTime") {
    //         endTime = info.value;
    //     }
    // });

    var parameterTable = divTable();

    parameterTable.append(tableRow("Algorithm", job.meta.algorithm));
    parameterTable.append(tableRow("Dataset", job.meta.dataset));
    parameterTable.append(tableRow("#Node", job.meta.nodeSize));
    // parameterTable.append(tableRow("#Thread", job.threadSize));
    parameterTable.append($(caption("Job Parameters")));
    parameterCard.append(parameterTable);



    return parameterCard;
}


function durationCard(job) {



    var durationCard = $('<div class="card"></div>');

    var startTime = job.meta.startTime;
    var endTime = job.meta.endTime;
    // job.system.infoIds.forEach(function (id) {
    //     var info = job.system.infos[id];
    //     if(info.name == "StartTime") {
    //         startTime = info.value;
    //     }
    //     if(info.name == "EndTime") {
    //         endTime = info.value;
    //     }
    // });

    var durationTable = divTable();

    durationTable.append($(caption("Job Duration")));
    durationTable.append(tableRow("StartTime", timeConverter(startTime) + ' (' + startTime + ')'));
    durationTable.append(tableRow("EndTime", timeConverter(endTime) + ' (' + endTime + ')'));
    durationTable.append(tableRow("Duration", parseInt(endTime) - parseInt(startTime) + " ms"));

    durationCard.append(durationTable);



    return durationCard;
}

function breakDownTableCard(job) {
    var breakDownCard = $('<div class="card"></div>');

    var breakDownTable = divTable();
    var totalTime = 0;
    breakDownTable.append(caption("Job Breakdown"));

    for (var i in job.meta.breakDown) {
        totalTime += job.meta.breakDown[i];
    }

    breakDownTable.append(breakDownRow("Total", totalTime, "s", totalTime));

    for (var i in job.meta.breakDown) {
        if(i == "Others") {
            breakDownTable.append(breakDownRow(i, job.meta.breakDown[i], "s", totalTime));
        } else {
            breakDownTable.append(breakDownRow(i, job.meta.breakDown[i], "s", totalTime));
        }

        totalTime += job.meta.breakDown[i];
    }

    breakDownCard.append(breakDownTable);

    return breakDownCard;
}

function breakDownChartCard(job) {
    var chart = $('<div class="card borderless"></div>');

    var table = divTable();
    table.append('<svg id="breakdown-chart" height="25" width="80"></svg>').each(function () {
        var breakDownData = [];
        for (var i in job.meta.breakDown) {
            breakDownData.push({label: i, value: job.meta.breakDown[i]});
        }
        pieChart("#breakdown-chart", breakDownData);
    });

    chart.append(table);
    return chart;
}

function emptyCard(job) {

    var arcCard = $('<div class="card"></div>');

    return arcCard;
}