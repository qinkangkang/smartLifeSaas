<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品分类的管理管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, ids = [], rootIds = [];
			for (var i=0; i<data.length; i++){
				ids.push(data[i].id);
			}
			ids = ',' + ids.join(',') + ',';
			for (var i=0; i<data.length; i++){
				if (ids.indexOf(','+data[i].parentId+',') == -1){
					if ((','+rootIds.join(',')+',').indexOf(','+data[i].parentId+',') == -1){
						rootIds.push(data[i].parentId);
					}
				}
			}
			for (var i=0; i<rootIds.length; i++){
				addRow("#treeTableList", tpl, data, rootIds[i], true);
			}
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							fstatus: getDictLabel(${fns:toJson(fns:getDictList('sys_status'))}, row.fstatus),
						blank123:0}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
		
		function refresh(){//刷新
			
			window.location="${ctx}/goods/categorys/categorys/";
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5>商品分类的管理列表 </h5>
			<div class="ibox-tools">
				<a class="collapse-link">
					<i class="fa fa-chevron-up"></i>
				</a>
				<a class="dropdown-toggle" data-toggle="dropdown" href="form_basic.html#">
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
	<form:form id="searchForm" modelAttribute="categorys" action="${ctx}/goods/categorys/categorys/" method="post" class="form-inline">
		<div class="form-group">
				<label>类别名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;
				<label>类别启用状态：</label>
				<form:select path="fstatus" class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sys_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="goods:categorys:categorys:add">
				<table:addRow url="${ctx}/goods/categorys/categorys/form" title="商品分类的管理"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th>类别名称</th>
				<th>启用状态</th>
				<th>备注信息</th>
				<shiro:hasPermission name="goods:categorys:categorys:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
		
		<!-- <td>
				{{row.createBy.id}}
			</td>
			<td>
				{{row.updateBy.id}}
			</td> 
			<td>
				{{row.updateDate}}
			</td>-->
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a  href="#" onclick="openDialogView('查看商品分类的管理', '${ctx}/goods/categorys/categorys/form?id={{row.id}}','800px', '500px')">
				{{row.name}}
			</a></td>
						
			
			<td>
				{{dict.fstatus}}
			</td>
			<td>
				{{row.remarks}}
			</td>
			
			<td>
			<shiro:hasPermission name="goods:categorys:categorys:view">
				<a href="#" onclick="openDialogView('查看商品分类的管理', '${ctx}/goods/categorys/categorys/form?id={{row.id}}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>  查看</a>
				</shiro:hasPermission>
			<shiro:hasPermission name="goods:categorys:categorys:edit">
   				<a href="#" onclick="openDialog('修改商品分类的管理', '${ctx}/goods/categorys/categorys/form?id={{row.id}}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
   			</shiro:hasPermission>
   			<shiro:hasPermission name="goods:categorys:categorys:del">
				<a href="${ctx}/goods/categorys/categorys/delete?id={{row.id}}" onclick="return confirmx('确认要删除该商品分类的管理及所有子商品分类的管理吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
			</shiro:hasPermission>
   			<shiro:hasPermission name="goods:categorys:categorys:add">
				<a href="#" onclick="openDialog('添加下级商品分类的管理', '${ctx}/goods/categorys/categorys/form?parent.id={{row.id}}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加下级商品分类的管理</a>
			</shiro:hasPermission>
			</td>
		</tr>
	</script>
</body>
</html>