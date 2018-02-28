<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户注册管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li>
			<a href="${ctx}/clotteryuser/cdLotteryUser/">用户注册列表</a>
		</li>
		<li class="active">
			<a href="${ctx}/clotteryuser/cdLotteryUser/form?id=${cdLotteryUser.id}">用户注册<shiro:hasPermission name="clotteryuser:cdLotteryUser:edit">${not empty cdLotteryUser.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="clotteryuser:cdLotteryUser:edit">查看</shiro:lacksPermission></a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cdLotteryUser" action="${ctx}/clotteryuser/cdLotteryUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">真实姓名:</label>
			<div class="controls">
				<form:input path="reality" readonly="true" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">手机号:</label>
			<div class="controls">
				<form:input path="mobile" readonly="true" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" readonly="true" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">身份证号:</label>
			<div class="controls">
				<form:input path="idNumber" readonly="true" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">余额:</label>
			<div class="controls">
				<form:input path="balance" readonly="true" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">积分:</label>
			<div class="controls">
				<form:input path="score" readonly="true" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>


		<div class="form-actions">
			<shiro:hasPermission name="clotteryuser:cdLotteryUser:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>