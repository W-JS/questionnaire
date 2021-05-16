$(function () {
    GenerateOnlineUser();// 生成在线用户信息

    if ($("#answer").attr('value') == 'false') {
        // 已填写友情提示
        layui.use('layer', function () {
            let layer = layui.layer;
            layer.open({
                title: '友情提示'
                , content: '您已回复过该问卷，目前只能查看该回复！！！'
            });
        });

        $("#answer").attr("disabled", 'disabled');
        $("#answerBtn").attr("disabled", 'disabled');
    }
});

// 回复用户留言
function Answer() {
    let ucId = $("#ucId").attr("value");
    let adminUserId = $('#onlineUser').val();
    let answer = $("#answer").val();
    if (!answer && answer == "") {
        $("#answer").focus();
        ShowFailure("回复的内容不能为空！！！");
        return;
    }

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/usercomment/usercommentSubmit',
        data: {
            'ucId': ucId,
            'adminUserId': adminUserId,
            'answer': answer,
            'answerTime': getNowFormatDate()
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                ShowSuccess("回复成功！！！");
                setTimeout(function () {
                    windowClose();
                }, 1000);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 关闭当前页面
function windowClose() {
    window.close();
    window.location.href = CONTEXT_PATH + "/usercomment/index";
}
