/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    //增加换行色和线框
    var tbl = document.getElementById("formOne:tblList");
    var tr = $(tbl).find("tr.ui-widget-content");
    $(tr).css("border", "2px solid #000000");
    for (var i = 0; i < tr.length; i++) {
        tr[i].style.backgroundColor = (tr[i].sectionRowIndex % 2 === 0) ? "" : "#E6E6FA";
    }
    $(tr).each(function (i, t) {
        $(t).find("td").css("border", "1px solid #000000");
    });
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