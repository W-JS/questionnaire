$(function () {
    isURL();
    HideAddQuestion();// 隐藏新建问题

    PQ();// 动态生成前置问题
    $("#pQ").change(PO);// 根据前置问题动态生成前置选项
    QT();// 动态生成问题类型

    $("#qTitle").blur(QTitle);
    $("#qDescription").blur(QDescription);

    $("#save").click(Save);
    $("#cancel").click(Cancel);
});

// 判断url是否正确
function isURL() {
    let url = window.location.pathname;
    let index = url.lastIndexOf("/")
    url = url.substring(index + 1, url.length);

    if (url == "addQuestion") {
        addQuestion();
    }
}

// 显示新建问题
function addQuestion() {
    let flag = $("#hideAddQuestion-li").length > 0;
    if (!flag) {
        let html =
            "<li id=\"hideAddQuestion-li\">\n" +
            "    <button type=\"button\" style=\"background-color: #005cbf;\" class=\"am-btn am-btn-default am-radius am-btn-xs\">新建问题\n" +
            "        <a id=\"hideAddQuestion\" href=\"javascript: void(0)\" class=\"am-close am-close-spin\" data-am-modal-close=\"\">×</a>\n" +
            "    </button>\n" +
            "</li>";
        $(html).appendTo($('#show-hide'));
    } else {
        $("#hideAddQuestion-li").show();
    }
}

// 隐藏新建问题
function HideAddQuestion() {
    $("#show-hide").on("mouseenter", "#hideAddQuestion", function () {
        $(this).click(function () {
            $("#hideAddQuestion-li").hide();
            window.location.href = CONTEXT_PATH + "/question/Question";
        })
    });
}

// 动态生成前置问题
function PQ() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getQuestionByQnId',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                let html = "";
                for (let i = 0; i < result.data.length; i++) {
                    html += "<option value=\"" + result.data[i].questionId + "\">" + result.data[i].questionTitle + "</option>";
                }
                $(html).appendTo($('#pQ'));
                $("#pQ option:first").prop("selected", 'selected');
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 根据前置问题动态生成前置选项
function PO() {
    let pQVal = $.trim($('#pQ option:selected').val());
    let pQText = $.trim($('#pQ option:selected').text());
    // console.log(pQText);

    if (pQVal != "null") {
        $.ajax({
            async: true, // 异步请求
            type: "get",
            url: CONTEXT_PATH + '/question/getOptionByQId',
            data: {
                'pQId': pQVal,
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    $('#pO option').remove();
                    let html = "";
                    for (let i = 0; i < result.data.length; i++) {
                        html += "<option value=\"" + result.data[i].optionId + "\">" + result.data[i].optionContent + "</option>";
                    }
                    $(html).appendTo($('#pO'));
                    $("#pO option:first").prop("selected", 'selected');
                } else {
                    ShowFailure("前置问题： " + pQText + " " + result.message);
                }
            }
        });
    }else{
        $('#pO option').remove();
        let html = "<option value=\"null\" selected>&nbsp;</option>";
        $(html).appendTo($('#pO'));
    }
}

// 动态生成问题类型
function QT() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/questiontype/getQuestionType',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                /*let html = "";
                for (let i = 0; i < result.data.length; i++) {
                    html +=
                        "<label class=\"am-checkbox-inline am-text-my\">\n" +
                        "    <input type=\"radio\" name=\"qt\" value=\"" + result.data[i].questiontypeId + "\">&nbsp;&nbsp;" + result.data[i].questiontypeName + "\n" +
                        "</label>";
                }
                $(html).appendTo($('#qt'));*/

                $('#qt option').remove();
                let html = "";
                for (let i = 0; i < result.data.length; i++) {
                    html += "<option value=\"" + result.data[i].questiontypeId + "\">" + result.data[i].questiontypeName + "</option>";
                }
                $(html).appendTo($('#qt'));
                $("#qt option:first").prop("selected", 'selected');
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 验证问题标题是否正确
function QTitle() {
    let flag = false;
    let qTitle = $.trim($("#qTitle").val());
    let qTitleValidate = /^.{3,50}$/;
    if (!qTitle || qTitle.length == 0) {
        ShowFailure('问题标题不能为空');
    } else {
        if (!qTitleValidate.test(qTitle)) {
            ShowFailure('问题标题格式不符合要求,问题标题长度在3到50个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证问题标题是否存在
function QTitleExists() {
    let flag = false;
    if (!QTitle()) {
        $("#qTitle").focus();
    } else {
        flag = true;
    }
    return flag;
}

// 验证问题描述是否正确
function QDescription() {
    let flag = false;
    let qDescription = $.trim($("#qDescription").val());
    let qDescriptionValidate = /^.{3,500}$/;
    if (!qDescription || qDescription.length == 0) {
        ShowFailure('问题描述不能为空');
    } else {
        if (!qDescriptionValidate.test(qDescription)) {
            ShowFailure('问题描述格式不符合要求,问题描述长度在3到500个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证问题描述是否存在
function QDescriptionExists() {
    let flag = false;
    if (!QDescription()) {
        $("#qDescription").focus();
    } else {
        flag = true;
    }
    return flag;
}

// 提交问题前表单验证
function Save() {
    if (!QTitleExists()) {
        return false;
    }

    if (!QDescriptionExists()) {
        return false;
    }

    saveSubmit();
}

// 提交问题信息
function saveSubmit() {
    let qTitle = $.trim($("#qTitle").val());
    let qDescription = $.trim($("#qDescription").val());
    let qStatus = $.trim($("input[name='qStatus']:checked").val());
    let pQVal = $.trim($('#pQ option:selected').val());
    let pOVal = $.trim($('#pO option:selected').val());
    // let qt = $.trim($("input[name='qt']:checked").val());
    let qt = $.trim($('#qt option:selected').val());

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/question/questionSubmit',
        data: {
            'qTitle': qTitle,
            'qDescription': qDescription,
            'qStatus': qStatus,
            'pQId': pQVal,
            'pOId': pOVal,
            'qtId': qt,
            'qCreateTime': getNowFormatDate()
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
    window.location.href = CONTEXT_PATH + "/questionnaire/addQuestionnaire";
}