$(function () {
    layui.use('rate', function () {
        var rate = layui.rate;

        //渲染
        var ins1 = rate.render({
            elem: '#score',  //绑定元素，指向容器选择器
            length: 5,// 评分组件中具体星星的个数
            value: 0,// 评分的初始值
            theme: "#FFB800",// 主题颜色
            half: true,// 设定组件是否可以选择半星
            text: true,// 是否显示评分对应的内容
            readonly: false// 是否只读，即只用于展示而不可点
            ,setText: function(value){
                var arrs = {
                    '1': '极差'
                    ,'2': '差'
                    ,'3': '中等'
                    ,'4': '好'
                    ,'5': '极好'
                };
                this.span.text(arrs[value] || ( value + "星"));
            }
            ,choose: function(value){
                console.log(value);
            }
        });
    });
});