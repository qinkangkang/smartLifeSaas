<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库调拨管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginFtransferDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endFtransferDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>仓库调拨列表 </h5>
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
	<form:form id="searchForm" modelAttribute="warehouseTransferOrder" action="${ctx}/warehouses/warehouseTransferOrder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>调拨单号：</span>
				<form:input path="ftransferOrder" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			&nbsp;&nbsp;&nbsp;
			<span>调出仓库：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseTransferOrder/selectwarehouseOut" id="warehouseOut" name="warehouseOut"  value="${warehouseTransferOrder.warehouseOut.id}"  title="选择调出仓库" labelName="warehouseOut.fname" 
					labelValue="${warehouseTransferOrder.warehouseOut.fname}" cssClass="form-control required" fieldLabels="调出仓库|备注" fieldKeys="fname|remarks" searchLabel="调出仓库" searchKey="fname" ></sys:gridselect>
			&nbsp;&nbsp;&nbsp;
			<span>调入仓库：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseTransferOrder/selectfwarehouseIn" id="fwarehouseIn" name="fwarehouseIn"  value="${warehouseTransferOrder.fwarehouseIn.id}"  title="选择调入仓库" labelName="fwarehouseIn.fname" 
					labelValue="${warehouseTransferOrder.fwarehouseIn.fname}" cssClass="form-control required" fieldLabels="调入仓库|备注" fieldKeys="fname|remarks" searchLabel="调入仓库" searchKey="fname" ></sys:gridselect>
				<br><br>
			<span>调拨日期：</span>
				<input id="beginFtransferDate" name="beginFtransferDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${warehouseTransferOrder.beginFtransferDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endFtransferDate" name="endFtransferDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${warehouseTransferOrder.endFtransferDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			&nbsp;&nbsp;&nbsp;
			<span>状态：</span>
				<form:radiobuttons class="i-checks" path="fstatus" items="${fns:getDictList('transferorder_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="warehouses:warehouseTransferOrder:add">
				<table:addRow url="${ctx}/warehouses/warehouseTransferOrder/form" title="仓库调拨"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseTransferOrder:edit">
			    <table:editRow url="${ctx}/warehouses/warehouseTransferOrder/form" title="仓库调拨" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseTransferOrder:del">
				<table:delRow url="${ctx}/warehouses/warehouseTransferOrder/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseTransferOrder:import">
				<table:importExcel url="${ctx}/warehouses/warehouseTransferOrder/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseTransferOrder:export">
	       		<table:exportExcel url="${ctx}/warehouses/warehouseTransferOrder/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column ftransferOrder">调拨单号</th>
				<th  class="sort-column warehouseOut.id">调出仓库</th>
				<th  class="sort-column fwarehouseIn.id">调入仓库</th>
				<th  class="sort-column ftransferDate">调拨日期</th>
				<th  class="sort-column fstatus">状态</th>
				<th  class="sort-column remarks">备注信息</th>
				<th style=" text-align:center;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="warehouseTransferOrder">
			<tr>
				<td> <input type="checkbox" id="${warehouseTransferOrder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看仓库调拨', '${ctx}/warehouses/warehouseTransferOrder/form?id=${warehouseTransferOrder.id}','800px', '500px')">
					${warehouseTransferOrder.ftransferOrder}
				</a></td>
				<td>
					${warehouseTransferOrder.warehouseOut.fname}
				</td>
				<td>
					${warehouseTransferOrder.fwarehouseIn.fname}
				</td>
				<td>
					<fmt:formatDate value="${warehouseTransferOrder.ftransferDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(warehouseTransferOrder.fstatus, 'transferorder_status', '')}
				</td>
				<td>
					${warehouseTransferOrder.remarks}
				</td>
				<td style=" text-align:center;">
					<shiro:hasPermission name="warehouses:warehouseTransferOrder:view">
						<a href="#" onclick="openDialogView('查看仓库调拨', '${ctx}/warehouses/warehouseTransferOrder/form?id=${warehouseTransferOrder.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<c:if test="${warehouseTransferOrder.fstatus != '1' }">
						<shiro:hasPermission name="warehouses:warehouseTransferOrder:edit">
	    					<a href="#" onclick="openDialog('修改仓库调拨', '${ctx}/warehouses/warehouseTransferOrder/form?id=${warehouseTransferOrder.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 调拨</a>
	    				</shiro:hasPermission>
					</c:if>
    				<shiro:hasPermission name="warehouses:warehouseTransferOrder:del">
						<a href="${ctx}/warehouses/warehouseTransferOrder/delete?id=${warehouseTransferOrder.id}" onclick="return confirmx('确认要删除该仓库调拨吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="warehouses:warehouseTransferOrder:del">
						<a href="${ctx}/warehouses/warehouseTransferOrder/revoke?id=${warehouseTransferOrder.id}" onclick="return confirmx('确认要撤销该调拨单吗？', this.href)"   class="btn btn-info btn-xs"><i class="fa fa-trash"></i> 撤销</a>
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