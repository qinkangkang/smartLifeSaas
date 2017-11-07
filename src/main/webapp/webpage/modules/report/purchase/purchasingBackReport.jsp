<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购退单明细</title>
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
		<h5>采购退单明细 </h5>
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
	 <form:form id="searchForm" modelAttribute="orderProChaDetail" action="${ctx}/report/reportData/purchasingBackOrderReport" method="post" class="form-inline">
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
	
		<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable" align="center">
		<tr>
			<td align="center">采购商品数&nbsp;&nbsp;<font size="4" color="red">${purBackDetail.purchaseBackGoodsNum }</font></td>
			<td align="center">采购笔数&nbsp;&nbsp;<font size="4" color="red">${purBackDetail.purchaseBackOrderNum }</font></td>
			<td align="center">采购金额&nbsp;&nbsp;<font size="4" color="red">${purBackDetail.purchaseBackAmountOfMoney }</font></td>
		</tr>
	
	</table>
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable" align="center">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column fspu.id">商品名</th>
				<th  class="sort-column fsku.id">货号</th>
				<th  class="sort-column fsku.id">条码</th>
				<th  class="sort-column fspu.id">品牌</th>
				<th  class="sort-column fspu.id">分类</th>
				<th  class="sort-column fspu.id">单位</th>
				<th  class="sort-column fgoodsnum">数量</th>
				<th  class="sort-column fgoodsprice">单价</th>
				<th  class="sort-column a.fgoodsdiscount">折扣(%)</th>
				<th  class="sort-column a.fdiscountprice">折后单价</th>
				<th  class="sort-column a.fcountmoney">金额</th>
	<!-- 			<th  class="sort-column fdiscountcountmoney">折后金额</th>
				<th  class="sort-column forderprocurement.id">单据编号</th>
				<th  class="sort-column forderprocurement.id">采购员</th>
				<th  class="sort-column forderprocurement.id">供应商</th>
				<th  class="sort-column forderprocurement.id">仓库</th>
				<th  class="sort-column forderprocurement.id">结算账户</th>
				<th  class="sort-column forderprocurement.id">单据总金额</th>
				<th  class="sort-column forderprocurement.id">实付金额</th>
				<th  class="sort-column forderprocurement.id">单据日期</th>
				<th  class="sort-column forderprocurement.id">单据备注</th>
				<th  class="sort-column fsku.id">商品备注</th> -->
				<th style=" text-align:center;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="OrderProChaDetail">
			<tr>
				 <td> <input type="checkbox" id="${OrderProChaDetail.id}" class="i-checks"></td>
				<td>
					${OrderProChaDetail.fspu.fgoodsname}
				</td>
				<td>
					${OrderProChaDetail.fsku.fgoodsnumber}
				</td>
				<td>
					${OrderProChaDetail.fsku.fbarcode}
				</td>
				<td>
					${OrderProChaDetail.fspu.brand.fbrandname}
				</td>
				<td>
					${OrderProChaDetail.fspu.categorys.name}
				</td>
				<td>
					${OrderProChaDetail.fspu.goodsUnit.funitname}
				</td>
				<td>
					${OrderProChaDetail.fgoodsnum}
				</td> 
				<td>
					${OrderProChaDetail.fgoodsprice}
				</td>
				<td>
					${OrderProChaDetail.fdiscount}
				</td>
				<td>
					${OrderProChaDetail.fdiscountPrice}
				</td>
				<td>
					${OrderProChaDetail.fcountprice}
				</td>
				<%--<td>
					${OrderProChaDetail.fdiscountcountprice}
				</td>
				<td>
					${OrderProChaDetail.fprocurementchargeback.fordernum}
				</td>
			 	<td>
					${OrderProChaDetail.fprocurementchargeback.fexecutor.name}
				</td>
				<td>
					${OrderProChaDetail.fprocurementchargeback.fsupplier.fname}
				</td>
				<td>
					${OrderProChaDetail.fprocurementchargeback.fwarehose.fname}
				</td>
				<td>
					${OrderProChaDetail.fprocurementchargeback.fdclearingaccount.faccountname}
				</td>
				<td>
					${OrderProChaDetail.fprocurementchargeback.fordercountprice}
				</td>
				<td>
					${OrderProChaDetail.fprocurementchargeback.factualpayment}
				</td>
				<td>
					<fmt:formatDate value="${OrderProChaDetail.fprocurementchargeback.fendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${OrderProChaDetail.fprocurementchargeback.remarks}
				</td>
				<td>
					${OrderProChaDetail.fsku.remarks}
				</td> --%>

			
		
				
				<td style=" text-align:center;">
					<shiro:hasPermission name="warehouses:warehouseGoodsInfo:view">
						<a href="#" onclick="openDialogView('采购退单明细详情', '${ctx}/report/reportData/purchaseBackOrderForm?id=${OrderProChaDetail.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
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