// 在bootstrap中可以使用如下方式实现弹出提示信息自动消失，如果没有使用bootstrap框架，可以自定义样式
//tip是提示信息，type:'info'是普通信息,'success'是成功信息，'danger'是失败信息,'warning'是警告信息
function ShowTip(tip, type) {
    let $tip = $('#tip');
    if ($tip.length == 0) {
        // 设置样式，也可以定义在css文件中
        $tip = $('<span id="tip" style="position:fixed;top:50px;left: 50%;z-index:9999;height: 35px;padding: 0 20px;line-height: 35px;"></span>');
        $('body').append($tip);
    }
    $tip.stop(true).prop('class', 'alert alert-' + type).text(tip).css('margin-left', -$tip.outerWidth() / 2).fadeIn(500).delay(2000).fadeOut(500);//设置显示位置和显示时间和消失时间
}

//普通信息
function ShowMsg(msg) {
    ShowTip(msg, 'info');
}

//成功信息
function ShowSuccess(msg) {
    ShowTip(msg, 'success');
}

//失败信息
function ShowFailure(msg) {
    ShowTip(msg, 'danger');
}

// 警告信息
function ShowWarn(msg) {
    ShowTip(msg, 'warning');
}
