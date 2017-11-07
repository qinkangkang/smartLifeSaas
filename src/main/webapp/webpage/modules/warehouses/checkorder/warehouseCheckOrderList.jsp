<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库盘点管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginCheckDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endCheckDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>仓库盘点列表 </h5>
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
	<form:form id="searchForm" modelAttribute="warehouseCheckOrder" action="${ctx}/warehouses/warehouseCheckOrder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>盘点单号：</span>
				<form:input path="fcheckOrder" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			&nbsp;&nbsp;&nbsp;
			<span>仓库：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseCheckOrder/selectwarehouse" id="warehouse" name="warehouse"  value="${warehouseCheckOrder.warehouse.id}"  title="选择仓库" labelName="warehouse.fname" 
					labelValue="${warehouseCheckOrder.warehouse.fname}" cssClass="form-control required" fieldLabels="仓库|备注" fieldKeys="fname|remarks" searchLabel="仓库" searchKey="fname" ></sys:gridselect>
			<br><br>
			<span>盘点日期：</span>
				<input id="beginCheckDate" name="beginCheckDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${warehouseCheckOrder.beginCheckDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCheckDate" name="endCheckDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${warehouseCheckOrder.endCheckDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			&nbsp;&nbsp;&nbsp;
			<span>状态：</span>
				<form:radiobuttons class="i-checks" path="fstatus" items="${fns:getDictList('checkorder_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="warehouses:warehouseCheckOrder:add">
				<table:addRow url="${ctx}/warehouses/warehouseCheckOrder/form" title="仓库盘点"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseCheckOrder:edit">
			    <table:editRow url="${ctx}/warehouses/warehouseCheckOrder/form" title="仓库盘点" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseCheckOrder:del">
				<table:delRow url="${ctx}/warehouses/warehouseCheckOrder/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseCheckOrder:import">
				<table:importExcel url="${ctx}/warehouses/warehouseCheckOrder/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseCheckOrder:export">
	       		<table:exportExcel url="${ctx}/warehouses/warehouseCheckOrder/export"></table:exportExcel><!-- 导出按钮 -->
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
				
				<th  class="sort-column fcheckOrder">盘点单号</th>
				<th  class="sort-column warehouse.id">仓库</th>
				<th  class="sort-column checkTotalNum">盘点总数</th>
				<th  class="sort-column profitLossTotalNum">盈亏总数</th>
				<!-- <th  class="sort-column profitLossTotalMoney">盈亏总金额</th> -->
				<th  class="sort-column checkDate">盘点日期</th>
				<th  class="sort-column fstatus">状态</th>
				<th  class="sort-column remarks">备注信息</th>
				<th style=" text-align:center;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="warehouseCheckOrder">
			<tr>
				<td> <input type="checkbox" id="${warehouseCheckOrder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看仓库盘点', '${ctx}/warehouses/warehouseCheckOrder/form?id=${warehouseCheckOrder.id}','800px', '500px')">
					${warehouseCheckOrder.fcheckOrder}
				</a></td>
				<td>
					${warehouseCheckOrder.warehouse.fname}
				</td>
				<td>
					${warehouseCheckOrder.checkTotalNum}
				</td>
				<td>
					${warehouseCheckOrder.profitLossTotalNum}
				</td>
				<%-- <td>
					${warehouseCheckOrder.profitLossTotalMoney}
				</td> --%>
				<td>
					<fmt:formatDate value="${warehouseCheckOrder.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(warehouseCheckOrder.fstatus, 'checkorder_status', '')}
				</td>
				<td>
					${warehouseCheckOrder.remarks}
				</td>
				
				<td style=" text-align:center;">
					<c:if test="${warehouseCheckOrder.fstatus == '0' }">
					<shiro:hasPermission name="warehouses:warehouseCheckOrder:edit">
    					<a href="#" onclick="openDialog('修改仓库盘点', '${ctx}/warehouses/warehouseCheckOrder/form?id=${warehouseCheckOrder.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 盘点</a>
    				</shiro:hasPermission>
    				</c:if>
    				
    				<c:if test="${warehouseCheckOrder.fstatus == '1' }">
    				<shiro:hasPermission name="warehouses:warehouseCheckOrder:view">
						<a href="#" onclick="openDialogView('查看仓库盘点', '${ctx}/warehouses/warehouseCheckOrder/form?id=${warehouseCheckOrder.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 已盘点</a>
					</shiro:hasPermission>	
    				</c:if>
    				<shiro:hasPermission name="warehouses:warehouseCheckOrder:del">
						<a href="${ctx}/warehouses/warehouseCheckOrder/delete?id=${warehouseCheckOrder.id}" onclick="return confirmx('确认要删除该仓库盘点吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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