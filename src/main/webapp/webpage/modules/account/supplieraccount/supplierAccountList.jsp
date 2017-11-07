<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>供应商对账/付款管理</title>
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
		<h5>供应商对账/付款列表 </h5>
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
	<form:form id="searchForm" modelAttribute="supplierAccount" action="${ctx}/account/supplieraccount/supplierAccount/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>供应商名称：</span>
				<sys:gridselect url="${ctx}/account/supplieraccount/supplierAccount/selectsupplierBasic" id="supplierBasic" name="supplierBasic"  value="${supplierAccount.supplierBasic.id}"  title="选择供应商名称" labelName="supplierBasic.fname" 
					labelValue="${supplierAccount.supplierBasic.fname}" cssClass="form-control required" fieldLabels="供应商|备注" fieldKeys="fname|remarks" searchLabel="供应商名称" searchKey="fname" ></sys:gridselect>
			
			<span>账目类型：</span>
				<sys:gridselect url="${ctx}/account/supplieraccount/supplierAccount/selectaccountType" id="accountType" name="accountType"  value="${supplierAccount.accountType.id}"  title="选择账目类型" labelName="accountType.ftypename" 
					labelValue="${supplierAccount.accountType.ftypename}" cssClass="form-control required" fieldLabels="账目类型|备注" fieldKeys="ftypename|remarks" searchLabel="账目类型" searchKey="ftypename" ></sys:gridselect>
			<span>结算账户：</span>
				<sys:gridselect url="${ctx}/account/supplieraccount/supplierAccount/selectclearingAccount" id="clearingAccount" name="clearingAccount"  value="${supplierAccount.clearingAccount.id}"  title="选择结算账户" labelName="clearingAccount.faccountname" 
					labelValue="${supplierAccount.clearingAccount.faccountname}" cssClass="form-control required" fieldLabels="账户名称|备注" fieldKeys="faccountname|remarks" searchLabel="结算账户" searchKey="faccountname" ></sys:gridselect>
			<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<span>单据编号：</span>
				<form:input path="foddNumbers" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>创建时间：</span>
				<input id="beginCreateDate" name="beginCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${supplierAccount.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endCreateDate" name="endCreateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${supplierAccount.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="account:supplieraccount:supplierAccount:add">
				<table:addRow url="${ctx}/account/supplieraccount/supplierAccount/form" title="供应商对账/付款"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:supplieraccount:supplierAccount:edit">
			    <table:editRow url="${ctx}/account/supplieraccount/supplierAccount/form" title="供应商对账/付款" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:supplieraccount:supplierAccount:del">
				<table:delRow url="${ctx}/account/supplieraccount/supplierAccount/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:supplieraccount:supplierAccount:import">
				<table:importExcel url="${ctx}/account/supplieraccount/supplierAccount/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:supplieraccount:supplierAccount:export">
	       		<table:exportExcel url="${ctx}/account/supplieraccount/supplierAccount/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column supplierBasic.id">供应商名称</th>
				<th  class="sort-column foddNumbers">单据编号</th>
				<th  class="sort-column accountType.id">账目类型</th>
				<th  class="sort-column clearingAccount.id">结算账户</th>
				<th  class="sort-column fbusinessHours">业务时间</th>
				<th  class="sort-column famountPay">应付金额</th>
				<th  class="sort-column fpayAmount">实付金额</th>
				<th  class="sort-column fsolehandlingAmount">本单应付金额</th>
				<th  class="sort-column fpreferentialAmount">优惠金额</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="supplierAccount">
			<tr>
				<td> <input type="checkbox" id="${supplierAccount.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看供应商对账/付款', '${ctx}/account/supplieraccount/supplierAccount/form?id=${supplierAccount.id}','800px', '500px')">
					${supplierAccount.supplierBasic.fname}
				</a></td>
				<td>
					${supplierAccount.foddNumbers}
				</td>
				<td>
					${supplierAccount.accountType.ftypename}
				</td>
				<td>
					${supplierAccount.clearingAccount.faccountname}
				</td>
				<td>
					<fmt:formatDate value="${supplierAccount.fbusinessHours}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${supplierAccount.famountPay}
				</td>
				<td>
					${supplierAccount.fpayAmount}
				</td>
				<td>
					${supplierAccount.fsolehandlingAmount}
				</td>
				<td>
					${supplierAccount.fpreferentialAmount}
				</td>
				<td>
					${supplierAccount.remarks}
				</td>
				<td>
					<shiro:hasPermission name="account:supplieraccount:supplierAccount:view">
						<a href="#" onclick="openDialogView('查看供应商对账/付款', '${ctx}/account/supplieraccount/supplierAccount/form?id=${supplierAccount.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:supplieraccount:supplierAccount:edit">
    					<a href="#" onclick="openDialog('修改供应商对账/付款', '${ctx}/account/supplieraccount/supplierAccount/form?id=${supplierAccount.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:supplieraccount:supplierAccount:del">
						<a href="${ctx}/account/supplieraccount/supplierAccount/delete?id=${supplierAccount.id}" onclick="return confirmx('确认要删除该供应商对账/付款吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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