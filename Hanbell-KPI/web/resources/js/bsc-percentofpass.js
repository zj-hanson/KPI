/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    var m = document.getElementById("formOne:m");
    var a = $("tr.ui-widget-content");
    $(a).css("border", "2px solid #000000");
    if (parseInt(m.value) < 7) {
        var tr = $("tr");
        $(tr).each(function (a, t) {
            for (var i = 0; i < $(t).find("td").length; i++) {
                if ($(t).find("td").length === 18) {
                    if (i >= 11 && i < 17) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 17) {
                    if (i >= 10 && i < 16) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 16) {
                    if (i >= 9 && i < 15) {
                        $(t).find("td").eq(i).hide();
                    }
                }
            }
        });
    } else {
        var tr = $("tr");
        $(tr).each(function (a, t) {
            for (var i = 0; i < $(t).find("td").length; i++) {
                if ($(t).find("td").length === 18) {
                    if (i >= 5 && i < 11) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 17) {
                    if (i >= 4 && i < 10) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 16) {
                    if (i >= 3 && i < 9) {
                        $(t).find("td").eq(i).hide();
                    }
                }
            }
        });
    }
    if (m !== null) {
        var tr = $("tr.ui-widget-content");
        $(tr).each(function (i, t) {
            var c;
            if ($(t).find("td").length === 16) {
                c = $(t).find("td")[parseInt(m.value) + 2];
            } else if ($(t).find("td").length === 18) {
                c = $(t).find("td")[parseInt(m.value) + 4];
            } else if ($(t).find("td").length === 17) {
                c = $(t).find("td")[parseInt(m.value) + 3];
            } else if ($(t).find("td").length === 19) {
                c = $(t).find("td")[parseInt(m.value) + 4];
            } else {
                c = $(t).find("td")[parseInt(m.value)];
            }
            if (c !== null) {
                $(c).css("color", "red");
            }
        });
    }
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
    //设置影藏和显示
    $("#upyear").click(function () {
        var tr = $("tr");
        $(tr).find("td").show();
        $(tr).each(function (a, t) {
            for (var i = 0; i < $(t).find("td").length; i++) {
                if ($(t).find("td").length === 18) {
                    if (i >= 11 && i < 17) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 17) {
                    if (i >= 10 && i < 16) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 16) {
                    if (i >= 9 && i < 15) {
                        $(t).find("td").eq(i).hide();
                    }
                }
            }
        });
        var m = document.getElementById("formOne:m");
        if (m !== null) {
            var tr = $("tr.ui-widget-content");
            $(tr).each(function (i, t) {
                var c;
                if ($(t).find("td").length === 18) {
                    c = $(t).find("td")[parseInt(m.value) + 4];
                } else if ($(t).find("td").length === 17) {
                    c = $(t).find("td")[parseInt(m.value) + 3];
                } else if ($(t).find("td").length === 16) {
                    c = $(t).find("td")[parseInt(m.value) + 2];
                } else {
                    c = $(t).find("td")[parseInt(m.value)];
                }
                if (c !== null) {
                    $(c).css("color", "red");
                }
            });
        }
    });
    $("#downyear").click(function () {
        var tr = $("tr");
        $(tr).find("td").show();
        $(tr).each(function (a, t) {
            for (var i = 0; i < $(t).find("td").length; i++) {
                if ($(t).find("td").length === 18) {
                    if (i >= 5 && i < 11) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 17) {
                    if (i >= 4 && i < 10) {
                        $(t).find("td").eq(i).hide();
                    }
                } else if ($(t).find("td").length === 16) {
                    if (i >= 3 && i < 9) {
                        $(t).find("td").eq(i).hide();
                    }
                }
            }
        });
        var m = document.getElementById("formOne:m");
        if (m !== null) {
            var tr = $("tr.ui-widget-content");
            $(tr).each(function (i, t) {
                var c;
                if ($(t).find("td").length === 18) {
                    c = $(t).find("td")[parseInt(m.value) + 4];
                } else if ($(t).find("td").length === 17) {
                    c = $(t).find("td")[parseInt(m.value) + 3];
                } else if ($(t).find("td").length === 16) {
                    c = $(t).find("td")[parseInt(m.value) + 2];
                } else {
                    c = $(t).find("td")[parseInt(m.value)];
                }
                if (c !== null) {
                    $(c).css("color", "red");
                }
            });
        }
    });

});

