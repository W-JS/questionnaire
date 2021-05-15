let questionnaire = new Array();// 保存原始问卷信息
let deleteQuestionnaire = new Array(); // 保存删除勾选的问题
let cancelFlag = false;// 是否已经保存和修改

$(function () {
    GenerateMessageManagement();// 生成消息管理数量
    GenerateOnlineUser();// 生成在线用户信息

    $("#items tr td").click(Checkbox);// 点击td元素选中复选框

    $("#deleteChoose").click(DeleteChoose);// 删除选择的问卷
});

// 点击td元素选中复选框
function Checkbox() {
    let checkbox = $(this).parent().find(":checkbox");// td的父元素为tr，在tr中找到checkbox
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    let row = $(this).parent().index();// 行数，从 0 开始
    let column = $(this).index();// 列数，从 0 开始
    let columns = $(this).parent().find("td").length;// 总列数

    questionnaire.push(checkbox.val());// 1 向数组存问卷编号

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

        SetQuestionnaire();// 设置选项弹出层
        let udQNId = questionnaire[0];

        if (udQNId == "show") {
            $("#questionnaireOperation").text("查看问卷");

            $("#qnTitle").attr("disabled", "disabled");
            $("#qnFuTitle").attr("disabled", "disabled");
            $("#qnDescription").attr("disabled", "disabled");

            $("#save").attr("hidden", "hidden");
            $("#update").attr("hidden", "hidden");
            $("#reset").attr("hidden", "hidden");
        }
        if (udQNId == "update") {
            $("#questionnaireOperation").text("修改问卷");

            $("#qnTitle").removeAttr("disabled");
            $("#qnFuTitle").removeAttr("disabled");
            $("#qnDescription").removeAttr("disabled");

            $("#update").removeAttr("hidden");
            $("#reset").removeAttr("hidden");
            $("#save").attr("hidden", "hidden");
        }
        if (udQNId == "delete") {
            DeleteSubmit();
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

// 将当前问卷信息填充到问卷弹出层中
function SetQuestionnaire() {
    let qnId = questionnaire[1];// 问卷编号
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/questionnaire/getQuestionnaireByQnId',
        data: {
            'qnId': qnId
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                let qnTitle = result.data.questionnaireTitle;
                let qnFuTitle = result.data.questionnaireFuTitle;
                let qnDescription = result.data.questionnaireDescription;

                questionnaire.push(qnTitle);// 2 向数组存问卷标题
                questionnaire.push(qnFuTitle);// 3 向数组存问卷副标题
                questionnaire.push(qnDescription);// 4 向数组存问卷描述

                $("#qnTitle").val(qnTitle);// 填充问卷标题
                $("#qnFuTitle").val(qnFuTitle);// 填充问卷副标题
                $("#qnDescription").val(qnDescription);// 填充问卷描述
            }
        }
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
                ShowSuccess("问卷：" + qnTitle + " 保存成功！！！");
                cancelFlag = true;
                $("#cancel").click();
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 提交问卷前表单验证 从修改问卷弹出层中获取修改后最新的问卷数据信息
function Update() {
    if (!QNTitleExists()) {
        return false;
    }

    if (!QNFuTitleExists()) {
        return false;
    }

    if (!QNDescriptionExists()) {
        return false;
    }

    UpdateSubmit();
}

// 提交问卷信息
function UpdateSubmit() {
    let qnId = questionnaire[1];// 问卷编号
    let qnTitle = $.trim($("#qnTitle").val());
    let qnFuTitle = $.trim($("#qnFuTitle").val());
    let qnDescription = $.trim($("#qnDescription").val());

    if (
        questionnaire[2] == qnTitle &&
        questionnaire[3] == qnFuTitle &&
        questionnaire[4] == qnDescription
    ) {
        ShowWarn("内容未修改！！！");
        $("#cancel").click();
        return;
    }

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/questionnaire/updateSubmit',
        data: {
            'qnId': qnId,
            'qnTitle': qnTitle,
            'qnFuTitle': qnFuTitle,
            'qnDescription': qnDescription,
            'qnCreateTime': getNowFormatDate()
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                ShowSuccess("修改成功！！！");
                setTimeout(function () {
                    window.location.href = window.location.pathname + window.location.search;
                }, 1000);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 提交问卷信息
function DeleteSubmit() {
    let qnId = questionnaire[1];// 问卷编号

    $('#questionnaireModal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            $.ajax({
                async: true, // 异步请求
                type: "post",
                url: CONTEXT_PATH + '/questionnaire/deleteSubmit1',
                data: {
                    'qnId': qnId
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
            $("#cancel").click();
        },
    });
}

// 删除选择的问题
function DeleteChoose() {
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    for (let i = 1; i < cbs.length; i++) {
        if (cbs[i].checked) {
            deleteQuestionnaire.push($(cbs[i]).val());
        }
    }
    if (deleteQuestionnaire.length > 0) {
        $('#questionnaireModal').modal({
            relatedTarget: this,
            onConfirm: function (options) {
                $.ajax({
                    async: true, // 异步请求
                    type: "post",
                    url: CONTEXT_PATH + '/questionnaire/deleteSubmit2',
                    data: {
                        'questionnaire': JSON.stringify(deleteQuestionnaire),
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
                for (let i = 1; i < cbs.length; i++) {
                    if (cbs[i].checked) {
                        cbs[i].checked = false;//不选
                    }
                }
                deleteQuestionnaire.length = 0;
            },
        });
    } else {
        ShowFailure("请选择需要删除的问题！！！");
    }
}

// 重置问卷信息
function Reset() {
    let udQNId = questionnaire[0];
    if (udQNId == "add") {
        $("#qnTitle").val("");// 重置 问卷标题
        $("#qnFuTitle").val("");// 重置 问卷副标题
        $("#qnDescription").val("");// 重置 问卷描述
        $("#qnTitle").focus();
        ShowSuccess("新建问卷信息重置成功！！！");
    } else if (udQNId == "update") {
        $("#qnTitle").val(questionnaire[2]);// 填充问卷标题
        $("#qnFuTitle").val(questionnaire[3]);// 填充问卷副标题
        $("#qnDescription").val(questionnaire[4]);// 填充问卷描述
        ShowSuccess("修改问卷信息重置成功！！！");
    }
}

// 退出问卷弹出层
function Cancel() {
    let udQNId = questionnaire[0];
    if (udQNId == "add" || udQNId == "update") {
        if (cancelFlag) {
            window.location.href = window.location.pathname + window.location.search;
        }
    }
}

// 点击新建问卷按钮
function AddQuestionnaire() {
    questionnaire.length = 0;//将原始问卷信息清空
    questionnaire.push("add");// 0 向数组存问卷操作

    $("#qnTitle").val("");// 重置 问卷标题
    $("#qnFuTitle").val("");// 重置 问卷副标题
    $("#qnDescription").val("");// 重置 问卷描述

    $("#questionnaireOperation").text("新建选项");

    $("#qnTitle").removeAttr("disabled");
    $("#qnFuTitle").removeAttr("disabled");
    $("#qnDescription").removeAttr("disabled");

    $("#save").removeAttr("hidden");
    $("#reset").removeAttr("hidden");
    $("#update").attr("hidden", "hidden");
}

// 点击修改问卷按钮
function UpdateQuestionnaire() {
    questionnaire.length = 0;//将原始问卷信息清空
    questionnaire.push("update");// 0 向数组存问卷操作
}

// 点击删除问卷按钮
function DeleteQuestionnaire() {
    questionnaire.length = 0;//将原始问卷信息清空
    questionnaire.push("delete");// 0 向数组存问卷操作
}