<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>收款流水管理</title>
	<meta name="decorator" content="default"/>
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
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>收款流水列表 </h5>
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
	<form:form id="searchForm" modelAttribute="cashFlow" action="${ctx}/account/cashflow/cashFlow/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>单据编号：</span>
				<form:input path="foddnumbers" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				
			<span>交易单号：</span>
				<form:input path="ftransactionnumbers" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>交易类型：</span>
				<form:select path="ftransactiontype"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('transaction_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>支付类型：</span>
				<form:select path="fpaytype"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('pay_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>交易状态：</span>
				<form:select path="fpaystatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('transaction_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		    <br/><br/>
			<span>创建时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${cashFlow.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${cashFlow.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="account:cashflow:cashFlow:add">
				<table:addRow url="${ctx}/account/cashflow/cashFlow/form" title="收款流水"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:cashflow:cashFlow:edit">
			    <table:editRow url="${ctx}/account/cashflow/cashFlow/form" title="收款流水" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:cashflow:cashFlow:del">
				<table:delRow url="${ctx}/account/cashflow/cashFlow/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:cashflow:cashFlow:import">
				<table:importExcel url="${ctx}/account/cashflow/cashFlow/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:cashflow:cashFlow:export">
	       		<table:exportExcel url="${ctx}/account/cashflow/cashFlow/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column foddnumbers">单据编号</th>
				<th  class="sort-column ftransactionnumbers">交易单号</th>
				<th  class="sort-column ftransactiontype">交易类型</th>
				<th  class="sort-column ftransactionaccount">交易金额</th>
				<th  class="sort-column fpaytype">支付类型</th>
				<th  class="sort-column fpaystatus">交易状态</th>
				<th  class="sort-column updateDate">更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cashFlow">
			<tr>
				<td> <input type="checkbox" id="${cashFlow.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看收款流水', '${ctx}/account/cashflow/cashFlow/form?id=${cashFlow.id}','800px', '500px')">
					${cashFlow.foddnumbers}
				</a></td>
				<td>
					${cashFlow.ftransactionnumbers}
				</td>
				<td>
					${fns:getDictLabel(cashFlow.ftransactiontype, 'transaction_type', '')}
				</td>
				<td>
					${cashFlow.ftransactionaccount}
				</td>
				<td>
					${fns:getDictLabel(cashFlow.fpaytype, 'pay_type', '')}
				</td>
				<td>
					${fns:getDictLabel(cashFlow.fpaystatus, 'transaction_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${cashFlow.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="account:cashflow:cashFlow:view">
						<a href="#" onclick="openDialogView('查看收款流水', '${ctx}/account/cashflow/cashFlow/form?id=${cashFlow.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:cashflow:cashFlow:edit">
    					<a href="#" onclick="openDialog('修改收款流水', '${ctx}/account/cashflow/cashFlow/form?id=${cashFlow.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:cashflow:cashFlow:del">
						<a href="${ctx}/account/cashflow/cashFlow/delete?id=${cashFlow.id}" onclick="return confirmx('确认要删除该收款流水吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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