<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购退单管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
			
	        laydate({
	            elem: '#beginCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
	<script>
		window.onload = function() {
			if (location.search("repagexxx")) {
				location.reload();
			}
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>采购退单列表</h5>
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
						<form:form id="searchForm" modelAttribute="orderProChargeback"
							action="${ctx}/order/prochargeback/orderProChargeback/"
							method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								
								<span>供应商：</span>
								<sys:gridselect
									url="${ctx}/order/prochargeback/orderProChargeback/selectfsupplier"
									id="fsupplier" name="fsupplier"
									value="${orderProChargeback.fsupplier.id}" title="选择供应商"
									labelName="fsupplier.fname"
									labelValue="${orderProChargeback.fsupplier.fname}"
									cssClass="form-control required" fieldLabels="供应商|备注"
									fieldKeys="fname|remarks" searchLabel="供应商名称" searchKey="fname"></sys:gridselect>
								<span>所属仓库：</span>
								<sys:gridselect
									url="${ctx}/order/prochargeback/orderProChargeback/selectfwarehose"
									id="fwarehose" name="fwarehose"
									value="${orderProChargeback.fwarehose.id}" title="选择仓库"
									labelName="fwarehose.fname"
									labelValue="${orderProChargeback.fwarehose.fname}"
									cssClass="form-control required" fieldLabels="仓库|备注"
									fieldKeys="fname|remarks" searchLabel="仓库名 " searchKey="fname"></sys:gridselect>
								<span>结算账户：</span>
								<sys:gridselect
									url="${ctx}/order/prochargeback/orderProChargeback/selectfdclearingaccount"
									id="fdclearingaccount" name="fdclearingaccount"
									value="${orderProChargeback.fdclearingaccount.id}"
									title="选择结算账户" labelName="fdclearingaccount.faccountname"
									labelValue="${orderProChargeback.fdclearingaccount.faccountname}"
									cssClass="form-control required" fieldLabels="账号名称|备注"
									fieldKeys="faccountname|remarks" searchLabel="账户名"
									searchKey="faccountname"></sys:gridselect><br/><br/>
								<span>采购单：</span>
								<sys:gridselect
									url="${ctx}/order/prochargeback/orderProChargeback/selectforderprocurement"
									id="forderprocurement" name="forderprocurement"
									value="${orderProChargeback.forderprocurement.id}"
									title="选择采购单" labelName="forderprocurement.fordernum"
									labelValue="${orderProChargeback.forderprocurement.fordernum}"
									cssClass="form-control required" fieldLabels="采购单|备注"
									fieldKeys="fordernum|remarks" searchLabel="采购单号"
									searchKey="fordernum"></sys:gridselect>
								<span>退单单号：</span>
								<form:input path="fordernum" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>是否打印 ：</span>
								<form:select path="fprint" class="form-control m-b">
									<form:option value="" label="" />
									<form:options items="${fns:getDictList('print_status')}"
										itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select><br/><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>欠款：</span>
								<form:input path="fdebt" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>创建时间：</span> <input id="beginCreateDate"
									name="beginCreateDate" type="text" maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${orderProChargeback.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
								- <input id="endCreateDate" name="endCreateDate" type="text"
									maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${orderProChargeback.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
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
								name="order:prochargeback:orderProChargeback:add">
								<table:addRow
									url="${ctx}/order/procurement/orderProDetail/list"
									title="采购退单"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:prochargeback:orderProChargeback:edit">
								<table:editRow
									url="${ctx}/order/prochargeback/orderProChargeback/form"
									title="采购退单" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:prochargeback:orderProChargeback:del">
								<table:delRow
									url="${ctx}/order/prochargeback/orderProChargeback/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:prochargeback:orderProChargeback:import">
								<table:importExcel
									url="${ctx}/order/prochargeback/orderProChargeback/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission
								name="order:prochargeback:orderProChargeback:export">
								<table:exportExcel
									url="${ctx}/order/prochargeback/orderProChargeback/export"></table:exportExcel>
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
							<th class="sort-column fwarehose.id">退货仓库</th>
							<th class="sort-column fordertype">类型
							</th>
							<th class="sort-column fstatus">状态
								</th>
							
							<th class="sort-column fordercountprice">订单总价</th>
							<th class="sort-column forderdiscount">折扣(%)</th>
							<th class="sort-column fdiscountprice">最终金额</th>
							<th class="sort-column fsponsor.id">所属商户</th>
							<!-- 
							<th class="sort-column fothermoney">其他费用</th>
							<th class="sort-column fcountprice">商品总额</th>
							<th class="sort-column fcutsmall">抹零</th>
							<th class="sort-column forderprocurement.id">采购单</th>
							<th class="sort-column fprint">是否打印 </th>
							<th class="sort-column fdebt">欠款</th>
							 
							<th class="sort-column createBy.id">创建者</th>
							<th class="sort-column createDate">创建时间</th>
							<th class="sort-column updateBy.id">更新者</th>
							<th class="sort-column updateDate">更新时间</th>
							-->
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="orderProChargeback">
							<tr>
								<td><input type="checkbox" id="${orderProChargeback.id}"
									class="i-checks"></td>
								<td><a href="#"
									onclick="openDialogView('查看采购退单', '${ctx}/order/prochargeback/orderProChargeback/form?id=${orderProChargeback.id}','800px', '500px')">
										${orderProChargeback.fordernum} </a></td>
								<td>${orderProChargeback.fsupplier.fname}</td>
								<td>${orderProChargeback.fwarehose.fname}</td>
								<td>${fns:getDictLabel(orderProChargeback.fordertype, 'chargeback_type', '')}
								</td>
								<td>${fns:getDictLabel(orderProChargeback.fstatus, 'procurement_charge_status', '')}
								</td>
								
								<td>${orderProChargeback.fordercountprice}</td>
								<td>${orderProChargeback.forderdiscount}</td>
								<td>${orderProChargeback.fdiscountprice}</td>
								<td>${orderProChargeback.fsponsor.name}</td>
								<!-- 
								<td>${orderProChargeback.fothermoney}</td>
								<td>${orderProChargeback.fcountprice}</td>
								<td>${orderProChargeback.fcutsmall}</td>
								<td>${orderProChargeback.forderprocurement.fordernum}</td>
								<td>${fns:getDictLabel(orderProChargeback.fprint, 'print_status', '')}
								</td>
								<td>${orderProChargeback.fdebt}</td>
								 
								<td>${orderProChargeback.createBy.loginName}</td>
								<td><fmt:formatDate
										value="${orderProChargeback.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${orderProChargeback.updateBy.loginName}</td>
								<td><fmt:formatDate
										value="${orderProChargeback.updateDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								-->
								<td><shiro:hasPermission
										name="order:prochargeback:orderProChargeback:view">
										<a href="#"
											onclick="openDialogView('查看采购退单', '${ctx}/order/prochargeback/orderProChargeback/form?id=${orderProChargeback.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="order:prochargeback:orderProChargeback:edit">
										<a href="#"
											onclick="openDialog('修改采购退单', '${ctx}/order/prochargeback/orderProChargeback/form?id=${orderProChargeback.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="order:prochargeback:orderProChargeback:del">
										<a
											href="${ctx}/order/prochargeback/orderProChargeback/delete?id=${orderProChargeback.id}"
											onclick="return confirmx('确认要删除该采购退单吗？', this.href)"
											class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
											删除</a>
									</shiro:hasPermission>
									 <shiro:hasPermission
										name="order:prochargeback:orderProChargeback:edit">
										<a
											href="${ctx}/order/prochargeback/orderProChargeback/repeal?id=${orderProChargeback.id}"
											onclick="return confirmx('确认要撤销该采购退单吗？', this.href)"
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