let url = window.location.pathname;
let pOId = "null";// 前置选项Id
let question = new Array();// 保存原始问题信息
let flag = false; // 默认不改变前置问题和前置选项
let deleteQuestion = new Array(); // 保存删除勾选的问题

// 当文档结构完全加载完毕再去执行函数中的代码
$(function () {
    isURL();
    HideQuestion();// 隐藏问题

    GenerateQNTitle();// 生成问卷标题

    $("#items tr td").click(Checkbox);// 点击td元素选中复选框

    $("#pQP").change(PQPGeneratePQ);// 根据前置问题的页数动态生成前置问题
    // 点击前置问题分页下拉框，当前置问题被默认选中后，将自动调用（因为前置问题下拉框的默认选中被改变）PQGeneratePO()
    // 即生成前置问题的同时也生成前置选项，不需要刻意调用 PQGeneratePO()

    $("#pQ").change(PQGeneratePO);// 根据前置问题动态生成前置选项

    $("#update").click(Update);// 从修改问题弹出层中获取修改后最新的问题数据信息
    $("#deleteChoose").click(DeleteChoose);// 删除选择的问题
});

// 判断url是否正确
function isURL() {
    // http://localhost:8080/questionnaire/question/Question?current=7
    // console.log(window.location.protocol);// http:
    // console.log(window.location.host);// localhost:8080
    // console.log(window.location.port);// 8080
    // console.log(window.location.pathname);// /questionnaire/question/Question
    // console.log(window.location.search);// ?current=7
    let index = url.lastIndexOf("/");
    url = url.substring(index + 1, url.length);

    if (url == "Question" || url == "QuestionByQNId" || url == "AllQuestion") {
        Question();
    }
}

// 显示问题
function Question() {
    let flag = $("#hideQuestion").length > 0;
    if (!flag) {
        let html =
            "<li id=\"hideQuestion-li\">\n" +
            "    <button type=\"button\" style=\"background-color: #005cbf;\" class=\"am-btn am-btn-default am-radius am-btn-xs\">问题\n" +
            "        <a id=\"hideQuestion\" href=\"javascript: void(0)\" class=\"am-close am-close-spin\" data-am-modal-close=\"\">×</a>\n" +
            "    </button>\n" +
            "</li>";
        $(html).appendTo($('#show-hide'));
    } else {
        $("#hideQuestion-li").show();
    }
}

