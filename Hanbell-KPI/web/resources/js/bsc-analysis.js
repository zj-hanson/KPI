/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    setColor();
    //只显示当月
    var thisMon = document.getElementById("formOne:j_idt13");
    function setShowOrHiden(i, o) {
        if (i === 0 || i === 13)
            return;
        $(o).hide();
    }
    function setThisMon() {
        if (thisMon.checked === true) {
            var table = document.getElementById("formOne:plgMISData");
            var trs = $(table).find("tr");
            trs.map(function (i, n) {
                $(n).find("td").map(function (i, o) {
                    setShowOrHiden(i, o);
                });
            });
        }
    }
    setThisMon();
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
            $(tr).css("border", "2px solid #000000");
            for (var i = 0; i < tr.length; i++) {
                tr[i].style.backgroundColor = (tr[i].sectionRowIndex % 2 === 0) ? "" : "#E6E6FA";
            }
            $(tr).each(function (i, t) {
                var c = $(t).find("td.ui-panelgrid-cell")[m.value];
                if (c !== null) {
                    $(c).css("color", "red");
                }
                $(t).find("td").css("border", "1px solid #000000");
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
