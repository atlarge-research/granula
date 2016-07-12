function pieChart(figure, data) {
    var width = 200, height = 200;

    nv.addGraph(function() {
        var chart = nv.models.pieChart()
            .x(function(d) { return d.label })
            .y(function(d) { return d.value })
            .showLabels(true)     //Display pie labels
            .labelThreshold(.05)  //Configure the minimum slice size for labels to show up
            .labelType("percent") //Configure what type of data to show in the label. Can be "key", "value" or "percent"
            .donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
            .donutRatio(0.35)     //Configure how big you want the donut hole size to be.
            .showLegend(false)
            .margin({"left":0,"right":0,"top":0,"bottom":0})

        d3.select(figure)
            .datum(data)
            .transition().duration(350)
            .call(chart).style({ 'width': width, 'height': height });

        return chart;
    });
}


function areaChart(figure, chartAttr, chartData) {
    var colors = d3.scale.category20();

    var chart;

    nv.addGraph(function () {
        chart = nv.models.stackedAreaChart()
            .useInteractiveGuideline(true)
            .x(function (d) { return d[0] })
            .y(function (d) { return d[1] / 1 })
            .controlLabels({stacked: "Absolute value", expanded: "Percentage"})
            .duration(100)
            .margin({"left":200, "bottom":70, "right":75});



        chart.xAxis.tickFormat(function (d) {
            return d / 1000
        }).axisLabel(chartAttr.xLabel);

        chart.yAxis.tickFormat(d3.format(',.2f')).axisLabel(chartAttr.yLabel);
        chart.y2Axis.tickFormat(d3.format(',.2f'));


        chart.legend.vers('furious');

        d3.select(figure).datum(chartData).call(chart);

        d3.select(figure).append("text")
            .attr("x", 550).attr("y", 267)
            .attr("text-anchor", "middle")
            .attr("fill", "gray")
            .text(chartAttr.title);

        d3.select(figure);

        // nv.utils.windowResize(chart.update);
        return chart;
    });
}