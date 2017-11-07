<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>销售明细</title>
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
		<h5>销售明细 </h5>
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
	<form:form id="searchForm" modelAttribute="orderMarketDetail" action="${ctx}/report/reportData/marketGoodsListReport" method="post" class="form-inline">
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
		<tr>
			<td align="center">销售商品数&nbsp;&nbsp;<font size="4" color="red">${marketDetail.marketGoodsNum }</font></td>
			<td align="center">销售额&nbsp;&nbsp;<font size="4" color="red">${marketDetail.marketValue }</font></td>
			<td align="center">销售毛利&nbsp;&nbsp;<font size="4" color="red">${marketDetail.marketGrossMargin }</font></td>
			<td align="center">销售毛利率(%)&nbsp;&nbsp;<font size="4" color="red">${marketDetail.marketGrossProfitMargin }</font></td>
		</tr>
	</table>
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column fspu.id">商品名</th>
				<th  class="sort-column fsku.id">货号</th>
				<th  class="sort-column fsku.id">条码</th>
				<th  class="sort-column fspu.id">品牌</th>
				<th  class="sort-column fspu.id">分类</th>
				<th  class="sort-column fspu.id">单位</th>
				<th  class="sort-column ">数量</th>
				<th  class="sort-column ">单价</th>
				<th  class="sort-column ">折扣(%)</th>
				<th  class="sort-column ">折后价</th>
				<th  class="sort-column ">金额</th>
	 			<th  class="sort-column ">折后金额</th>
				<th  class="sort-column ">采购均价</th>
				<!--<th  class="sort-column ">采购成本</th>
				<th  class="sort-column ">毛利润</th>
				<th  class="sort-column ">毛利率(%)</th>
			 	<th  class="sort-column ">单据编号</th>
				<th  class="sort-column ">制单人</th>
				<th  class="sort-column ">销售员</th>
				<th  class="sort-column ">客户</th>
				<th  class="sort-column ">仓库</th>
				<th  class="sort-column ">结算账户</th> 
				<th  class="sort-column ">单据总金额</th> 
				<th  class="sort-column ">实收金额</th> 
				<th  class="sort-column ">单据日期</th> 
				<th  class="sort-column ">销售员所属门店</th> 
				<th  class="sort-column ">仓库所属门店</th> 
				<th  class="sort-column ">单据备注</th> 
				<th  class="sort-column ">商品备注</th>  -->
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="orderMarketDetail">
			<tr>
				 <td>
					${orderMarketDetail.goodsSpu.fgoodsname}
				</td>
				<td>
					${orderMarketDetail.goodsSku.fgoodsnumber}
				</td>
				<td>
					${orderMarketDetail.goodsSku.fbarcode}
				</td>
			 	<td>
					${orderMarketDetail.goodsSpu.brand.fbrandname}
				</td>
				<td>
					${orderMarketDetail.goodsSpu.categorys.name}
				</td>
				<td>
					${orderMarketDetail.goodsSpu.goodsUnit.funitname}
				</td>
				<td>
					${orderMarketDetail.fgoodsnum}
				</td>
				<td>
					${orderMarketDetail.fgoodsprice}
				</td>
				<td>
					${orderMarketDetail.fgoodsdiscount}
				</td>
				<td>
					${orderMarketDetail.fdiscountprice}
				</td>
				<td>
					${orderMarketDetail.fcountmoney}
				</td>
				<td>
					${orderMarketDetail.fdiscountcountmoney}
				</td>
		 		<td>
					${orderMarketDetail.goodsSpu.fbuyprice}
				</td>
				<%--<td>
					${orderMarketDetail.purchasingCost}
				</td>
				<td>
					${orderMarketDetail.grossProfit}
				</td>
				<td>
					${orderMarketDetail.grossProfitMargin}
				</td>
				<td>
					${orderMarketDetail.orderMarket.fordernum}
				</td>
				<td>
					${orderMarketDetail.orderMarket.createBy.name}
				</td>
				<td>
					${orderMarketDetail.orderMarket.updateBy.name}
				</td>
				<td>
					${orderMarketDetail.orderMarket.customerBasic.fname}
				</td>
				<td>
					${orderMarketDetail.orderMarket.warehouse.fname}
				</td>
				<td>
					${orderMarketDetail.orderMarket.clearingAccount.faccountname}
				</td>
				<td>
					${orderMarketDetail.orderMarket.fordercountprice}
				</td>
				<td>
					${orderMarketDetail.orderMarket.fdiscountprice}
				</td>
				<td>
					${orderMarketDetail.orderMarket.fendtime}
				</td>
				<td>
					${orderMarketDetail.orderMarket.updateBy.office.name}
				</td>
				<td>
					${orderMarketDetail.orderMarket.warehouse.office.name}
				</td>
				<td>
					${orderMarketDetail.orderMarket.remarks}
				</td>
				<td>
					${orderMarketDetail.goodsSku.remarks}
				</td> --%>
				<td>
					<shiro:hasPermission name="warehouses:warehouse:view">
						<a href="#" onclick="openDialogView('查看销售商品详情', '${ctx}/report/reportData/marketGoodsForm?id=${orderMarketDetail.id}','900px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
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