<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户对账管理</title>
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
		<h5>客户对账列表 </h5>
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
	<form:form id="searchForm" modelAttribute="customerAccount" action="${ctx}/account/customeraccount/customerAccount/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>客户名称：</span>
				<sys:gridselect url="${ctx}/account/customeraccount/customerAccount/selectcustomerBasic" id="customerBasic" name="customerBasic"  value="${customerAccount.customerBasic.id}"  title="选择客户名称" labelName="customerBasic.fname" 
					labelValue="${customerAccount.customerBasic.fname}" cssClass="form-control required" fieldLabels="客户名称|备注" fieldKeys="fname|remarks" searchLabel="客户姓名" searchKey="fname" ></sys:gridselect>
			<span>客户分类：</span>
				<sys:gridselect url="${ctx}/account/customeraccount/customerAccount/selectcustomerCate" id="customerCate" name="customerCate"  value="${customerAccount.customerCate.id}"  title="选择客户分类" labelName="customerCate.fname" 
					labelValue="${customerAccount.customerCate.fname}" cssClass="form-control required" fieldLabels="客户分类|备注" fieldKeys="fname|remarks" searchLabel="客户类别" searchKey="fname" ></sys:gridselect>
			<span>账目类型：</span>
				<sys:gridselect url="${ctx}/account/customeraccount/customerAccount/selectaccountType" id="accountType" name="accountType"  value="${customerAccount.accountType.id}"  title="选择账目类型" labelName="accountType.ftypename" 
					labelValue="${customerAccount.accountType.ftypename}" cssClass="form-control required" fieldLabels="账目类型|备注" fieldKeys="ftypename|remarks" searchLabel="账目类型" searchKey="ftypename" ></sys:gridselect>
			<br/><br/>
			<span>结算账户：</span>
				<sys:gridselect url="${ctx}/account/customeraccount/customerAccount/selectclearingAccount" id="clearingAccount" name="clearingAccount"  value="${customerAccount.clearingAccount.id}"  title="选择结算账户" labelName="clearingAccount.faccountname" 
					labelValue="${customerAccount.clearingAccount.faccountname}" cssClass="form-control required" fieldLabels="账户名称|备注" fieldKeys="faccountname|remarks" searchLabel="结算账户" searchKey="faccountname" ></sys:gridselect>
			<span>创建时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${customerAccount.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${customerAccount.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="account:customeraccount:customerAccount:add">
				<table:addRow url="${ctx}/account/customeraccount/customerAccount/form" title="客户对账"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customeraccount:customerAccount:edit">
			    <table:editRow url="${ctx}/account/customeraccount/customerAccount/form" title="客户对账" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customeraccount:customerAccount:del">
				<table:delRow url="${ctx}/account/customeraccount/customerAccount/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customeraccount:customerAccount:import">
				<table:importExcel url="${ctx}/account/customeraccount/customerAccount/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customeraccount:customerAccount:export">
	       		<table:exportExcel url="${ctx}/account/customeraccount/customerAccount/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column customerBasic.id">客户名称</th>
				<th  class="sort-column customerCate.id">客户分类</th>
				<th  class="sort-column foddNumbers">单据编号</th>
				<th  class="sort-column accountType.id">账目类型</th>
				<th  class="sort-column clearingAccount.id">结算账户</th>
				<th  class="sort-column fbusinessHours">业务时间</th>
				<th  class="sort-column famountReceivable">应收金额</th>
				<th  class="sort-column fpaidAmount">实收金额</th>
				<th  class="sort-column fresidualAmount">本单应收余额</th>
				<th  class="sort-column fpreferentialAmount">优惠金额</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="customerAccount">
			<tr>
				<td> <input type="checkbox" id="${customerAccount.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看客户对账', '${ctx}/account/customeraccount/customerAccount/form?id=${customerAccount.id}','800px', '500px')">
					${customerAccount.customerBasic.fname}
				</a></td>
				<td>
					${customerAccount.customerCate.fname}
				</td>
				<td>
					${customerAccount.foddNumbers}
				</td>
				<td>
					${customerAccount.accountType.ftypename}
				</td>
				<td>
					${customerAccount.clearingAccount.faccountname}
				</td>
				<td>
					<fmt:formatDate value="${customerAccount.fbusinessHours}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${customerAccount.famountReceivable}
				</td>
				<td>
					${customerAccount.fpaidAmount}
				</td>
				<td>
					${customerAccount.fresidualAmount}
				</td>
				<td>
					${customerAccount.fpreferentialAmount}
				</td>
				<td>
					${customerAccount.remarks}
				</td>
				<td>
					<shiro:hasPermission name="account:customeraccount:customerAccount:view">
						<a href="#" onclick="openDialogView('查看客户对账', '${ctx}/account/customeraccount/customerAccount/form?id=${customerAccount.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:customeraccount:customerAccount:edit">
    					<a href="#" onclick="openDialog('修改客户对账', '${ctx}/account/customeraccount/customerAccount/form?id=${customerAccount.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:customeraccount:customerAccount:del">
						<a href="${ctx}/account/customeraccount/customerAccount/delete?id=${customerAccount.id}" onclick="return confirmx('确认要删除该客户对账吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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