$(function () {
    isURL();
    HideQuestionnaire();// 隐藏问卷
});

// 判断url是否正确
function isURL(){
    let url = window.location.pathname;
    let index = url.lastIndexOf("/")
    url = url.substring(index + 1, url.length);

    if (url == "index") {
        Questionnaire();
    }
}

// 显示问卷
function Questionnaire() {
    let flag = $("#hideQuestionnaire-li").length > 0;
    if (!flag) {
        let html =
            "<li id=\"hideQuestionnaire-li\">\n" +
            "    <button type=\"button\" style=\"background-color: #005cbf;\" class=\"am-btn am-btn-default am-radius am-btn-xs\">问卷\n" +
            "        <a id=\"hideQuestionnaire\" href=\"javascript: void(0)\" class=\"am-close am-close-spin\" data-am-modal-close=\"\">×</a>\n" +
            "    </button>\n" +
            "</li>";
        // $(html).appendTo($('.daohang ul li').last());// ok
        // $(html).appendTo($('.daohang ul').last());// ok
        // $(html).appendTo($('#show-hide').last());// ok
        $(html).appendTo($('#show-hide'));// ok
    }else {
        $("#hideQuestionnaire-li").show();
    }
}

// 隐藏问卷
function HideQuestionnaire() {
    $("#show-hide").on("mouseenter","#hideQuestionnaire", function () {
        $(this).click(function () {// "#hideQuestionnaire" 可替换为 this
            $("#hideQuestionnaire-li").hide();
            window.location.href = CONTEXT_PATH + "/";
        })
    });
}