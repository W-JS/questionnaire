let currentSCQuestionId = "";// 当前问题（单项选择题）
let currentSCQuestion = false;// 默认不是当前问题（单项选择题）
let currentMCQuestionId = "";// 当前问题（多项选择题）
let currentMCQuestion = false;// 默认不是当前问题（多项选择题）
let currentJMQuestionId = "";// 当前问题（判断题）
let currentJMQuestion = false;// 默认不是当前问题（判断题）

let rearQuestionArray = new Array();// 保存当前问题和后置问题的问题信息（key: 当前问题编号，value: 后置问题编号）

let scQuestionArray = new Array();// 保存用户选中的 单项选择题 的选项信息（key: 问题编号，value: 选项编号）
let mcQuestionArray = new Array();// 保存用户选中的 多项选择题 的选项信息（key: 选项编号，value: 问题编号）
let jmQuestionArray = new Array();// 保存用户选中的 判断题 的选项信息（key: 问题编号，value: 选项编号）
let fbQuestionArray = new Array();// 保存用户选中的 填空题 的选项信息（key: 选项编号，value: 值）
let sQuestionArray = new Array();// 保存用户选中的 评分题 的选项信息（key: 选项编号，value: 值）

let showCount = 0;// 保存当前显示的问题的个数

$(function () {
    layui.use('rate', function () {
        let rate = layui.rate;
        // 循环渲染
        $(".score").each(function () {
            //渲染
            let ins1 = rate.render({
                elem: this,  //绑定元素，指向容器选择器
                length: 5,// 评分组件中具体星星的个数
                value: 0,// 评分的初始值
                theme: "#FFB800",// 主题颜色
                half: true,// 设定组件是否可以选择半星
                text: true,// 是否显示评分对应的内容
                readonly: false// 是否只读，即只用于展示而不可点
                , setText: function (value) {
                    let arrs = {
                        '1': '极差'
                        , '2': '差'
                        , '3': '中等'
                        , '4': '好'
                        , '5': '极好'
                    };
                    this.span.text(arrs[value] || (value + "星"));
                }
                , choose: function (value) {
                    // sQuestionArray[this.elem[0].id] = String(value);
                    sQuestionArray[this.elem[0].id] = value.toString();
                    // console.log("id: " + this.elem[0].id + " value: " + value);
                }
            });
        })
    });

    $("#save").click(Save);

    ShowCount();
});

