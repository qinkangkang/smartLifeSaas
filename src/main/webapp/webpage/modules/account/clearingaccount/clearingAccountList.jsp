<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>结算账户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>结算账户列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="clearingAccount" action="${ctx}/account/clearingaccount/clearingAccount/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>账户名称：</span>
				<form:input path="faccountname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>启用状态：</span>
				<form:radiobuttons class="i-checks" path="fstatus" items="${fns:getDictList('sys_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="account:clearingaccount:clearingAccount:add">
				<table:addRow url="${ctx}/account/clearingaccount/clearingAccount/form" title="结算账户"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:clearingaccount:clearingAccount:edit">
			    <table:editRow url="${ctx}/account/clearingaccount/clearingAccount/form" title="结算账户" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:clearingaccount:clearingAccount:del">
				<table:delRow url="${ctx}/account/clearingaccount/clearingAccount/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:clearingaccount:clearingAccount:import">
				<table:importExcel url="${ctx}/account/clearingaccount/clearingAccount/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:clearingaccount:clearingAccount:export">
	       		<table:exportExcel url="${ctx}/account/clearingaccount/clearingAccount/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column faccountname">账户名称</th>
				<th  class="sort-column faccountnumber">账号</th>
				<th  class="sort-column faccountholder">开户人</th>
				<th  class="sort-column faccounttype">账户类型</th>
				<th  class="sort-column fbalance">当前余额</th>
				<th  class="sort-column fstore.id">所属门店</th>
				<th  class="sort-column remarks">备注信息</th>
				<th  class="sort-column fstatus">启用状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="clearingAccount">
			<tr>
				<td> <input type="checkbox" id="${clearingAccount.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看结算账户', '${ctx}/account/clearingaccount/clearingAccount/form?id=${clearingAccount.id}','800px', '500px')">
					${clearingAccount.faccountname}
				</a></td>
				<td>
					${clearingAccount.faccountnumber}
				</td>
				<td>
					${clearingAccount.faccountholder}
				</td>
				<td>
					${fns:getDictLabel(clearingAccount.faccounttype, 'faccountt_ype', '')}
				</td>
				<td>
					${clearingAccount.fbalance}
				</td>
				<td>
					${clearingAccount.fstore.name}
				</td>
				<td>
					${clearingAccount.remarks}
				</td>
				<td>
					${fns:getDictLabel(clearingAccount.fstatus, 'sys_status', '')}
				</td>
				<td>
					<shiro:hasPermission name="account:clearingaccount:clearingAccount:view">
						<a href="#" onclick="openDialogView('查看结算账户', '${ctx}/account/clearingaccount/clearingAccount/form?id=${clearingAccount.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:clearingaccount:clearingAccount:edit">
    					<a href="#" onclick="openDialog('修改结算账户', '${ctx}/account/clearingaccount/clearingAccount/form?id=${clearingAccount.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:clearingaccount:clearingAccount:del">
						<a href="${ctx}/account/clearingaccount/clearingAccount/delete?id=${clearingAccount.id}" onclick="return confirmx('确认要删除该结算账户吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>