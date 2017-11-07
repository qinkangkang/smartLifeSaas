<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>商户管理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		laydate({
			elem : '#beginCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});
		laydate({
			elem : '#endCreateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});

		laydate({
			elem : '#beginUpdateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});
		laydate({
			elem : '#endUpdateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});

	});
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>商户管理列表</h5>
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
						<form:form id="searchForm" modelAttribute="merchant"
							action="${ctx}/merchant/management/merchant/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
							
								<span>所属区域：</span>
								<sys:gridselect
									url="${ctx}/merchant/management/merchant/selectarea" id="area"
									name="area" value="${merchant.area.id}" title="选择所属区域"
									labelName="area.name" labelValue="${merchant.area.name}"
									cssClass="form-control required" fieldLabels="所属区域|备注"
									fieldKeys="name|remarks" searchLabel="区域名" searchKey="name"></sys:gridselect>
								
								<span>商户名称：</span>
								<form:input path="fname" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />
								
								<span>负责人：</span>
								<form:input path="fmaster" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />
								<span>手机：</span>
								<form:input path="cellphone" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" /><br/><br/>
								<span>创建时间：</span> <input id="beginCreateDate"
									name="beginCreateDate" type="text" maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${merchant.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
								- <input id="endCreateDate" name="endCreateDate" type="text"
									maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${merchant.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
									
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>更新时间：</span> <input id="beginUpdateDate"
									name="beginUpdateDate" type="text" maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${merchant.beginUpdateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
								- <input id="endUpdateDate" name="endUpdateDate" type="text"
									maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${merchant.endUpdateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="merchant:management:merchant:add">
								<table:addRow url="${ctx}/merchant/management/merchant/form"
									title="商户管理"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="merchant:management:merchant:edit">
								<table:editRow url="${ctx}/merchant/management/merchant/form"
									title="商户管理" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="merchant:management:merchant:del">
								<table:delRow
									url="${ctx}/merchant/management/merchant/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="merchant:management:merchant:import">
								<table:importExcel
									url="${ctx}/merchant/management/merchant/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="merchant:management:merchant:export">
								<table:exportExcel
									url="${ctx}/merchant/management/merchant/export"></table:exportExcel>
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
							<th class="sort-column fname">商户名称</th>
							<th class="sort-column area.id">所属区域</th>
							<th class="sort-column code">区域编号</th>
							<th class="sort-column fmaster">负责人</th>
							<th class="sort-column cellphone">手机</th>
							<th class="sort-column femail">邮箱</th>
							<th class="sort-column fsort">排序</th>
							<th class="sort-column fstatus">是否启用</th>
							<th class="sort-column remarks">备注信息</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="merchant">
							<tr>
								<td><input type="checkbox" id="${merchant.id}"
									class="i-checks"></td>
								<td><a href="#"
									onclick="openDialogView('查看商户管理', '${ctx}/merchant/management/merchant/form?id=${merchant.id}','800px', '500px')">
										${merchant.fname} </a></td>
								<td>${merchant.area.name}</td>
								<td>${merchant.code}</td>
								<td>${merchant.fmaster}</td>
								<td>${merchant.cellphone}</td>
								<td>${merchant.femail}</td>
								<td>${merchant.fsort}</td>
								<td>
								<c:if test="${merchant.fstatus !=2}">启用</c:if>
								<c:if test="${merchant.fstatus ==2}">禁用</c:if>
								</td>
								<td>${merchant.remarks}</td>
								<td><shiro:hasPermission
										name="merchant:management:merchant:view">
										<a href="#"
											onclick="openDialogView('查看商户管理', '${ctx}/merchant/management/merchant/form?id=${merchant.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> <shiro:hasPermission name="merchant:management:merchant:edit">
										<a href="#"
											onclick="openDialog('修改商户管理', '${ctx}/merchant/management/merchant/form?id=${merchant.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission name="merchant:management:merchant:del">
										<a
											href="${ctx}/merchant/management/merchant/delete?id=${merchant.id}"
											onclick="return confirmx('确认要删除该商户管理吗？', this.href)"
											class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
											删除</a>
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