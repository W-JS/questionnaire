let rearQuestionArray = new Array();// 保存当前问题和后置问题的问题信息（key: 当前问题编号，value: 后置问题编号）

let scQuestionArray = new Array();// 保存用户选中的 单项选择题 的选项信息（key: 问题编号，value: 选项编号）
let mcQuestionArray = new Array();// 保存用户选中的 多项选择题 的选项信息（key: 选项编号，value: 问题编号）
let jmQuestionArray = new Array();// 保存用户选中的 判断题 的选项信息（key: 问题编号，value: 选项编号）
let fbQuestionArray1 = new Array();// 保存用户选中的 填空题 的选项信息（key: 问题编号，value: 选项编号）
let fbQuestionArray2 = new Array();// 保存用户选中的 填空题 的选项信息（key: 选项编号，value: 值）
let sQuestionArray1 = new Array();// 保存用户选中的 评分题 的选项信息（key: 问题编号，value: 选项编号）
let sQuestionArray2 = new Array();// 保存用户选中的 评分题 的选项信息（key: 选项编号，value: 值）

let showCount = 0;// 保存当前显示的问题的个数

$(function () {
    if ($(".questionnaire").attr("value") == "false") {
        selectedOptionsAndFillContent();// 用户已填写问卷，动态选中选项和填充文本内容
    } else {
        // 生成评分组件
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
                        sQuestionArray1[$(this.elem[0]).attr("name")] = this.elem[0].id;
                        sQuestionArray2[this.elem[0].id] = value.toString();
                        // console.log("qId: " + $(this.elem[0]).attr("name") + "  oId: " + this.elem[0].id + "  value: " + value);
                        SetButtonSelected();// 设置显示的每种题型的按钮是否被选中
                        $(".sub.s").parent("li").children("h3").click();
                    }
                });
            })
        });
    }

    GenerateNumber();// 生成每种题型的个数的按钮
    SetShowNumber();// 设置显示的问题的问题序号

    $("#save").click(Save);

    // 点击题型，显示该题型的问题序号按钮
    $(".question .nLi h3").click(function () {
        if ($(this).parent(".nLi").hasClass("on")) {
            //当前状态展开的时候，继续点击无效
        } else {
            $(this).parents("ul").find(".sub").slideUp(300, function () {
                $(this).parents("ul").find(".nLi").removeClass("on");
            });
            $(this).next(".sub").slideDown(300, function () {
                $(this).parent(".nLi").addClass("on");
            });
        }
    });

    // 点击问题序号按钮，滚动到当前问题序号处
    $(".sub button").click(function () {
        $('html,body').animate({scrollTop: $("#" + $(this).text() + "").offset().top - 50}, 1000);
    });
});

