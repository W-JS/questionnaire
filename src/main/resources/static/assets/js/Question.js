let pOId = "null";// 前置选项Id
let question = new Array();// 保存原始问题信息
let flag = false; // 默认没有前置问题和前置选项

// 当文档结构完全加载完毕再去执行函数中的代码
$(function () {
    isURL();
    HideQuestion();// 隐藏问题

    $("#items tr td").click(Checkbox);// 点击td元素选中复选框

    $("#pQP").change(PQPGeneratePQ);// 根据前置问题的页数动态生成前置问题
    // 点击前置问题分页下拉框，当前置问题被默认选中后，将自动调用（因为前置问题下拉框的默认选中被改变）PQGeneratePO()
    // 即生成前置问题的同时也生成前置选项，不需要刻意调用 PQGeneratePO()
    $("#pQ").change(PQGeneratePO);// 根据前置问题动态生成前置选项

    $("#update").click(Update);// 从修改问题弹出层中获取修改后最新的问题数据信息

});

// 判断url是否正确
function isURL() {
    let url = window.location.pathname;
    let index = url.lastIndexOf("/")
    url = url.substring(index + 1, url.length);

    if (url == "Question") {
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
        url: CONTEXT_PATH + '/question/getNotFrontQuestionRowsByQnId',
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

    // let qId = question[0];// 问题编号
    let qId = $("#qId").val();

    if (pQPVal != "null") {// 选中前置问题分页具体项
        pOId = "null";// 选中前置问题分页具体项
        $.ajax({
            async: true, // 异步请求
            type: "get",
            url: CONTEXT_PATH + '/question/getNotFrontQuestionPageByQnId',
            data: {
                'current': pQPVal,
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    $('#pQ option').remove();
                    let html = "";
                    for (let i = 0; i < result.data.length; i++) {
                        if (qId != result.data[i].questionId) {// 前置问题不能为当前问题
                            html += "<option value=\"" + result.data[i].questionId + "\">" + result.data[i].questionTitle + "</option>";
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
        // pOId = question[7];// 选中前置问题分页第一项
        pOId = $("#pOId").val();// 选中前置问题分页第一项
        console.log("pOId：" + pOId);
        // let pQId = question[5];// 前置问题Id
        let pQId = $("#pQId").val();// 前置问题Id
        // let pQTitle = question[6];// 前置问题Title
        let pQTitle = $("#pQTitle").val();// 前置问题Title
        if (pQId != "" && pQTitle != "") {// 有前置问题
            SetPQ(pQId, pQTitle);// 设置前置问题
        } else {// 无前置问题
            $('#pQ option').remove();
            let html1 = "<option value=\"not\" selected>&nbsp;</option>";
            $(html1).appendTo($('#pQ'));
            $('#pO option').remove();
            let html2 = "<option value=\"not\" selected>&nbsp;</option>";
            $(html2).appendTo($('#pO'));
        }
    }
}

/*// 重新设置前置问题
function ResetPQ(qId) {
    console.log("ResetPQ: 重新设置前置问题");
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getQuestionByQnIdAndQId',
        data: {
            'qId': qId
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                $('#pQ option').remove();
                // let html = "<option value=\"" + result.data.questionId + "\" selected>" + result.data.questionTitle + "</option>";
                let html = "<option value=\"" + result.data.questionId + "\">" + result.data.questionTitle + "</option>";
                $(html).appendTo($('#pQ'));
                $("#pQ option:first").prop("selected", 'selected');
            } else {
                ShowFailure(result.message);
            }
        }
    });
}*/

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
            url: CONTEXT_PATH + '/option/getOptionByQId',
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
                        console.log("PQGeneratePO: 遍历选项");
                        // 遍历select的option，然后设置一项为选中
                        $('#pO option').each(function () {
                            if ($(this).val() == pOId) {// 遍历选中正确选项
                                $(this).attr("selected", true);
                                console.log("PQGeneratePO: 选中默认选项");
                            }
                        });
                    } else {// 选中前置问题分页具体项
                        console.log("PQGeneratePO: 不遍历选项");
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
// function GenerateQT() {
function GenerateQT(qtId) {
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
                            // if ($(this).val() == question[4]) {
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

// 点击td元素选中复选框
function Checkbox() {
    let checkbox = $(this).parent().find(":checkbox");// td的父元素为tr，在tr中找到checkbox
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    let row = $(this).parent().index();// 行数，从 0 开始
    let column = $(this).index();// 列数，从 0 开始
    let columns = $(this).parent().find("td").length;// 总列数

    $("#qId").val(checkbox.val());
    question.push(checkbox.val());// 向数组存问题编号

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

        let udQId = $("#udQId").val();
        if (udQId == "update") {
            SetUpdateQuestion();
        }
        if (udQId == "delete") {
            SetDeleteQuestion();
        }
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
    // let qId = question[0];// 问题编号
    let qId = $("#qId").val();
    console.log("qId：" + qId)
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getQuestionByQnIdAndQId',
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
                        $("#qTitle").val(qTitle);// 填充问题标题
                        $("#qDescription").val(qDescription);// 填充问题描述
                        $(":radio[name='qStatus'][value='" + qStatus + "']").prop("checked", "checked");// 选中正确的是否必填项
                        GeneratePQP();// 动态生成前置问题的页数
                        // GenerateQT();// 动态生成问题类型
                        GenerateQT(qtId);// 动态生成问题类型

                        question.push(qTitle);// 向数组存问题标题
                        question.push(qDescription);// 向数组存问题描述
                        question.push(qStatus);// 向数组存问题是否必填
                        question.push(qtId);// 向数组存问题类型
                        flag = false;// 没有前置问题和前置选项
                    }

                    // 2、当前问题有前置问题（默认选择存在的前置问题）
                    if (i == 1) {
                        let pQId = result.data[i].preQuestion.questionId;
                        if (result.data[0].question.preQuestionId == pQId) {
                            pOId = result.data[0].question.preOptionId;// 动态改变前置选项，主要用于判断是否需要遍历选项选中正确选项
                            $("#pQId").val(pQId);// 前置问题Id
                            let pQTitle = result.data[i].preQuestion.questionTitle;
                            $("#pQTitle").val(pQTitle);// 前置问题Title
                            $("#pOId").val(pOId);// 设置前置选项，只在当前行有效
                            SetPQ(pQId, pQTitle);// 设置前置问题

                            question.push(pQId);// 向数组存前置问题编号
                            question.push(pQTitle);// 向数组存前置问题标题
                            // question.push(result.data[0].question.preOptionId);// 向数组存问题编号
                            question.push(pOId);// 向数组存前置选项编号
                            flag = true;// 有前置问题和前置选项
                        }
                    }
                }
                console.log("开始");
                // for (let i in question) {
                //     console.log(question[i]);
                // }
                for (let i = 0; i < question.length; i++) {
                    console.log(question[i]);
                }
                console.log("结束");

                console.log("问题长度：" + question.length);
                console.log("问题操作：" + question[0]);
                console.log("问题编号：" + question[1]);
                console.log("问题标题：" + question[2]);
                console.log("问题描述：" + question[3]);
                console.log("是否必填：" + question[4]);
                console.log("问题类型：" + question[5]);
                if (flag) {
                    console.log("前置问题编号：" + question[6]);
                    console.log("前置问题标题：" + question[7]);
                    console.log("前置选项编号：" + question[8]);
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 将当前问题信息填充到删除问题弹出层中
function SetDeleteQuestion() {
    // let qId = question[0];// 问题编号
    let qId = $("#qId").val();
    console.log("删除问题: " + qId)

    /*$.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getAllQuestionByQnId',
        data: {
            'qId': qId,
            'qCreateTime': getNowFormatDate()
        },
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
    });*/
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

    updateSubmit();
}

// 提交问题信息
function updateSubmit() {
    // let qId = $.trim(question[0]);// 问题编号
    let qId = $.trim($("#qId").val());
    let qTitle = $.trim($("#qTitle").val());
    // let qTitle = $.trim($("#qTitle").val());
    let qDescription = $.trim($("#qDescription").val());
    let qStatus = $.trim($("input[name='qStatus']:checked").val());
    let pQVal = $.trim($('#pQ option:selected').val());
    let pOVal = $.trim($('#pO option:selected').val());
    let qt = $.trim($('#qt option:selected').val());
    console.log("qId: " + qId);
    console.log("qTitle: " + qTitle);
    console.log("qDescription: " + qDescription);
    console.log("qStatus: " + qStatus);
    console.log("pQVal: " + pQVal);
    console.log("pOVal: " + pOVal);
    console.log("qt: " + qt);

    /*$.ajax({
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
                // $("#updateCancel").click();
            } else {
                ShowFailure(result.message);
            }
        }
    });*/
}

// 点击修改问题按钮
function UpdateQuestion() {
    question.length = 0;//将原始问题信息清空
    $("#udQId").val("update");
    question.push("update");

    // $("#pQId").val("");// 将前置问题Id置为空
    // $("#pQTitle").val("");// 将前置问题Title置为空

}

// 点击删除问题按钮
function DeleteQuestion() {
    $("#udQId").val("delete");
}
