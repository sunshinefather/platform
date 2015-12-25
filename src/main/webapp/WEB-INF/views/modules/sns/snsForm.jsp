<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加圈子管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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
		<li><a href="${ctx}/sns/sns/">添加圈子列表</a></li>
		<li class="active"><a href="${ctx}/sns/sns/form?id=${sns.id}">添加圈子<shiro:hasPermission name="sns:sns:edit">${not empty sns.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sns:sns:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sns" action="${ctx}/sns/sns/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">圈子名称：</label>
			<div class="controls">
				<form:input path="snsname" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">圈子配图ID：</label>
			<div class="controls">
				<form:hidden id="snsphotoid" path="snsphotoid" htmlEscape="false" maxlength="32" class="input-xlarge"/>
				<sys:ckfinder input="snsphotoid" type="files" uploadPath="/sns/sns" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">简介：</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">圈子创建人Id：</label>
			<div class="controls">
				<sys:treeselect id="createuserid" name="createuserid" value="${sns.createuserid}" labelName="" labelValue="${sns.createuserid}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">圈子主题，标题：</label>
			<div class="controls">
				<form:input path="snssubject" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">圈子类别ID：</label>
			<div class="controls">
				<form:select path="categoryid" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否默认拥有的圈子  为true时表示孕妇注册时自动绑定到我的圈。：</label>
			<div class="controls">
				<form:radiobuttons path="defaults" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否推荐，为true时显示到我的圈里面。：</label>
			<div class="controls">
				<form:radiobuttons path="recommend" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关注人数：</label>
			<div class="controls">
				<form:input path="attentioncount" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">帖子数量：</label>
			<div class="controls">
				<form:input path="postcount" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">今日帖子数目：</label>
			<div class="controls">
				<form:input path="todaypostcount" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sns:sns:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>