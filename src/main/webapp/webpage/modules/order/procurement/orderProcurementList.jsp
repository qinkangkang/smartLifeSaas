<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购单管理</title>
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
				<h5>采购单列表</h5>
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
						<form:form id="searchForm" modelAttribute="orderProcurement"
							action="${ctx}/order/procurement/orderProcurement/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">

								<span>所属仓库：</span>
								<sys:gridselect
									url="${ctx}/order/procurement/orderProcurement/selectfwarehose"
									id="fwarehose" name="fwarehose"
									value="${orderProcurement.fwarehose.id}" title="选择仓库"
									labelName="fwarehose.fname"
									labelValue="${orderProcurement.fwarehose.fname}"
									cssClass="form-control required" fieldLabels="仓库名|备注"
									fieldKeys="fname|remarks" searchLabel="所属仓库" searchKey="fname"></sys:gridselect>
								<span>所属门店：</span>
								<sys:treeselect id="fstore" name="fstore.id"
									value="${orderProcurement.fstore.id}" labelName="fstore.name"
									labelValue="${orderProcurement.fstore.name}" title="门店"
									url="/sys/office/treeData?type=2"
									cssClass="form-control required" />

								<span>打印模板：</span>
								<sys:gridselect
									url="${ctx}/order/procurement/orderProcurement/selectfmodeltype"
									id="fmodeltype" name="fmodeltype"
									value="${orderProcurement.fmodeltype.id}" title="选择打印模板"
									labelName="fmodeltype.fname"
									labelValue="${orderProcurement.fmodeltype.fname}"
									cssClass="form-control required" fieldLabels="模板|备注"
									fieldKeys="fname|remarks" searchLabel="打印模板" searchKey="fname"></sys:gridselect>
								<br /> <br /> <span>是否打印 ：</span>
								<form:select path="fprint" class="form-control m-b">
									<form:option value="" label="" />
									<form:options items="${fns:getDictList('print_status')}"
										itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>采购单号：</span>
								<form:input path="fordernum" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span>订单状态：</span>
								<form:select path="fstatus" class="form-control m-b">
									<form:option value="" label="" />
									<form:options items="${fns:getDictList('procurement_status')}"
										itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select>
								<br />


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
								name="order:procurement:orderProcurement:add">
								<table:addRow
									url="${ctx}/order/procurement/orderProcurement/form"
									title="采购单"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:procurement:orderProcurement:edit">
								<table:editRow
									url="${ctx}/order/procurement/orderProcurement/form"
									title="采购单" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:procurement:orderProcurement:del">
								<table:delRow
									url="${ctx}/order/procurement/orderProcurement/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:procurement:orderProcurement:import">
								<table:importExcel
									url="${ctx}/order/procurement/orderProcurement/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:procurement:orderProcurement:export">
								<table:exportExcel
									url="${ctx}/order/procurement/orderProcurement/export"></table:exportExcel>
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
							<th class="sort-column fsupplier.id">供应商</th>
							<th class="sort-column fwarehose.id">采购仓库</th>
							<th class="sort-column fseniorarchirist.id">审批人</th>
							<th class="sort-column fordercountprice">原价</th>
							<th class="sort-column fdiscountprice">成交价</th>
							<th class="sort-column fordertype">类型</th>
							<th class="sort-column fstatus">状态</th>
							
							<!-- 
							<th class="sort-column fsponsor.id">所属商户</th>
							<th class="sort-column createBy.id">创建者</th>
							<th class="sort-column createDate">创建时间</th>
							<th class="sort-column updateBy.id">更新者</th>
							<th class="sort-column updateDate">更新时间</th>
							
							<th class="sort-column fexecutor.id">执行人</th>
							
							<th class="sort-column fprint">是否打印</th>
							<th class="sort-column factualpayment">实付款</th>
							<th class="sort-column fdebt">欠款</th>
				<th class="sort-column remarks">备注信息</th>
				<th  class="sort-column fothermoney">其他费用</th>
				<th  class="sort-column fcountprice">商品价格总额</th> 
				<th  class="sort-column fcutsmall">抹零</th>
				<th  class="sort-column fendtime">订单结束时间</th>
				<th  class="sort-column fdclearingaccountid.id">结算账户</th>
				<th  class="sort-column fdaccounttype.id">其他费用类型</th>
				<th  class="sort-column fmodeltype.id">打印模板</th>
				-->
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="orderProcurement">
							<tr>
								<td><input type="checkbox" id="${orderProcurement.id}"
									class="i-checks"></td>
								<td><a href="#"
									onclick="openDialogView('查看采购单', '${ctx}/order/procurement/orderProcurement/form?id=${orderProcurement.id}','800px', '500px')">
										${orderProcurement.fordernum} </a></td>
								<td>${orderProcurement.fsupplier.fname}</td>
								<td>${orderProcurement.fwarehose.fname}</td>
								<td>${orderProcurement.fseniorarchirist.loginName}</td>
								<td>${orderProcurement.fordercountprice}</td>
								<td>${orderProcurement.fdiscountprice}</td>
								<td>${fns:getDictLabel(orderProcurement.fordertype, 'procurement_type', '')}
								</td>
								<td>${fns:getDictLabel(orderProcurement.fstatus, 'procurement_status', '')}
								

								<!-- 
								<td>${orderProcurement.fsponsor.name}</td>
								<td>${orderProcurement.createBy.loginName}</td>
								<td><fmt:formatDate value="${orderProcurement.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${orderProcurement.updateBy.loginName}</td>
								<td><fmt:formatDate value="${orderProcurement.updateDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								
								<td>${orderProcurement.fexecutor.loginName}</td>
								
								<td>${orderProcurement.factualpayment}</td>
								<td>${orderProcurement.fdebt}</td>
								
								</td>
								<td>${fns:getDictLabel(orderProcurement.fprint, 'print_status', '')}
								</td>
								
				<td>${orderProcurement.remarks}</td>	
				<td>
					${orderProcurement.fcountprice}
				</td>
				<td>
					${orderProcurement.fcutsmall}
				</td>
				<td>
					<fmt:formatDate value="${orderProcurement.fendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${orderProcurement.fothermoney}
				</td>
				<td>
					${orderProcurement.fdclearingaccountid.faccountname}
				</td>
				<td>
					${orderProcurement.fdaccounttype.ftypename}
				</td>
				<td>
					${orderProcurement.fmodeltype.fname}
				</td> -->
								<td><shiro:hasPermission
										name="order:procurement:orderProcurement:view">
										<a href="#"
											onclick="openDialogView('查看采购单', '${ctx}/order/procurement/orderProcurement/view?id=${orderProcurement.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="order:procurement:orderProcurement:edit">
										<a href="#"
											onclick="openDialog('修改采购单', '${ctx}/order/procurement/orderProcurement/view?id=${orderProcurement.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="order:procurement:orderProcurement:del">
										<a
											href="${ctx}/order/procurement/orderProcurement/delete?id=${orderProcurement.id}"
											onclick="return confirmx('确认要删除该采购单吗？', this.href)"
											class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
											删除</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="order:procurement:orderProcurement:edit">
										<a
											href="${ctx}/order/procurement/orderProcurement/repeal?id=${orderProcurement.id}"
											onclick="return confirmx('确认要撤销该采购单吗？', this.href)"
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