// 鼠标点击触发（单项选择题/多项选择题/判断题）
function ClickOption(object) {
    // 当前选项所在的问题有后置问题
    if ($($(object).parent("label").parent("div").parent("div").find("div:nth-child(2)").children("span:first")[0]).attr("value") == "true") {
        if (object.value == "sc-true" || object.value == "jm-true") {
            // 当前选项有后置问题（单项选择题/判断题）
            $(object).parent("label").parent("div").parent("div").next().attr("style", "display:block;color:green;");
            $($(object).parent("label").parent("div").parent("div").next()[0]).children("div:first-child").addClass("Show");
            let key = object.name;// key: 当前问题的问题编号
            let value = $(object).parent("label").parent("div").parent("div").next().children("div:nth-child(3)").children("label").children("input")[0].name;// value: 下一个问题的问题编号
            rearQuestionArray[key] = value;
        } else if (object.value == "sc-false" || object.value == "jm-false") {
            // 当前选项无后置问题（单项选择题/判断题）
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
                    if ($(":radio[name=" + value + "]:checked").length == 0) {
                        // 后置问题没有被选中
                        $(":radio[name=" + value + "]").parent("label").parent("div").parent("div").attr("style", "display:none;");
                        $(":radio[name=" + value + "]").parent("label").parent("div").parent("div").children("div:first-child").removeClass("Show");
                    } else {
                        // 后置问题被选中
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

        if (object.value == "mc-true") {
            // 当前选项有后置问题（多项选择题）
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
                        if ($(":checkbox[name=" + value + "]:checked").length == 0) {
                            // 后置问题没有被选中
                            $(":checkbox[name=" + value + "]").parent("label").parent("div").parent("div").attr("style", "display:none;");
                            $(":checkbox[name=" + value + "]").parent("label").parent("div").parent("div").children("div:first-child").removeClass("Show");
                        } else {
                            // 后置问题被选中
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
    }

    if (object.value == "sc-true" || object.value == "sc-false") {// 点击单项选择题
        scQuestionArray[object.name] = object.id;
        $(".sub.sc").parent("li").children("h3").click();
    } else if (object.value == "jm-true" || object.value == "jm-false") {// 点击判断题
        jmQuestionArray[object.name] = object.id;
        $(".sub.jm").parent("li").children("h3").click();
    } else if (object.value == "mc-true" || object.value == "mc-false") {// 点击多项选择题
        if (mcQuestionArray[object.id] != null) {
            delete mcQuestionArray[object.id];
        } else {
            mcQuestionArray[object.id] = object.name;
        }
        $(".sub.mc").parent("li").children("h3").click();
    }

    SetShowNumber();// 设置显示的问题的问题序号
    SetButtonSelected();// 设置显示的每种题型的按钮是否被选中
}

// 鼠标移出触发（填空题）
function BlurOption1(object) {
    let value = $.trim($(object).val());
    if (value != null && value != "") {
        fbQuestionArray1[object.name] = object.id;
        fbQuestionArray2[object.id] = value;
        $(object).parent("div").children("span").text($(object).val().length);
    } else {
        delete fbQuestionArray1[object.name];
        delete fbQuestionArray2[object.id];
    }
    SetButtonSelected();// 设置显示的每种题型的按钮是否被选中
}

// 鼠标移出触发（用户留言）
function BlurOption2(object) {
    $(object).parent("div").children("span").text($(object).val().length);
    SetButtonSelected();// 设置显示的每种题型的按钮是否被选中
}

// 键盘松开触发（填空题/用户留言）
function KeyUpOption(object) {
    $(object).parent("div").children("span").text($(object).val().length);
}

// 提交用户填写的问卷信息
function Save() {
    let flag = true;
    $(".sub button").each(function () {
        let value = this.innerText;
        if (value != null && value != "") {
            let color = $(this).attr("style").toString();
            color = color.substring(color.indexOf('#') + 1, color.indexOf(';'));
            if (color == "d8d8d8") {
                let requiredLength = $("#" + value + "").parent("div").children("div:nth-child(2)").children("span:nth-child(3)").length;
                if (requiredLength != 0) {
                    ShowFailure("您还有问题没有填写！！！");
                    $('html,body').animate({scrollTop: $("#" + $(this).text() + "").offset().top - 50}, 1000);
                    flag = false;
                    return false;
                }
                // console.log();
            }
        }
    });
    if (!flag) {
        return;
    }

    console.log("单项选择题: ");
    let sc = new Array();
    for (let i in scQuestionArray) {
        console.log("qId: " + i + " oId: " + scQuestionArray[i]);
        let object = new Object();
        object.qId = i;
        object.oId = scQuestionArray[i];
        object.qt = "singleChoice";
        sc.push(object);
    }

    console.log("多项选择题: ");
    let mc = new Array();
    for (let i in mcQuestionArray) {
        console.log("oId: " + i + " qId: " + mcQuestionArray[i]);
        let object = new Object();
        object.oId = i;
        object.qId = mcQuestionArray[i];
        object.qt = "multipleChoice";
        mc.push(object);
    }

    console.log("判断题: ");
    let jm = new Array();
    for (let i in jmQuestionArray) {
        console.log("qId: " + i + " oId: " + jmQuestionArray[i]);
        let object = new Object();
        object.qId = i;
        object.oId = jmQuestionArray[i];
        object.qt = "judgment";
        jm.push(object);
    }

    console.log("填空题: ");
    let fb = new Array();
    for (let i in fbQuestionArray1) {
        let oId = fbQuestionArray1[i];
        console.log("qId: " + i + " oId: " + oId + " value: " + fbQuestionArray2[oId]);
        let object = new Object();
        object.qId = i;
        object.oId = oId;
        object.value = fbQuestionArray2[oId];
        object.qt = "fillBlank";
        fb.push(object);
    }

    console.log("评分题: ");
    let s = new Array();
    for (let i in sQuestionArray1) {
        let oId = sQuestionArray1[i];
        console.log("qId: " + i + " oId: " + oId + " value: " + sQuestionArray2[oId]);
        let object = new Object();
        object.qId = i;
        object.oId = oId;
        object.value = sQuestionArray2[oId];
        object.qt = "score";
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
            'userId': $('#onlineUser').val(),
            'qnId': $(".questionnaire .QN-Title").attr("value"),
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
                ShowSuccess("成功提交问卷，感谢您的参与！！！");
                setTimeout(function () {
                    window.location.href = window.location.pathname + window.location.search;
                }, 1000);
            } else {
                ShowFailure(result.message);
            }
        }
    });
}

// 生成每种题型的个数的按钮
function GenerateNumber() {
    let html = "";
    // 生成单项选择题的个数的按钮
    for (let i = 0; i < $(".questionnaire .sc").length; i++) {
        html += "                                <button></button>";
    }
    $(html).appendTo($(".sub.sc"));

    html = "";
    // 生成多项选择题的个数的按钮
    for (let i = 0; i < $(".questionnaire .mc").length; i++) {
        html += "                                <button></button>";
    }
    $(html).appendTo($(".sub.mc"));

    html = "";
    // 生成判断题的个数的按钮
    for (let i = 0; i < $(".questionnaire .jm").length; i++) {
        html += "                                <button></button>";
    }
    $(html).appendTo($(".sub.jm"));

    html = "";
    // 生成填空题的个数的按钮
    for (let i = 0; i < $(".questionnaire .fb").length; i++) {
        html += "                                <button></button>";
    }
    $(html).appendTo($(".sub.fb"));

    html = "";
    // 生成评分题的个数的按钮
    for (let i = 0; i < $(".questionnaire .s").length; i++) {
        html += "                                <button></button>";
    }
    $(html).appendTo($(".sub.s"));

    html = "";
    // 生成用户留言的个数的按钮
    for (let i = 0; i < $(".questionnaire .uc").length; i++) {
        html += "                                <button></button>";
    }
    $(html).appendTo($(".sub.uc"));
}

// 设置显示的问题的问题序号
function SetShowNumber() {
    if ($(".Show").length != showCount) {
        // 循环设置问题题号
        let i = 0;
        $(".Show").each(function () {
            $(this).text((++i) + "、");
        });
        showCount = i;
        // SetButtonShowAndText();// 设置每种题型的按钮显示和文本
    }

    // 删除所有问题的id属性（包括显示和隐藏的问题）
    $(".QNumber").each(function () {
        $(this).removeAttr("id");
    });
    let i = 0;
    // 重新给显示的问题添加id属性
    $(".Show").each(function () {
        $(this).attr("id", ++i);
    });

    SetButtonShowAndText();// 设置每种题型的按钮显示和文本
}

// 设置每种题型的按钮显示和文本
function SetButtonShowAndText() {
    let scLength = $(".questionnaire .sc.Show").length;// 显示的单项选择题的个数
    let mcLength = $(".questionnaire .mc.Show").length;// 显示的多项选择题的个数
    let jmLength = $(".questionnaire .jm.Show").length;// 显示的判断题的个数
    let fbLength = $(".questionnaire .fb.Show").length;// 显示的填空题的个数
    let sLength = $(".questionnaire .s.Show").length;// 显示的评分题的个数
    let ucLength = $(".questionnaire .uc.Show").length;// 显示的用户留言的个数
    let length = 0;// 显示的问题按钮的序号

    let i = 0;
    // 生成显示的单项选择题按钮的序号文本
    $(".sub.sc button").each(function () {
        $(this).hide();
        if (i < scLength) {
            $(this).text(++length);
            $(this).attr("style", "background-color: #d8d8d8;");
            $(this).attr("title", $("#" + length + "").parent("div").children("div:nth-child(2)").children("span:first-child").text());
            $(this).show();
            i++;
        }
    });

    i = 0;
    // 生成显示的多项选择题按钮的序号文本
    $(".sub.mc button").each(function () {
        $(this).hide();
        if (i < mcLength) {
            $(this).text(++length);
            $(this).attr("style", "background-color: #d8d8d8;");
            $(this).attr("title", $("#" + length + "").parent("div").children("div:nth-child(2)").children("span:first-child").text());
            $(this).show();
            i++;
        }
    });

    i = 0;
    // 生成显示的判断题按钮的序号文本
    $(".sub.jm button").each(function () {
        $(this).hide();
        if (i < jmLength) {
            $(this).text(++length);
            $(this).attr("style", "background-color: #d8d8d8;");
            $(this).attr("title", $("#" + length + "").parent("div").children("div:nth-child(2)").children("span:first-child").text());
            $(this).show();
            i++;
        }
    });

    i = 0;
    // 生成显示的填空题按钮的序号文本
    $(".sub.fb button").each(function () {
        $(this).hide();
        if (i < fbLength) {
            $(this).text(++length);
            $(this).attr("style", "background-color: #d8d8d8;");
            $(this).attr("title", $("#" + length + "").parent("div").children("div:nth-child(2)").children("span:first-child").text());
            $(this).show();
            i++;
        }
    });

    i = 0;
    // 生成显示的评分题按钮的序号文本
    $(".sub.s button").each(function () {
        $(this).hide();
        if (i < sLength) {
            $(this).text(++length);
            $(this).attr("style", "background-color: #d8d8d8;");
            $(this).attr("title", $("#" + length + "").parent("div").children("div:nth-child(2)").children("span:first-child").text());
            $(this).show();
            i++;
        }
    });

    i = 0;
    // 生成显示的用户留言按钮的序号文本
    $(".sub.uc button").each(function () {
        $(this).hide();
        if (i < ucLength) {
            $(this).text(++length);
            $(this).attr("style", "background-color: #d8d8d8;");
            $(this).attr("title", $("#" + length + "").parent("div").children("div:nth-child(2)").children("span:first-child").text());
            $(this).show();
            i++;
        }
    });
}

// 设置显示的每种题型的按钮是否被选中
function SetButtonSelected() {
    // 点击单项选择题，获取已选中的问题序号
    let scArray = new Array();
    let scLength = $(".sc.Show").length;
    $(".sc.Show").each(function () {
        $(this).parent("div").find(".radio").each(function () {
            if ($(this).children("label").children("input")[0].checked) {
                scArray.push($(this).parent("div").children("div:first-child").text().replace('、', ''));
            }
        });
    });
    // console.log(scArray);
    // 根据选中的问题序号，设置对应序号按钮的颜色
    let i = 0;
    $(".sub.sc button").each(function () {
        if ($(this).text() != null && $(this).text() != "") {
            if ($(this).text() == scArray[i]) {
                i++;
                $(this).attr("style", "background-color: #85d1e0;");
            } else {
                $(this).attr("style", "background-color: #d8d8d8;");
            }
        }
    });
    // 将未显示的问题序号按钮的文本置为空，删除样式，隐藏
    i = 0;
    $(".sub.sc button").each(function () {
        if (i++ >= scLength && $(this).text() != null && $(this).text() != "") {
            // console.log("i: " + i);
            $(this).text("");
            $(this).removeAttr("style");
            $(this).removeAttr("title");
            $(this).hide();
        }
    });


    // 点击多项选择题，获取已选中的问题序号
    let mcArray = new Array();
    let mcLength = $(".mc.Show").length;
    $(".mc.Show").each(function () {
        $(this).parent("div").find(".checkbox").each(function () {
            if ($(this).children("label").children("input")[0].checked) {
                let value = $(this).parent("div").children("div:first-child").text().replace('、', '');
                mcArray[value] = value;// 去重
            }
        });
    });
    mcArray = mcArray.filter(Boolean);// 过滤后只保留值
    // console.log(mcArray);
    // 根据选中的问题序号，设置对应序号按钮的颜色
    i = 0;
    $(".sub.mc button").each(function () {
        if ($(this).text() != null && $(this).text() != "") {
            if ($(this).text() == mcArray[i]) {
                i++;
                $(this).attr("style", "background-color: #85d1e0;");
            } else {
                $(this).attr("style", "background-color: #d8d8d8;");
            }
        }
    });
    // 将未显示的问题序号按钮的文本置为空，删除样式，隐藏
    i = 0;
    $(".sub.mc button").each(function () {
        if (i++ >= mcLength && $(this).text() != null && $(this).text() != "") {
            // console.log("i: " + i);
            $(this).text("");
            $(this).removeAttr("style");
            $(this).removeAttr("title");
            $(this).hide();
        }
    });


    // 点击判断题，获取已选中的问题序号
    let jmArray = new Array();
    let jmLength = $(".jm.Show").length;
    $(".jm.Show").each(function () {
        $(this).parent("div").find(".radio").each(function () {
            if ($(this).children("label").children("input")[0].checked) {
                jmArray.push($(this).parent("div").children("div:first-child").text().replace('、', ''));
            }
        });
    });
    // console.log(jmArray);
    // 根据选中的问题序号，设置对应序号按钮的颜色
    i = 0;
    $(".sub.jm button").each(function () {
        if ($(this).text() != null && $(this).text() != "") {
            if ($(this).text() == jmArray[i]) {
                i++;
                $(this).attr("style", "background-color: #85d1e0;");
            } else {
                $(this).attr("style", "background-color: #d8d8d8;");
            }
        }
    });
    // 将未显示的问题序号按钮的文本置为空，删除样式，隐藏
    i = 0;
    $(".sub.jm button").each(function () {
        if (i++ >= jmLength && $(this).text() != null && $(this).text() != "") {
            // console.log("i: " + i);
            $(this).text("");
            $(this).removeAttr("style");
            $(this).removeAttr("title");
            $(this).hide();
        }
    });


    // 点击填空题，获取已选中的问题序号
    let fbArray = new Array();
    $(".fb.Show").each(function () {
        $(this).parent("div").find(".fillBlank").each(function () {
            let value = $(this).children("textarea").val();
            if (value != null && value != "") {
                fbArray.push($(this).parent("div").children("div:first-child").text().replace('、', ''));
            }
        });
    });
    // console.log(fbArray);
    // 根据选中的问题序号，设置对应序号按钮的颜色
    i = 0;
    $(".sub.fb button").each(function () {
        if ($(this).text() != null && $(this).text() != "") {
            if ($(this).text() == fbArray[i]) {
                i++;
                $(this).attr("style", "background-color: #85d1e0;");
            } else {
                $(this).attr("style", "background-color: #d8d8d8;");
            }
        }
    });


    // 点击评分题，获取已选中的问题序号
    let sArray = new Array();
    $(".s.Show").each(function () {
        $(this).parent("div").find(".score").each(function () {
            let value = sQuestionArray2[this.id];
            if (value != 0 && value != null && value != "") {
                sArray.push($(this).parent("div").children("div:first-child").text().replace('、', ''));
            }
        });
    });
    // console.log(sArray);
    // 根据选中的问题序号，设置对应序号按钮的颜色
    i = 0;
    $(".sub.s button").each(function () {
        if ($(this).text() != null && $(this).text() != "") {
            if ($(this).text() == sArray[i]) {
                i++;
                $(this).attr("style", "background-color: #85d1e0;");
            } else {
                $(this).attr("style", "background-color: #d8d8d8;");
            }
        }
    });


    // 点击用户留言，获取已选中的问题序号
    let ucArray = new Array();
    $(".uc.Show").each(function () {
        $(this).parent("div").find(".fillBlank").each(function () {
            // let value = ucQuestionArray[this.id];
            // if (value != 0 && value != null && value != "") {
            //     ucArray.push($(this).parent("div").children("div:first-child").text().replace('、', ''));
            // }
            let value = $(this).children("textarea").val();
            if (value != null && value != "") {
                ucArray.push($(this).parent("div").children("div:first-child").text().replace('、', ''));
            }
        });
    });
    // console.log(ucArray);
    // 根据选中的问题序号，设置对应序号按钮的颜色
    i = 0;
    $(".sub.uc button").each(function () {
        if ($(this).text() != null && $(this).text() != "") {
            if ($(this).text() == ucArray[i]) {
                i++;
                $(this).attr("style", "background-color: #85d1e0;");
            } else {
                $(this).attr("style", "background-color: #d8d8d8;");
            }
        }
    });
}

// 鼠标按下填空题，展开导航侧边栏
function ShowOnFBh3() {
    $(".sub.fb").parent("li").children("h3").click();
}

// 鼠标按下填空题，展开导航侧边栏
function ShowOnUCh3() {
    $(".sub.uc").parent("li").children("h3").click();
}

// 用户已填写问卷，动态选中选项和填充文本内容
function selectedOptionsAndFillContent() {
    // 已填写友情提示
    layui.use('layer', function () {
        let layer = layui.layer;
        layer.open({
            title: '友情提示'
            , content: '您已填写过该问卷，目前只能查看该问卷！！！'
        });
    });

    let userOperating = $('#userOperating option:selected').val();
    // let userOperating = $("#userOperating").parent("p").children("div").children("div").children("ul").children("li:first-child").attr("data-value");// 得到用户编号
    let qnId = $(".questionnaire .QN-Title").attr("value");

    $.ajax({
        async: true, // 异步请求
        type: "get",
        url: CONTEXT_PATH + '/getAllAnswerByUserIdAndQNId',
        data: {
            'userId': userOperating,
            'qnId': qnId,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                for (let i = 0; i < result.data.length; i++) {
                    if (result.data[i].qt == "singleChoice") {
                        for (let j = 0; j < result.data[i].singleChoice.length; j++) {
                            let sc = "#" + result.data[i].singleChoice[j].answer.optionId + "";
                            $(sc).prop("checked", true);
                            $(sc).parent("label").parent("div").parent("div").removeAttr("style");
                            $(sc).parent("label").parent("div").parent("div").children("div:first-child").addClass("Show");
                        }
                    } else if (result.data[i].qt == "multipleChoice") {
                        for (let j = 0; j < result.data[i].multipleChoice.length; j++) {
                            let mc = "#" + result.data[i].multipleChoice[j].answer.optionId + "";
                            $(mc).prop("checked", true);
                            $(mc).parent("label").parent("div").parent("div").removeAttr("style");
                            $(mc).parent("label").parent("div").parent("div").children("div:first-child").addClass("Show");
                        }
                    } else if (result.data[i].qt == "judgment") {
                        for (let j = 0; j < result.data[i].judgment.length; j++) {
                            let jm = "#" + result.data[i].judgment[j].answer.optionId + "";
                            $(jm).prop("checked", true);
                            $(jm).parent("label").parent("div").parent("div").removeAttr("style");
                            $(jm).parent("label").parent("div").parent("div").children("div:first-child").addClass("Show");
                        }
                    } else if (result.data[i].qt == "fillBlank") {
                        for (let j = 0; j < result.data[i].fillBlank.length; j++) {
                            let fb = "#" + result.data[i].fillBlank[j].answer.optionId + "";
                            $(fb).text(result.data[i].fillBlank[j].answer.optionContent);
                            $(fb).parent("div").children("span").text($(fb).val().length);
                        }
                    } else if (result.data[i].qt == "score") {
                        for (let j = 0; j < result.data[i].score.length; j++) {
                            let value = parseFloat(result.data[i].score[j].answer.optionContent);
                            let oId = result.data[i].score[j].answer.optionId;
                            sQuestionArray2[oId] = value.toString();
                        }

                        // 生成评分组件
                        layui.use('rate', function () {
                            let rate = layui.rate;
                            for (let j = 0; j < result.data[i].score.length; j++) {
                                let s = "#" + result.data[i].score[j].answer.optionId + "";
                                let value = parseFloat(result.data[i].score[j].answer.optionContent);

                                //渲染
                                let ins1 = rate.render({
                                    elem: s,  //绑定元素，指向容器选择器
                                    length: 5,// 评分组件中具体星星的个数
                                    value: value,// 评分的初始值
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
                                });
                            }
                        });
                    } else if (result.data[i].qt == "userComment") {
                        $("#userComments").text(result.data[i].userComment.usercommentContent);
                        $("#userComments").parent("div").children("span").text($("#userComments").val().length);
                    }
                }

                SetShowNumber();// 设置显示的问题的问题序号
                SetButtonSelected();// 设置显示的每种题型的按钮是否被选中

            } else {
                ShowFailure(result.message);
            }
        }
    });
}