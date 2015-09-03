<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head></head>
<body>
<div>
    <table>
        <tr>
            <td width="100%" class="ver01">
                <div class="box1" panelTitle="查询" showStatus="false">
                    <form action="${path}/getUsersOfPager.action" id="queryForm" method="post">
                        <div class="filterProper_main">
                            <div class="filterProper_content">
                                <table class="filterProper_searchTable">
                                    <tr>
                                        <td>账号：</td>
                                        <td>
                                            <input type="text" id="searchInputLoginName" name="user.loginName" />
                                            <input type="text" style="width:2px;display:none;" />
                                        </td>
                                        <td>姓名：</td>
                                        <td>
                                            <input type="text" id="searchInputUser" name="user.name" />
                                            <input type="text" style="width:2px;display:none;" />
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>注册机构：</td>
                                        <td>
                                            <input type="text" id="searchInputRa" name="user.raName" />
                                            <input type="text" style="width:2px;display:none;" />
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td style="padding-left:10px;">
                                            <button type="button" onclick="searchHandler()">
                                                <span class="icon_find">查询</span>
                                            </button>
                                        </td>
                                        <td style="padding-left:5px;">
                                            <button type="button" onclick="resetSearch()">
                                                <span class="icon_reload">重置</span>
                                            </button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="padding_right5">
                    <div id="user"></div>
                </div>
            </td>
            <td class="ver01">
                <div class="box1" panelTitle="查询" showStatus="false">
                    <div class="cusBoxContent" style="width:500px;">
                        <div id="role"></div>
                    </div>
                </div>
            </td>
        </tr>
    </table>
</div>
<script src="${path}/resources/scripts/vendor/jquery/jquery.js"></script>
<script src="${path}/resources/scripts/vendor/qui/language/cn.js"></script>
<script src="${path}/resources/scripts/vendor/qui/framework.js"></script>
<script src="${path}/resources/scripts/vendor/qui/table/quiGrid.js"></script>
<script>
function initComplete() {
    console.log('test clicked');
}
</script>
</body>
</html>
