let userMessageArray = new Array();// 保存原始用户信息

$(function () {
    UserMessage();// 保存原始个人信息

    $("#birthday").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });

    $("#userName").blur(Username);
    $("#userPhone").blur(Phone);
    $("#userEmail").blur(Email);

    $("#oldPassword").blur(OldPassword);
    $("#newPassword").blur(NewPassword);
    $("#confirmPassword").blur(ConfirmPassword);
});

// 动态生成前置问题的页数
/*
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
}*/

// 提交问题信息
/*function saveSubmit() {
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
                                ShowSuccess("问题：" + qTitle + " 保存成功！！！");
                                setTimeout(function () {
                                    window.location.href = window.location.pathname + window.location.search;
                                }, 1000);
                            } else {
                                ShowFailure(result.message);
                            }
                        }
                    });
                } else {
                    ShowSuccess("问题：" + qTitle + " 保存成功！！！");
                    setTimeout(function () {
                        window.location.href = window.location.pathname + window.location.search;
                    }, 1000);
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });

    /!*$.ajax({
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
    });*!/
}*/

// 保存原始个人信息
function UserMessage() {
    userMessageArray['userName'] = $("#userName").val();
    userMessageArray['userPhone'] = $("#userPhone").val();
    userMessageArray['userEmail'] = $("#userEmail").val();
    userMessageArray['sex'] = $(":radio[name='sex']:checked").val();
    userMessageArray['birthday'] = $("#birthday").val();
}

