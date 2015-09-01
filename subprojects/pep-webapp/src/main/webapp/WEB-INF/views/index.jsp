<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Index</title>
    <link rel="stylesheet" href="${path}/resources/css/theme/blue/default.css" />
</head>
<body class="plat-content-bg">
    <div id="headerWrap" class="plat-header-wrap">
        <div id="logoPanel" class="plat-logo"></div>
        <div id="plat-menu-wrap" class="plat-menu-wrap"></div>
        <div id="toolsPanel" class="plat-tools-wrap">
            <ul class="plat-tools">
                <li class="selected">
                    <a href="###">
                        <div class="plat-tools-bt-user"><span>&nbsp;</span></div>
                        current username
                    </a>
                </li>
                <li class="ThemeTools">
                    <a href="javascript:void(0);">
                        <div class="plat-tools-bt-setting"><span>&nbsp;</span></div>
                        <span>设置</span>
                        <div class="plat-tools-bt-icodown"><span>&nbsp;</span></div>
                    </a>
                    <ul id="settingSelector" class="plat-theme-list setting1">
                        <li class="nolast" datatype="subtitle">
                            <div class="plat-tools-bt-theme"><span>&nbsp;</span></div>&nbsp;<span>切换主题</span>
                        </li>
                        <li class="nolast sub" datatype="theme" themecolor="red">&nbsp;&nbsp;<div class="themeico red"><span>&nbsp;</span></div>&nbsp;<span>中国红</span></li>
                        <li class="nolast sub" datatype="theme" themecolor="green">&nbsp;&nbsp;<div class="themeico gree"><span>&nbsp;</span></div>&nbsp;<span>生命绿</span></li>
                        <li class="nolast sub" datatype="theme" themecolor="blue">&nbsp;&nbsp;<div class="themeico blue"><span>&nbsp;</span></div>&nbsp;<span>经典蓝</span></li>
                        <li class="nolast" datatype="changepass">
                            <div class="plat-tools-bt-setting"><span>&nbsp;</span></div>&nbsp;<span>修改密码</span>
                        </li>
                    </ul>
                </li>
                <li style="padding-left: 0px;">
                    <a href="javascript:void(0);" id="logoutButton">
                        <div class="plat-tools-bt-logout"><span>&nbsp;</span></div> 退出
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div id="contentwrap" class="plat-content-wrap">
        <div id="content" class="plat-content"></div>
    </div>
    <div id="footer" class="plat-footer">
        <div class="plat-footer-inner">版权所有<span class="cc">©</span></div>
    </div>
</body>
</html>