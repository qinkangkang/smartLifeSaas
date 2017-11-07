<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>资金流水账管理</title>
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
		<h5>资金流水账列表 </h5>
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
	<form:form id="searchForm" modelAttribute="capitalAccount" action="${ctx}/account/capitalaccount/capitalAccount/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>单据编号：</span>
				<form:input path="foddNumbers" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>账目类型：</span>
				<sys:gridselect url="${ctx}/account/capitalaccount/capitalAccount/selectaccountType" id="accountType" name="accountType"  value="${capitalAccount.accountType.id}"  title="选择账目类型" labelName="accountType.ftypename" 
					labelValue="${capitalAccount.accountType.ftypename}" cssClass="form-control required" fieldLabels="账目类型|备注" fieldKeys="ftypename|remarks" searchLabel="账目类型" searchKey="ftypename" ></sys:gridselect>
			<span>结算账户：</span>
				<sys:gridselect url="${ctx}/account/capitalaccount/capitalAccount/selectclearingAccount" id="clearingAccount" name="clearingAccount"  value="${capitalAccount.clearingAccount.id}"  title="选择结算账户" labelName="clearingAccount.faccountname" 
					labelValue="${capitalAccount.clearingAccount.faccountname}" cssClass="form-control required" fieldLabels="账户名称|备注" fieldKeys="faccountname|remarks" searchLabel="结算账户" searchKey="faccountname" ></sys:gridselect>
			<span>收入/支出：</span>
				<form:select path="fexpenditureflag"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('fund_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="account:capitalaccount:capitalAccount:add">
				<table:addRow url="${ctx}/account/capitalaccount/capitalAccount/form" title="资金流水账"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:capitalaccount:capitalAccount:edit">
			    <table:editRow url="${ctx}/account/capitalaccount/capitalAccount/form" title="资金流水账" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:capitalaccount:capitalAccount:del">
				<table:delRow url="${ctx}/account/capitalaccount/capitalAccount/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:capitalaccount:capitalAccount:import">
				<table:importExcel url="${ctx}/account/capitalaccount/capitalAccount/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:capitalaccount:capitalAccount:export">
	       		<table:exportExcel url="${ctx}/account/capitalaccount/capitalAccount/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column foddNumbers">单据编号</th>
				<th  class="sort-column fbusinessHours">业务时间</th>
				<th  class="sort-column accountType.id">账目类型</th>
				<th  class="sort-column clearingAccount.id">结算账户</th>
				<th  class="sort-column ftrader">交易人</th>
				<th  class="sort-column fincome">收入</th>
				<th  class="sort-column fexpenditure">支出</th>
				<th  class="sort-column fprofit">盈利</th>
				<th  class="sort-column faccountBalance">账号余额</th>
				<th  class="sort-column finitialamount">期初金额</th>
				<th  class="sort-column fexpenditureflag">收入/支出</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="capitalAccount">
			<tr>
				<td> <input type="checkbox" id="${capitalAccount.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看目标信息', '<c:if test="${fn:startsWith(capitalAccount.foddNumbers, 'CG')}">${ctx}/order/procurement/orderProcurement/formbyordernum?numid=${capitalAccount.foddNumbers}</c:if><c:if test="${fn:startsWith(capitalAccount.foddNumbers, 'CT')}">${ctx}/order/prochargeback/orderProChargeback/formbyordernum?numid=${capitalAccount.foddNumbers}</c:if><c:if test="${fn:startsWith(capitalAccount.foddNumbers, 'FK')}">${ctx}/account/supplieraccount/supplierAccount/formbyordernum?numid=${capitalAccount.foddNumbers}</c:if><c:if test="${fn:startsWith(capitalAccount.foddNumbers, 'XS')}">${ctx}/market/ordermarket/orderMarket/formbyordernum?numid=${capitalAccount.foddNumbers}</c:if><c:if test="${fn:startsWith(capitalAccount.foddNumbers, 'XT')}">${ctx}/market/ordermarketchargeback/orderMarketCha/formbyordernum?numid=${capitalAccount.foddNumbers}</c:if>','800px', '500px')">
					${capitalAccount.foddNumbers}
				</a></td><!-- /account/capitalaccount/capitalAccount/form -->
				
				<td>
					<fmt:formatDate value="${capitalAccount.fbusinessHours}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${capitalAccount.accountType.ftypename}
				</td>
				<td>
					${capitalAccount.clearingAccount.faccountname}
				</td>
				<td>
					${capitalAccount.ftrader.loginName}
				</td>
				<td>
					${capitalAccount.fincome}
				</td>
				<td>
					${capitalAccount.fexpenditure}
				</td>
				<td>
					${capitalAccount.fprofit}
				</td>
				<td>
					${capitalAccount.faccountBalance}
				</td>
				<td>
					${capitalAccount.finitialamount}
				</td>
				<td>
					${fns:getDictLabel(capitalAccount.fexpenditureflag, 'fund_status', '')}
				</td>
				<td>
					${capitalAccount.remarks}
				</td>
				<td>
					<shiro:hasPermission name="account:capitalaccount:capitalAccount:view">
						<a href="#" onclick="openDialogView('查看资金流水账', '${ctx}/account/capitalaccount/capitalAccount/form?id=${capitalAccount.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:capitalaccount:capitalAccount:edit">
    					<a href="#" onclick="openDialog('修改资金流水账', '${ctx}/account/capitalaccount/capitalAccount/form?id=${capitalAccount.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:capitalaccount:capitalAccount:del">
						<a href="${ctx}/account/capitalaccount/capitalAccount/delete?id=${capitalAccount.id}" onclick="return confirmx('确认要删除该资金流水账吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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