// 验证用户名是否正确
function Username() {
    let flag = false;
    let userName = $.trim($("#userName").val());
    let userNameValidate = /^[\u4e00-\u9fa5a-zA-Z0-9_-]{3,16}$/;
    if (!userName || userName.length == 0) {
        ShowFailure('用户名不能为空');
    } else {
        if (!userNameValidate.test(userName)) {
            ShowFailure('用户名格式不符合要求,用户名长度在3到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证用户名是否存在
function UsernameExists() {
    let flag = false;
    if (!Username()) {
        $("#userName").focus();
    } else {
        let userName = $.trim($("#userName").val());
        if (userMessageArray['userName'] != userName) {
            $.ajax({
                async: false, // 同步请求
                type: "get",
                url: CONTEXT_PATH + '/user/usernameRegExists',
                data: {
                    'usernameReg': userName,
                },
                dataType: 'json',
                success: function (result) {
                    if (result.state == 1) {
                        $("#userName").focus();
                        ShowFailure('该用户名已存在！！！');
                    } else {
                        flag = true;
                    }
                }
            });
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证手机号是否正确
function Phone() {
    let flag = false;
    let userPhone = $.trim($("#userPhone").val());
    let userPhoneValidate = /^((\d{3}-\d{8}|\d{4}-\d{7,8})|(1[3|5|7|8][0-9]{9}))$/;
    if (!userPhone || userPhone.length == 0) {
        ShowFailure('手机号不能为空');
    } else {
        if (!userPhoneValidate.test(userPhone)) {
            ShowFailure('手机号格式不符合要求');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证手机号是否存在
function PhoneExists() {
    let flag = false;
    if (!Phone()) {
        $("#userPhone").focus();
    } else {
        let userPhone = $.trim($("#userPhone").val());
        if (userMessageArray['userPhone'] != userPhone) {
            $.ajax({
                async: false, // 同步请求
                type: "get",
                url: CONTEXT_PATH + '/user/phoneRegExists',
                data: {
                    'phoneReg': userPhone,
                },
                dataType: 'json',
                success: function (result) {
                    if (result.state == 1) {
                        $("#userPhone").focus();
                        ShowFailure('该手机号已存在！！！');
                    } else {
                        flag = true;
                    }
                }
            });
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证电子邮件是否正确
function Email() {
    let flag = false;
    let userEmail = $.trim($("#userEmail").val());
    let userEmailValidate = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    if (!userEmail || userEmail.length == 0) {
        ShowFailure('电子邮件不能为空');
    } else {
        if (!userEmailValidate.test(userEmail)) {
            ShowFailure('电子邮件格式不符合要求');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证电子邮件是否存在
function EmailExists() {
    let flag = false;
    if (!Email()) {
        $("#userEmail").focus();
    } else {
        let userEmail = $.trim($("#userEmail").val());
        if (userMessageArray['userEmail'] != userEmail) {
            $.ajax({
                async: false, // 同步请求
                type: "get",
                url: CONTEXT_PATH + '/user/emailRegExists',
                data: {
                    'emailReg': userEmail,
                },
                dataType: 'json',
                success: function (result) {
                    if (result.state == 1) {
                        $("#userEmail").focus();
                        ShowFailure('该电子邮箱已存在！！！');
                    } else {
                        flag = true;
                    }
                }
            });
        } else {
            flag = true;
        }
    }
    return flag;
}

// 更新个人信息
function Update1() {
    if (!UsernameExists()) {
        return false;
    }
    if (!PhoneExists()) {
        return false;
    }
    if (!EmailExists()) {
        return false;
    }

    let userId = $("#userId").val();
    let userName = $("#userName").val();
    let userPhone = $("#userPhone").val();
    let userEmail = $("#userEmail").val();
    let sex = $(":radio[name='sex']:checked").val();
    let birthday = $("#birthday").val();
    if (
        userMessageArray['userName'] == userName &&
        userMessageArray['userPhone'] == userPhone &&
        userMessageArray['userEmail'] == userEmail &&
        userMessageArray['sex'] == sex &&
        userMessageArray['birthday'] == birthday) {
        ShowWarn("个人信息未修改！！！");
        return;
    }

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/updateSubmit1',
        data: {
            'userId': userId,
            'userName': userName,
            'userPhone': userPhone,
            'userEmail': userEmail,
            'sex': sex,
            'birthday': birthday,
            'userUpdateTime': getNowFormatDate()
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                ShowSuccess("个人信息更新成功！！！");
                setTimeout(function () {
                    window.location.href = window.location.pathname + window.location.search;
                }, 1000);

            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 重置个人信息
function Reset1() {
    $("#userName").val(userMessageArray['userName']);
    $("#userPhone").val(userMessageArray['userPhone']);
    $("#userEmail").val(userMessageArray['userEmail']);
    $(":radio[name='sex']").each(function () {
        if (this.value == userMessageArray['sex']) {
            this.checked = true;
        }
    });
    $("#birthday").val(userMessageArray['birthday']);
    ShowSuccess("个人信息重置成功！！！");
}

// 验证旧密码是否正确
function OldPassword() {
    let flag = false;
    let oldPassword = $.trim($("#oldPassword").val());
    let passwordValidate = /^[0-9a-zA-Z_]{6,16}$/;
    if (!oldPassword || oldPassword.length == 0) {
        ShowFailure('原密码不能为空');
    } else {
        if (!passwordValidate.test(oldPassword)) {
            ShowFailure('密码格式不符合要求,密码长度在6到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证新密码是否正确
function NewPassword() {
    let flag = false;
    let newPassword = $.trim($("#newPassword").val());
    let passwordValidate = /^[0-9a-zA-Z_]{6,16}$/;
    if (!newPassword || newPassword.length == 0) {
        ShowFailure('新密码不能为空');
    } else {
        if (!passwordValidate.test(newPassword)) {
            ShowFailure('密码格式不符合要求,密码长度在6到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证确认密码是否正确
function ConfirmPassword() {
    let flag = false;
    let oldPassword = $.trim($("#oldPassword").val());
    let newPassword = $.trim($("#newPassword").val());

    if (!OldPassword()) {
        $("#oldPassword").focus();
    } else if (!NewPassword()) {
        $("#newPassword").focus();
    } else if (oldPassword == newPassword) {
        $("#newPassword").val("");
        $("#newPassword").focus();
        ShowFailure("新密码不能与原密码相同！！！");
    } else {
        let confirmPassword = $.trim($("#confirmPassword").val());
        if (newPassword != confirmPassword) {
            $("#confirmPassword").focus();
            ShowFailure('确认密码不正确，请重新输入！！！');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 判断原密码是否与数据库中保存的密码相等
function PasswordExists() {
    let flag = true;
    let userId = $("#userId").val();
    let oldPassword = $.trim($("#oldPassword").val());
    $.ajax({
        async: false, // 同步请求
        type: "get",
        url: CONTEXT_PATH + '/user/passwordExists',
        data: {
            'userId': userId,
            'password': oldPassword,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 0) {
                $("#oldPassword").val("");
                $("#oldPassword").focus();
                ShowFailure(result.message);
                flag = false;
            }
        }
    });
    return flag;
}

// 更新密码
function Update2() {
    if (!ConfirmPassword()) {
        return false;
    }

    if (!PasswordExists()) {
        return false;
    }

    let userId = $("#userId").val();
    let newPassword = $.trim($("#newPassword").val());
    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/updateSubmit2',
        data: {
            'userId': userId,
            'password': newPassword,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                ShowSuccess("密码修改成功！！！");
                setTimeout(function () {
                    Exit();
                }, 1000);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 重置密码
function Reset2() {
    $("#oldPassword").val("");
    $("#newPassword").val("");
    $("#confirmPassword").val("");
    ShowSuccess("密码重置成功！！！");
}
