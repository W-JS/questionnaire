let url = window.location.pathname;
let option = new Array();// 保存原始选项信息
let deleteOption = new Array(); // 保存删除勾选的问题

$(function () {
    isURL();
    HideOption();// 隐藏选项

    // GenerateQNTitle();// 生成
    // GenerateQTitle();// 生成问题标题
    GenerateQNTitleAndQTitle();// 生成问卷标题和问题标题

    $("#items tr td").click(Checkbox);// 点击td元素选中复选框

    $("#deleteChoose").click(DeleteChoose);// 删除选择的选项
});

// 判断url是否正确
function isURL() {
    let index = url.lastIndexOf("/")
    url = url.substring(index + 1, url.length);

    if (url == "Option" || url == "OptionByQId" || url == "AllOption") {
        Option();
    }
}

// 显示选项
function Option() {
    let flag = $("#hideOption").length > 0;
    if (!flag) {
        let html =
            "<li id=\"hideOption-li\">\n" +
            "    <button type=\"button\" style=\"background-color: #005cbf;\" class=\"am-btn am-btn-default am-radius am-btn-xs\">选项\n" +
            "        <a id=\"hideOption\" href=\"javascript: void(0)\" class=\"am-close am-close-spin\" data-am-modal-close=\"\">×</a>\n" +
            "    </button>\n" +
            "</li>";
        $(html).appendTo($('#show-hide'));
    } else {
        $("#hideOption-li").show();
    }
}

// 隐藏选项
function HideOption() {
    $("#show-hide").on("mouseenter", "#hideOption", function () {
        $(this).click(function () {
            $("#hideOption-li").hide();
            window.location.href = CONTEXT_PATH + "/question/addQuestion";
        })
    });
}

// 点击td元素选中复选框
function Checkbox() {
    let checkbox = $(this).parent().find(":checkbox");// td的父元素为tr，在tr中找到checkbox
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    let row = $(this).parent().index();// 行数，从 0 开始
    let column = $(this).index();// 列数，从 0 开始
    let columns = $(this).parent().find("td").length;// 总列数

    option.push(checkbox.val());// 1 向数组存选项编号

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

        let udOId = option[0];

        $('#updateOption .am-popup-hd h4').remove();
        let html1 = "";
        $('#updateOption .am-popup-bd .updateOption p .Btn').remove();
        let html2 = "";

        $("#oContent").attr("disabled", "disabled");

        if (udOId == "show") {
            html1 = "<h4 class=\"am-popup-title\">查看选项</h4>";
            html2 = "<input style=\"margin-right: 100px;\" class=\"Btn\" type=\"hidden\"/>";
        }
        if (udOId == "update") {
            $("#oContent").removeAttr("disabled");
            html1 = "<h4 class=\"am-popup-title\">修改选项</h4>";
            html2 = "<button style=\"margin-right: 100px;\" type=\"button\" class=\"am-btn am-btn-success am-radius Btn\" onclick=\"Update()\">修改</button>";
        }
        if (udOId == "delete") {
            html1 = "<h4 class=\"am-popup-title\">删除选项</h4>";
            html2 = "<button style=\"margin-right: 100px;\" type=\"button\" class=\"am-btn am-btn-success am-radius Btn\" onclick=\"DeleteSubmit()\">删除</button>";
        }

        $(html1).appendTo($('#updateOption .am-popup-hd'));
        $(html2).prependTo($('#updateOption .am-popup-bd .updateOption p'));
        SetUpdateOption();
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

// 将当前选项信息填充到修改选项弹出层中
function SetUpdateOption() {
    let oId = option[1];// 选项编号
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/option/getOptionByOId',
        data: {
            'oId': oId
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                let oContent = result.data.optionContent;

                option.push(oContent);// 2 向数组存选项内容

                $("#oContent").val(oContent);// 填充选项内容
            } else {
                ShowFailure(result.message);
            }
        }
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

// 提交选项前表单验证 从修改选项弹出层中获取修改后最新的选项数据信息
function Update() {
    if (!OContentExists()) {
        return false;
    }

    UpdateSubmit();
}

// 提交选项信息
function UpdateSubmit() {
    let oId = option[1];// 选项编号
    let oContent = $.trim($("#oContent").val());

    if (option[2] == oContent) {
        ShowWarn("内容未修改！！！");
        $("#updateCancel").click();
        return;
    }

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/option/updateSubmit',
        data: {
            'oId': oId,
            'oContent': oContent,
            'oCreateTime': getNowFormatDate()
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                ShowSuccess("修改成功！！！");
                window.location.href = window.location.pathname + window.location.search;
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 提交选项信息
function DeleteSubmit() {
    let oId = option[1];// 信息编号

    $('#my-confirm').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            console.log(oId);
            $.ajax({
                async: true, // 异步请求
                type: "post",
                url: CONTEXT_PATH + '/option/deleteSubmit1',
                data: {
                    'oId': oId
                },
                dataType: 'json',
                success: function (result) {
                    if (result.state == 1) {
                        ShowSuccess("删除成功！！！");
                        window.location.href = window.location.pathname + window.location.search;
                    } else {
                        ShowFailure(result.message);
                    }
                }
            });
        },
        onCancel: function () {
            $("#updateCancel").click();
        },
    });
}

// 删除选择的选项
function DeleteChoose() {
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    for (let i = 1; i < cbs.length; i++) {
        if (cbs[i].checked) {
            deleteOption.push($(cbs[i]).val());
        }
    }
    if (deleteOption.length > 0) {
        $('#my-confirm').modal({
            relatedTarget: this,
            onConfirm: function (options) {
                $.ajax({
                    async: true, // 异步请求
                    type: "post",
                    url: CONTEXT_PATH + '/option/deleteSubmit2',
                    data: {
                        'option': JSON.stringify(deleteOption),
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.state == 1) {
                            ShowSuccess("删除成功！！！");
                            window.location.href = window.location.pathname + window.location.search;
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
                deleteOption.length = 0;
            },
        });
    } else {
        ShowFailure("请选择需要删除的问题！！！");
    }
}

// 点击显示选项按钮
function ShowOption() {
    option.length = 0;//将原始选项信息清空
    option.push("show");// 0 向数组存选项操作
}

// 点击修改选项按钮
function UpdateOption() {
    option.length = 0;//将原始选项信息清空
    option.push("update");// 0 向数组存选项操作
}

// 点击删除选项按钮
function DeleteOption() {
    option.length = 0;//将原始选项信息清空
    option.push("delete");// 0 向数组存选项操作
}