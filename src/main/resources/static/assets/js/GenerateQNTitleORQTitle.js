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
                let html = "<a href=\"" + CONTEXT_PATH + "/question/QuestionByQNId?qnId=" + result.data.questionnaireId + "\" style=\"text-decoration: none;\">" + result.data.questionnaireTitle + "</a>";
                $(html).appendTo($('#top .qnTitle'));
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
                let html1 = "<a href=\"" + CONTEXT_PATH + "/question/QuestionByQNId?qnId=" + result.data[0].questionnaire.questionnaireId + "\" style=\"text-decoration: none;\">" + result.data[0].questionnaire.questionnaireTitle + "</a>";
                $(html1).appendTo($('#top .qnTitle'));

                let html2 = "<a href=\"" + CONTEXT_PATH + "/option/OptionByQId?qId=" + result.data[1].question.questionId + "\" style=\"text-decoration: none;\">" + result.data[1].question.questionTitle + "</a>";
                $(html2).appendTo($('#top .qTitle'));
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
