$(function () {
    $("#birthdayReg").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });

    // 登录
    $("input[name='userLog']").blur(function () {
        UserLog(this);
    });
    // $("#userLog1").blur(UserLog1);
    // $("#userLog2").blur(UserLog2);
    // $("#codeLogBtn").click(CodeLogBtn);
    $("#passwordLog").blur(PasswordLog);
    $("#codeLog").blur(CodeLog);
    // $("#login").click(Login);

    // 注册
    $("#usernameReg").blur(UsernameReg);
    $("#phoneReg").blur(PhoneReg);
    $("#emailReg").blur(EmailReg);
    $("#passwordReg").blur(PasswordReg);
    $("#confirmPasswordReg").blur(ConfirmPasswordReg);
    // $("#register").click(Register);

    // 页面切换
    // $("#switchReg").click(SwitchReg);
    // $("#switchLog").click(SwitchLog);
});

// 登录
// 验证用户名/手机号码/电子邮箱是否正确
function UserLog(object) {
    let flag = false;
    let userLog = $.trim($(object).val());
    let usernameValidate = /^[\u4e00-\u9fa5a-zA-Z0-9_-]{3,16}$/;
    let phoneValidate = /^((\d{3}-\d{8}|\d{4}-\d{7,8})|(1[3|5|7|8][0-9]{9}))$/;
    let emailValidate = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    if (!userLog || userLog.length == 0) {
        ShowFailure('用户名/手机号码/电子邮箱不能为空');
    } else {
        if (usernameValidate.test(userLog) || phoneValidate.test(userLog) || emailValidate.test(userLog)) {
            flag = true;
        } else {
            ShowFailure('格式不符合要求');
        }
    }
    return flag;
}

// 验证用户名/手机号码/电子邮箱是否存在
function UserLogExists(object) {
    let flag = false;
    if (!UserLog(object)) {
        $(object).focus();
    } else {
        let loginMethod = 'username';
        let userLog = $.trim($(object).val());
        let usernameValidate = /^[\u4e00-\u9fa5a-zA-Z0-9_-]{3,16}$/;
        let phoneValidate = /^((\d{3}-\d{8}|\d{4}-\d{7,8})|(1[3|5|7|8][0-9]{9}))$/;
        let emailValidate = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;

        if (emailValidate.test(userLog)) { // 判断为电子邮箱
            loginMethod = 'email';
        } else if (phoneValidate.test(userLog)) { // 判断为手机号
            loginMethod = 'phone';
        } else if (usernameValidate.test(userLog)) { // 判断为用户名
            loginMethod = 'username';
        }

        $.ajax({
            async: false, // 同步请求
            type: "get",
            url: CONTEXT_PATH + '/user/userLogExists',
            data: {
                'loginMethod': loginMethod,
                'userLog': userLog
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    $("#userId").val(result.message);
                    flag = true;
                } else {
                    $(object).focus();
                    ShowFailure(result.message);
                }
            }
        });
    }
    return flag;
}

// 点击获取验证码
function CodeLogBtn(object) {
    if (UserLogExists($("#userLog2"))) {
        // let codeBtn = $("#codeLogBtn");
        GenerateAuthCode(object);
    }
    /*if (UserLogExists2()) {
        // let codeBtn = $("#codeLogBtn");
        GenerateAuthCode(object);
    }*/
}

