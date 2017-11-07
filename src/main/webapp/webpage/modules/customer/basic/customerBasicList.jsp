<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>客户基本信息管理</title>
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

	});
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>客户基本信息列表</h5>
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
						<form:form id="searchForm" modelAttribute="customerBasic"
							action="${ctx}/customer/basic/customerBasic/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>客户名称：</span>
								<form:input path="fname" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />
									<span>客户分类：</span>
								<form:select path="fcategoryId" class="form-control m-b">
									<form:option value="" label="请选择" selected="selected" />
									<c:forEach items="${customerCateList }" var="cate">
										<form:option value="${cate.id }" label="${cate.fname }" />
									</c:forEach>
									<!-- 
					<form:options items="${fns:getDictList('fIsRelate')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					 -->
								</form:select>
								<span>客户性别：</span>
								<form:select path="fsex" class="form-control m-b">
									<form:option value="" label="请选择" />
									<form:option value="1" label="男" />
									<form:option value="2" label="女" />
								</form:select>
								<span>客户手机：</span>
								<form:input path="fcellphone" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" /><br/><br/>
								<span>客户欠款：</span>
								<form:input path="fdebt" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" />
								<span>客户状态：</span>
								<form:select path="fstatus" class="form-control m-b">
									<form:option value="" label="请选择" />
									<form:option value="1" label="启用" />
									<form:option value="2" label="失效" />
								</form:select>
								<span>客户折扣：</span>
								<form:input path="fdiscount" htmlEscape="false" maxlength="64"
									class=" form-control input-sm" /><br/><br/>

								<span>创建时间：</span> <input id="beginCreateDate"
									name="beginCreateDate" type="text" maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${customerBasic.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
								- <input id="endCreateDate" name="endCreateDate" type="text"
									maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${customerBasic.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="customer:basic:customerBasic:add">
								<table:addRow url="${ctx}/customer/basic/customerBasic/form"
									title="客户基本信息"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:basic:customerBasic:edit">
								<table:editRow url="${ctx}/customer/basic/customerBasic/form"
									title="客户基本信息" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:basic:customerBasic:del">
								<table:delRow
									url="${ctx}/customer/basic/customerBasic/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:basic:customerBasic:import">
								<table:importExcel
									url="${ctx}/customer/basic/customerBasic/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="customer:basic:customerBasic:export">
								<table:exportExcel
									url="${ctx}/customer/basic/customerBasic/export"></table:exportExcel>
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
							<th class="sort-column fcategoryId">分类</th>
							<th class="sort-column fname">名称</th>
							<th class="sort-column fsex">性别</th>
							
							
							<th class="sort-column fcellphone">手机</th>
							
							<th class="sort-column fstatus">状态</th>
							<th class="sort-column createBy.id">创建者</th>
							<th class="sort-column createDate">创建时间</th>
							<th class="sort-column updateBy.id">更新者</th>
							<th class="sort-column updateDate">更新时间</th>


							<!-- 
				<th class="sort-column flinkman">联系人</th>
				<th class="sort-column fphone">电话</th>
				<th class="sort-column femail">邮箱</th>
				<th class="sort-column fdebt">欠款</th>
				<th class="sort-column fisrelate">是否关联</th>
				<th  class="sort-column remarks">备注</th>
				<th  class="sort-column ffax">传真</th>
				<th  class="sort-column ffddress">客户地址</th>
				<th  class="sort-column fbirthday">客户生日</th>
				<th  class="sort-column fbankaccount">银行账户</th>
				<th  class="sort-column fbankaccountNum">银行账号</th>
				<th  class="sort-column fbank">开户行</th>
				<th  class="sort-column fbankaccountname">开户人姓名</th>
				<th  class="sort-column fbankaccountpersonId">开户人身份证</th>
				<th  class="sort-column fbankphone">开户预留手机号</th>
				<th  class="sort-column fsort">排序</th>
				<th  class="sort-column fshipmenttype">发货方式</th> 
				<th  class="sort-column fcustomerinfo">关联账号信息ID</th>
				<th  class="sort-column fpaytype">支付方式</th>
				-->
							<th class="sort-column fdiscount">折扣</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="customerBasic">
							<tr>
								<td><input type="checkbox" id="${customerBasic.id}"
									class="i-checks"></td>
								<td><a href="#"
									onclick="openDialogView('查看分类信息', '${ctx}/customer/category/customerCate/form?id=${customerBasic.id}','800px', '500px')">
										${customerBasic.fcatename} </a></td>
								<td><a href="#"
									onclick="openDialogView('查看客户基本信息', '${ctx}/customer/basic/customerBasic/view?id=${customerBasic.id}','800px', '500px')">
										${customerBasic.fname} </a></td>
								<td><c:if test="${customerBasic.fsex == 1 }">男</c:if> <c:if
										test="${customerBasic.fsex == 2 }">女</c:if></td>
								
								
								<td>${customerBasic.fcellphone}</td>
								
								

								<td><c:if test="${customerBasic.fstatus == 1 }">启用</c:if> <c:if
										test="${customerBasic.fstatus == 2 }">失效</c:if></td>
								<td>${customerBasic.createBy.loginName}</td>
								<td><fmt:formatDate value="${customerBasic.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${customerBasic.updateBy.loginName}</td>
								<td><fmt:formatDate value="${customerBasic.updateDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>

								<td>${customerBasic.fdiscount}</td>
								<!--
				<td>${customerBasic.flinkman}</td>
				<td>${customerBasic.fphone}</td>
				<td>${customerBasic.femail}</td>
				<td>${customerBasic.fdebt}</td>
				<td><c:if test="${customerBasic.fisrelate == 0 }">无</c:if>
									<c:if test="${customerBasic.fisrelate == 1 }">
										<a href="#"
											onclick="openDialogView('查看客户关联信息', '${ctx}/customer/info/customerInfo/form?id=${customerBasic.fcustomerinfo}','800px', '500px')">
											有 </a>
									</c:if></td>
				<td>
					${customerBasic.remarks}
				</td> 
				<td>
					${customerBasic.fbirthday}
				</td>
				
				<td>
					${customerBasic.ffax}
				</td>
				<td>
					${customerBasic.fbankaccount}
				</td>
				<td>
					${customerBasic.fbankaccountnum}
				</td>
				<td>
					${customerBasic.fbank}
				</td>
				<td>
					${customerBasic.fbankaccountname}
				</td>
				<td>
					${customerBasic.fbankaccountpersonid}
				</td>
				<td>
					${customerBasic.fbankphone}
				</td>
				
				<td>
					${customerBasic.fsort}
				</td>
				<td>
					${customerBasic.faddress}
				</td>
				<td>
					${fns:getDictLabel(customerBasic.fshipmenttype, 'fshipmenttype', '')}
				</td>
				
				<td>
					${customerBasic.fcustomerinfo}
				</td>
				<td>
					${customerBasic.fpaytype}
				</td>
			
				 -->
								<td><shiro:hasPermission
										name="customer:basic:customerBasic:view">
										<a href="#"
											onclick="openDialogView('查看客户基本信息', '${ctx}/customer/basic/customerBasic/view?id=${customerBasic.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> <shiro:hasPermission name="customer:basic:customerBasic:edit">
										<a href="#"
											onclick="openDialog('修改客户基本信息', '${ctx}/customer/basic/customerBasic/form?id=${customerBasic.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission name="customer:basic:customerBasic:del">
										<a
											href="${ctx}/customer/basic/customerBasic/delete?id=${customerBasic.id}"
											onclick="return confirmx('确认要删除该客户基本信息吗？', this.href)"
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