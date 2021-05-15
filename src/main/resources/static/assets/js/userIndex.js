$(function () {
    GenerateOnlineUser();// 生成在线用户信息
});

// 点击删除问卷按钮
function DeleteQuestionnaire(object) {
    $('#questionnaireModal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            let userId = $('#onlineUser').val();
            let qnId = object.id;
            $.ajax({
                async: true, // 异步请求
                type: "post",
                url: CONTEXT_PATH + '/userIndex/deleteSubmit',
                data: {
                    'userId': userId,
                    'qnId': qnId,
                },
                dataType: 'json',
                success: function (result) {
                    if (result.state == 1) {
                        ShowSuccess("删除成功！！！");
                        setTimeout(function () {
                            window.location.href = window.location.pathname + window.location.search;
                        }, 1000);
                    } else {
                        ShowFailure(result.message);
                    }
                }
            });
        },
        onCancel: function () {
            ShowMsg("取消删除！！！");
        },
    });
}