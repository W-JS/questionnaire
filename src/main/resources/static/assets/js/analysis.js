let chart;

// makeCharts('light', '#FFFFFF')
function makeCharts(theme, bgColor) {
    if (chart) {
        chart.clear();
    }

    // background
    if (document.body) {
        document.body.style.backgroundColor = bgColor;
    }

    let data = new Array();

    let object = new Object();
    object.question = "选项01";
    object.litres = 100;
    data.push(object);

    object = new Object();
    object.question = "选项02";
    object.litres = 100;
    data.push(object);

    object = new Object();
    object.question = "选项03";
    object.litres = 100;
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

}