function tableRow(key, value) {
    return $('<tr><td>' + key + '</td><td>' + value + '</td></tr>');
}

function tableProgressRow(actual, maximum) {
    return $('<tr><td><progress class="progress progress-striped" value="'+actual+'" max="'+maximum+'"></progress></td></tr>');
}

function resTableRow(name, value, unit, peak) {

    var valueText = (value) ? value.toFixed(2) + " " + unit : "None";
    var progressBar = (value) ? '<progress class="progress progress-striped" value="'+value+'" max="'+peak+'"></progress>' : "";

    return $('<tr>' +
        '<td class="col-md-3">' + name + '</td>' +
        '<td class="col-md-2">' + valueText + '</td>' +
        '<td class="col-md-2">' + peak.toFixed(2) + " " + unit + '</td>' +
        '<td class="col-md-4">'+progressBar+'</td>' +
        '</tr>');
}


function breakDownRow(name, value, unit, peak) {

    var valueText = (value) ? (value/ 1000).toFixed(0) + " " + unit : "None";
    var progressBar = (value) ? '<progress class="progress progress-striped" value="'+value+'" max="'+peak+'"></progress>' : "";

    return $('<tr>' +
        '<td class="col-md-3">' + name + '</td>' +
        '<td class="col-md-3">' + valueText  + '</td>' +
        '<td class="col-md-6">'+progressBar+'</td>' +
        '</tr>');
}


function divCard(size) {
    if(size) {
        return $('<div class="card col-md-'+size+'"></div>')
    } else {
        return $('<div class="card"></div>')
    }

}


function errorCard(size) {
    if(size) {
        return $('<div class="card col-md-'+size+' error-card"></div>')
    } else {
        return $('<div class="card error-card"></div>')
    }

}

function tableHeader(key, value) {
    return $('<tr><th>' + key + '</th><th>' + value + '</th></tr>');
}

function caption(text) {
    return $('<caption style="caption-side:top" class="text-lg-center">'+text+'</caption>');
}

function divTable() {
    return $('<table class="table table-sm" />');
}

function divCardGroup() {
    return $('<div class="card-group"></div>');
}

function tabDiv(tabs) {

    var tabDiv = $('<div role="tabpanel">');
    var tabList = $('<ul class="nav nav-tabs" role="tablist" />');
    var tabContent = $('<div class="tab-content" />');

    tabDiv.append(tabList);
    tabDiv.append(tabContent);

    tabs.forEach(function(tabItem, i) {
        var currentList = (i==0) ? "active" : "";
        var currentContent = (i==0) ? "active in" : "";
        tabList.append('<li class="nav-item">' +
            '<a class="nav-link '+ currentList +'" data-toggle="tab" href="#'+tabItem.id+'" role="tab">'+tabItem.name+'</a>' +
            '</li>');

        var content = $('<div class="tab-pane fade '+currentContent+'" id="' + tabItem.id + '" role="tabpanel"></div>');
        tabContent.append(content);
    });

    // tabList.find('a').on("click", function() {
    //     jQuery(this).tab('show');
    //     window.dispatchEvent(new Event('resize'));
    //     $(window).trigger('resize'); // Added this line to force NVD3 to redraw the chart
    //
    // });

    return tabDiv;


}

function collapseCard(id, label, content) {
    var card = $('<div class="card" />');

    var labelClass = (content) ? "" : " disabled";

    var labelDiv = $('<div role="tab" data-toggle="collapse" href="#'+id+'">');
    labelDiv.append($('<h4><a class="label '+labelClass+'">'+label+'</a></h4>'));

    var contentDiv = $('<div id="'+id+'" class="panel-collapse collapse in" role="tabpanel"></div>');
    if(content) {
        contentDiv.append(content);
    }

    card.append(labelDiv);
    card.append(contentDiv);
    return card;
}






function timeConverter(timestamp){
    var a = new Date(parseInt(timestamp));
    var year = a.getFullYear();
    var month = a.getMonth();
    var day = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    var ms = a.getMilliseconds();
    var time = year  + '/' + month + '/' + day + ' ' + hour + ':' + min + ':' + sec + ":" + ms;
    return time;
}

function logging(text) {
    console.log(text);
}


function logging2(texts) {
    var output = texts.reduce(function (a, b) {
        return a + " " + b;
    })
    console.log(output);
}



function change_brightness(hex, percent){
    // strip the leading # if it's there
    hex = hex.replace(/^\s*#|\s*$/g, '');

    // convert 3 char codes --> 6, e.g. `E0F` --> `EE00FF`
    if(hex.length == 3){
        hex = hex.replace(/(.)/g, '$1$1');
    }

    var r = parseInt(hex.substr(0, 2), 16),
        g = parseInt(hex.substr(2, 2), 16),
        b = parseInt(hex.substr(4, 2), 16);

    if(percent >= 0) {
        hex =  '#' +
            ((0|(1<<8) + r + (256 - r) * percent / 100).toString(16)).substr(1) +
            ((0|(1<<8) + g + (256 - g) * percent / 100).toString(16)).substr(1) +
            ((0|(1<<8) + b + (256 - b) * percent / 100).toString(16)).substr(1);
    } else if(percent < 0) {
        hex =  '#' +
            ((0|(1<<8) + r * (100 - percent) / 100).toString(16)).substr(1) +
            ((0|(1<<8) + g * (100 - percent) / 100).toString(16)).substr(1) +
            ((0|(1<<8) + b * (100 - percent) / 100).toString(16)).substr(1);
    }

    return hex;
}
