let currentSCQuestionId = "";// 当前问题（单项选择题）
let currentSCQuestion = false;// 默认不是当前问题（单项选择题）
let currentMCQuestionId = "";// 当前问题（多项选择题）
let currentMCQuestion = false;// 默认不是当前问题（多项选择题）
let currentJMQuestionId = "";// 当前问题（判断题）
let currentJMQuestion = false;// 默认不是当前问题（判断题）

let array = new Array();

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
                    console.log("id: " + this.elem[0].id + " value: " + value);
                }
            });
        })
    });
});

function ClickOption(object) {
    if ($($(object).parent("label").parent("div").parent("div").find("div:nth-child(2)").children("span:first")[0]).attr("value") == "true") {// 当前选项所在的问题有后置问题
        if (object.value == "sc-true" || object.value == "jm-true") {// 当前选项有后置问题（单项选择题）
            // console.log("当前选项所在的问题有后置问题 当前选项有后置问题");

            $(object).parent("label").parent("div").parent("div").next().attr("style", "display:block;color:green;");

            let key = object.name;// key: 当前问题的问题编号
            let value = $(object).parent("label").parent("div").parent("div").next().children("div:nth-child(3)").children("label").children("input")[0].name;// value: 下一个问题的问题编号
            array[key] = value;
        } else if (object.value == "sc-false" || object.value == "jm-false") {// 当前选项无后置问题（单项选择题）
            // console.log("当前选项所在的问题有后置问题 当前选项无后置问题");
            let key = object.name;// key: 当前问题的问题编号
            let value;// value: 下一个问题的问题编号

            let flag = true;
            while (flag) {
                value = array[key];// value: 下一个问题的问题编号
                if (value != null) {
                    delete array[key];
                    if ($(":radio[name=" + value + "]:checked").length == 0) {// 后置问题没有被选中
                        $(":radio[name=" + value + "]").parent("label").parent("div").parent("div").attr("style", "display:none;");
                    } else {// 后置问题被选中
                        $(":radio[name=" + value + "]:checked").parent("label").parent("div").parent("div").attr("style", "display:none;");
                        $(":radio[name=" + value + "]:checked").prop("checked", false);
                    }
                    key = value;
                } else {
                    flag = false;
                }
            }
        }

        if (object.value == "mc-true") {// 当前选项有后置问题（多项选择题）
            // console.log("当前选项所在的问题有后置问题 当前选项有后置问题");

            $(object).parent("label").parent("div").parent("div").next().attr("style", "display:block;color:blue;");

            let key = object.name;// key: 当前问题的问题编号

            if (array[key] == null) {
                let value = $(object).parent("label").parent("div").parent("div").next().children("div:nth-child(3)").children("label").children("input")[0].name;// value: 下一个问题的问题编号
                array[key] = value;
            } else {
                let value;// value: 下一个问题的问题编号
                let flag = true;
                while (flag) {
                    value = array[key];// value: 下一个问题的问题编号
                    if (value != null) {
                        delete array[key];
                        if ($(":checkbox[name=" + value + "]:checked").length == 0) {// 后置问题没有被选中
                            $(":checkbox[name=" + value + "]").parent("label").parent("div").parent("div").attr("style", "display:none;");
                        } else {// 后置问题被选中
                            $(":checkbox[name=" + value + "]:checked").parent("label").parent("div").parent("div").attr("style", "display:none;");
                            $(":checkbox[name=" + value + "]:checked").prop("checked", false);
                        }
                        key = value;
                    } else {
                        flag = false;
                    }
                }
            }
        }
        /*else if (object.value == "mc-false") {// 当前选项无后置问题（多项选择题）
            // console.log("当前选项所在的问题有后置问题 当前选项无后置问题");
            let key = object.name;// key: 当前问题的问题编号
            let value;// value: 下一个问题的问题编号

            let flag = true;
            while (flag) {
                value = array[key];// value: 下一个问题的问题编号
                if (value != null) {
                    delete array[key];
                    if ($(":checkbox[name=" + value + "]:checked").length == 1) {// 后置问题被选中
                        $(":checkbox[name=" + value + "]:checked").parent("label").parent("div").parent("div").attr("style", "display:none;");
                        $(":checkbox[name=" + value + "]:checked").prop("checked", false);
                    } else {// 后置问题没有被选中
                        $(":checkbox[name=" + value + "]").parent("label").parent("div").parent("div").attr("style", "display:none;");
                    }
                    key = value;
                } else {
                    flag = false;
                }
            }
        }*/
    } else {
        // console.log("当前选项所在的问题无后置问题");
    }

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