<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>客户分类管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		laydate({
			elem : '#createDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});
		laydate({
			elem : '#updateDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});
	});
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>客户分类列表</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i
						class="fa fa-wrench"></i>
					</a>
					<!--  
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			-->
					<a class="close-link"> <i class="fa fa-times"></i>
					</a>
				</div>
			</div>

			<div class="ibox-content">
				<sys:message content="${message}" />

				<!--查询条件-->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="customerCate"
							action="${ctx}/customer/category/customerCate/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<!-- 
			<span>主键：</span>
				<form:input path="id" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>创建者：</span>
				<form:input path="createBy.id" htmlEscape="false"  class=" form-control input-sm"/>
			<span>创建时间：</span>
				<input id="createDate" name="createDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${customerCate.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>更新者：</span>
				<form:input path="updateBy.id" htmlEscape="false"  class=" form-control input-sm"/>
			<span>更新时间：</span>
				<input id="updateDate" name="updateDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${customerCate.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 -->
								<span>分类名称：</span>
								<form:input path="fname" htmlEscape="false" maxlength="255"
									class=" form-control input-sm" />
								<span>折扣：</span>
								<form:input path="fdiscount" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />
								<span>客户分类状态：</span>
								<form:select path="fstatus" class="form-control m-b">
									<form:option value="" label="请选择" selected="selected" />
									<form:option value="1" label="启用" />
									<form:option value="2" label="失效" />
									<!--  
					<form:options items="${fns:getDictList('select')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					-->
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
							<shiro:hasPermission name="customer:customerCate:add">
								<table:addRow url="${ctx}/customer/category/customerCate/form"
									title="客户分类"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:customerCate:edit">
								<table:editRow url="${ctx}/customer/category/customerCate/form"
									title="客户分类" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:customerCate:del">
								<table:delRow
									url="${ctx}/customer/category/customerCate/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:customerCate:import">
								<table:importExcel
									url="${ctx}/customer/category/customerCate/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:customerCate:export">
								<table:exportExcel
									url="${ctx}/customer/category/customerCate/export"></table:exportExcel>
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
							<th class="sort-column fname">分类名称</th>
							<th class="sort-column fdiscount">折扣</th>
							<th class="sort-column fstatus">客户分类状态</th>
							<th class="sort-column remarks">备注信息</th>
							<th class="sort-column createBy.id">创建者</th>
							<th class="sort-column createDate">创建时间</th>
							<th class="sort-column updateBy.id">更新者</th>
							<th class="sort-column updateDate">更新时间</th>
							<!--  
				<th  class="sort-column delFlag">逻辑删除标记（0：显示；1：隐藏）</th>
				-->
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="customerCate">
							<tr>
								<td><input type="checkbox" id="${customerCate.id}"
									class="i-checks"></td>
								<td><a href="#"
									onclick="openDialogView('查看客户分类', '${ctx}/customer/category/customerCate/form?id=${customerCate.id}','800px', '500px')">
										${customerCate.fname} </a></td>
								<td>${customerCate.fdiscount}</td>
								<td>
									<!-- 
					${fns:getDictLabel(customerCate.fstatus, 'select', '')}
					 --> <c:if test="${customerCate.fstatus == 1}">启用</c:if> <c:if
										test="${customerCate.fstatus == 2}">禁用</c:if>

								</td>
								<td>${customerCate.remarks}</td>
								<td>${customerCate.createBy.loginName}</td>
								<td><fmt:formatDate value="${customerCate.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${customerCate.updateBy.loginName}</td>
								<td><fmt:formatDate value="${customerCate.updateDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<!-- 
				<td>
					${fns:getDictLabel(customerCate.delFlag, 'del_flag', '')}
				</td>
				 -->
								<td><shiro:hasPermission name="customer:customerCate:view">
										<a href="#"
											onclick="openDialogView('查看客户分类', '${ctx}/customer/category/customerCate/form?id=${customerCate.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> <shiro:hasPermission name="customer:customerCate:edit">
										<a href="#"
											onclick="openDialog('修改客户分类', '${ctx}/customer/category/customerCate/form?id=${customerCate.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission name="customer:customerCate:del">
										<a
											href="${ctx}/customer/category/customerCate/delete?id=${customerCate.id}"
											onclick="return confirmx('确认要删除该客户分类吗？', this.href)"
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