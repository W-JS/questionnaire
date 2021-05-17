let userMessageArray = new Array();// 保存原始用户信息

$(function () {
    GenerateOnlineUser();// 生成在线用户信息
    UserMessage();// 保存原始个人信息

    $("#birthday").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });

    $("#userName").blur(Username);
    $("#userPhone").blur(Phone);
    $("#userEmail").blur(Email);

    $("#oldPassword").blur(OldPassword);
    $("#newPassword1").blur(NewPassword1);
    $("#confirmPassword1").blur(ConfirmPassword1);

    $("#sendCode").click(SendCode);
    $("#code").blur(Code);
    $("#newPassword2").blur(NewPassword2);
    $("#confirmPassword2").blur(ConfirmPassword2);
});

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

// 验证新密码1是否正确
function NewPassword1() {
    let flag = false;
    let newPassword1 = $.trim($("#newPassword1").val());
    let passwordValidate = /^[0-9a-zA-Z_]{6,16}$/;
    if (!newPassword1 || newPassword1.length == 0) {
        ShowFailure('新密码不能为空');
    } else {
        if (!passwordValidate.test(newPassword1)) {
            ShowFailure('密码格式不符合要求,密码长度在6到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证确认密码1是否正确
function ConfirmPassword1() {
    let flag = false;
    let oldPassword = $.trim($("#oldPassword").val());
    let newPassword1 = $.trim($("#newPassword1").val());

    if (!OldPassword()) {
        $("#oldPassword").focus();
    } else if (!NewPassword1()) {
        $("#newPassword1").focus();
    } else if (oldPassword == newPassword1) {
        $("#newPassword1").val("");
        $("#newPassword1").focus();
        ShowFailure("新密码不能与原密码相同！！！");
    } else {
        let confirmPassword1 = $.trim($("#confirmPassword1").val());
        if (newPassword1 != confirmPassword1) {
            $("#confirmPassword1").focus();
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
    if (!ConfirmPassword1()) {
        return false;
    }

    if (!PasswordExists()) {
        return false;
    }

    let userId = $("#userId").val();
    let newPassword1 = $.trim($("#newPassword1").val());
    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/updateSubmit2',
        data: {
            'userId': userId,
            'password': newPassword1,
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
    $("#newPassword1").val("");
    $("#confirmPassword1").val("");
    $("#oldPassword").focus();
    ShowSuccess("密码重置成功！！！");
}


// 点击发送验证码
function SendCode() {
    let sendCode = $("#sendCode");
    GenerateAuthCode(sendCode);
}

// 生成验证码
function GenerateAuthCode(object) {
    let userId = $.trim($("#userId").val());
    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/generateAuthCode2',
        data: {
            'userId': userId,
            'codeLength': 6
        },
        dataType: 'json',
        success: function (result) {
            if (result.data != null) {
                // console.log('发送本地验证码：' + result.data);
                let countDown = 60;
                object.attr('disabled', true).html(countDown + 's');
                let countInterval = setInterval(function () {
                    countDown--;
                    object.html(countDown + 's');
                    if (countDown == 0) {
                        object.attr('disabled', false).html('重新获取');
                        clearInterval(countInterval);
                    }
                }, 1000);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 验证验证码是否正确
function Code() {
    let flag = false;
    let code = $.trim($("#code").val());
    let codeValidate = /^[0-9a-zA-Z]{6}$/;
    if (!code || code.length == 0) {
        ShowFailure('验证码不能为空，请点击发送验证码按钮获取验证码');
    } else {
        if (!codeValidate.test(code)) {
            ShowFailure('验证码格式不符合要求,验证码长度为6个字符');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证新密码2是否正确
function NewPassword2() {
    let flag = false;
    let newPassword2 = $.trim($("#newPassword2").val());
    let passwordValidate = /^[0-9a-zA-Z_]{6,16}$/;
    if (!newPassword2 || newPassword2.length == 0) {
        ShowFailure('新密码不能为空');
    } else {
        if (!passwordValidate.test(newPassword2)) {
            ShowFailure('密码格式不符合要求,密码长度在6到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证确认密码2是否正确
function ConfirmPassword2() {
    let flag = false;
    let newPassword2 = $.trim($("#newPassword2").val());

    if (!Code()) {
        $("#code").focus();
    } else if (!NewPassword2()) {
        $("#newPassword2").focus();
    } else {
        let confirmPassword2 = $.trim($("#confirmPassword2").val());
        if (newPassword2 != confirmPassword2) {
            $("#confirmPassword2").focus();
            ShowFailure('确认密码不正确，请重新输入！！！');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 判断验证码是否与redis中保存的验证码相等
function CodeExists() {
    let flag = true;
    let userId = $("#userId").val();
    let code = $.trim($("#code").val());
    $.ajax({
        async: false, // 同步请求
        type: "get",
        url: CONTEXT_PATH + '/user/codeExists',
        data: {
            'userId': userId,
            'code': code,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 0) {
                $("#code").val("");
                $("#code").focus();
                ShowFailure(result.message);
                flag = false;
            }
        }
    });
    return flag;
}

// 更新密码
function Update3() {
    if (!ConfirmPassword2()) {
        return false;
    }

    if (!CodeExists()) {
        return false;
    }

    let userId = $("#userId").val();
    let newPassword2 = $.trim($("#newPassword2").val());
    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/updateSubmit2',
        data: {
            'userId': userId,
            'password': newPassword2,
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
function Reset3() {
    $("#code").val("");
    $("#newPassword2").val("");
    $("#confirmPassword2").val("");
    $("#code").focus();
    ShowSuccess("密码重置成功！！！");
}
