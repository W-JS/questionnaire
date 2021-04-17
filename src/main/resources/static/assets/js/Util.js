// 获取系统当前时间 格式：yyyy-MM-dd HH:mm:ss
function getNowFormatDate() {
    let now = new Date();
    let year = now.getFullYear();   //年
    let month = now.getMonth() + 1; //月
    let day = now.getDate();        //日
    let hh = now.getHours();        //时
    let mm = now.getMinutes();      //分
    let ss = now.getSeconds();      //秒
    let clock = year + "-";

    if (month < 10)
        clock += "0";
    clock += month + "-";
    if (day < 10)
        clock += "0";
    clock += day + " ";
    if (hh < 10)
        clock += "0";
    clock += hh + ":";
    if (mm < 10) clock += '0';
    clock += mm + ":";
    if (ss < 10) clock += '0';
    clock += ss;
    return clock;
}