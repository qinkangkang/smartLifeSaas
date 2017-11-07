<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>调拨明细</title>
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
		<h5>调拨明细 </h5>
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
	<form:form id="searchForm" modelAttribute="warehouseTransferGoods" action="${ctx}/report/reportData/transferGoodsListReport" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
		
		 </div>	
	</form:form>
	<br/>
	</div>
	</div> 
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
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
	<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable" align="center">
		<%-- <tr>
			<td align="center">盘点商品数&nbsp;&nbsp;<font size="4" color="red">${checkGoodsDetail.checkGoodsNum }</font></td>
			<td align="center">盘点仓库数&nbsp;&nbsp;<font size="4" color="red">${checkGoodsDetail.checkWarehouseNum }</font></td>
			<td align="center">盘点盈亏数&nbsp;&nbsp;<font size="4" color="red">${checkGoodsDetail.checkProfitAndLossNum }</font></td>
			<td align="center">盘点盈亏金额(元)&nbsp;&nbsp;<font size="4" color="red">${checkGoodsDetail.checkProfitAndLossMoney }</font></td>
		</tr> --%>
	</table>
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column">单号</th>
				<th  class="sort-column">状态</th>
				<th  class="sort-column">调拨时间</th>
				<th  class="sort-column">商品名称</th>
				<th  class="sort-column">货号</th>
				<th  class="sort-column">条码</th>
				<th  class="sort-column">分类</th>
				<th  class="sort-column">品牌</th>
				<th  class="sort-column">单位</th>
				<th  class="sort-column">调拨数量</th>
				<th  class="sort-column">调出前数量</th>
				<th  class="sort-column">调入前数量</th>
				<th  class="sort-column">调出仓库</th>
				<th  class="sort-column">调入仓库</th>
				<th  class="sort-column">调拨人</th>
				<th  class="sort-column">商品备注</th>
				<th  class="sort-column">单据备注</th>
				<!-- <th  class="sort-column goodsSpu.fgoodsname">商品名称</th>
				<th  class="sort-column goodsSku.fgoodsnumber">货号</th>
				<th  class="sort-column goodsSku.fbarcode">条码</th>
				<th  class="sort-column goodsSpu.categorys.name">分类</th>
				<th  class="sort-column goodsSpu.brand.fbrandname">品牌</th>
				<th  class="sort-column goodsSpu.goodsUnit.funitname">单位</th>
				<th  class="sort-column checkOrder.warehouse.fname">仓库</th>
				<th  class="sort-column checkNum">盘点数量</th>
				<th  class="sort-column checkBeforeNum">盘点前数量</th>
				<th  class="sort-column profitLossNum">盈亏数量</th>
				<th  class="sort-column profitLossMoney">盈亏金额</th>
				<th  class="sort-column profitLossMoney">盈亏状况</th>
				<th  class="sort-column checkOrder.updateBy.name">盘点人</th>
				<th  class="sort-column checkOrder.checkDate">盘点时间</th>
				<th  class="sort-column remarks">备注</th> -->
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="warehouseTransferGoods">
			<tr>
				 <td>
					${warehouseTransferGoods.transferOrder.ftransferOrder}
				</td>
				 <td>
					${warehouseTransferGoods.transferOrder.fstatus}
				</td>
				 <td>
					${warehouseTransferGoods.transferOrder.ftransferDate}
				</td>
				 <td>
					${warehouseTransferGoods.goodsSpu.fgoodsname}
				</td>
				 <td>
					${warehouseTransferGoods.goodsSpu.fgoodsname}
				</td>
				 <td>
					${warehouseTransferGoods.goodsSku.fgoodsnumber}
				</td>
				 <td>
					${warehouseTransferGoods.goodsSpu.categorys.name}
				</td>
				 <td>
					${warehouseTransferGoods.goodsSpu.brand.fbrandname}
				</td>
				 <td>
					${warehouseTransferGoods.goodsSpu.goodsUnit.funitname}
				</td>
				 <td>
					${warehouseTransferGoods.transferNum}
				</td>
				 <td>
					${warehouseTransferGoods.ftransferInBeforeNum}
				</td>
				 <td>
					${warehouseTransferGoods.ftransferOutBeforeNum}
				</td>
				 <td>
					${warehouseTransferGoods.transferOrder.warehouseOut.fname}
				</td>
				 <td>
					${warehouseTransferGoods.transferOrder.fwarehouseIn.fname}
				</td>
				<td>
					<shiro:hasPermission name="warehouses:warehouse:view">
						<a href="#" onclick="openDialogView('查看销售商品详情', '${ctx}/report/reportData/transferGoodsForm?id=${warehouseTransferGoods.id}','900px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
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