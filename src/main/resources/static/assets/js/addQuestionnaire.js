$(function () {
    isURL();
    HideAddQuestionnaire();// 隐藏新建问卷

    $("#qnTitle").blur(QNTitle);
    $("#qnFuTitle").blur(QNFuTitle);
    $("#qnDescription").blur(QNDescription);
    $("#save").click(Save);
    $("#cancel").click(Cancel);
});

// 判断url是否正确
function isURL() {
    let url = window.location.pathname;
    let index = url.lastIndexOf("/")
    url = url.substring(index + 1, url.length);

    if (url == "addQuestionnaire") {
        AddQuestionnaire();
    }
}

// 显示新建问卷
function AddQuestionnaire() {
    let flag = $("#hideAddQuestionnaire-li").length > 0;
    if (!flag) {
        let html =
            "<li id=\"hideAddQuestionnaire-li\">\n" +
            "    <button type=\"button\" style=\"background-color: #005cbf;\" class=\"am-btn am-btn-default am-radius am-btn-xs\">新建问卷\n" +
            "        <a id=\"hideAddQuestionnaire\" href=\"javascript: void(0)\" class=\"am-close am-close-spin\" data-am-modal-close=\"\">×</a>\n" +
            "    </button>\n" +
            "</li>";
        $(html).appendTo($('#show-hide'));
    } else {
        $("#hideAddQuestionnaire-li").show();
    }
}

// 隐藏新建问卷
function HideAddQuestionnaire() {
    $("#show-hide").on("mouseenter", "#hideAddQuestionnaire", function () {
        $(this).click(function () {
            $("#hideAddQuestionnaire-li").hide();
            window.location.href = CONTEXT_PATH + "/questionnaire/index";
        })
    });
}

// 验证问卷标题是否正确
function QNTitle() {
    let flag = false;
    let qnTitle = $.trim($("#qnTitle").val());
    let qnTitleValidate = /^.{3,50}$/;
    if (!qnTitle || qnTitle.length == 0) {
        ShowFailure('问卷标题不能为空');
    } else {
        if (!qnTitleValidate.test(qnTitle)) {
            ShowFailure('问卷标题格式不符合要求,问卷标题长度在3到50个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证问卷标题是否存在
function QNTitleExists() {
    let flag = false;
    if (!QNTitle()) {
        $("#qnTitle").focus();
    } else {
        flag = true;
    }
    return flag;
}

// 验证问卷副标题是否正确
function QNFuTitle() {
    let flag = false;
    let qnFuTitle = $.trim($("#qnFuTitle").val());
    let qnFuTitleValidate = /^.{3,50}$/;
    if (!qnFuTitle || qnFuTitle.length == 0) {
        ShowFailure('问卷副标题不能为空');
    } else {
        if (!qnFuTitleValidate.test(qnFuTitle)) {
            ShowFailure('问卷副标题格式不符合要求,问卷副标题长度在3到50个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证问卷副标题是否存在
function QNFuTitleExists() {
    let flag = false;
    if (!QNFuTitle()) {
        $("#qnFuTitle").focus();
    } else {
        flag = true;
    }
    return flag;
}

// 验证问卷描述是否正确
function QNDescription() {
    let flag = false;
    let qnDescription = $.trim($("#qnDescription").val());
    let qnDescriptionValidate = /^.{3,500}$/;
    if (!qnDescription || qnDescription.length == 0) {
        ShowFailure('问卷描述不能为空');
    } else {
        if (!qnDescriptionValidate.test(qnDescription)) {
            ShowFailure('问卷描述格式不符合要求,问卷描述长度在3到500个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证问卷描述是否存在
function QNDescriptionExists() {
    let flag = false;
    if (!QNDescription()) {
        $("#qnDescription").focus();
    } else {
        flag = true;
    }
    return flag;
}

// 提交问卷前表单验证
function Save() {
    if (!QNTitleExists()) {
        return false;
    }

    if (!QNFuTitleExists()) {
        return false;
    }

    if (!QNDescriptionExists()) {
        return false;
    }

    saveSubmit();
}

// 提交问卷信息
function saveSubmit() {
    let qnTitle = $.trim($("#qnTitle").val());
    let qnFuTitle = $.trim($("#qnFuTitle").val());
    let qnDescription = $.trim($("#qnDescription").val());
    let userId = $.trim($("#userId").val());

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/questionnaire/questionnaireSubmit',
        data: {
            'qnTitle': qnTitle,
            'qnFuTitle': qnFuTitle,
            'qnDescription': qnDescription,
            'qnStatus': 0,
            'qnCreateTime': getNowFormatDate(),
            'userId': userId
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                window.location.href = CONTEXT_PATH + "/question/addQuestion";
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

function Cancel() {
    window.location.href = CONTEXT_PATH + "/questionnaire/index";
}