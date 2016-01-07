// JavaScript Document
function h(q) { return document.getElementById(q); }

function HoverTabs(n) {
    //如果有N个标签,就将i<=N;
    //本功能非常OK,兼容IE7,FF,IE6
    for (var i = 1; i <= 3; i++) {
        h('gn_' + i).className = 'normaltab';
     }
    h('gn_' + n).className = 'hovertab';
}
function HoverTwo(n) {
    for (var i = 1; i <= 2; i++) {
        h('tb_' + i).className = 'normaltab_tow';
        h('tbc_0' + i).className = 'undis';
     }
        h('tbc_0' + n).className = 'dis';
        h('tb_' + n).className = 'hovertab_tow now';
}
function HoverTable(n) {
    for (var i = 1; i <= 2; i++) {
        h('aa_' + i).className = 'normaltab_table';
        h('aac_0' + i).className = 'undis';
     }
        h('aac_0' + n).className = 'dis';
        h('aa_' + n).className = 'hovertab_table';
}
