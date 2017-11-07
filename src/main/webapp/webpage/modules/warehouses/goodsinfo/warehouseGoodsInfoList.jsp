<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库商品管理</title>
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
		<h5>仓库商品列表 </h5>
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
	<form:form id="searchForm" modelAttribute="warehouseGoodsInfo" action="${ctx}/warehouses/warehouseGoodsInfo/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>仓库：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseGoodsInfo/selectwarehouse" id="warehouse" name="warehouse"  value="${warehouseGoodsInfo.warehouse.id}"  title="选择仓库" labelName="warehouse.fname" 
					labelValue="${warehouseGoodsInfo.warehouse.fname}" cssClass="form-control required" fieldLabels="仓库|备注" fieldKeys="fname|remarks" searchLabel="仓库" searchKey="fname" ></sys:gridselect>
			&nbsp;&nbsp;&nbsp;
			<span>商品品牌：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseGoodsInfo/selectgoodsBrand" id="goodsBrand" name="goodsBrand"  value="${warehouseGoodsInfo.goodsBrand.id}"  title="选择商品品牌" labelName="goodsBrand.fbrandname" 
					labelValue="${warehouseGoodsInfo.goodsBrand.fbrandname}" cssClass="form-control required" fieldLabels="品牌|备注" fieldKeys="fbrandname|remarks" searchLabel="品牌" searchKey="fbrandname" ></sys:gridselect>
			&nbsp;&nbsp;&nbsp;
			<span>商品分类：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseGoodsInfo/selectgoodsCategory" id="goodsCategory" name="goodsCategory"  value="${warehouseGoodsInfo.goodsCategory.id}"  title="选择商品分类" labelName="goodsCategory.name" 
					labelValue="${warehouseGoodsInfo.goodsCategory.name}" cssClass="form-control required" fieldLabels="分类|备注" fieldKeys="name|remarks" searchLabel="分类" searchKey="name" ></sys:gridselect>
			<%-- <span>单位：</span>
				<sys:gridselect url="${ctx}/warehouses/warehouseGoodsInfo/selectgoodsUnit" id="goodsUnit" name="goodsUnit"  value="${warehouseGoodsInfo.goodsUnit.id}"  title="选择单位" labelName="goodsUnit.funitname" 
					labelValue="${warehouseGoodsInfo.goodsUnit.funitname}" cssClass="form-control required" fieldLabels="单位|备注" fieldKeys="funitname|remarks" searchLabel="单位" searchKey="funitname" ></sys:gridselect> --%>
			<br><br>
			<span>商品：</span>
				<form:input path="queryConditions" htmlEscape="false"  class=" form-control input-sm" placeholder="名称、货号、条码"/>
			&nbsp;&nbsp;&nbsp;
			<span>库存总量：</span>
				<form:input path="beginFtotalinventory" htmlEscape="false"  class=" form-control input-sm"/>至
				<form:input path="endFtotalinventory" htmlEscape="false"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;
				<form:checkbox path="inventoryFilter" htmlEscape="false" value="0"/><span>过滤无库存</span>
				&nbsp;&nbsp;&nbsp;
				<form:checkbox path="inventoryWarning" htmlEscape="false" value="0"/><span>库存预警</span>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="warehouses:warehouseGoodsInfo:add">
				<table:addRow url="${ctx}/warehouses/warehouseGoodsInfo/form" title="仓库商品"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseGoodsInfo:edit">
			    <table:editRow url="${ctx}/warehouses/warehouseGoodsInfo/form" title="仓库商品" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseGoodsInfo:del">
				<table:delRow url="${ctx}/warehouses/warehouseGoodsInfo/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseGoodsInfo:import">
				<table:importExcel url="${ctx}/warehouses/warehouseGoodsInfo/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="warehouses:warehouseGoodsInfo:export">
	       		<table:exportExcel url="${ctx}/warehouses/warehouseGoodsInfo/export"></table:exportExcel><!-- 导出按钮 -->
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
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable" align="center">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column goodsSpu.id">商品名</th>
				<th  class="sort-column goodsSku.id">货号</th>
				<!-- <th  class="sort-column goodsSku.id">条码</th> -->
				<th  class="sort-column warehouse.id">仓库</th>
				<!-- <th  class="sort-column goodsBrand.id">商品品牌</th>
				<th  class="sort-column goodsCategory.id">商品分类</th> 
				<th  class="sort-column goodsSpu.id">采购价</th>
				<th  class="sort-column goodsSku.id">销售价</th>
				<th  class="sort-column goodsSku.id">预警下限</th>
				<th  class="sort-column goodsSku.id">预警上限</th> -->
				<th  class="sort-column finventory">可用库存量</th>
				<th  class="sort-column flockinventory">锁定库存量</th>
				<th  class="sort-column ftotalinventory">库存总量</th>
			<!-- 	<th  class="sort-column goodsSpu.id">单位</th> -->
				<!-- <th  class="sort-column remarks">备注信息</th> -->
				<th style=" text-align:center;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="warehouseGoodsInfo">
			<tr>
				<td> <input type="checkbox" id="${warehouseGoodsInfo.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看仓库商品', '${ctx}/warehouses/warehouseGoodsInfo/form?id=${warehouseGoodsInfo.id}','800px', '500px')">
					${warehouseGoodsInfo.goodsSpu.fgoodsname}
				</a></td>
				<td>
					${warehouseGoodsInfo.goodsSku.fgoodsnumber}
				</td>
				<%-- <td>
					${warehouseGoodsInfo.goodsSku.fbarcode}
				</td> --%>
				<td>
					${warehouseGoodsInfo.warehouse.fname}
				</td>
				<%-- <td>
					${warehouseGoodsInfo.goodsBrand.fbrandname}
				</td>
				<td>
					${warehouseGoodsInfo.goodsCategory.name}
				</td> 
				<td>
					${warehouseGoodsInfo.goodsSpu.fbuyprice}
				</td>
				<td>
					${warehouseGoodsInfo.goodsSku.fonsalesprice}
				</td>
				<td>
					${warehouseGoodsInfo.goodsSku.fstorelowerno}
				</td>
				<td>
					${warehouseGoodsInfo.goodsSku.fstoreupperno}
				</td> --%>
				<td>
					${warehouseGoodsInfo.finventory}
				</td>
				<td>
					${warehouseGoodsInfo.flockinventory}
				</td>
				<td>
					${warehouseGoodsInfo.ftotalinventory}
				</td>
				<%-- <td>
					${warehouseGoodsInfo.goodsUnit.funitname}
				</td> --%>
			<%-- 	<td>
					${warehouseGoodsInfo.goodsSku.remarks}
				</td> --%>
				<td style=" text-align:center;">
					<shiro:hasPermission name="warehouses:warehouseGoodsInfo:view">
						<a href="#" onclick="openDialogView('查看仓库商品', '${ctx}/warehouses/warehouseGoodsInfo/form?id=${warehouseGoodsInfo.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="warehouses:warehouseGoodsInfo:edit">
    					<a href="#" onclick="openDialog('修改仓库商品', '${ctx}/warehouses/warehouseGoodsInfo/form?id=${warehouseGoodsInfo.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<%-- <shiro:hasPermission name="warehouses:warehouseGoodsInfo:del">
						<a href="${ctx}/warehouses/warehouseGoodsInfo/delete?id=${warehouseGoodsInfo.id}" onclick="return confirmx('确认要删除该仓库商品吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission> --%>
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