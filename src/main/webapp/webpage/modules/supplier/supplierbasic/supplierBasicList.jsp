<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>供应商信息管理</title>
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
		<h5>供应商信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="supplierBasic" action="${ctx}/supplier/supplierbasic/supplierBasic/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>供应商名称：</span>
				<form:input path="fname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>联系人：</span>
				<form:input path="flinkman" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>联系人手机：</span>
				<form:input path="fcellphone" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>供应商状态：</span>
				<form:select path="fstatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sys_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:add">
				<table:addRow url="${ctx}/supplier/supplierbasic/supplierBasic/form" title="供应商信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:edit">
			    <table:editRow url="${ctx}/supplier/supplierbasic/supplierBasic/form" title="供应商信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:del">
				<table:delRow url="${ctx}/supplier/supplierbasic/supplierBasic/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:import">
				<table:importExcel url="${ctx}/supplier/supplierbasic/supplierBasic/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:export">
	       		<table:exportExcel url="${ctx}/supplier/supplierbasic/supplierBasic/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column fname">供应商名称</th>
				<th  class="sort-column fbdid">负责人</th>
				<th  class="sort-column fdiscount">折扣</th>
				<th  class="sort-column fdebt">欠款</th>
				<th  class="sort-column flinkman">联系人</th>
				<th  class="sort-column fcellphone">联系人手机</th>
				<th  class="sort-column remarks">备注信息</th>
				<th  class="sort-column fstatus">供应商状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="supplierBasic">
			<tr>
				<td> <input type="checkbox" id="${supplierBasic.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看供应商信息', '${ctx}/supplier/supplierbasic/supplierBasic/form?id=${supplierBasic.id}','800px', '500px')">
					${supplierBasic.fname}
				</a></td>
				<td>
					${supplierBasic.fbdid}
				</td>
				<td>
					${supplierBasic.fdiscount}
				</td>
				<td>
					${supplierBasic.fdebt}
				</td>
				<td>
					${supplierBasic.flinkman}
				</td>
				<td>
					${supplierBasic.fcellphone}
				</td>
				<td>
					${supplierBasic.remarks}
				</td>
				<td>
					${fns:getDictLabel(supplierBasic.fstatus, 'sys_status', '')}
				</td>
				<td>
					<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:view">
						<a href="#" onclick="openDialogView('查看供应商信息', '${ctx}/supplier/supplierbasic/supplierBasic/form?id=${supplierBasic.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:edit">
    					<a href="#" onclick="openDialog('修改供应商信息', '${ctx}/supplier/supplierbasic/supplierBasic/form?id=${supplierBasic.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="supplier:supplierbasic:supplierBasic:del">
						<a href="${ctx}/supplier/supplierbasic/supplierBasic/delete?id=${supplierBasic.id}" onclick="return confirmx('确认要删除该供应商信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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