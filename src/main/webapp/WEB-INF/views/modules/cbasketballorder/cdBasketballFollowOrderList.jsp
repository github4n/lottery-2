<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>竞彩篮球订单管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/cbasketballorder/cdBasketballFollowOrder/">竞彩篮球订单列表</a></li>
    <%--<shiro:hasPermission name="cbasketballorder:cdBasketballFollowOrder:edit">
        <li><a href="${ctx}/cbasketballorder/cdBasketballFollowOrder/form">竞彩篮球订单添加</a></li>
    </shiro:hasPermission>--%>
</ul>
<form:form id="searchForm" modelAttribute="cdBasketballFollowOrder"
           action="${ctx}/cbasketballorder/cdBasketballFollowOrder/" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <label>订单号 ：</label><form:input path="orderNum" htmlEscape="false" maxlength="50" class="input-small"/>
    <label class="control-label">投注方式:</label>
    <form:select id="buyWays" path="buyWays">
        <form:option value="" label="全部"/>
        <form:option value="1" label="混投"/>
        <form:option value="2" label="胜负"/>
        <form:option value="3" label="让分胜负"/>
        <form:option value="4" label="大小分"/>
        <form:option value="5" label="胜分差"/>
    </form:select>
    &nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form:form>
<tags:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>订单号</th>
        <th>投注方式</th>
        <th>注数</th>
        <th>金额</th>
        <th>用户</th>
        <th>下单时间</th>
        <th>订单状态</th>
        <shiro:hasPermission name="cbasketballorder:cdBasketballFollowOrder:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="cdBasketballFollowOrder">
        <tr>
            <td>
                <a href="${ctx}/cbasketballorder/cdBasketballFollowOrder/form?id=${cdBasketballFollowOrder.id}">${cdBasketballFollowOrder.orderNum}</a>
            </td>
            <td><%--${cdBasketballFollowOrder.buyWays}--%>
                <c:choose>
                    <c:when test="${cdBasketballFollowOrder.buyWays==1}">
                        混投
                    </c:when>
                    <c:when test="${cdBasketballFollowOrder.buyWays==2}">
                        胜负
                    </c:when>
                    <c:when test="${cdBasketballFollowOrder.buyWays==3}">
                        让分胜负
                    </c:when>
                    <c:when test="${cdBasketballFollowOrder.buyWays==4}">
                        大小分
                    </c:when>
                    <c:when test="${cdBasketballFollowOrder.buyWays==5}">
                        胜分差
                    </c:when>

                    <c:otherwise>
                        订单异常
                    </c:otherwise>
                </c:choose></td>
            <td>${cdBasketballFollowOrder.acount}</td>
            <td>${cdBasketballFollowOrder.price}</td>
            <td>${cdBasketballFollowOrder.uid}</td>
            <td>${cdBasketballFollowOrder.createDate}</td>
            <td><c:choose>
                <c:when test="${cdBasketballFollowOrder.status==1}">
                    未付款
                </c:when>
                <c:when test="${cdBasketballFollowOrder.status==2}">
                    已付款
                </c:when>
                <c:when test="${cdBasketballFollowOrder.status==3}">
                    已出票
                </c:when>

                <c:otherwise>
                    订单异常
                </c:otherwise>
            </c:choose></td>
            <shiro:hasPermission name="cbasketballorder:cdBasketballFollowOrder:edit">
                <td>
                    <a href="${ctx}/cbasketballorder/cdBasketballFollowOrder/form?id=${cdBasketballFollowOrder.id}">查看/出票</a>
                    <a href="${ctx}/cbasketballorder/cdBasketballFollowOrder/delete?id=${cdBasketballFollowOrder.id}"
                       onclick="return confirmx('确认要删除该竞彩篮球订单吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>
