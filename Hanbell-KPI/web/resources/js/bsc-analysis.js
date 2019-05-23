/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    setColor();
    //指标说明
    var sc = document.getElementById("formOne:sc");
    if (sc !== null) {
        var count = sc.value;
        for (var i = 0; i < count; i++) {
            var h = "formOne:repSummary:" + i + ":hdnSummary";
            var d = "formOne:repSummary:" + i + ":summary";
            var hdnSummary = document.getElementById(h);
            var displaySummary = document.getElementById(d);
            //alert($(hdnSummary).val());
            $(displaySummary).html($(hdnSummary).val());
        }
    }
    //指标分析
    var ac = document.getElementById("formOne:ac");
    if (ac !== null) {
        var count = ac.value;
        for (var i = 0; i < count; i++) {
            var hi = "formOne:repAnalysis:" + i + ":hdnIssue";
            var hf = "formOne:repAnalysis:" + i + ":hdnFactor";
            var hc = "formOne:repAnalysis:" + i + ":hdnCountermeasure";
            var di = "formOne:repAnalysis:" + i + ":issue";
            var df = "formOne:repAnalysis:" + i + ":factor";
            var dc = "formOne:repAnalysis:" + i + ":countermeasure";
            $(document.getElementById(di)).html($(document.getElementById(hi)).val());
            $(document.getElementById(df)).html($(document.getElementById(hf)).val());
            $(document.getElementById(dc)).html($(document.getElementById(hc)).val());
        }
    }
});

var setColor = function () {
    var m = document.getElementById("formOne:m");
    if (m !== null) {
        var chartData = document.getElementById("formOne:plgChartData");
        if (chartData !== undefined && chartData !== null) {
            var tr = $(chartData).find("tr.ui-widget-content");
            $(tr).each(function (i, t) {
                var c = $(t).find("td.ui-panelgrid-cell")[m.value];
                if (c !== null) {
                    $(c).css("color", "red");
                }
            });
        }
        //MIS当月数据显示红色
        var misData = document.getElementById("formOne:plgMISData");
        if (misData !== undefined && misData !== null) {
            var tr = $(misData).find("tr.ui-widget-content");
            $(tr).each(function (i, t) {
                var c = $(t).find("td.ui-panelgrid-cell")[13];
                if (c !== null) {
                    $(c).css("color", "red");
                }
            });
        }
    }
};