// 隐藏问题
function HideQuestion() {
    $("#show-hide").on("mouseenter", "#hideQuestion", function () {
        $(this).click(function () {
            $("#hideQuestion-li").hide();
            window.location.href = CONTEXT_PATH + "/questionnaire/addQuestionnaire";
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
                $('#pQP option').remove();
                let html = "";
                html += "<option value=\"null\" selected>&nbsp;</option>";
                for (let i = 1; i <= result.data.total; i++) {
                    html += "<option value=\"" + i + "\">第&nbsp;" + i + "&nbsp;页</option>";
                }
                $(html).appendTo($('#pQP'));
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 根据前置问题的页数动态生成前置问题
function PQPGeneratePQ() {
    let pQPVal = $.trim($('#pQP option:selected').val());// 前置问题分页值
    let pQPText = $.trim($('#pQP option:selected').text());// 前置问题分页文本

    let qId = question[1];// 问题编号
    // console.log("当前问题编号：" + qId);

    if (pQPVal != "null") {// 选中前置问题分页具体项
        pOId = "null";// 选中前置问题分页具体项
        flag = true;// 改变了前置问题和前置选项
        $.ajax({
            async: true, // 异步请求
            type: "get",
            url: CONTEXT_PATH + '/question/getNoPrependedQuestionPage2ByQnId',
            data: {
                "qId": qId,
                'current': pQPVal,
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    // 1、前置选项列表不能出现当前问题的后置选项
                    let length1 = result.data.length - 1;// 0: 未被前置，无后置问题 1: 被前置，有后置问题
                    $('#pQ option').remove();
                    let html = "";
                    for (let i = length1; i >= 0; i--) {
                        if (i == 0) {
                            let length2 = result.data[i].questionList.length;// 前置选项列表的长度
                            for (let j = 0; j < result.data[i].questionList.length; j++) {
                                if (length1 == 1) {// 当前问题有传递后置问题
                                    let flag = false;
                                    for (let k = 0; k < result.data[1].rearQuestionList.length; k++) {
                                        // 前置问题不能为当前问题的传递后置问题
                                        if (result.data[i].questionList[j].questionId == result.data[1].rearQuestionList[k].questionId) {
                                            length2--;
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        continue;
                                    }
                                }
                                // 前置问题不能为当前问题
                                if (result.data[i].questionList[j].questionId == qId) {
                                    length2--;
                                    continue;
                                }
                                html += "<option value=\"" + result.data[i].questionList[j].questionId + "\">" + result.data[i].questionList[j].questionTitle + "</option>";
                            }
                            if (length2 == 0) {
                                html += "<option value=\"null\">&nbsp;</option>";
                            }
                        }

                    }
                    $(html).appendTo($('#pQ'));
                    $("#pQ option:first").prop("selected", 'selected');
                } else {
                    ShowFailure("前置问题： " + pQPText + " " + result.message);
                }
            }
        });
    } else {// 选中前置问题分页第一项
        pOId = question[8];// 选中前置问题分页第一项
        let pQId = question[6];// 前置问题Id
        let pQTitle;// 前置问题Title
        if (question[7]) {
            pQTitle = question[7];// 前置问题Title
        } else {
            pQTitle = "";// 前置问题Title
        }
        if (pQId != "" && pQTitle != "") {// 有前置问题
            SetPQ(pQId, pQTitle);// 设置前置问题
            flag = true;// 改变了前置问题和前置选项
        } else {// 无前置问题
            $('#pQ option').remove();
            let html1 = "<option value=\"not\" selected>&nbsp;</option>";
            $(html1).appendTo($('#pQ'));
            $('#pO option').remove();
            let html2 = "<option value=\"not\" selected>&nbsp;</option>";
            $(html2).appendTo($('#pO'));
            flag = false;// 没有改变前置问题和前置选项
        }
    }
}

// 设置前置问题
function SetPQ(qId, qTitle) {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getAsyncNull',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                $('#pQ option').remove();
                let html = "<option value=\"null\">&nbsp;</option>";
                html += "<option value=\"" + qId + "\">" + qTitle + "</option>";
                $(html).appendTo($('#pQ'));
                $("#pQ option:last").prop("selected", 'selected');
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 根据前置问题动态生成前置选项
function PQGeneratePO() {
    let pQVal = $.trim($('#pQ option:selected').val());// 前置问题值
    let pQText = $.trim($('#pQ option:selected').text());// 前置问题文本

    if (pQVal != "null" && pQVal != "not") {
        $.ajax({
            async: false, // 异步请求
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

                    if (pOId != "null") {// 选中前置问题分页第一项
                        // 遍历select的option，然后设置一项为选中
                        $('#pO option').each(function () {
                            if ($(this).val() == pOId) {// 遍历选中正确选项
                                $(this).attr("selected", true);
                                // console.log("PQGeneratePO: 遍历选项选中默认选项");
                            }
                        });
                    } else {// 选中前置问题分页具体项
                        $("#pO option:first").prop("selected", 'selected');
                    }
                } else {
                    ShowFailure("前置问题： " + pQText + " " + result.message);
                }
            }
        });
    } else {
        if (pQVal == "not") {
            $('#pO option').remove();
            let html = "<option value=\"not\" selected>&nbsp;</option>";
            $(html).appendTo($('#pO'));
        } else {
            $('#pO option').remove();
            let html = "<option value=\"null\" selected>&nbsp;</option>";
            $(html).appendTo($('#pO'));
        }

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
                        // 遍历select的option，然后设置一项为选中
                        $("#qt option").each(function () {
                            if ($(this).val() == question[5]) {
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

// 点击td元素选中复选框
function Checkbox() {
    let checkbox = $(this).parent().find(":checkbox");// td的父元素为tr，在tr中找到checkbox
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    let row = $(this).parent().index();// 行数，从 0 开始
    let column = $(this).index();// 列数，从 0 开始
    let columns = $(this).parent().find("td").length;// 总列数

    question.push(checkbox.val());// 1 向数组存问题编号

    if (column > 0 && column < columns - 1) {
        // 判断当前tr下的复选框是否被选中
        if (checkbox.prop("checked")) {
            checkbox.prop("checked", false);// 如果被选中，那么就取消选中
        } else {
            checkbox.prop("checked", true);// 如果没有被选中，那么就将其选中
        }
    } else if (column == columns - 1) {
        for (let i = 1; i < cbs.length; i++) {
            cbs[i].checked = false;
        }
        cbs[row + 1].checked = true;

        let udQId = question[0];

        $('#deleteQuestion .am-popup-hd h4').remove();
        let html1 = "";
        $('#deleteQuestion .am-popup-bd .updateQuestion p .Btn').remove();
        let html2 = "";

        if (udQId == "show") {
            html1 = "<h4 class=\"am-popup-title\">查看问题</h4>";
            html2 = "<input style=\"margin-right: 100px;\" class=\"Btn\" type=\"hidden\"/>";
            SetDeleteQuestion();
        }
        if (udQId == "update") {
            SetUpdateQuestion();
        }
        if (udQId == "delete") {
            html1 = "<h4 class=\"am-popup-title\">删除问题</h4>";
            html2 = "<button style=\"margin-right: 100px;\" type=\"button\" class=\"am-btn am-btn-success am-radius Btn\" onclick=\"DeleteSubmit()\">删除</button>";
            SetDeleteQuestion();
        }

        $(html1).appendTo($('#deleteQuestion .am-popup-hd'));
        $(html2).prependTo($('#deleteQuestion .am-popup-bd .updateQuestion p'));
    }
    setChecked(this);
}

// 全选和全不选
function setChecked(obj) {
    let cbs = $("input[name='cb[]']");//获取所有复选框对象

    //JS的if判断中Undefined类型视为false，其他类型视为true；
    //obj.id是定义过的值，类型为非Undefined，所以视为true。
    if (obj.id) {
        for (let i = 1; i < cbs.length; i++) {
            if (obj.checked == true) {
                cbs[i].checked = true;//全选
            } else {
                cbs[i].checked = false;//全不选
            }
        }
    } else {
        //先假设子选择全选，那么使全选框选中。
        cbs[0].checked = true;

        //若子选项没有全选，全选框不选中。
        for (let i = 1; i < cbs.length; i++) {
            if (cbs[i].checked == false) {
                cbs[0].checked = false;
            }
        }
    }
}

// 将当前问题信息填充到修改问题弹出层中
function SetUpdateQuestion() {
    let qId = question[1];// 问题编号
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getQuestionAndPreQuestionByQId',
        data: {
            'qId': qId
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                let length = result.data.length;
                for (let i = 0; i < length; i++) {
                    // 1、当前问题无前置问题（默认不选择前置问题）
                    if (i == 0) {
                        let qTitle = result.data[i].question.questionTitle;
                        let qDescription = result.data[i].question.questionDescription;
                        let qStatus = result.data[i].question.questionStatus;
                        let qtId = result.data[i].question.questiontypeId;

                        question.push(qTitle);// 2 向数组存问题标题
                        question.push(qDescription);// 3 向数组存问题描述
                        question.push(qStatus);// 4 向数组存问题是否必填
                        question.push(qtId);// 5 向数组存问题类型

                        $("#qTitle").val(qTitle);// 填充问题标题
                        $("#qDescription").val(qDescription);// 填充问题描述
                        $(":radio[name='qStatus'][value='" + qStatus + "']").prop("checked", "checked");// 选中正确的是否必填项
                        GeneratePQP();// 动态生成前置问题的页数
                        GenerateQT();// 动态生成问题类型
                    }

                    // 2、当前问题有前置问题（默认选择存在的前置问题）
                    if (i == 1) {
                        let pQId = result.data[i].preQuestion.questionId;
                        if (result.data[0].question.preQuestionId == pQId) {
                            let pQTitle = result.data[i].preQuestion.questionTitle;
                            pOId = result.data[0].question.preOptionId;// 动态改变前置选项，主要用于判断是否需要遍历选项选中正确选项

                            question.push(pQId);// 6 向数组存前置问题编号
                            question.push(pQTitle);// 7 向数组存前置问题标题
                            question.push(pOId);// 8 向数组存前置选项编号

                            SetPQ(pQId, pQTitle);// 设置前置问题
                        }
                    }
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 将当前问题信息填充到删除问题弹出层中
function SetDeleteQuestion() {
    let qId = question[1];// 问题编号
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getQuestionAndPreQuestionAndPreOptionByQId',
        data: {
            'qId': qId
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                let length = result.data.length;
                for (let i = 0; i < length; i++) {
                    // 1、当前问题
                    if (i == 0) {
                        let qTitle = result.data[i].question.questionTitle;
                        let qDescription = result.data[i].question.questionDescription;
                        let qStatus = result.data[i].question.questionStatus;

                        $("#qTitleDelete").val(qTitle);// 填充问题标题
                        $("#qDescriptionDelete").val(qDescription);// 填充问题描述

                        if (qStatus != 0) {
                            $("#qStatusDelete").val("是");// 填充是否必填
                        } else {
                            $("#qStatusDelete").val("否");// 填充是否必填
                        }
                    }

                    // 2、当前问题的问题类型
                    if (i == 1) {
                        let qtId = result.data[i].questionType.questiontypeId;
                        if (result.data[0].question.questiontypeId == qtId) {
                            let qtName = result.data[i].questionType.questiontypeName;
                            $("#qtDelete").val(qtName);// 填充问题类型
                        }
                    }

                    // 3、当前问题的前置问题
                    if (i == 2) {
                        let pQId = result.data[i].preQuestion.questionId;
                        if (result.data[0].question.preQuestionId == pQId) {
                            let pQTitle = result.data[i].preQuestion.questionTitle;
                            $("#pQDelete").val(pQTitle);// 填充前置问题
                        }
                    }

                    // 4、当前问题的前置选项
                    if (i == 3) {
                        let pOId = result.data[i].preOption.optionId;
                        if (result.data[0].question.preOptionId == pOId) {
                            let pOContent = result.data[i].preOption.optionContent;
                            $("#pODelete").val(pOContent);// 填充前置选项
                        }
                    }
                }
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

// 提交问题前表单验证 从修改问题弹出层中获取修改后最新的问题数据信息
function Update() {
    if (!QTitleExists()) {
        return false;
    }

    if (!QDescriptionExists()) {
        return false;
    }

    UpdateSubmit();
}

// 提交问题信息
function UpdateSubmit() {
    let qId = question[1];// 问题编号
    let qTitle = $.trim($("#qTitle").val());
    let qDescription = $.trim($("#qDescription").val());
    let qStatus = $.trim($("input[name='qStatus']:checked").val());
    let pQVal = $.trim($('#pQ option:selected').val());
    let pOVal = $.trim($('#pO option:selected').val());
    let qt = $.trim($('#qt option:selected').val());
    // console.log(
    //     qTitle + " " +
    //     qDescription + " " +
    //     qStatus + " " +
    //     pQVal + " " +
    //     pOVal + " " +
    //     qt
    // );
    // let str = "";
    // for (let i in question) {
    //     str += question[i] + " ";
    // }
    // console.log(str);

    if (flag) {
        if (
            question[2] == qTitle &&
            question[3] == qDescription &&
            question[4] == qStatus &&
            question[5] == qt &&
            question[6] == pQVal &&
            question[8] == pOVal

        ) {
            ShowWarn("内容未修改！！！");
            $("#updateCancel").click();
            return;
        }
    } else {
        if (
            question[2] == qTitle &&
            question[3] == qDescription &&
            question[4] == qStatus &&
            question[5] == qt
        ) {
            ShowWarn("内容未修改！！！");
            $("#updateCancel").click();
            return;
        }
    }

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/question/updateSubmit',
        data: {
            'qId': qId,
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
                ShowSuccess("修改成功！！！");
                if (url == "AllQuestion") {
                    window.location.href = CONTEXT_PATH + "/question/AllQuestion" + window.location.search;
                } else {
                    window.location.href = CONTEXT_PATH + "/question/Question" + window.location.search;
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 删除问题信息
function DeleteSubmit() {
    let qId = question[1];// 问题编号

    $('#my-confirm').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            $.ajax({
                async: true, // 异步请求
                type: "post",
                // url: CONTEXT_PATH + '/question/deleteSubmit1',
                url: CONTEXT_PATH + '/question/deleteSubmit2',
                data: {
                    'qId': qId
                },
                dataType: 'json',
                success: function (result) {
                    if (result.state == 1) {
                        ShowSuccess("删除成功！！！");
                        window.location.href = CONTEXT_PATH + "/question/Question" + window.location.search;
                    } else {
                        ShowFailure(result.message);
                    }
                }
            });
        },
        onCancel: function () {
            $("#deleteCancel").click();
        },
    });
}

// 删除选择的问题
function DeleteChoose() {
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    for (let i = 1; i < cbs.length; i++) {
        if (cbs[i].checked) {
            deleteQuestion.push($(cbs[i]).val());
        }
    }
    if (deleteQuestion.length > 0) {
        $('#my-confirm').modal({
            relatedTarget: this,
            onConfirm: function (options) {
                $.ajax({
                    async: true, // 异步请求
                    type: "post",
                    url: CONTEXT_PATH + '/question/deleteSubmit3',
                    data: {
                        'question': JSON.stringify(deleteQuestion),
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.state == 1) {
                            ShowSuccess("删除成功！！！");
                            window.location.href = CONTEXT_PATH + "/question/Question" + window.location.search;
                        } else {
                            ShowFailure(result.message);
                        }
                    }
                });
            },
            onCancel: function () {
                for (let i = 1; i < cbs.length; i++) {
                    if (cbs[i].checked) {
                        cbs[i].checked = false;//不选
                    }
                }
                deleteQuestion.length = 0;
            },
        });
    } else {
        ShowFailure("请选择需要删除的问题！！！");
    }
}

// 点击显示问题按钮
function ShowQuestion() {
    question.length = 0;//将原始问题信息清空
    question.push("show");// 0 向数组存问题操作
}

// 点击修改问题按钮
function UpdateQuestion() {
    question.length = 0;//将原始问题信息清空
    question.push("update");// 0 向数组存问题操作
}

// 点击删除问题按钮
function DeleteQuestion() {
    question.length = 0;//将原始问题信息清空
    question.push("delete");// 0 向数组存问题操作
    $("#pQDelete").val("");// 填充问题描述
    $("#pODelete").val("");// 填充问题描述
}