// 鼠标点击触发（单项选择题/多项选择题/判断题）
function ClickOption(object) {
    if ($($(object).parent("label").parent("div").parent("div").find("div:nth-child(2)").children("span:first")[0]).attr("value") == "true") {// 当前选项所在的问题有后置问题
        if (object.value == "sc-true" || object.value == "jm-true") {// 当前选项有后置问题（单项选择题）
            $(object).parent("label").parent("div").parent("div").next().attr("style", "display:block;color:green;");
            $($(object).parent("label").parent("div").parent("div").next()[0]).children("div:first-child").addClass("Show");
            let key = object.name;// key: 当前问题的问题编号
            let value = $(object).parent("label").parent("div").parent("div").next().children("div:nth-child(3)").children("label").children("input")[0].name;// value: 下一个问题的问题编号
            rearQuestionArray[key] = value;
        } else if (object.value == "sc-false" || object.value == "jm-false") {// 当前选项无后置问题（单项选择题）
            let key = object.name;// key: 当前问题的问题编号
            let value;// value: 下一个问题的问题编号
            let flag = true;
            while (flag) {
                value = rearQuestionArray[key];// value: 下一个问题的问题编号
                if (value != null) {
                    delete rearQuestionArray[key];
                    if (object.value == "sc-false") {
                        delete scQuestionArray[value];
                    } else if (object.value == "jm-false") {
                        delete jmQuestionArray[value];
                    }
                    if ($(":radio[name=" + value + "]:checked").length == 0) {// 后置问题没有被选中
                        $(":radio[name=" + value + "]").parent("label").parent("div").parent("div").attr("style", "display:none;");
                        $(":radio[name=" + value + "]").parent("label").parent("div").parent("div").children("div:first-child").removeClass("Show");
                    } else {// 后置问题被选中
                        $(":radio[name=" + value + "]:checked").parent("label").parent("div").parent("div").attr("style", "display:none;");
                        $(":radio[name=" + value + "]:checked").parent("label").parent("div").parent("div").children("div:first-child").removeClass("Show");
                        $(":radio[name=" + value + "]:checked").prop("checked", false);
                    }
                    key = value;
                } else {
                    flag = false;
                }
            }
        }

        if (object.value == "mc-true") {// 当前选项有后置问题（多项选择题）
            $(object).parent("label").parent("div").parent("div").next().attr("style", "display:block;color:blue;");
            $($(object).parent("label").parent("div").parent("div").next()[0]).children("div:first-child").addClass("Show");
            let key = object.name;// key: 当前问题的问题编号
            if (rearQuestionArray[key] == null) {
                let value = $(object).parent("label").parent("div").parent("div").next().children("div:nth-child(3)").children("label").children("input")[0].name;// value: 下一个问题的问题编号
                rearQuestionArray[key] = value;
            } else {
                let value;// value: 下一个问题的问题编号
                let flag = true;
                while (flag) {
                    value = rearQuestionArray[key];// value: 下一个问题的问题编号
                    if (value != null) {
                        delete rearQuestionArray[key];
                        for (let i in mcQuestionArray) {
                            if (mcQuestionArray[i] == value) {
                                delete mcQuestionArray[i];
                            }
                        }
                        if ($(":checkbox[name=" + value + "]:checked").length == 0) {// 后置问题没有被选中
                            $(":checkbox[name=" + value + "]").parent("label").parent("div").parent("div").attr("style", "display:none;");
                            $(":checkbox[name=" + value + "]").parent("label").parent("div").parent("div").children("div:first-child").removeClass("Show");
                        } else {// 后置问题被选中
                            $(":checkbox[name=" + value + "]:checked").parent("label").parent("div").parent("div").attr("style", "display:none;");
                            $(":checkbox[name=" + value + "]:checked").parent("label").parent("div").parent("div").children("div:first-child").removeClass("Show");
                            $(":checkbox[name=" + value + "]:checked").prop("checked", false);
                        }
                        key = value;
                    } else {
                        flag = false;
                    }
                }
            }
        }
    } else {
        // console.log("当前选项所在的问题无后置问题");
    }

    if (object.value == "sc-true" || object.value == "sc-false") {
        scQuestionArray[object.name] = object.id;
    } else if (object.value == "jm-true" || object.value == "jm-false") {
        jmQuestionArray[object.name] = object.id;
    } else if (object.value == "mc-true" || object.value == "mc-false") {
        if (mcQuestionArray[object.id] != null) {
            delete mcQuestionArray[object.id];
        } else {
            mcQuestionArray[object.id] = object.name;
        }
    }

    ShowCount();
    /*$.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/getRearQuestionByQIdAndOId',
        data: {
            "qId": object.name,
            "oId": object.id,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                // 单项选择题
                if (object.value == "sc") {
                    if (currentSCQuestionId != object.name) {// console.log("当前选项: 有后置问题 添加存在的后置问题");
                        currentSCQuestion = true;
                        currentSCQuestionId = object.name;
                    } else {// console.log("当前选项: 有后置问题");
                        return;
                    }
                    GenerateSCQuestionAndOption(object, result);// 动态生成问题信息和选项信息（单项选择题）
                }

                // 多项选择题
                if (object.value == "mc") {
                    if (currentMCQuestionId != object.name) {// console.log("当前选项: 有后置问题 添加存在的后置问题");
                        currentMCQuestion = true;
                        currentMCQuestionId = object.name;
                    } else {
                        if (currentMCQuestion) {// console.log("当前选项: 有后置问题 删除存在的后置问题");
                            currentMCQuestionId = "";
                            currentMCQuestion = false;
                            $(object).parent("label").parent("div").parent("div").next().remove();
                        }
                        return;
                    }
                    GenerateMCQuestionAndOption(object, result);// 动态生成问题信息和选项信息（多项选择题）
                }

                // 判断题
                if (object.value == "jm") {
                    if (currentJMQuestionId != object.name) {// console.log("当前选项: 有后置问题 添加存在的后置问题");
                        currentJMQuestion = true;
                        currentJMQuestionId = object.name;
                    } else {// console.log("当前选项: 有后置问题");
                        return;
                    }
                    GenerateSCQuestionAndOption(object, result);// 动态生成问题信息和选项信息（单项选择题）
                }
            } else {
                // 单项选择题
                if (object.value == "sc") {
                    if (currentSCQuestionId == object.name) {
                        if (currentSCQuestion) {// console.log("其它选项: 无后置问题 删除存在的后置问题");
                            currentSCQuestionId = "";
                            currentSCQuestion = false;
                            $(object).parent("label").parent("div").parent("div").next().remove();
                        }
                    }
                }

                // 判断题
                if (object.value == "jm") {
                    if (currentJMQuestionId == object.name) {
                        if (currentJMQuestion) {// console.log("其它选项: 无后置问题 删除存在的后置问题");
                            currentJMQuestionId = "";
                            currentJMQuestion = false;
                            $(object).parent("label").parent("div").parent("div").next().remove();
                        }
                    }
                }
            }
        }
    });*/
}

// 鼠标移出触发（填空题）
function BlurOption(object) {
    let value = $.trim($(object).val());
    if (value != null && value != "") {
        fbQuestionArray[object.id] = value;
    } else {
        delete fbQuestionArray[object.id];
    }
}

