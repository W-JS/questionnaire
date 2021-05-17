let user = new Array();// 保存原始用户信息
let deleteUser = new Array(); // 保存删除勾选的用户

$(function () {
    GenerateMessageManagement();// 生成消息管理数量
    GenerateOnlineUser();// 生成在线用户信息

    $("#birthday").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });
    $("#createTime").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });
    $("#updateTime").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });
    $("#deleteTime").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });
    $("#lastLoginTime").jeDate({
        format: "YYYY-MM-DD hh:mm:ss"
    });

    $("#userName").blur(Username);
    $("#userPhone").blur(Phone);
    $("#userEmail").blur(Email);

    $("#items tr td").click(Checkbox);// 点击td元素选中复选框

    $("#deleteChoose").click(DeleteChoose);// 删除选择的用户
});

// 点击td元素选中复选框
function Checkbox() {
    let checkbox = $(this).parent().find(":checkbox");// td的父元素为tr，在tr中找到checkbox
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    let row = $(this).parent().index();// 行数，从 0 开始
    let column = $(this).index();// 列数，从 0 开始
    let columns = $(this).parent().find("td").length;// 总列数

    user['userId'] = checkbox.val();// 1 向数组存用户编号
    // user.push(checkbox.val());// 1 向数组存用户编号

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

        let udUser = user['operating'];
        // let udUser = user[0];
        if (udUser == "update") {
            SetUser();// 设置用户弹出层
        }
        if (udUser == "delete") {
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

        //若子用户没有全选，全选框不选中。
        for (let i = 1; i < cbs.length; i++) {
            if (cbs[i].checked == false) {
                cbs[0].checked = false;
            }
        }
    }
}

