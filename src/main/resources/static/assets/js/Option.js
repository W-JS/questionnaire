$(function () {
    isURL();
    HideOption();// 隐藏选项
});

// 判断url是否正确
function isURL() {
    let url = window.location.pathname;
    let index = url.lastIndexOf("/")
    url = url.substring(index + 1, url.length);

    if (url == "Option") {
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