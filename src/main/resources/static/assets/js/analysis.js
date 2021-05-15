let chart;
let url_qnId = GetQueryString('qnId');

$(function () {
    makeCharts('light', '#FFFFFF');
});

// 动态生成图形
function makeCharts(theme, bgColor) {
    if (chart) {
        chart.clear();
    }

    // background
    if (document.body) {
        document.body.style.backgroundColor = bgColor;
    }

    let all = 0;// 总人数
    let filled = 0;// 已填写人数
    let unFilled = 0;// 未填写人数

    $.ajax({
        async: false, // 同步请求
        type: "get",
        url: CONTEXT_PATH + '/dataAnalysis/getWriteQuestionnaireNumberByQNId',
        data: {
            'qnId': url_qnId,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                all = result.data.all;
                filled = result.data.filled;
                unFilled = result.data.unFilled;
            } else {
                ShowFailure(result.message);
            }
        }
    });

    let data = new Array();

    let object = new Object();
    object.question = "已填";
    object.litres = filled;
    data.push(object);

    object = new Object();
    object.question = "未填";
    object.litres = unFilled;
    data.push(object);

    // pie chart
    chart = AmCharts.makeChart("analysis", {
        type: "pie",
        theme: theme,
        dataProvider: data,
        /*dataProvider: [
            {
                "question": "选项01",
                "litres": 156.9
            },
            {
                "question": "选项02",
                "litres": 131.1
            },
            {
                "question": "选项03",
                "litres": 115.8
            },
            {
                "question": "选项04",
                "litres": 109.9
            },
            {
                "question": "选项05",
                "litres": 108.3
            },
            {
                "question": "选项06",
                "litres": 65
            },
            {
                "question": "选项07",
                "litres": 50
            }
        ],*/
        titleField: "question",
        valueField: "litres",
        balloonText: "[[title]]<br><b>[[value]]</b> ([[percents]]%)",
        legend: {
            align: "center",
            markerType: "circle"
        }
    });
    chart = AmCharts.makeChart("analysis2", {
        type: "pie",
        theme: theme,
        dataProvider: data,
        /*dataProvider: [
            {
                "question": "选项01",
                "litres": 156.9
            },
            {
                "question": "选项02",
                "litres": 131.1
            },
            {
                "question": "选项03",
                "litres": 115.8
            },
            {
                "question": "选项04",
                "litres": 109.9
            },
            {
                "question": "选项05",
                "litres": 108.3
            },
            {
                "question": "选项06",
                "litres": 65
            },
            {
                "question": "选项07",
                "litres": 50
            }
        ],*/
        titleField: "question",
        valueField: "litres",
        balloonText: "[[title]]<br><b>[[value]]</b> ([[percents]]%)",
        legend: {
            align: "center",
            markerType: "circle"
        }
    });
}

// 关闭当前页面
function windowClose() {
    window.close();
}