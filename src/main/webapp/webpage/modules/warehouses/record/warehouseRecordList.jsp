<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库流水管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginBusinessTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endBusinessTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>仓库流水列表 </h5>
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
	<form:form id="searchForm" modelAttribute="warehouseRecord" action="${ctx}/warehouses/warehouseRecord/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>仓库：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseRecord/selectwarehouse" id="warehouse" name="warehouse"  value="${warehouseRecord.warehouse.id}"  title="选择仓库" labelName="warehouse.fname" 
					labelValue="${warehouseRecord.warehouse.fname}" cssClass="form-control required" fieldLabels="仓库|备注" fieldKeys="fname|remarks" searchLabel="仓库" searchKey="fname" ></sys:gridselect>
			&nbsp;&nbsp;&nbsp;
			<span>商品SPU：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseRecord/selectgoodsSpu" id="goodsSpu" name="goodsSpu"  value="${warehouseRecord.goodsSpu.id}"  title="选择商品SPU" labelName="goodsSpu.fgoodsname" 
					labelValue="${warehouseRecord.goodsSpu.fgoodsname}" cssClass="form-control required" fieldLabels="商品名|备注" fieldKeys="fgoodsname|remaks" searchLabel="商品名" searchKey="fgoodsname" ></sys:gridselect>
			<br><br>
			<span>业务时间：</span>
				<input id="beginBusinessTime" name="beginBusinessTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${warehouseRecord.beginBusinessTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endBusinessTime" name="endBusinessTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${warehouseRecord.endBusinessTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
		<%-- 	<shiro:hasPermission name="warehouses:warehouseRecord:add">
				<table:addRow url="${ctx}/warehouses/warehouseRecord/form" title="仓库流水"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseRecord:edit">
			    <table:editRow url="${ctx}/warehouses/warehouseRecord/form" title="仓库流水" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseRecord:del">
				<table:delRow url="${ctx}/warehouses/warehouseRecord/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseRecord:import">
				<table:importExcel url="${ctx}/warehouses/warehouseRecord/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseRecord:export">
	       		<table:exportExcel url="${ctx}/warehouses/warehouseRecord/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission> --%>
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
				<th  class="sort-column businessTime">业务时间</th>
				<th  class="sort-column goodsSku.id">货号</th>
				<th  class="sort-column goodsSpu.id">名称</th>
				<th  class="sort-column warehouse.id">仓库</th>
				<th  class="sort-column businessType">业务类型</th>
				<th  class="sort-column businessorderNumber">业务单号</th>
				<th  class="sort-column changeNum">改变数量</th>
				<th  class="sort-column remainingNum">剩余数量</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="warehouseRecord">
			<tr>
				<td> <input type="checkbox" id="${warehouseRecord.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看仓库流水', '${ctx}/warehouses/warehouseRecord/form?id=${warehouseRecord.id}','800px', '500px')">
					<fmt:formatDate value="${warehouseRecord.businessTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<td>
					${warehouseRecord.goodsSku.fgoodsnumber}
				</td>
				<td>
					${warehouseRecord.goodsSpu.fgoodsname}
				</td>
				<td>
					${warehouseRecord.warehouse.fname}
				</td>
				<td>
					${fns:getDictLabel(warehouseRecord.businessType, 'warehouse_business_type', '')}
				</td>
				<td>
					${warehouseRecord.businessorderNumber}
				</td>
				<td>
					${warehouseRecord.changeNum}
				</td>
				<td>
					${warehouseRecord.remainingNum}
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