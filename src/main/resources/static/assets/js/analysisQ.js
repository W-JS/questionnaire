let chart;
let url_qnId = GetQueryString('qnId');
let url_qId = GetQueryString('qId');

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

    let data = new Array();
    let object;

    $.ajax({
        async: false, // 同步请求
        type: "get",
        url: CONTEXT_PATH + '/dataAnalysis/getOptionsNumberByQNIdAndQId',
        data: {
            'qnId': url_qnId,
            'qId': url_qId,
        },
        dataType: 'json',
        success: function (result) {
            if (result.state == 1) {
                for (let key in result.data) {
                    object = new Object();
                    object.option = key;
                    object.litres = result.data[key];
                    data.push(object);
                }
            } else {
                ShowFailure(result.message);
            }
        }
    });

    // pie chart
    chart = AmCharts.makeChart(url_qId, {
        type: "pie",
        theme: theme,
        dataProvider: data,
        titleField: "option",
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
        titleField: "option",
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