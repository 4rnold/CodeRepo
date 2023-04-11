<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../base.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>黑马应用配置管理</title>
    <script type="text/javascript">
        function submitForm(configName){
            document.getElementById("configName").value = configName;
            document.getElementById("editForm").submit()
        }
    </script>
</head>
<body>
    <div id="frameContent" class="content-wrapper" style="margin-left:0px;">
        <section class="content-header">
            <h1>
                配置管理
                <small>查看配置</small>
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
                            <a href="#tab-form" data-toggle="tab">配置详情</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <form id="editForm" action="${ctx}/config/remove.do" method="post">
                            <input type="hidden" name="id" value="${configInfo.id}">
                            <input type="hidden" name="configName" value="" id="configName">
                            <div class="tab-pane active" id="tab-form">
                                <div class="row data-type">
                                    <div class="col-md-2 title">项目名称</div>
                                    <div class="col-md-10 data">
                                        ${configInfo.projectName}
                                    </div>
                                    <div class="col-md-2 title">环境名称</div>
                                    <div class="col-md-10 data">
                                        ${configInfo.envName}
                                    </div>
                                    <div class="col-md-2 title">集群号</div>
                                    <div class="col-md-10 data">
                                        ${configInfo.clusterNumber}
                                    </div>
                                    <div class="col-md-2 title">服务名称</div>
                                    <div class="col-md-10 data line-height36">
                                        ${configInfo.serviceName}
                                    </div>
                                    <!--遍历-->
                                    <c:forEach items="${props}" var="prop">
                                        <div class="col-md-2 title">
                                            ${prop.key}
                                        </div>
                                        <div class="col-md-8 data">
                                            ${prop.value}
                                        </div>
                                        <div class="col-md-2 data">
                                            <button type="button" onclick='submitForm("${prop.key}")'  class="btn bg-purple">删除</button>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="col-md-10 data text-center">
                                    <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</body>
</html>