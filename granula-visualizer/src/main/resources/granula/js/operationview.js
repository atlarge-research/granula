var operation = {};
var isOperation = true;
var operations = {};
var infos = {};
var actors = {};
var missions = {};
var operationTab = {};


function operationView(job, opTab) {
    if(job.system) {
        operations = job.system.operations;
        infos = job.system.infos;
        actors = job.system.actors;
        missions = job.system.missions;
        operationTab = opTab;

        var operation = operations[job.system.rootIds[0]];
        drawOperChart(operation);
    } else {

    }


}

function operDesrCard(operation) {
    var card = divCard();
    var table = divTable();

    var summaryInfo = job.system.infos[operation.infoIds["Summary"]].summary;
    var summary = job.system.descriptions[summaryInfo].text;

    table.append(caption(getOperationName(operation)));

    table.append($('<p class="text-left">'+summary+'</p>'));
    card.append(table);
    return card;
}

function operInfoCard(visual) {
    var card = divCard();
    var table = divTable();

    table.append(caption(visual.name));
    visual.infoIds.forEach(function (infoId) {
        var info = job.system.infos[infoId];
        table.append(tableRow(info.name, info.value));
    });

    card.append(table);
    return card;
}



function operDurationCard(operation) {
    var card = divCard();
    var table = divTable();

    var startTime = info(operation, "StartTime");
    var endTime = info(operation, "EndTime");
    table.append(caption("Duration"));


    table.append(tableRow("StartTime", timeConverter(startTime) + ' (' + startTime + ')'));
    table.append(tableRow("EndTime", timeConverter(endTime) + ' (' + endTime + ')'));
    table.append(tableRow("Duration", parseInt(endTime) - parseInt(startTime) + " ms"));

    card.append(table);
    return card;
}

function drawOperChart(operation) {

    operationTab.empty();
    var cardRow1 = divCardGroup();
    var cardRow2 = divCardGroup();
    var cardRow3 = divCardGroup();

    operationTab.append(cardRow1);
    operationTab.append(cardRow2);
    operationTab.append(cardRow3);



    cardRow1.append(navigationLink(operation));
    cardRow2.append(operDesrCard(operation), operDurationCard(operation));

    var operationChart = operChart(operation);
    cardRow3.append(operationChart);

    var visualCount = 0;
    var visualRow;
    for(var name in operation.visualIds) {
        visualCount++;
        if(visualCount % 2 == 1) {
            visualRow = divCardGroup();
            operationTab.append(visualRow);
        }
        visualRow.append(operInfoCard(visual(operation, name)));
    }
    if(visualCount % 2 == 1) {visualRow.append(emptyCard());}


    operationTab.append(operEnvCard(operation));

}

function operEnvCard(operation) {
    var card;

    var label, content;
    if(job.env) {
        label = "Environment Data";


        var startTime = info(operation, "StartTime");
        var endTime = info(operation, "EndTime");
        var interval = conf.interval;
        while((endTime - startTime) / interval > 300) {
            interval = interval * 10;
            if(interval >= 10000) {
                break;
            }
        }

        var tabAttrs = envTabAttrs(job.env, startTime, endTime, interval, "oper");
        operEnvTabMap = list2Map(tabAttrs, "id");

        var startTime = operation.infoIds["StartTime"].value;
        var endTime = operation.infoIds["EndTime"].value;

        content = envTabs(startTime, endTime, "oper", operEnvTabMap);
        content.find(".tab-pane").removeClass("active");
        card = collapseCard("oper-env-card", label, content);
    }
    return card;
}



function info(entity, name) {
    return job.system.infos[entity.infoIds[name]].value
}


function visual(entity, name) {
    return job.system.visuals[entity.visualIds[name]]
}

function navigationLink(operation) {

    var currOperation = operation;

    var ancestorLink = $('<p class="link"></p>');

    // ancestorLink.prepend('&nbsp;&nbsp;>&nbsp;&nbsp;' + currentOperation.getTitle());
    // currentOperation = currentOperation.getSuperoperation();

    while(currOperation != null) {
        var link = $('<a uuid="' + currOperation.uuid + '">' + getOperationName(currOperation)+'</a>');

        link.on('click', function (e) {
            e.preventDefault();
            drawOperChart(operations[$(this).attr('uuid')]);
        });
        ancestorLink.prepend(link);
        ancestorLink.prepend('&nbsp;&nbsp;>&nbsp;&nbsp;');
        currOperation = operations[currOperation.parentId];

    }
    ancestorLink.prepend('Job');
    return ancestorLink;
}

