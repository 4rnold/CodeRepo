<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../base.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>黑马有应用配置管理</title>
</head>
<body>
    <div id="frameContent" class="content-wrapper" style="margin-left:0px;">
        <section class="content-header">
            <h1>
                配置管理
                <small>上传配置</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            </ol>
        </section>
        <section class="content">
            <div class="box-body">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#tab-form" data-toggle="tab">上传配置</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <form action="${pageContext.request.contextPath}/config/push.do" method="post" enctype="multipart/form-data">
                            配置文件：<input type="file" name="configFile"><br/>
                            <input type="submit" value="上传">
                        </form>
                    </div>
                </div>
            </div>

        </section>
    </div>
</body>

</html>