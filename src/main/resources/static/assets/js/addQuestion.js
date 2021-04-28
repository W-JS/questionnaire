$(function () {
    isURL();
    HideAddQuestion();// 隐藏新建问题

    GenerateQNTitle();// 生成问卷标题

    GeneratePQP();// 动态生成前置问题的页数

    $("#pQP").change(PQPGeneratePQ);// 根据前置问题的页数动态生成前置问题
    // 新建问题的前置问题为未被前置的问题
    // 被前置的问题：问题A的 question_id 存在于（等于）问题B的 pre_question_id，说明问题A是被前置的问题
    // 未被前置的问题：问题A的 question_id 不存在于（不等于）问题B的 pre_question_id，说明问题A是未被前置的问题

    // PQ();// 动态生成前置问题
    $("#pQ").change(PQGeneratePO);// 根据前置问题动态生成前置选项

    GenerateQT();// 动态生成问题类型

    GenerateQNAndQ();// 动态生成当前问卷的所有问题

    $("#qTitle").blur(QTitle);
    $("#qDescription").blur(QDescription);

    $("#save").click(Save);
    $("#reset").click(Reset);
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

// 动态生成前置问题的页数
function GeneratePQP() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getNoPrependedQuestionRowsByQnId',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                // $('#pQP option').remove();
                let html = "";
                for (let i = 1; i <= result.data.total; i++) {
                    html += "<option value=\"" + i + "\">第&nbsp;" + i + "&nbsp;页</option>";
                }
                $(html).appendTo($('#pQP'));

                // $("#pQP option:first").prop("selected", 'selected');
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

/*// 动态生成前置问题
function PQ() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getAllQuestionByQnId',
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
}*/

// 根据前置问题的页数动态生成前置问题
function PQPGeneratePQ() {
    let pQPVal = $.trim($('#pQP option:selected').val());
    let pQPText = $.trim($('#pQP option:selected').text());

    if (pQPVal != "null") {
        $.ajax({
            async: true, // 异步请求
            type: "get",
            url: CONTEXT_PATH + '/question/getNoPrependedQuestionPage1ByQnId',
            data: {
                'current': pQPVal,
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    $('#pQ option').remove();
                    let html = "";
                    let length = result.data[0].questionList.length;// 前置问题列表的长度
                    for (let i = 0; i < result.data[0].questionList.length; i++) {
                        // 前置问题不能为填空题
                        if (result.data[0].questionList[i].questiontypeId == "fillBlank") {
                            length--;
                            continue;
                        }
                        html += "<option value=\"" + result.data[0].questionList[i].questionId + "\">" + result.data[0].questionList[i].questionTitle + "</option>";
                    }
                    if (length == 0) {
                        html += "<option value=\"null\">&nbsp;</option>";
                    }
                    $(html).appendTo($('#pQ'));
                    $("#pQ option:first").prop("selected", 'selected');
                } else {
                    ShowFailure("前置问题： " + pQPText + " " + result.message);
                }
            }
        });
    } else {
        $('#pQ option').remove();
        let html1 = "<option value=\"null\" selected>&nbsp;</option>";
        $(html1).appendTo($('#pQ'));
        $('#pO option').remove();
        let html2 = "<option value=\"null\" selected>&nbsp;</option>";
        $(html2).appendTo($('#pO'));
    }
}

// 根据前置问题动态生成前置选项
function PQGeneratePO() {
    let pQVal = $.trim($('#pQ option:selected').val());
    let pQText = $.trim($('#pQ option:selected').text());

    if (pQVal != "null") {
        $.ajax({
            async: true, // 异步请求
            type: "get",
            url: CONTEXT_PATH + '/option/getOptionByPQId',
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
    } else {
        $('#pO option').remove();
        let html = "<option value=\"null\" selected>&nbsp;</option>";
        $(html).appendTo($('#pO'));
    }
}

// 动态生成问题类型
function GenerateQT() {
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

                for (let i = 0; i < result.data.length; i++) {
                    if (i == 0) {
                        $('#qt option').remove();
                        let html = "";
                        for (let j = 0; j < result.data[i].list.length; j++) {
                            html += "<option value=\"" + result.data[i].list[j].questiontypeId + "\">" + result.data[i].list[j].questiontypeName + "</option>";
                        }
                        $(html).appendTo($('#qt'));
                    }
                    if (i == 1) {
                        let qtId = result.data[i].qtId;
                        // 遍历select的option，然后设置一项为选中
                        $("#qt option").each(function () {
                            if ($(this).val() == qtId) {
                                $(this).attr("selected", true);
                            }
                        });
                    }
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 动态生成当前问卷的所有问题
function GenerateQNAndQ() {
    let html = "";
    for (let i = 0; i < 5; i++) {
        html +=
            "<div class=\"am-form-group am-cf\">\n" +
            "    <div class=\"left am-text-my\">问题标题：</div>\n" +
            "    <div class=\"right\">\n" +
            "        <input type=\"text\" class=\"am-input-my\" placeholder=\"请输入问题标题\">\n" +
            "    </div>\n" +
            "</div>";
    }
    $(html).appendTo($('#showQNAndQ'));
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
    let qt = $.trim($('#qt option:selected').val());
    let oContent = "";
    let flag = true;// 是否直接生成选项，默认是

    if (qt == "singleChoice" || qt == "multipleChoice" || qt == "judgment") {
        // 单项选择题、多项选择题和判断题，跳转到新建选项页面
        flag = false;
    } else if (qt == "fillBlank") {
        // 填空题 不跳转，重置问题信息
        oContent = "填空题";
    } else if (qt == "score") {
        // 评分题 不跳转，重置问题信息
        oContent = "评分题";
    }

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
                if (flag) {
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
                                Reset();
                                ShowSuccess("问题：" + qTitle + " 保存成功！！！");
                            } else {
                                ShowFailure(result.message);
                            }
                        }
                    });
                } else {
                    ShowSuccess("问题：" + qTitle + " 保存成功！！！");
                    setTimeout(function () {
                        // window.location.href = CONTEXT_PATH + "/option/addOption";
                    }, 1000);
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });

    /*$.ajax({
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
                if (qt == "singleChoice" || qt == "multipleChoice") {
                    window.location.href = CONTEXT_PATH + "/option/addOption";
                } else {
                    ShowSuccess("问题：" + qTitle + " 保存成功！！！");
                    setTimeout(function () {
                        window.location.href = CONTEXT_PATH + "/question/addQuestion";
                    }, 1000);
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });*/
}

// 重置问题信息
function Reset() {
    $("#qTitle").val("");// 重置 问题标题
    $("#qDescription").val("");// 重置 问题描述
    $("input:radio[name='qStatus']")[0].checked = true;// 重置 是否必填

    $("#pQP option[value='null']").attr("selected", true);// 重置 前置问题和前置选项（选择前置问题分页第一项）
    $("#pQP option[value='null']").removeAttr("selected");// 删除属性，否则只能重置一次（属性存在，无法重新设置）
    $("#qt option:first").attr("selected", true);// 重置 问题类型
    $("#qt option:first").removeAttr("selected");// 删除属性，否则只能重置一次（属性存在，无法重新设置）

    $("#qTitle").focus();
    ShowSuccess("问题信息重置成功！！！");
}

function Cancel() {
    window.location.href = CONTEXT_PATH + "/questionnaire/addQuestionnaire";
}