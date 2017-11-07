<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库管理</title>
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
		<h5>仓库列表 </h5>
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
	<form:form id="searchForm" modelAttribute="warehouse" action="${ctx}/warehouses/warehouse/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>仓库名：</span>
				<form:input path="fname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>仓库状态：</span>
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
			<shiro:hasPermission name="warehouses:warehouse:add">
				<table:addRow url="${ctx}/warehouses/warehouse/form" title="仓库"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouse:edit">
			    <table:editRow url="${ctx}/warehouses/warehouse/form" title="仓库" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouse:del">
				<table:delRow url="${ctx}/warehouses/warehouse/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouse:import">
				<table:importExcel url="${ctx}/warehouses/warehouse/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouse:export">
	       		<table:exportExcel url="${ctx}/warehouses/warehouse/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column fname">仓库名</th>
				<th  class="sort-column charge_person">仓库负责人</th>
				<th  class="sort-column a.office_id">所属门店</th>
				<th  class="sort-column a.create_by">创建者</th>
				<th  class="sort-column a.update_by">更新者</th>
				<th  class="sort-column a.create_date">创建时间</th>
				<!-- <th  class="sort-column update_date">更新时间</th>
				<th  class="sort-column remarks">备注信息</th> -->
				<th  class="sort-column fstatus">仓库状态</th>
			
				
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="warehouse">
			<tr>
				<td> <input type="checkbox" id="${warehouse.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看仓库', '${ctx}/warehouses/warehouse/form?id=${warehouse.id}','400px', '500px')">
					${warehouse.fname}
				</a></td>
				<td>
					${warehouse.chargePerson.name}
				</td>
				<td>
					${warehouse.office.name}
				</td>
				<td>
					${warehouse.createBy.name}
				</td>
				<td>
					${warehouse.updateBy.name}
				</td>
				<td>
					<fmt:formatDate value="${warehouse.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			<%-- 	<td>
					<fmt:formatDate value="${warehouse.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${warehouse.remarks}
				</td> --%>
				<td>
					${fns:getDictLabel(warehouse.fstatus, 'sys_status', '')}
				</td>
			
				
				<td>
					<shiro:hasPermission name="warehouses:warehouse:view">
						<a href="#" onclick="openDialogView('查看仓库', '${ctx}/warehouses/warehouse/form?id=${warehouse.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="warehouses:warehouse:edit">
    					<a href="#" onclick="openDialog('修改仓库', '${ctx}/warehouses/warehouse/form?id=${warehouse.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="warehouses:warehouse:del">
						<a href="${ctx}/warehouses/warehouse/delete?id=${warehouse.id}" onclick="return confirmx('确认要删除该仓库吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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