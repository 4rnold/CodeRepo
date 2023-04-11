<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../base.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="${ctx}/img/user2-160x160.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p> ${sessionScope.user.userName}</p>
                <a href="#">${sessionScope.user.gender}</a>
            </div>
        </div>

        <!-- sidebar menu: : style can be found in sidebar.less-->
        <%--<ul class="sidebar-menu">
            <li class="header">菜单</li>


                        <c:forEach items="${sessionScope.modules}" var="item">
                            <c:if test="${item.ctype==0}">
                                <li class="treeview">
                                    <a href="#">
                                        <i class="fa fa-cube"></i> <span>${item.name}</span>
                                        <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                                    </a>
                                    <ul class="treeview-menu">
                                        <c:forEach items="${sessionScope.modules}" var="item2">
                                            <c:if test="${item2.ctype==1 && item2.parentId == item.id}">
                                                <li id="${item2.id}">
                                                    <a onclick="setSidebarActive(this)" href="${item2.curl}" target="iframe">
                                                        <i class="fa fa-circle-o"></i>${item2.name}
                                                    </a>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:if>
                        </c:forEach>



           </ul>--%>
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="header">项目列表</li>
            <!-- 菜单 -->
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-folder"></i> <span>开发环境</span>
                    <span class="pull-right-container">
                        <i class="fa fa-angle-left pull-right"></i>
                    </span>
                </a>
                <ul class="treeview-menu">
                    <li id="admin-login">
                        <a href="${pageContext.request.contextPath}/config/list.do?projectName=tensquare&envName=dev" onclick="setSidebarActive(this)" target="iframe">
                            <i class="fa fa-circle-o"></i> 十次方
                        </a>
                    </li>
                    <li id="admin-register">
                        <a href="all-admin-register.html">
                            <i class="fa fa-circle-o"></i> SaaS-Export
                        </a>
                    </li>
                    <li id="admin-404">
                        <a href="all-admin-404.html">
                            <i class="fa fa-circle-o"></i> 黑马头条
                        </a>
                    </li>
                    <li id="admin-500">
                        <a href="all-admin-500.html">
                            <i class="fa fa-circle-o"></i> 传智健康
                        </a>
                    </li>
                    <li id="admin-blank">
                        <a href="all-admin-blank.html">
                            <i class="fa fa-circle-o"></i> 冰眼冷链
                        </a>
                    </li>
                    <li id="admin-datalist">
                        <a href="all-admin-datalist.html">
                            <i class="fa fa-circle-o"></i> IHRM
                        </a>
                    </li>
                    <li id="admin-dataform">
                        <a href="all-admin-dataform.html">
                            <i class="fa fa-circle-o"></i> 万信金融
                        </a>
                    </li>
                    <li id="admin-profile">
                        <a href="all-admin-profile.html">
                            <i class="fa fa-circle-o"></i> 立可得
                        </a>
                    </li>
                    <li id="admin-invoice">
                        <a href="all-admin-invoice.html">
                            <i class="fa fa-circle-o"></i> 智慧学成
                        </a>
                    </li>
                    <li id="admin-invoice-print">
                        <a href="all-admin-invoice-print.html">
                            <i class="fa fa-circle-o"></i> 速运新BoS
                        </a>
                    </li>
                </ul>
            </li>

            <li class="treeview">
                <a href="#">
                    <i class="fa fa-folder"></i> <span>测试环境</span>
                    <span class="pull-right-container">
                                <i class="fa fa-angle-left pull-right"></i>
                            </span>
                </a>
                <ul class="treeview-menu">
                    <li id="elements-timeline">
                        <a href="all-admin-login.html">
                            <i class="fa fa-circle-o"></i> 十次方
                        </a>
                    </li>
                    <li id="elements-sliders">
                        <a href="all-admin-register.html">
                            <i class="fa fa-circle-o"></i> SaaS-Export
                        </a>
                    </li>
                    <li id="elements-buttons">
                        <a href="all-admin-404.html">
                            <i class="fa fa-circle-o"></i> 黑马头条
                        </a>
                    </li>
                    <li id="elements-icons">
                        <a href="all-admin-500.html">
                            <i class="fa fa-circle-o"></i> 传智健康
                        </a>
                    </li>
                    <li id="elements-general">
                        <a href="all-admin-blank.html">
                            <i class="fa fa-circle-o"></i> 冰眼冷链
                        </a>
                    </li>
                </ul>
            </li>

            <li class="treeview">
                <a href="#">
                    <i class="fa fa-folder"></i> <span>生产环境</span>
                    <span class="pull-right-container">
                                <i class="fa fa-angle-left pull-right"></i>
                            </span>
                </a>
                <ul class="treeview-menu">
                    <li id="form-advanced">
                        <a href="all-admin-login.html">
                            <i class="fa fa-circle-o"></i> 十次方
                        </a>
                    </li>
                    <li id="form-general">
                        <a href="all-admin-register.html">
                            <i class="fa fa-circle-o"></i> SaaS-Export
                        </a>
                    </li>
                    <li id="form-editors">
                        <a href="all-admin-404.html">
                            <i class="fa fa-circle-o"></i> 黑马头条
                        </a>
                    </li>
                </ul>
            </li>
        </ul>
       </section>
       <!-- /.sidebar -->
</aside>