// 提交用户填写的问卷信息
function Save() {
    console.log("单项选择题: ");
    let sc = new Array();
    for (let i in scQuestionArray) {
        console.log("qId: " + i + " oId: " + scQuestionArray[i]);
        let object = new Object();
        object.qId = i;
        object.oId = scQuestionArray[i];
        sc.push(object);
    }

    console.log("多项选择题: ");
    let mc = new Array();
    for (let i in mcQuestionArray) {
        console.log("oId: " + i + " qId: " + mcQuestionArray[i]);
        let object = new Object();
        object.oId = i;
        object.qId = mcQuestionArray[i];
        mc.push(object);
    }

    console.log("判断题: ");
    let jm = new Array();
    for (let i in jmQuestionArray) {
        console.log("qId: " + i + " oId: " + jmQuestionArray[i]);
        let object = new Object();
        object.qId = i;
        object.oId = jmQuestionArray[i];
        jm.push(object);
    }

    console.log("填空题: ");
    let fb = new Array();
    for (let i in fbQuestionArray) {
        console.log("oId: " + i + " value: " + fbQuestionArray[i]);
        let object = new Object();
        object.oId = i;
        object.value = fbQuestionArray[i];
        fb.push(object);
    }

    console.log("评分题: ");
    let s = new Array();
    for (let i in sQuestionArray) {
        console.log("oId: " + i + " value: " + sQuestionArray[i]);
        let object = new Object();
        object.oId = i;
        object.value = sQuestionArray[i];
        s.push(object);
    }

    let userComments = $.trim($("#userComments").val());
    console.log("用户留言: ");
    console.log(userComments);

    $.ajax({
        async: true, // 异步请求
        type: "post",
        url: CONTEXT_PATH + '/saveSubmit',
        data: {
            'JSONsc': JSON.stringify(sc),
            'JSONmc': JSON.stringify(mc),
            'JSONjm': JSON.stringify(jm),
            'JSONfb': JSON.stringify(fb),
            'JSONs': JSON.stringify(s),
            'userComments': userComments,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                ShowSuccess("success");
                // ShowSuccess("删除成功！！！");
                // setTimeout(function () {
                //     window.location.href = window.location.pathname + window.location.search;
                // }, 1000);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 获取拥有 .Show 的个数
function ShowCount() {
    if ($(".Show").length != showCount) {
        // 循环设置问题题号
        let i = 0;
        $(".Show").each(function () {
            $(this).text((++i) + "、");
        });
        showCount = i;
    }
}

// 动态生成问题信息和选项信息（单项选择题）
function GenerateSCQuestionAndOption(object, result) {
    let html = "";
    html +=
        "<div class=\"am-form-group am-cf\">\n" +
        "    <div class=\"QNumber\">1、</div>\n" +
        "    <div class=\"topic\">\n" +
        "        <span class=\"qTitle\">" + result.data[0].question.questionTitle + "</span>\n" +
        "        <span class=\"qt\">（单选）</span>\n";

    if (result.data[0].question.questionStatus == 1) {
        html +=
            "        <span class=\"qStatus\">*</span>\n";
    }

    html +=
        "    </div>\n";

    let optionList = result.data[0].optionList;
    for (let i = 0; i < optionList.length; i++) {
        html +=
            "    <div class=\"radio\">\n" +
            "        <label class=\"radio-mr\">\n" +
            "            <input onclick=\"ClickOption(this)\" class=\"input-r\" type=\"radio\" " + "id=\" " + optionList[i].option.optionId + " \" " + "name=\" " + optionList[i].option.questionId + " \">" + " " + optionList[i].option.optionContent + "\n" +
            "        </label>\n" +
            "    </div>\n";
    }

    html +=
        "</div>";
    $(html).insertAfter($(object).parent("label").parent("div").parent("div"));
}

// 动态生成问题信息和选项信息（多项选择题）
function GenerateMCQuestionAndOption(object, result) {
    let html = "";
    html +=
        "<div class=\"am-form-group am-cf\">\n" +
        "    <div class=\"QNumber\">1、</div>\n" +
        "    <div class=\"topic\">\n" +
        "        <span class=\"qTitle\">" + result.data[0].question.questionTitle + "</span>\n" +
        "        <span class=\"qt\">（单选）</span>\n";

    if (result.data[0].question.questionStatus == 1) {
        html +=
            "        <span class=\"qStatus\">*</span>\n";
    }

    html +=
        "    </div>\n";

    let optionList = result.data[0].optionList;
    for (let i = 0; i < optionList.length; i++) {
        html +=
            "    <div class=\"checkbox\">\n" +
            "        <label class=\"checkbox-mr\">\n" +
            "            <input onclick=\"ClickOption(this)\" class=\"input-cb\" type=\"checkbox\" " + "id=\" " + optionList[i].option.optionId + " \" " + "name=\" " + optionList[i].option.questionId + " \">" + " " + optionList[i].option.optionContent + "\n" +
            "        </label>\n" +
            "    </div>\n";
    }

    html +=
        "</div>";
    $(html).insertAfter($(object).parent("label").parent("div").parent("div"));
}