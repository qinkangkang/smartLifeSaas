<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品管理管理</title>
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
		<h5>商品管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="goodsSpu" action="${ctx}/goods/spu/goodsSpu/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>商品单位：</span>
				<sys:gridselect url="${ctx}/goods/spu/goodsSpu/selectgoodsUnit" id="goodsUnit" name="goodsUnit"  value="${goodsSpu.goodsUnit.id}"  title="选择商品单位" labelName="goodsUnit.funitname" 
					labelValue="${goodsSpu.goodsUnit.funitname}" cssClass="form-control required" fieldLabels="商品单位|备注" fieldKeys="funitname|remarks" searchLabel="商品单位" searchKey="funitname" ></sys:gridselect>
			&nbsp;&nbsp;&nbsp;
			<span>商品品牌：</span>
				<sys:gridselect url="${ctx}/goods/spu/goodsSpu/selectbrand" id="brand" name="brand"  value="${goodsSpu.brand.id}"  title="选择商品所属品牌" labelName="brand.fbrandname" 
					labelValue="${goodsSpu.brand.fbrandname}" cssClass="form-control required" fieldLabels="商品品牌|备注" fieldKeys="fbrandname|remarks" searchLabel="商品品牌" searchKey="fbrandname" ></sys:gridselect>
			&nbsp;&nbsp;&nbsp;
			<span>商品类别：</span>
				<sys:gridselect url="${ctx}/goods/spu/goodsSpu/selectcategorys" id="categorys" name="categorys"  value="${goodsSpu.categorys.id}"  title="选择商品所属分类" labelName="categorys.name" 
					labelValue="${goodsSpu.categorys.name}" cssClass="form-control required" fieldLabels="商品类别|备注" fieldKeys="name|remarks" searchLabel="商品类别" searchKey="name" ></sys:gridselect>
			<br></br>
			<span>商品名称：</span>
				<form:input path="fgoodsname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			&nbsp;&nbsp;&nbsp;	
			
			<span>商品状态：</span>
				<form:select path="fstatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sys_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				&nbsp;&nbsp;&nbsp;	
			
			<span>季节：</span>
				<form:select path="fseason"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('season_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="goods:spu:goodsSpu:add">
				<table:addRow url="${ctx}/goods/spu/goodsSpu/form" title="商品管理"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:spu:goodsSpu:edit">
			    <table:editRow url="${ctx}/goods/spu/goodsSpu/form" title="商品管理" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:spu:goodsSpu:del">
				<table:delRow url="${ctx}/goods/spu/goodsSpu/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:spu:goodsSpu:import">
				<table:importExcel url="${ctx}/goods/spu/goodsSpu/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:spu:goodsSpu:export">
	       		<table:exportExcel url="${ctx}/goods/spu/goodsSpu/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column fgoodsname">商品名称</th>
				<th  class="sort-column fbuyprice">采购价</th>
				<th  class="sort-column goodsUnit.id">商品单位</th>
				<th  class="sort-column brand.id">商品品牌</th>
				<th  class="sort-column categorys.id">商品类别</th>
				<th  class="sort-column feason">季节</th>
				<th  class="sort-column fonsalestime">上架时间</th>
				<th  class="sort-column foffsalestime">下架时间</th>
				<th  class="sort-column fstatus">启用状态</th>
				<th  class="sort-column createBy.id">创建人</th>
				<th  class="sort-column updatateBy.id">更新人</th>
				<th  class="sort-column remarks">备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="goodsSpu">
			<tr>
				<td> <input type="checkbox" id="${goodsSpu.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看商品管理', '${ctx}/goods/spu/goodsSpu/form?id=${goodsSpu.id}','800px', '500px')">
					${goodsSpu.fgoodsname}
				</a></td>
				
				<td>
					${goodsSpu.fbuyprice}
				</td>
				<td>
					${goodsSpu.goodsUnit.funitname}
				</td>
				<td>
					${goodsSpu.brand.fbrandname}
				</td>
				<td>
					${goodsSpu.categorys.name}

				</td>
				
				<td>
					${fns:getDictLabel(goodsSpu.fseason, 'season_status', '')}
				</td>
				
				<td>
					<fmt:formatDate value="${goodsSpu.fonsalestime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${goodsSpu.foffsalestime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(goodsSpu.fstatus, 'sys_status', '')}
				</td>
				<td>
					${goodsSpu.createBy.name}
				</td>
				<td>
					${goodsSpu.updateBy.name}
				</td>
				<td>
					${goodsSpu.remarks}
				</td>
				<td>
					<shiro:hasPermission name="goods:spu:goodsSpu:view">
						<a href="#" onclick="openDialogView('查看商品管理', '${ctx}/goods/spu/goodsSpu/form?id=${goodsSpu.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="goods:spu:goodsSpu:edit">
    					<a href="#" onclick="openDialog('修改商品管理', '${ctx}/goods/spu/goodsSpu/form?id=${goodsSpu.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="goods:spu:goodsSpu:del">
						<a href="${ctx}/goods/spu/goodsSpu/delete?id=${goodsSpu.id}" onclick="return confirmx('确认要删除该商品管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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