// 将当前用户信息填充到用户弹出层中
function SetUser() {
    let userId = user['userId'];// 用户编号
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/user/getUserByUserId',
        data: {
            'userId': userId
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                user['userName'] = result.data.userName;// 2 向数组存用户名
                user['userPhone'] = result.data.userPhone;// 3 向数组存手机号
                user['userEmail'] = result.data.userEmail;// 4 向数组存电子邮箱
                user['userSex'] = result.data.userSex;// 5 向数组存性别
                user['userBirthday'] = renderTime(result.data.userBirthday);// 6 向数组存生日
                user['userStatus'] = result.data.userStatus;// 7 向数组存状态
                user['userCreateTime'] = renderTime(result.data.userCreateTime);// 8 向数组存注册时间
                user['userUpdateTime'] = renderTime(result.data.userUpdateTime);// 9 向数组存信息更新时间
                user['userDeleteTime'] = renderTime(result.data.userDeleteTime);// 10 向数组存注销时间
                user['userLastLoginTime'] = renderTime(result.data.userLastLoginTime);// 11 向数组存最后一次登录时间

                /*for (let i in user) {
                    console.log(i + " " + user[i]);
                }*/

                /*user.push(userName);// 2 向数组存用户名
                user.push(userPhone);// 3 向数组存手机号
                user.push(userEmail);// 4 向数组存电子邮箱
                user.push(userSex);// 5 向数组存性别
                user.push(userBirthday);// 6 向数组存生日
                user.push(userStatus);// 7 向数组存状态
                user.push(userCreateTime);// 8 向数组存注册时间
                user.push(userUpdateTime);// 9 向数组存信息更新时间
                user.push(userDeleteTime);// 10 向数组存注销时间
                user.push(userLastLoginTime);// 11 向数组存最后一次登录时间*/

                Reset("false");
            }
        }
    });
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
        if (user['userName'] != userName) {
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
        if (user['userPhone'] != userPhone) {
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
        if (user['userEmail'] != userEmail) {
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

// 提交用户前表单验证 从用户弹出层中获取修改后最新的用户数据信息
function Update() {
    if (!UsernameExists()) {
        return false;
    }
    if (!PhoneExists()) {
        return false;
    }
    if (!EmailExists()) {
        return false;
    }

    UpdateSubmit();
}

// 提交用户信息
function UpdateSubmit() {
    let userId = user['userId'];

    let userName = $("#userName").val();
    let userPhone = $("#userPhone").val();
    let userEmail = $("#userEmail").val();
    let sex = $(":radio[name='sex']:checked").val();
    let birthday = $("#birthday").val();
    let status = $(":radio[name='status']:checked").val();
    let createTime = $("#createTime").val();
    let updateTime = $("#updateTime").val();
    let deleteTime = $("#deleteTime").val();
    let lastLoginTime = $("#lastLoginTime").val();

    if (
        user['userName'] == userName &&
        user['userPhone'] == userPhone &&
        user['userEmail'] == userEmail &&
        user['userSex'] == sex &&
        user['userBirthday'] == birthday &&
        user['userStatus'] == status &&
        user['userCreateTime'] == createTime &&
        user['userUpdateTime'] == updateTime &&
        user['userDeleteTime'] == deleteTime &&
        user['userLastLoginTime'] == lastLoginTime
    ) {
        ShowWarn("个人信息未修改！！！");
        return;
    }

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/user/updateSubmit3',
        data: {
            'userId': userId,
            'userName': userName,
            'userPhone': userPhone,
            'userEmail': userEmail,
            'userSex': sex,
            'userBirthday': birthday,
            'userStatus': status,
            'userCreateTime': createTime,
            'userUpdateTime': updateTime,
            'userDeleteTime': deleteTime,
            'userLastLoginTime': lastLoginTime
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                ShowSuccess("用户信息更新成功！！！");
                setTimeout(function () {
                    window.location.href = window.location.pathname + window.location.search;
                }, 1000);

            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 提交用户信息
function DeleteSubmit() {
    $('#userModal').modal({
        relatedTarget: this,
        onConfirm: function (options) {
            let userId = user['userId'];// 用户编号
            // let userId = user[1];// 用户编号
            $.ajax({
                async: true, // 异步请求
                type: "post",
                url: CONTEXT_PATH + '/user/deleteSubmit1',
                data: {
                    'userId': userId
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

// 删除选择的用户
function DeleteChoose() {
    deleteUser.length = 0;
    let cbs = $("input[name='cb[]']");//获取所有复选框对象
    for (let i = 1; i < cbs.length; i++) {
        if (cbs[i].checked) {
            deleteUser.push($(cbs[i]).val());
        }
    }
    if (deleteUser.length > 0) {
        $('#userModal').modal({
            relatedTarget: this,
            onConfirm: function (options) {
                $.ajax({
                    async: true, // 异步请求
                    type: "post",
                    url: CONTEXT_PATH + '/user/deleteSubmit2',
                    data: {
                        'user': JSON.stringify(deleteUser),
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
                deleteUser.length = 0;
            },
        });
    } else {
        ShowFailure("请选择需要删除的用户！！！");
    }
}

// 重置用户信息
function Reset(object) {
    $("#userName").val(user['userName']);// 填充用户名
    $("#userPhone").val(user['userPhone']);// 填充手机号
    $("#userEmail").val(user['userEmail']);// 填充电子邮箱
    $(":radio[name='sex'][value='" + user['userSex'] + "']").prop("checked", "checked");// 选中正确的性别
    $("#birthday").val(user['userBirthday']);// 填充生日
    $(":radio[name='status'][value='" + user['userStatus'] + "']").prop("checked", "checked");// 选中正确的状态
    $("#createTime").val(user['userCreateTime']);// 填充注册时间
    $("#updateTime").val(user['userUpdateTime']);// 填充信息更新时间
    $("#deleteTime").val(user['userDeleteTime']);// 填充注销时间
    $("#lastLoginTime").val(user['userLastLoginTime']);// 填充注册时间最后一次登录时间

    if (object == 'true') {
        ShowSuccess('用户信息重置成功！！！');
    }
}

// 点击修改用户按钮
function UpdateUser() {
    user.length = 0;//将原始用户信息清空
    user['operating'] = "update";// 0 向数组存用户操作
    // user.push("update");
}

// 点击删除用户按钮
function DeleteUser() {
    user.length = 0;//将原始用户信息清空
    user['operating'] = "delete";// 0 向数组存用户操作
    // user.push("delete");// 0 向数组存用户操作
}