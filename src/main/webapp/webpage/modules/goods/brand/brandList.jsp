<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>品牌管理管理</title>
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
		<h5>品牌管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="brand" action="${ctx}/goods/brand/brand/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>品牌名称：</span>
				<form:input path="fbrandname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			&nbsp;&nbsp;&nbsp;
			<span>启用状态：</span>
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
			<shiro:hasPermission name="goods:brand:brand:add">
				<table:addRow url="${ctx}/goods/brand/brand/form" title="品牌管理"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:brand:brand:edit">
			    <table:editRow url="${ctx}/goods/brand/brand/form" title="品牌管理" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:brand:brand:del">
				<table:delRow url="${ctx}/goods/brand/brand/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:brand:brand:import">
				<table:importExcel url="${ctx}/goods/brand/brand/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:brand:brand:export">
	       		<table:exportExcel url="${ctx}/goods/brand/brand/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column fbrandname">品牌名称</th>
<!-- 				<th  class="sort-column fsort">排序</th> -->
				<th  class="sort-column createDate">创建时间</th>
				<th  class="sort-column updateDate">更新时间</th>
				<th  class="sort-column createBy.id">创建人</th>
				<th  class="sort-column updateBy.id">更新人</th>
				<th  class="sort-column fstatus">启用状态</th>
				<th  class="sort-column description">备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="brand">
			<tr>
				<td> <input type="checkbox" id="${brand.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看品牌管理', '${ctx}/goods/brand/brand/form?id=${brand.id}','800px', '500px')">
					${brand.fbrandname}
				</a></td>
			
				<td>
					<fmt:formatDate value="${brand.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${brand.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				
				<td>
					${brand.createBy.name}
				</td>
				<td>
					${brand.updateBy.name}
				</td>
				<td>
					${fns:getDictLabel(brand.fstatus, 'sys_status', '')}
				</td>
				<td>
					${brand.description}
				</td>
				<td>
					<shiro:hasPermission name="goods:brand:brand:view">
						<a href="#" onclick="openDialogView('查看品牌管理', '${ctx}/goods/brand/brand/form?id=${brand.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="goods:brand:brand:edit">
    					<a href="#" onclick="openDialog('修改品牌管理', '${ctx}/goods/brand/brand/form?id=${brand.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="goods:brand:brand:del">
						<a href="${ctx}/goods/brand/brand/delete?id=${brand.id}" onclick="return confirmx('确认要删除该品牌管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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