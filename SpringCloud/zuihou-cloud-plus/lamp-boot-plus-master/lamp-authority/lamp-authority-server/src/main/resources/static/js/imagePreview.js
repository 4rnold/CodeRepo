/**
 * Created by hucf on 2019/11/20.
 */
var ImagePreview = {
    default_params: {},
    init: function (param) {
        if (param) {
            $.extend(this.default_params, param);
        }
        ip_global_img_list = [];
        var id = this.default_params.id;
        if (typeof id == 'string') {
            if ((ip_global_img_list = $("#" + id).find(".play")).length == 0) {
                return;
            }
        } else if (typeof id == 'object' && id != null) {
            ip_global_img_list = $(id);
        }
        this.fnGenerateHtml();
        this.fnBindEvent(ip_global_img_list);
    },
    fnBindEvent: function (ip_global_img_list) {
        if (ip_global_img_list.length == 0) {
            return;
        }
        $(ip_global_img_list).on("click", this.onClickEvent);
        this.onDragEvent($("#ip-img-preview"));
        $("#ip-img-preview").on('mousewheel DOMMouseScroll', this.onMouseScrollEvent);

        $('#ip-img-floatshadow').on("click", function () {
            $('#ip-img-preview').hide();
            $("#ip-left").hide();
            $("#ip-right").hide();
            $('#ip-img-floatshadow').hide();
            $('#ip-img-preview').attr("src", "");
        });
        $("#ip-left").hover(this.fnMouseOver, this.fnMouseOut).click(this.fnPrev);
        $("#ip-right").hover(this.fnMouseOver, this.fnMouseOut).click(this.fnNext);
    },
    fnGenerateHtml: function () {
        $("body").append('<img id="ip-img-preview" style="position: fixed;left: 50%;top: 50%;transform: translate(-50%, -50%);z-index: 19941206;cursor: move;display: none"/><div id="ip-img-floatshadow" style="z-index: 19941205;background-color: #000;opacity: .5;top: 0;left: 0;width: 100%;height: 100%;position: fixed;display: none" title="点击空白处关闭"></div>');
        // $("body").append('<div id="ip-left" style="display: none; width: 100px; height: 100px;left: 5px; top: 50%; position:fixed;z-index:19941207; cursor: pointer;">' +
        //     '<div style="left:-30px;border: 50px solid;border-color: transparent #1CB9C4 transparent transparent;position: absolute;"></div></div>');
        // $("body").append('<div id="ip-right" style="display: none; width: 100px; height: 100px; right: 5px; top: 50%; position:fixed; z-index:19941207;cursor: pointer;">' +
        //     '<div style="left:30px;border: 50px solid;border-color: transparent transparent transparent #1CB9C4;position: absolute;"></div></div>');
    },
    fnMouseOver: function () {
        $(this).css("background", "rgb(134, 134, 134)");
        $(this).css("border", "1px solid rgb(111, 111, 111)");
    },
    fnMouseOut: function () {
        $(this).css("background", "");
        $(this).css("border", "");
    },
    fnPrev: function () {
        if (typeof(ip_global_cur) == "number" && ip_global_cur > 0) {
            ImagePreview.fnReset();
            $("#ip-img-preview").animate({left: "48%"}, 100);
            $("#ip-img-preview").attr("src", ip_global_img_list[--ip_global_cur].src);
            $("#ip-img-preview").animate({left: "50.5%"}, 100);
            $("#ip-img-preview").animate({left: "50%"}, 100);
            ImagePreview.fnAdjustMaxWidth();
        }
    },
    fnNext: function () {
        if (typeof(ip_global_cur) == "number" && ip_global_cur < ip_global_img_list.length - 1) {
            ImagePreview.fnReset();
            $("#ip-img-preview").animate({left: "52%"}, 100);
            $("#ip-img-preview").attr("src", ip_global_img_list[++ip_global_cur].src);
            $("#ip-img-preview").animate({left: "49.5%"}, 100);
            $("#ip-img-preview").animate({left: "50%"}, 100);
            ImagePreview.fnAdjustMaxWidth();
        }
    },
    fnGetIndexOfCurImg: function (cur) {
        for (var i = 0; i < ip_global_img_list.length; i++) {
            if ($(ip_global_img_list[i]).is(cur)) {
                return i;
            }
        }
    },
    onClickEvent: function (e) {
        ImagePreview.fnReset();
        $("#ip-img-preview").attr("src", $(this).attr("id"));
        ImagePreview.fnAdjustMaxWidth();
        ip_global_cur = ImagePreview.fnGetIndexOfCurImg($(this));
        $("#ip-img-floatshadow").fadeIn();
        $("#ip-img-preview").fadeIn();
        $("#ip-left").fadeIn();
        $("#ip-right").fadeIn();
    },
    fnAdjustMaxWidth: function () {
        //最长边判定，避免超出屏幕画幅的展示
        var widthFlag = true;
        var max = $("#ip-img-preview").width();
        if (max < $("#ip-img-preview").height()) {
            widthFlag = false;
            max = $("#ip-img-preview").height();
        }
        if (widthFlag && $(window).width() < max) {
            $("#ip-img-preview").css("width", "75%");
        } else if (!widthFlag && $(window).height() < max) {
            $("#ip-img-preview").css("height", "75%");
        }
    },
    fnReset: function () {
        $("#ip-img-preview").css("left", "50%");
        $("#ip-img-preview").css("top", "50%");
        $("#ip-img-preview").css("width", "");
        $("#ip-img-preview").css("height", "");
    },
    onMouseScrollEvent: function (e) {
        e.preventDefault();
        var wheel = e.originalEvent.wheelDelta || -e.originalEvent.detail;
        var delta = Math.max(-1, Math.min(1, wheel));
        if (delta < 0) { //向下滚动
            $(this).width($(this).width() / 1.1);
            $(this).height($(this).height() / 1.1);
        } else { //向上滚动
            $(this).width($(this).width() * 1.1);
            $(this).height($(this).height() * 1.1);
        }
    },
    onDragEvent: function (obj) {
        obj.bind("mousedown", start);

        function start(event) {
            if (event.button == 0) {
                gapX = event.clientX - obj.offset().left;
                gapY = event.clientY - obj.offset().top;
                $(document).bind("mousemove", move);
                $(document).bind("mouseup", stop);
            }
            return false;
        }

        function move(event) {
            obj.css({
                "left": (event.clientX - gapX + obj.width() / 2) + "px",
                "top": (event.clientY - gapY + obj.height() / 2) + "px"
            });
            return false;
        }

        function stop() {
            $(document).unbind("mousemove", move);
            $(document).unbind("mouseup", stop);
        }
    }
};