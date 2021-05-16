$(function () {
    GenerateOnlineUser();// 生成在线用户信息
});

// 关闭当前页面
function windowClose() {
    window.close();
    window.location.href = CONTEXT_PATH + "/questionnaire/index";
}
