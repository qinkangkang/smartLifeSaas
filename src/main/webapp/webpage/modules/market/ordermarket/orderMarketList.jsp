<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>销售订单管理</title>
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
				<h5>销售订单列表</h5>
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
						<form:form id="searchForm" modelAttribute="orderMarket"
							action="${ctx}/market/ordermarket/orderMarket/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>销售单号：</span>
								<form:input path="fordernum" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>客户名称：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarket/orderMarket/selectcustomerBasic"
									id="customerBasic" name="customerBasic"
									value="${orderMarket.customerBasic.id}" title="选择客户名称"
									labelName="customerBasic.fname"
									labelValue="${orderMarket.customerBasic.fname}"
									cssClass="form-control required" fieldLabels="客户名称|备注信息"
									fieldKeys="fname|remarks" searchLabel="客户名称" searchKey="fname"></sys:gridselect>
								<span>仓库名称：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarket/orderMarket/selectwarehouse"
									id="warehouse" name="warehouse"
									value="${orderMarket.warehouse.id}" title="选择仓库名称"
									labelName="warehouse.fname"
									labelValue="${orderMarket.warehouse.fname}"
									cssClass="form-control required" fieldLabels="仓库名称|备注信息"
									fieldKeys="fname|remarks" searchLabel="仓库名称" searchKey="fname"></sys:gridselect><br/><br/>
								<span>账目类型：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarket/orderMarket/selectaccountType"
									id="accountType" name="accountType"
									value="${orderMarket.accountType.id}" title="选择账目类型"
									labelName="accountType.ftypename"
									labelValue="${orderMarket.accountType.ftypename}"
									cssClass="form-control required" fieldLabels="结算账户|备注信息"
									fieldKeys="ftypename|remarks" searchLabel="结算账户"
									searchKey="ftypename"></sys:gridselect>
								<span>结算账户：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarket/orderMarket/selectclearingAccount"
									id="clearingAccount" name="clearingAccount"
									value="${orderMarket.clearingAccount.id}" title="选择结算账户"
									labelName="clearingAccount.faccountname"
									labelValue="${orderMarket.clearingAccount.faccountname}"
									cssClass="form-control required" fieldLabels="账目类型|备注信息"
									fieldKeys="faccountname|remarks" searchLabel="账目类型"
									searchKey="faccountname"></sys:gridselect>
								<span>订单类型：</span>
								<form:select path="fordertype" class="form-control m-b">
									<form:option value="" label="" />
									<form:options items="${fns:getDictList('sdelas_type')}"
										itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select>
								<span>订单状态 ：</span>
								<form:select path="fstatus" class="form-control m-b">
									<form:option value="" label="" />
									<form:options items="${fns:getDictList('sdeals_status')}"
										itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select>
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="market:ordermarket:orderMarket:add">
								<table:addRow url="${ctx}/market/ordermarket/orderMarket/form"
									title="销售订单"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="market:ordermarket:orderMarket:edit">
								<table:editRow url="${ctx}/market/ordermarket/orderMarket/view"
									title="销售订单" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="market:ordermarket:orderMarket:del">
								<table:delRow
									url="${ctx}/market/ordermarket/orderMarket/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="market:ordermarket:orderMarket:import">
								<table:importExcel
									url="${ctx}/market/ordermarket/orderMarket/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="market:ordermarket:orderMarket:export">
								<table:exportExcel
									url="${ctx}/market/ordermarket/orderMarket/export"></table:exportExcel>
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
							<th class="sort-column fordernum">销售单号</th>
							
							<th class="sort-column warehouse.id">仓库</th>
							<th class="sort-column accountType.id">账目类型</th>
							
							
							<th class="sort-column fordertype">订单类型</th>
							<th class="sort-column fstatus">订单状态</th>
							
							<th class="sort-column fordercountprice">订单总价</th>
							<th class="sort-column forderdiscount">折扣(%)</th>
							<th class="sort-column fdiscountprice">最终价</th>
							<th class="sort-column fordermodel">订单形式</th>
							<!-- 
							<th class="sort-column fshipmenttype">发货方式</th>
							<th class="sort-column fendtime">结束时间</th>
							<th class="sort-column customerBasic.id">客户名称</th>
							<th class="sort-column clearingAccount.id">结算账户</th>
							<th class="sort-column fproceeds">实收款</th>
							<th class="sort-column fdebt">欠款</th>
							<th class="sort-column remarks">备注信息</th>
							 -->
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="orderMarket">
							<tr>
								<td><input type="checkbox" id="${orderMarket.id}"
									class="i-checks"></td>
								<td><a href="#"
									onclick="openDialogView('查看销售订单', '${ctx}/market/ordermarket/orderMarket/form?id=${orderMarket.id}','800px', '500px')">
										${orderMarket.fordernum} </a></td>
								
								<td>${orderMarket.warehouse.fname}</td>
								<td>${orderMarket.accountType.ftypename}</td>
								
								
								<td>${fns:getDictLabel(orderMarket.fordertype, 'sdelas_type', '')}
								</td>
								<td>${fns:getDictLabel(orderMarket.fstatus, 'sdeals_status', '')}
								</td>
								
								<td>${orderMarket.fordercountprice}</td>
								<td>${orderMarket.forderdiscount}</td>
								<td>${orderMarket.fdiscountprice}</td>
								<td>${fns:getDictLabel(orderMarket.fordermodel, 'order_model', '')}
								</td>
								<!--
								<td>${fns:getDictLabel(orderMarket.fshipmenttype, 'shipment_type', '')}
								</td>
								<td><fmt:formatDate value="${orderMarket.fendtime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${orderMarket.customerBasic.fname}</td>
								<td>${orderMarket.clearingAccount.faccountname}</td>
								<td>${orderMarket.fproceeds}</td>
								<td>${orderMarket.fdebt}</td>
								<td>${orderMarket.remarks}</td>
								 -->
								<td width="175px"><shiro:hasPermission
										name="market:ordermarket:orderMarket:view">
										<a href="#"
											onclick="openDialogView('查看销售订单', '${ctx}/market/ordermarket/orderMarket/view?id=${orderMarket.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="market:ordermarket:orderMarket:edit">
										<a href="#"
											onclick="openDialog('修改销售订单', '${ctx}/market/ordermarket/orderMarket/view?id=${orderMarket.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission name="market:ordermarket:orderMarket:del">
										<a
											href="${ctx}/market/ordermarket/orderMarket/delete?id=${orderMarket.id}"
											onclick="return confirmx('确认要删除该销售订单吗？', this.href)"
											class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
											删除</a>
									</shiro:hasPermission>
									<shiro:hasPermission
										name="market:ordermarket:orderMarket:edit">
										<a href="#"
											onclick="openDialog('修改销售订单', '${ctx}/market/ordermarket/orderMarket/exchange?id=${orderMarket.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											换货</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="market:ordermarket:orderMarket:edit">
										<a
											href="${ctx}/market/ordermarket/orderMarket/revoke?id=${orderMarket.id}"
											onclick="return confirmx('确认要撤销该销售订单吗？', this.href)"
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