/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    var m = document.getElementById("formOne:m1");
    if (m !== null) {
        var chartData = document.getElementById("formOne:plgChartData1");
        var tr = $(chartData).find("tr.ui-widget-content");
        $(tr).each(function (i, t) {
            var c = $(t).find("td.ui-panelgrid-cell")[m.value];
            if (c !== null) {
                $(c).css("color", "red");
            }
        });
        //MIS当月数据显示红色
        var misData = document.getElementById("formOne:plgMISData1");
        if (misData !== undefined && misData !== null) {
            var tr = $(misData).find("tr.ui-widget-content");
            $(tr).each(function (i, t) {
                var c = $(t).find("td.ui-panelgrid-cell")[12];
                if (c !== null) {
                    $(c).css("color", "red");
                }
            });
        }
    }
    //指标说明
    var sc1 = document.getElementById("formOne:sc1");
    if (sc1 !== null) {
        var count = sc1.value;
        for (var i = 0; i < count; i++) {
            var h = "formOne:repSummary1:" + i + ":hdnSummary1";
            var d = "formOne:repSummary1:" + i + ":summary1";
            var hdnSummary = document.getElementById(h);
            var displaySummary = document.getElementById(d);
            //alert($(hdnSummary).val());
            $(displaySummary).html($(hdnSummary).val());
        }
    }
    //指标分析
    var ac1 = document.getElementById("formOne:ac1");
    if (ac1 !== null) {
        var count = ac1.value;
        for (var i = 0; i < count; i++) {
            var hi = "formOne:repAnalysis1:" + i + ":hdnIssue1";
            var hf = "formOne:repAnalysis1:" + i + ":hdnFactor1";
            var hc = "formOne:repAnalysis1:" + i + ":hdnCountermeasure1";
            var di = "formOne:repAnalysis1:" + i + ":issue1";
            var df = "formOne:repAnalysis1:" + i + ":factor1";
            var dc = "formOne:repAnalysis1:" + i + ":countermeasure1";
            $(document.getElementById(di)).html($(document.getElementById(hi)).val());
            $(document.getElementById(df)).html($(document.getElementById(hf)).val());
            $(document.getElementById(dc)).html($(document.getElementById(hc)).val());
        }
    }
});