// 验证验证码是否正确
function CodeLog() {
    let flag = false;
    let codeLog = $.trim($("#codeLog").val());
    let codeValidate = /^[0-9a-zA-Z]{6}$/;
    if (!codeLog || codeLog.length == 0) {
        ShowFailure('验证码不能为空');
    } else {
        if (!codeValidate.test(codeLog)) {
            ShowFailure('验证码格式不符合要求');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证验证码是否存在
function CodeLogExists() {
    let flag = false;
    if (!CodeLog()) {
        $("#codeLog").focus();
    } else {
        let code = $.trim($("#codeLog").val());
        flag = CodeExists(code);
        if (!flag) {
            $("#codeLog").focus();
        }
    }
    return flag;
}

// 验证密码是否正确
function PasswordLog() {
    let flag = false;
    let passwordLog = $.trim($("#passwordLog").val());
    let passwordValidate = /^[0-9a-zA-Z_]{6,16}$/;
    if (!passwordLog || passwordLog.length == 0) {
        ShowFailure('密码不能为空');
    } else {
        if (!passwordValidate.test(passwordLog)) {
            ShowFailure('密码格式不符合要求,密码长度在6到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证密码是否存在
function PasswordLogExists() {
    let flag = false;
    if (!PasswordLog()) {
        $("#passwordLog").focus();
    } else {
        let userId = $.trim($("#userId").val());
        let passwordLog = $.trim($("#passwordLog").val());
        $.ajax({
            async: false, // 同步请求
            type: "get",
            url: CONTEXT_PATH + '/user/passwordLogExists',
            data: {
                'userId': userId,
                'passwordLog': passwordLog
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    flag = true;
                } else {
                    $("#passwordLog").focus();
                    ShowFailure(result.message);
                }
            }
        });
    }
    return flag;
}

// 登录前表单验证
function Login1() {
    if (!UserLogExists($("#userLog1"))) {
        return false;
    }
    /*if (!UserLogExists1()) {
        return false;
    }*/

    if (!PasswordLogExists()) {
        return false;
    }

    LoginSubmit1();
}

// 提交用户登录信息
function LoginSubmit1() {
    let userId = $.trim($("#userId").val());
    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/loginSubmit',
        data: {
            'userId': userId,
            'userLastLoginTimeLog': getNowFormatDate()
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                window.location.href = CONTEXT_PATH + "/";
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 登录前表单验证
function Login2() {
    if (!UserLogExists($("#userLog2"))) {
        return false;
    }

    // if (!UserLogExists2()) {
    //     return false;
    // }

    if (!CodeLogExists()) {
        return false;
    }

    LoginSubmit1();
}

// 注册
// 生成用户ID
function GeneratorUserID() {
    let userId = $.trim($("#userId").val());
    if (!userId || userId.length == 0) {
        $.ajax({
            async: true, // 异步请求
            type: "get",
            url: CONTEXT_PATH + '/user/generatorUserID',
            data: {},
            dataType: 'text',
            success: function (result) {
                $("#userId").val(result);
            }
        });
    }
}

// 验证用户名是否正确
function UsernameReg() {
    let flag = false;
    let usernameReg = $.trim($("#usernameReg").val());
    let usernameValidate = /^[\u4e00-\u9fa5a-zA-Z0-9_-]{3,16}$/;
    if (!usernameReg || usernameReg.length == 0) {
        ShowFailure('用户名不能为空');
    } else {
        if (!usernameValidate.test(usernameReg)) {
            ShowFailure('用户名格式不符合要求,用户名长度在3到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证用户名是否存在
function UsernameRegExists() {
    let flag = false;
    if (!UsernameReg()) {
        $("#usernameReg").focus();
    } else {
        let usernameReg = $.trim($("#usernameReg").val());
        $.ajax({
            async: false, // 同步请求
            type: "get",
            url: CONTEXT_PATH + '/user/usernameRegExists',
            data: {
                'usernameReg': usernameReg,
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    $("#usernameReg").focus();
                    ShowFailure('该用户名已存在！！！');
                } else {
                    flag = true;
                    // ShowSuccess(result.message);
                }
            }
        });
    }
    return flag;
}

// 验证手机号是否正确
function PhoneReg() {
    let flag = false;
    let phoneReg = $.trim($("#phoneReg").val());
    let phoneValidate = /^((\d{3}-\d{8}|\d{4}-\d{7,8})|(1[3|5|7|8][0-9]{9}))$/;
    if (!phoneReg || phoneReg.length == 0) {
        ShowFailure('手机号不能为空');
    } else {
        if (!phoneValidate.test(phoneReg)) {
            ShowFailure('手机号格式不符合要求');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证手机号是否存在
function PhoneRegExists() {
    let flag = false;
    if (!PhoneReg()) {
        $("#phoneReg").focus();
    } else {
        let phoneReg = $.trim($("#phoneReg").val());
        $.ajax({
            async: false, // 同步请求
            type: "get",
            url: CONTEXT_PATH + '/user/phoneRegExists',
            data: {
                'phoneReg': phoneReg,
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    $("#phoneReg").focus();
                    ShowFailure('该手机号已存在！！！');
                } else {
                    flag = true;
                    // ShowSuccess(result.message);
                }
            }
        });
    }
    return flag;
}

// 验证电子邮件是否正确
function EmailReg() {
    let flag = false;
    let emailReg = $.trim($("#emailReg").val());
    let emailValidate = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    if (!emailReg || emailReg.length == 0) {
        ShowFailure('电子邮件不能为空');
    } else {
        if (!emailValidate.test(emailReg)) {
            ShowFailure('电子邮件格式不符合要求');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证电子邮件是否存在
function EmailRegExists() {
    let flag = false;
    if (!EmailReg()) {
        $("#emailReg").focus();
    } else {
        let emailReg = $.trim($("#emailReg").val());
        $.ajax({
            async: false, // 同步请求
            type: "get",
            url: CONTEXT_PATH + '/user/emailRegExists',
            data: {
                'emailReg': emailReg,
            },
            dataType: 'json',
            success: function (result) {
                if (result.state == 1) {
                    $("#emailReg").focus();
                    ShowFailure('该电子邮箱已存在！！！');
                } else {
                    flag = true;
                    // ShowSuccess(result.message);
                }
            }
        });
    }
    return flag;
}

// 验证密码是否正确
function PasswordReg() {
    let flag = false;
    let passwordReg = $.trim($("#passwordReg").val());
    let passwordValidate = /^[0-9a-zA-Z_]{6,16}$/;
    if (!passwordReg || passwordReg.length == 0) {
        ShowFailure('密码不能为空');
    } else {
        if (!passwordValidate.test(passwordReg)) {
            ShowFailure('密码格式不符合要求,密码长度在6到16个字符之间');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 验证确认密码是否正确
function ConfirmPasswordReg() {
    let flag = false;
    if (!PasswordReg()) {
        $("#passwordReg").focus();
    } else {
        let passwordReg = $.trim($("#passwordReg").val());
        let confirmPasswordReg = $.trim($("#confirmPasswordReg").val());
        if (passwordReg != confirmPasswordReg) {
            $("#confirmPasswordReg").focus();
            ShowFailure('确认密码不正确，请重新输入！！！');
        } else {
            flag = true;
        }
    }
    return flag;
}

// 显示今天的日期
function BirthdayReg() {
    let flag = false;
    let birthdayReg = $.trim($("#birthdayReg").val());
    if (!birthdayReg || birthdayReg.length == 0) {
        $("#birthdayReg").focus();
        ShowFailure('日期不能为空');
    } else {
        flag = true;
    }
    return flag;
}

// 注册前表单验证
function Register() {
    if (!UsernameRegExists()) {
        return false;
    }
    if (!PhoneRegExists()) {
        return false;
    }
    if (!EmailRegExists()) {
        return false;
    }
    if (!ConfirmPasswordReg()) {
        return false;
    }
    if (!BirthdayReg()) {
        return false;
    }

    registerSubmit();
}

// 提交用户注册信息
function registerSubmit() {
    let userId = $.trim($("#userId").val());
    let usernameReg = $.trim($("#usernameReg").val());
    let phoneReg = $.trim($("#phoneReg").val());
    let emailReg = $.trim($("#emailReg").val());
    let passwordReg = $.trim($("#passwordReg").val());
    let sexReg = $('input[name="sexReg"]:checked').val();
    let birthdayReg = $.trim($("#birthdayReg").val());
    $.ajax({
        async: false, // 同步请求
        type: "post",
        url: CONTEXT_PATH + '/user/registerSubmit',
        data: {
            'userId': userId,
            'usernameReg': usernameReg,
            'phoneReg': phoneReg,
            'emailReg': emailReg,
            'passwordReg': passwordReg,
            'sexReg': sexReg,
            'birthdayReg': birthdayReg,
            'statusReg': 0,
            'typeReg': 0,
            'createTimeReg': getNowFormatDate()
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                window.location.href = CONTEXT_PATH + "/user/login";
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 公有
// 生成验证码
function GenerateAuthCode(object) {
    let userId = $.trim($("#userId").val());
    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/generateAuthCode1',
        data: {
            'userId': userId,
            'codeLength': 6
        },
        dataType: 'json',
        success: function (result) {
            if (result.data != null) {
                // console.log('发送本地验证码：' + result.data);
                let countDown = 60;
                $(object).attr('disabled', true).html(countDown + 's');
                let countInterval = setInterval(function () {
                    countDown--;
                    $(object).html(countDown + 's');
                    if (countDown == 0) {
                        $(object).attr('disabled', false).html('重新获取');
                        clearInterval(countInterval);
                    }
                }, 1000);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 验证验证码是否存在
function CodeExists(code) {
    let flag = false;
    let userId = $.trim($("#userId").val());
    $.ajax({
        async: false, // 同步请求
        type: "get",
        url: CONTEXT_PATH + '/user/codeExists',
        data: {
            'userId': userId,
            'code': code
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                flag = true;
            } else {
                ShowFailure(result.message);
            }
        }
    });
    return flag;
}

// 页面切换
// 切换到注册
function SwitchReg() {
    GeneratorUserID();
    $('#divReg').removeClass('hide');
    $('#divLog1').addClass('hide');
    $('#divLog2').addClass('hide');
    var valOfScroll = $("#divLog1").offset().top;
    $("html,body").animate({
        scrollTop: valOfScroll
    }, 600);
}

// 切换到密码登录
function SwitchLog1() {
    $('#divLog1').removeClass('hide');
    $('#divLog2').addClass('hide');
    $('#divReg').addClass('hide');
    var valOfScroll = $("#divReg").offset().top;
    $("html,body").animate({
        scrollTop: valOfScroll
    }, 600);
}

// 切换到验证码登录
function SwitchLog2() {
    $('#divLog2').removeClass('hide');
    $('#divLog1').addClass('hide');
    $('#divReg').addClass('hide');
    var valOfScroll = $("#divReg").offset().top;
    $("html,body").animate({
        scrollTop: valOfScroll
    }, 600);
}