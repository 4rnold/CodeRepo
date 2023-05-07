<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>${fileName}</title>
    <!-- HTML5 Shim and Respond.js IE10 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 10]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="author" content="FH Admin QQ313596790"/>

    <link rel="stylesheet" href="css/fontawesome-all.min.css">
    <link rel="stylesheet" href="css/animate.min.css">
    <link rel="stylesheet" href="css/style.css">

    <!-- 代码编辑器 -->
    <script src="js/jquery.js"></script>
    <script>
        var codetype = "java";
        var unid = "59396e99ae344";
    </script>
    <script src="js/runcode.js"></script>
    <style type="text/css" media="screen">
        #editor {
        / / position: absolute;
            width: 100%;
            height: 565px;
            float: left;
            font-size: 14px;
        }
    </style>
    <!-- 代码编辑器 -->

</head>
<body>

<!-- [加载状态 ] start -->
<div class="loader-bg">
    <div class="loader-track">
        <div class="loader-fill"></div>
    </div>
</div>
<!-- [ 加载状态  ] End -->

<!-- [ 主内容区 ] start -->
<div class="starter-template">
    <div id="editor" class="ace_editor ace-monokai ace_dark"><textarea id="codeContent" class="ace_text-input"
                                                                       wrap="off" autocorrect="off" autocapitalize="off"
                                                                       spellcheck="false"
                                                                       style="opacity: 0; height: 17px; width: 8px; left: 45px; top: 0px;">${pd.code}</textarea>
    </div>
</div>
<!-- [ 主内容区 ] end -->

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/pre-loader.js"></script>
<script src="js/sweetalert.min.js"></script>
<script src="js/ace.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">

    $(cmainFrame());

    window.onresize = function () {
        cmainFrame();
    };

    if (ie_error()) {
        $('#editor').hide();
    } else {
        $('#editorBox').hide();
        ace.require("ace/ext/language_tools");
        var editor = ace.edit("editor");
        editor.setOptions({
            enableBasicAutocompletion: true,
            enableSnippets: true,
            enableLiveAutocompletion: true
        });
        editor.setTheme("ace/theme/monokai");
        editor.getSession().setMode("ace/mode/java");
    }

    function cmainFrame() {
        var hmain = document.getElementById("editor");
        var bheight = document.documentElement.clientHeight;
        hmain.style.width = '100%';
        hmain.style.height = (bheight) + 'px';
    }

</script>

</body>
</html>