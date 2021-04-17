$(function () {
    isURL();
    HideAddOption();// 隐藏新建选项

    $("#oContent").blur(OContent);

    $("#save").click(Save);
    $("#cancel").click(Cancel);
});

// 判断url是否正确
function isURL(){
    let url = window.location.pathname;
    let index = url.lastIndexOf("/")
    url = url.substring(index + 1, url.length);

    if (url == "addOption") {
        addOption();
    }
}

// 显示新建选项
function addOption() {
    let flag = $("#hideAddOption-li").length > 0;
    if (!flag) {
        let html =
            "<li id=\"hideAddOption-li\">\n" +
            "    <button type=\"button\" style=\"background-color: #005cbf;\" class=\"am-btn am-btn-default am-radius am-btn-xs\">新建选项\n" +
            "        <a id=\"hideAddOption\" href=\"javascript: void(0)\" class=\"am-close am-close-spin\" data-am-modal-close=\"\">×</a>\n" +
            "    </button>\n" +
            "</li>";
        $(html).appendTo($('#show-hide'));
    }else {
        $("#hideAddOption-li").show();
    }
}

// 隐藏新建选项
function HideAddOption() {
    $("#show-hide").on("mouseenter","#hideAddOption", function () {
        $(this).click(function () {
            $("#hideAddOption-li").hide();
            window.location.href = CONTEXT_PATH + "/option/Option";
        })
    });
}

// 验证选项内容是否正确
function OContent() {
    let flag = false;
    let oContent = $.trim($("#oContent").val());
    let oContentValidate = /^.{1,50}$/;
    if (!oContent || oContent.length == 0) {
        ShowFailure('选项内容不能为空');
    } else {
        if (!oContentValidate.test(oContent)) {
            ShowFailure('选项内容格式不符合要求,选项内容长度在1到50个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证选项内容是否存在
function OContentExists() {
    let flag = false;
    if (!OContent()) {
        $("#oContent").focus();
    } else {
        flag = true;
    }
    return flag;
}

// 提交选项前表单验证
function Save() {
    if (!OContentExists()) {
        return false;
    }

    saveSubmit();
}

// 提交选项信息
function saveSubmit() {
    let oContent = $.trim($("#oContent").val());

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/option/optionSubmit',
        data: {
            'oContent': oContent,
            'oCreateTime': getNowFormatDate()
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                window.location.href = CONTEXT_PATH + "/option/addOption";
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

function Cancel() {
    window.location.href = CONTEXT_PATH + "/question/addQuestion";
}