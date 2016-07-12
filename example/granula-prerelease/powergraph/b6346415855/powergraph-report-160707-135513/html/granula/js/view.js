

function setJob() {

    $('#job').append(jobPanel(job));
    // $('a[href="#environment-tab"]').tab('show');
    // $('a[href="#operation-tab"]').tab('show');
}



function jobPanel(job) {

    var jobPanel = $('<div class="card text-xs-center job-card" ></div>');
    var panelWrapper = $('<div class="row"><div class="col-md-12 col-centered"></div></div>');
    panelWrapper.find('div').append(jobPanel);

    jobPanel.append();


    jobPanel.append(jobHeader(job));

    var tabItems = [{id: "overview-tab", name: "Overview"},
        {id: "operation-tab", name: "Platform"},
        {id: "environment-tab", name: "Environment"}];



    jobPanel.append(jobTabDiv(tabItems, job));

    jobOverview(job, jobPanel.find('#'+tabItems[0].id));
    operationView(job, jobPanel.find('#'+tabItems[1].id));
    envView(job, jobPanel.find('#'+tabItems[2].id));
    
    jobPanel.append(jobFooter(job));
    return panelWrapper;
}


function jobHeader(job) {
    var name;

    if (job.meta) {
        name = job.meta.name;
    } else {
        name = "Job [Undefined]";
    }

    var header = $('<div class="card-header dark job-border">' + name + '</div>');
    return header;

}


function jobFooter(job) {
    var name;
    if (job.meta) {
        name = job.meta.name;
    } else {
        name = "Job [Undefined]";
    }
    var footer = $('<div class="card-header dark">' + name + '</div>');
    return footer;
}



function jobTabDiv(tabs, job) {

    var tabDiv = $('<div role="tabpanel">');
    var tabList = $('<ul class="nav nav-tabs" role="tablist" />');
    var tabContent = $('<div class="tab-content" />');

    tabDiv.append(tabList);
    tabDiv.append(tabContent);

    tabs.forEach(function(tabItem, i) {

        var isEnabled;
        if(job.meta) {
            if(i==0) {
                isEnabled = true;
            } else if(i==1) {
                isEnabled = (job.system) ? true : false;
            } else if(i==2) {
                isEnabled = (job.env) ? true : false;
            }
        } else {
            isEnabled = false;
        }

        var labelClasses = (i==0) ? "active" : "";
        labelClasses += (isEnabled) ? "" : " disabled";
        var labelToggle = (isEnabled) ? "tab"  : "";
        var tabLabel = $('<li class="nav-item"></li>');
        tabLabel.append($('<a class="nav-link '+ labelClasses +'" data-toggle="'+labelToggle+ '" href="#'+tabItem.id+'" role="tab">'+tabItem.name+'</a>'));
        tabList.append(tabLabel);



        var currentContent = (i==0) ? "active in" : "";
        var content = $('<div class="tab-pane fade '+currentContent+'" id="' + tabItem.id + '" role="tabpanel"></div>');
        tabContent.append(content);
    });

    return tabDiv;
}
