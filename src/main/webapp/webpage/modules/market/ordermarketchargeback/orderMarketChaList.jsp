<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>销售退货单管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
	});
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>销售退货单列表</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i
						class="fa fa-wrench"></i>
					</a>
					<ul class="dropdown-menu dropdown-user">
						<li><a href="#">选项1</a></li>
						<li><a href="#">选项2</a></li>
					</ul>
					<a class="close-link"> <i class="fa fa-times"></i>
					</a>
				</div>
			</div>

			<div class="ibox-content">
				<sys:message content="${message}" />

				<!--查询条件-->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="orderMarketCha"
							action="${ctx}/market/ordermarketchargeback/orderMarketCha/"
							method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>客户名称：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectcustomerBasic"
									id="customerBasic" name="customerBasic"
									value="${orderMarketCha.customerBasic.id}" title="选择客户名称"
									labelName="customerBasic.fname"
									labelValue="${orderMarketCha.customerBasic.fname}"
									cssClass="form-control required" fieldLabels="客户姓名|备注"
									fieldKeys="fname|remarks" searchLabel="客户名称" searchKey="fname"></sys:gridselect>
								<span>仓库名称：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectwarehouse"
									id="warehouse" name="warehouse"
									value="${orderMarketCha.warehouse.id}" title="选择仓库名称"
									labelName="warehouse.fname"
									labelValue="${orderMarketCha.warehouse.fname}"
									cssClass="form-control required" fieldLabels="仓库名称|备注"
									fieldKeys="fname|remarks" searchLabel="仓库名称" searchKey="fname"></sys:gridselect>
								<span>结算账户：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectclearingAccount"
									id="clearingAccount" name="clearingAccount"
									value="${orderMarketCha.clearingAccount.id}" title="选择结算账户"
									labelName="clearingAccount.faccountname"
									labelValue="${orderMarketCha.clearingAccount.faccountname}"
									cssClass="form-control required" fieldLabels="结算账户|备注"
									fieldKeys="faccountname|remarks" searchLabel="结算账户"
									searchKey="faccountname"></sys:gridselect><br/><br/>
								<!-- 
								<span>所属商户：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectfsponsor"
									id="fsponsor" name="fsponsor.id"
									value="${orderMarketCha.fsponsor.id}" title="选择商户"
									labelName="fsponsor.name"
									labelValue="${orderMarketCha.fsponsor.name}"
									cssClass="form-control required" fieldLabels="商户名|备注"
									fieldKeys="name|remarks" searchLabel="所属商户" searchKey="name"></sys:gridselect>
								 -->
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission
								name="market:ordermarketchargeback:orderMarketCha:add">
								<table:addRow
									url="${ctx}/market/ordermarket/orderMarketDetail/list"
									title="销售退货单"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="market:ordermarketchargeback:orderMarketCha:edit">
								<table:editRow
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/form"
									title="销售退货单" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="market:ordermarketchargeback:orderMarketCha:del">
								<table:delRow
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="market:ordermarketchargeback:orderMarketCha:import">
								<table:importExcel
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="market:ordermarketchargeback:orderMarketCha:export">
								<table:exportExcel
									url="${ctx}/market/ordermarketchargeback/orderMarketCha/export"></table:exportExcel>
								<!-- 导出按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip"
								data-placement="left" onclick="sortOrRefresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>

						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm "
								onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm "
								onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>

				<!-- 表格 -->
				<table id="contentTable"
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>

							<th class="sort-column fordernum">单号</th>
							
							<th class="sort-column warehouse.id">仓库</th>
							
							
							<th class="sort-column fordertype">订单类型</th>
							<th class="sort-column fstatus">订单状态</th>
							<th class="sort-column fcountprice">商品总额</th>
							<th class="sort-column fordercountprice">订单总额</th>
							<th class="sort-column forderdiscount">折扣(%)</th>
							<th class="sort-column fdiscountprice">折后价</th>
							<th class="sort-column fordermodel">订单形式</th>
							<th class="sort-column fsponsor.id">商户</th>
							<!-- 
							<th class="sort-column fshipmenttype">发货方式</th>
							<th class="sort-column fothermoney">其他费用</th>
							<th class="sort-column customerBasic.id">客户</th>
							<th class="sort-column accountType.id">账目类型</th>
							<th class="sort-column clearingAccount.id">结算账户</th>
							<th class="sort-column fdebt">欠款</th>
							<th class="sort-column remarks">备注信息</th>
							<th class="sort-column createBy.id">创建者</th>
							<th class="sort-column createDate">创建时间</th>
							<th class="sort-column updateBy.id">更新者</th>
							<th class="sort-column updateDate">更新时间</th>
							 -->
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="orderMarketCha">
							<tr>
								<td><input type="checkbox" id="${orderMarketCha.id}"
									class="i-checks"></td>

								<td><a href="#"
									onclick="openDialogView('查看销售退货单', '${ctx}/market/ordermarketchargeback/orderMarketCha/form?id=${orderMarketCha.id}','800px', '500px')">
										${orderMarketCha.fordernum} </a></td>
								
								<td>${orderMarketCha.warehouse.fname}</td>
								
								
								
								<td>${fns:getDictLabel(orderMarketCha.fordertype, 'chargeback_type', '')}
								</td>
								<td>${fns:getDictLabel(orderMarketCha.fstatus, 'market_charge_status', '')}
								</td>
								<td>${orderMarketCha.fcountprice}</td>
								<td>${orderMarketCha.fordercountprice}</td>
								<td>${orderMarketCha.forderdiscount}</td>
								<td>${orderMarketCha.fdiscountprice}</td>
								<td>${fns:getDictLabel(orderMarketCha.fordermodel, 'order_model', '')}
								</td>
								<td>${orderMarketCha.fsponsor.name}</td>
								<!-- 
								<td>${fns:getDictLabel(orderMarketCha.fshipmenttype, 'shipment_type', '')}</td>
								<td>${orderMarketCha.fothermoney}</td>
								<td>${orderMarketCha.customerBasic.fname}</td>
								<td>${orderMarketCha.accountType.ftypename}</td>
								<td>${orderMarketCha.clearingAccount.faccountname}</td>
								<td>${orderMarketCha.fdebt}</td>
								<td>${orderMarketCha.remarks}</td>
								<td>${orderMarketCha.createBy.loginName}</td>
								<td><fmt:formatDate value="${orderMarketCha.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${orderMarketCha.updateBy.loginName}</td>
								<td><fmt:formatDate value="${orderMarketCha.updateDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								-->
								<td><shiro:hasPermission
										name="market:ordermarketchargeback:orderMarketCha:view">
										<a href="#"
											onclick="openDialogView('查看销售退货单', '${ctx}/market/ordermarketchargeback/orderMarketCha/view?id=${orderMarketCha.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="market:ordermarketchargeback:orderMarketCha:edit">
										<a href="#"
											onclick="openDialog('修改销售退货单', '${ctx}/market/ordermarketchargeback/orderMarketCha/form?id=${orderMarketCha.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="market:ordermarketchargeback:orderMarketCha:del">
										<a
											href="${ctx}/market/ordermarketchargeback/orderMarketCha/delete?id=${orderMarketCha.id}"
											onclick="return confirmx('确认要删除该销售退货单吗？', this.href)"
											class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
											删除</a>
									</shiro:hasPermission><shiro:hasPermission
										name="market:ordermarketchargeback:orderMarketCha:del">
										<a
											href="${ctx}/market/ordermarketchargeback/orderMarketCha/revoke?id=${orderMarketCha.id}"
											onclick="return confirmx('确认要撤销该销售退货单吗？', this.href)"
											class="btn btn-info btn-xs"><i class="fa fa-trash"></i>
											撤销</a>
									</shiro:hasPermission></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<!-- 分页代码 -->
				<table:page page="${page}"></table:page>
				<br /> <br />
			</div>
		</div>
	</div>
</body>
</html>