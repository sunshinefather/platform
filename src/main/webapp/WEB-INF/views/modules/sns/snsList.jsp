<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>圈子管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}//sns/sns/">圈子列表</a></li>
		<shiro:hasPermission name="sns:sns:edit"><li><a href="${ctx}//sns/sns/form">圈子添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sns" action="${ctx}//sns/sns/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>圈子名称：</label>
				<form:input path="snsname" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>圈子名称</th>
				<th>创建时间</th>
				<th>关注人数</th>
				<th>帖子数量</th>
				<th>今日帖子数目</th>
				<shiro:hasPermission name="sns:sns:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sns">
			<tr>
				<td><a href="${ctx}//sns/sns/form?id=${sns.id}">
					${sns.snsname}
				</a></td>
				<td>
					<fmt:formatDate value="${sns.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${sns.attentioncount}
				</td>
				<td>
					${sns.postcount}
				</td>
				<td>
					${sns.todaypostcount}
				</td>
				<shiro:hasPermission name="sns:sns:edit"><td>
    				<a href="${ctx}//sns/sns/form?id=${sns.id}">修改</a>
					<a href="${ctx}//sns/sns/delete?id=${sns.id}" onclick="return confirmx('确认要删除该圈子吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>