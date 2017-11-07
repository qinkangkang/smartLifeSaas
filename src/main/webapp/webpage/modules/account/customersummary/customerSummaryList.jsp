<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户基本信息管理</title>
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
		<h5>客户基本信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="customerSummary" action="${ctx}/account/customersummary/customerSummary/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>客户分类：</span>
				<sys:gridselect url="${ctx}/account/customersummary/customerSummary/selectcustomerCate" id="customerCate" name="customerCate"  value="${customerSummary.customerCate.id}"  title="选择客户分类" labelName="customerCate.fname" 
					labelValue="${customerSummary.customerCate.fname}" cssClass="form-control required" fieldLabels="客户分类|备注" fieldKeys="fname|remarks" searchLabel="客户类别" searchKey="fname" ></sys:gridselect>
			<span>联系人：</span>
				<form:input path="flinkman" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/><br/><br/>
			<span>客户名称：</span>
				<form:input path="fname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>手机：</span>
				<form:input path="fcellphone" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			
			
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="account:customersummary:customerSummary:add">
				<table:addRow url="${ctx}/account/customersummary/customerSummary/form" title="客户基本信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customersummary:customerSummary:edit">
			    <table:editRow url="${ctx}/account/customersummary/customerSummary/form" title="客户基本信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customersummary:customerSummary:del">
				<table:delRow url="${ctx}/account/customersummary/customerSummary/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customersummary:customerSummary:import">
				<table:importExcel url="${ctx}/account/customersummary/customerSummary/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:customersummary:customerSummary:export">
	       		<table:exportExcel url="${ctx}/account/customersummary/customerSummary/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column fname">客户名称</th>
				<th  class="sort-column customerCate.id">客户分类</th>
				<th  class="sort-column flinkman">联系人</th>
				<th  class="sort-column fcellphone">手机</th>
				<th  class="sort-column fdebt">客户欠款</th>
				<th  class="sort-column office.id">所属门店</th>
				<!-- th>操作</th> -->
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="customerSummary">
			<tr>
				<td> <input type="checkbox" id="${customerSummary.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看客户基本信息', '${ctx}/account/customersummary/customerSummary/form?id=${customerSummary.id}','800px', '500px')">
					${customerSummary.fname}
				</a></td>
				<td>
					${customerSummary.customerCate.fname}
				</td>
				<td>
					${customerSummary.flinkman}
				</td>
				<td>
					${customerSummary.fcellphone}
				</td>
				<td>
					${customerSummary.fdebt}
				</td>
				<td>
					${customerSummary.office.name}
				</td>
				<!--  td>
					<shiro:hasPermission name="account:customersummary:customerSummary:view">
						<a href="#" onclick="openDialogView('查看客户基本信息', '${ctx}/account/customersummary/customerSummary/form?id=${customerSummary.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:customersummary:customerSummary:edit">
    					<a href="#" onclick="openDialog('修改客户基本信息', '${ctx}/account/customersummary/customerSummary/form?id=${customerSummary.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:customersummary:customerSummary:del">
						<a href="${ctx}/account/customersummary/customerSummary/delete?id=${customerSummary.id}" onclick="return confirmx('确认要删除该客户基本信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
				-->
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