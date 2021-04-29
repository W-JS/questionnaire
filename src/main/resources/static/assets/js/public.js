$(function () {
    GenerateOnlineUser();// 生成在线用户信息

    $("#search").click(Search);// 根据不同的搜索方式搜索不同的内容，问卷/问题/选项

    $("#userOperating").change(UserOperating);// 用户操作
});

// 生成问卷标题
function GenerateQNTitle() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/questionnaire/getQuestionnaire',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                $('#qnTitle').attr("href",CONTEXT_PATH + "/question/QuestionByQNId?qnId=" + result.data.questionnaireId);
                $('#qnTitle').text(result.data.questionnaireTitle);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 生成问卷标题和问题标题
function GenerateQNTitleAndQTitle() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/questionnaire/getQuestionnaireAndQuestion',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                $('#qnTitle').attr("href", CONTEXT_PATH + "/question/QuestionByQNId?qnId=" + result.data[0].questionnaire.questionnaireId);
                $('#qnTitle').text(result.data[0].questionnaire.questionnaireTitle);

                $('#qTitle').attr("href", CONTEXT_PATH + "/option/OptionByQId?qId=" + result.data[1].question.questionId);
                $('#qTitle').text(result.data[1].question.questionTitle);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

/*
// 生成问题标题
function GenerateQTitle() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/question/getQuestion',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                let html = "<a href=\"" + CONTEXT_PATH + "/option/OptionByQId?qId=" + result.data[0].question.questionId + "\" style=\"text-decoration: none;\">" + result.data[0].question.questionTitle + "</a>";
                $(html).appendTo($('#top .qTitle'));
            } else {
                ShowFailure(result.message);
            }
        }
    });
}*/

// 生成在线用户信息
function GenerateOnlineUser() {
    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/user/getOnlineUser',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                $('#onlineUser').val(result.data.userId);// 设置值
                $('#onlineUser').text(result.data.userName);// 设置文本
                // console.log($.trim($('#onlineUser').val()));// 取值
                // console.log($.trim($('#onlineUser').text()));// 取文本
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 根据不同的搜索方式搜索不同的内容，问卷/问题/选项
function Search() {
    let searchWay = $.trim($('#searchWay option:selected').val());// 搜索方式：问卷/问题/选项
    let searchContent = $.trim($('#searchContent').val());// 搜索内容

    if (searchContent != "" && searchContent.length != 0) {
        if (searchWay == "questionnaire") {
            window.location.href = CONTEXT_PATH + "/questionnaire/search" + "?searchWay=" + searchWay + "&searchContent=" + searchContent;
        } else if (searchWay == "question") {
            window.location.href = CONTEXT_PATH + "/question/search" + "?searchWay=" + searchWay + "&searchContent=" + searchContent;
        } else if (searchWay == "option") {
            window.location.href = CONTEXT_PATH + "/option/search" + "?searchWay=" + searchWay + "&searchContent=" + searchContent;
        }
    } else {
        $('#searchContent').focus();
        ShowFailure("请输入搜索内容！！！");
    }


}


// 用户操作
function UserOperating() {
    let userOperating = $.trim($('#userOperating option:selected').val());
    if (userOperating == "personalCenter") {
        PersonalCenter();
    } else if (userOperating == "exit") {
        Exit();
    }
}

// 个人中心
function PersonalCenter() {
    console.log("personalCenter personalCenter personalCenter personalCenter personalCenter");
}

// 退出登录
function Exit() {
    // $.trim($('#onlineUser').val());
    window.location.href = CONTEXT_PATH + "/user/logout";
    // window.location.href = CONTEXT_PATH + "/user/logout" + "?userId=" + $.trim($('#onlineUser').val());
}
