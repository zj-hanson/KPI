/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var s;
    countTime();
});
function countTime() {
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var date = myDate.getDate();
    var h = myDate.getHours();
    var m = myDate.getMinutes();
    var s = myDate.getSeconds();
    var now = year + '年' + getNow(month) + "月" + getNow(date) + "日   " + getNow(h) + ':' + getNow(m) + ":" + getNow(s);
    document.getElementById("sj1").innerHTML = now;
    setTimeout(countTime, 1000);
}
function getNow(s) {
    return parseInt(s) > 9 ? s : '0' + s;
}
