
function operChart(operation) {

    var subOperList = operList(operation.childIds);

    var supActorIds = subOperList.map(function (subOper) {return subOper.actorId;})
    var subActors = actorList(supActorIds);

    var bgWidth = 1000;
    var bgHeight = (subActors.length > 3) ? 50+50* subActors.length : 50+50*3;



    $('#operation-chart').remove();
    var svg = $('<svg id="operation-chart" width="100%" ' +
        ' viewBox="0 0 1000 '+bgHeight+'" />');
    $('body').append(svg); // work-around for dynamic svg



    var board = Snap("#operation-chart");



    var bgRect = rect(board, 0, 0, bgWidth, bgHeight, 5, "#EEE");
    bgRect.attr({uuid: operation.parentId});
    bgRect.click(function(){
        if(operations[this.attr('uuid')]) {
            drawOperChart(operations[this.attr('uuid')]);
        }

    });

    var mRect = {x: 210, y:20, w: bgWidth - 210 - 55, h:bgHeight - 50};

    var explRect = {x: 20, y:30, w: 200, h:40};

    svgText(board, "Explore HERE! >", explRect, 18, "#444");

    var mainOp = mainOperation(board, mRect);


    var startTime = info(operation, "StartTime");
    var endTime = info(operation, "EndTime");


    subOperList.forEach(function (subOper) {
        var actorIndex = supActorIds.indexOf(subOper.actorId);
        subOperation(board, mRect, startTime, endTime, subOper, actorIndex);
    });

    if(subOperList.length == 0) {
        svgText(board, "This operation does not contain any child operations.", mRect, 20, "#888");
    }

    axislines(board, mRect, operation);

    return svg;
}

function axislines(board, mRect, operation) {

    var ticsSize = 5;
    svgLine(board, mRect.x, mRect.y , mRect.x + mRect.w, mRect.y );

    var duration = info(operation, "Duration") / ticsSize;

    var jobStartTime =job.meta.startTime;
    var jobEndTime = job.meta.endTime;
    var operStartTime = info(operation, "StartTime");

    var distance = mRect.w / ticsSize;
    for (var i = 0; i <= ticsSize; i++) {
        svgLine(board, mRect.x + distance * i, mRect.y, mRect.x + distance * i, mRect.y+10);
        svgLine(board, mRect.x + distance * i, mRect.y + mRect.h, mRect.x + distance * i, mRect.y + mRect.h - 10);

        var progressRatio = (parseFloat(operStartTime) + duration * i - jobStartTime) / (jobEndTime - jobStartTime) * 100;
        var labelRect1 = {x:mRect.x + distance * i - 30, y:mRect.y - 25, w:100, h:30};
        var labelRect2 = {x:mRect.x + distance * i - 30, y:mRect.y + mRect.h - 5, w:100, h:30};
        svgText(board, (progressRatio).toFixed(2) + "%", labelRect1, 14, "#777");
        svgText(board, (duration * i / 1000).toFixed(2)+"s", labelRect2, 14, "#777");
    }

    svgLine(board, mRect.x, mRect.y + mRect.h, mRect.x + mRect.w, mRect.y + mRect.h);
}

function subOperation(board, mRect, startTime, endTime, subOper, i) {

    var isCompleted = (info(subOper, "Completeness") === "true") ? true : false;
    var x = (info(subOper, "StartTime") - startTime) / (endTime - startTime);
    var x2 = (info(subOper, "EndTime") - startTime) / (endTime - startTime);

    x = x * (mRect.w) + mRect.x;
    x2 = x2 * mRect.w + mRect.x;
    var sRect = {x: x, y:mRect.y + 10 +i*50, w: (x2 - x), h:(45)};


    var subOperRect;
    if(isCompleted) {
        subOperRect = rect(board, sRect.x, sRect.y, sRect.w, sRect.h, 2, "#39F");
    } else {
        subOperRect = rect(board, sRect.x, sRect.y, sRect.w, sRect.h, 2, "#F63");
    }

    subOperRect.attr({strokeWidth: 1, stroke: change_brightness("#39F", -80)});


    var hint = getOperationName(subOper) + " [" + info(subOper, "Duration")  + " ms]";
    subOperRect.append(Snap.parse('<title>' + hint + '</title>'));
    svgText(board, getMissionName(subOper), sRect, 18, "#FFF");

    subOperRect.attr({uuid: subOper.uuid});
    subOperRect.click(function(){
        drawOperChart(operations[this.attr('uuid')]);
    });

}

function mainOperation(board, mRect) {
    var mainOpRect = rect(board, mRect.x, mRect.y, mRect.w, mRect.h, 2, "#6FC");
}

function operList(operIds) {
    return operIds.map(function (id) {
        return operations[id];
    })
}

function actorList(actorIds) {

    function listMethod(actorIds) {
        return actorIds.map(function (id) {
            return actors[id];
        })
    }

    function equalMethod(actor) {
        return actor.name;
    }

    return _.unique(listMethod(actorIds), equalMethod);
}
//
// function infoMap(infoIds) {
//     var iMap = {};
//     infoIds.forEach(function (id) {
//         iMap[infos[id].name] = infos[id];
//     });
//     return iMap;
// }

function rect(board, x, y, w, h, r, color) {
    var svg = board.rect(x, y, w, h, r);
    svg.attr({fill: color});
    return svg;
}

function svgLine(board, x1, y1, x2, y2) {

    var svg = board.line(x1, y1, x2, y2);
    svg.attr({strokeWidth: 1, stroke: "#888"});
}

function svgText(board, text, rect, size, color) {
    var svg = board.text(rect.x, rect.y, text);
    svg.attr({"font-size": size});
    var x = rect.x + rect.w / 2 - (svg.getBBox().cx - rect.x);
    var y = rect.y + rect.h / 2 - (svg.getBBox().cy - rect.y);

    if(svg.getBBox().w < rect.w * 0.9 && svg.getBBox().h < rect.h * 0.9) {
        svg.remove();
        svg = board.text(x, y, text);
        svg.attr({fill: color, "font-size": size, "pointer-events":"none"});
        return svg;
    } else {
        svg.remove();
        return null;
    }

}

function getOperationName(operation) {
    var actor = job.system.actors[operation.actorId];

    var mission = job.system.missions[operation.missionId];
    return actor.name+"-"+ mission.name;
}


function getMissionName(operation) {
    var mission = job.system.missions[operation.missionId];
    return mission.name;
}