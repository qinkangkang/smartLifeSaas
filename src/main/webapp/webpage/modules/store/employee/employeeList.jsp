<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>员工账号信息管理</title>
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
		<h5>员工账号信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="employee" action="${ctx}/store/employee/employee/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>员工账号：</span>
				<form:input path="faccountNumber" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>员工姓名：</span>
				<form:input path="fname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>所属门店：</span>
				<sys:gridselect url="${ctx}/store/employee/employee/selectstore" id="store" name="store"  value="${employee.store.id}"  title="选择所属门店" labelName="store.fname" 
					labelValue="${employee.store.fname}" cssClass="form-control required" fieldLabels="客户名称|备注信息" fieldKeys="fname|remarks" searchLabel="客户名称" searchKey="fname" ></sys:gridselect>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="store:employee:employee:add">
				<table:addRow url="${ctx}/store/employee/employee/form" title="员工账号信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="store:employee:employee:edit">
			    <table:editRow url="${ctx}/store/employee/employee/form" title="员工账号信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="store:employee:employee:del">
				<table:delRow url="${ctx}/store/employee/employee/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="store:employee:employee:import">
				<table:importExcel url="${ctx}/store/employee/employee/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="store:employee:employee:export">
	       		<table:exportExcel url="${ctx}/store/employee/employee/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column faccountNumber">员工账号</th>
				<th  class="sort-column fname">员工姓名</th>
				<th  class="sort-column store.id">所属门店</th>
				<th  class="sort-column ftimeEnter">入职时间</th>
				<th  class="sort-column froleID">员工角色</th>
				<th  class="sort-column fstatus">员工状态</th>
				<th  class="sort-column flastTime">最后登录时间</th>
				<th  class="sort-column flastIP">最后登录IP</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="employee">
			<tr>
				<td> <input type="checkbox" id="${employee.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看员工账号信息', '${ctx}/store/employee/employee/form?id=${employee.id}','800px', '500px')">
					${employee.faccountNumber}
				</a></td>
				<td>
					${employee.fname}
				</td>
				<td>
					${employee.store.fname}
				</td>
				<td>
					<fmt:formatDate value="${employee.ftimeEnter}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${employee.froleID}
				</td>
				<td>
					${fns:getDictLabel(employee.fstatus, 'sys_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${employee.flastTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${employee.flastIP}
				</td>
				<td>
					<shiro:hasPermission name="store:employee:employee:view">
						<a href="#" onclick="openDialogView('查看员工账号信息', '${ctx}/store/employee/employee/form?id=${employee.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="store:employee:employee:edit">
    					<a href="#" onclick="openDialog('修改员工账号信息', '${ctx}/store/employee/employee/form?id=${employee.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="store:employee:employee:del">
						<a href="${ctx}/store/employee/employee/delete?id=${employee.id}" onclick="return confirmx('确认要删除该员工账号信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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