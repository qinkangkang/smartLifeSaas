<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>账目类型管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>账目类型列表 </h5>
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
	<form:form id="searchForm" modelAttribute="accountType" action="${ctx}/account/accountmanagement/accountType/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>类型名称：</span>
				<form:input path="ftypename" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>创建时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${accountType.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${accountType.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>开启状态：</span>
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
			<shiro:hasPermission name="account:accountmanagement:accountType:add">
				<table:addRow url="${ctx}/account/accountmanagement/accountType/form" title="账目类型"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:accountmanagement:accountType:edit">
			    <table:editRow url="${ctx}/account/accountmanagement/accountType/form" title="账目类型" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:accountmanagement:accountType:del">
				<table:delRow url="${ctx}/account/accountmanagement/accountType/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:accountmanagement:accountType:import">
				<table:importExcel url="${ctx}/account/accountmanagement/accountType/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:accountmanagement:accountType:export">
	       		<table:exportExcel url="${ctx}/account/accountmanagement/accountType/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column ftypename">类型名称</th>
				<th  class="sort-column fstore.id">所属门店</th>
				<th  class="sort-column forder">排序</th>
				<th  class="sort-column createBy.id">创建者</th>
				<th  class="sort-column createDate">创建时间</th>
				<th  class="sort-column updateBy.id">更新者</th>
				<th  class="sort-column updateDate">更新时间</th>
				<th  class="sort-column remarks">备注信息</th>
				<th  class="sort-column fstatus">开启状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="accountType">
			<tr>
				<td> <input type="checkbox" id="${accountType.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看账目类型', '${ctx}/account/accountmanagement/accountType/form?id=${accountType.id}','800px', '500px')">
					${accountType.ftypename}
				</a></td>
				<td>
					${accountType.fstore.name}
				</td>
				<td>
					${accountType.forder}
				</td>
				<td>
					${accountType.createBy.loginName}
				</td>
				<td>
					<fmt:formatDate value="${accountType.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${accountType.updateBy.loginName}
				</td>
				<td>
					<fmt:formatDate value="${accountType.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${accountType.remarks}
				</td>
				<td>
					${fns:getDictLabel(accountType.fstatus, 'sys_status', '')}
				</td>
				<td>
					<shiro:hasPermission name="account:accountmanagement:accountType:view">
						<a href="#" onclick="openDialogView('查看账目类型', '${ctx}/account/accountmanagement/accountType/form?id=${accountType.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:accountmanagement:accountType:edit">
    					<a href="#" onclick="openDialog('修改账目类型', '${ctx}/account/accountmanagement/accountType/form?id=${accountType.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:accountmanagement:accountType:del">
						<a href="${ctx}/account/accountmanagement/accountType/delete?id=${accountType.id}" onclick="return confirmx('确认要删除该账目类型吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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