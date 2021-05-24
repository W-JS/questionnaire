let x = $(window);
let e = $("#shape");

$("html,body").ready(function () {
    let scrollbar = x.scrollTop();
    let isClick = 0;

    $(window).scroll(function () {
        scrollbar = x.scrollTop();
        (scrollbar <= 500) ? ($("#shape").hide()) : ($("#shape").show());
    });

    $("#shape").hover(
        function () {
            $(".shapeColor").show();
        },

        function () {
            $(".shapeColor").hide();
        }
    );

    $(".shapeColor").click(
        function () {
            $(".shapeFly").show();
            $("html,body").animate({scrollTop: 0}, "slow");
            $("#shape").delay("200").animate({marginTop: "-1000px"}, "slow", function () {
                $("#shape").css("margin-top", "-125px");
                $(".shapeFly").hide();
            });

        })

});