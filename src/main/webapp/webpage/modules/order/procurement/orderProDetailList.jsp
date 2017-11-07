<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购单详情管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		
		validateForm = $("#inputForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
		
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
<script type="text/javascript">
	function checkForm(index){
	
　　	var nownum = document.getElementById("nownum"+index).value;
　　	var freturnnum = document.getElementById("freturnnum"+index).value;
	var reg = new RegExp("^[0-9]*$");
	
	if(!/^[0-9]*$/.test(freturnnum)){
 		alert("请输入数字!");
 		return false;
	}

　　	if(parseInt(freturnnum) >parseInt(nownum)){
　　　　	alert("退货数量不能大于仓库可用数量!");
　　　　	return false;
　　}
} 	
</script>

<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。

		  if(validateForm.form()){
			  $("#inputForm").submit();		  
			  return false;
		  }
	
		  return false;
		}
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>采购退单商品选择</h5>
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
					<font color="#999999">注意：供应商和仓库为必选条件</font>
						<form:form id="searchForm" modelAttribute="orderProDetail"
							action="${ctx}/order/procurement/orderProDetail/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>所属仓库：</span>
								<sys:gridselect
									url="${ctx}/order/procurement/orderProDetail/selectwarehouse"
									id="warehouse" name="warehouse"
									value="${orderProDetail.warehouse.id}" title="选择仓库"
									labelName="warehouse.fname"
									labelValue="${orderProDetail.warehouse.fname}"
									cssClass="form-control required" fieldLabels="仓库名|备注"
									fieldKeys="fname|remarks" searchLabel="所属仓库" searchKey="fname"></sys:gridselect>
								<span>供应商：</span>
								<sys:gridselect
									url="${ctx}/order/procurement/orderProDetail/selectsupplier"
									id="supplierBasic" name="supplierBasic"
									value="${orderProDetail.supplierBasic.id}" title="选择供应商"
									labelName="supplierBasic.fname"
									labelValue="${orderProDetail.supplierBasic.fname}"
									cssClass="form-control required" fieldLabels="供应商|备注"
									fieldKeys="fname|remarks" searchLabel="供应商" searchKey="fname"></sys:gridselect>

							</div>
						</form:form>
						<br />
						
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="order:procurement:orderProDetail:del">
								<table:returnRow
									url="${ctx}/order/procurement/orderProDetail/deleteAll"
									id="contentTable"></table:returnRow>
							<!-- 退货按钮 -->
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
				<table id="contentTable"
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>
							<th class="sort-column warehouse.id">仓库</th>
							<th class="sort-column supplierBasic.id">供应商</th>
							<th class="sort-column goodsSpu.id">商品</th>
							<th class="sort-column goodsSku.id">货号</th>
							<th class="sort-column goodsSpu.fbuyprice">采购价</th>
							<th class="sort-column finventory">可用库存量</th>
							<th class="sort-column freturnnum">退货数量</th>
							<th class="sort-column remarks">备注信息</th>
							<th>操作</th>
						</tr>
					</thead>

					<!-- ***************************************************************************************** -->
					<!-- 我要把下面的数据获传到后台 -->
					<!-- ${ctx}/order/procurement/orderProDetail/returnAll -->
					<tbody>
						<form:form id="inputForm" modelAttribute="orderProDetail" action="javascript:returnAll()" method="post" class="form-horizontal">
							<c:forEach items="${page.list}" var="orderProDetail"
								varStatus="status">

								<tr>
									<td><input type="checkbox" name="subcheck"
										id="${orderProDetail.id}" class="i-checks"
										value="${orderProDetail.id}"></td>
									<td>${orderProDetail.warehouse.fname}</td>
									<td>${orderProDetail.supplierBasic.fname}</td>
									<td>${orderProDetail.goodsSpu.fgoodsname}</td>
									<td>${orderProDetail.goodsSku.fgoodsnumber}</td>
									<td>${orderProDetail.goodsSpu.fbuyprice}</td>
									<td><input type="text" id="nownum${status.index+1}"
										name="nownum" value="${orderProDetail.finventory}" readonly /></td>
									<td width="10"><input type="text"
										id="freturnnum${status.index+1}"
										onblur="javascript:checkForm(${status.index+1})" /></td>

									<td>${orderProDetail.remarks}</td>
									<td><shiro:hasPermission
											name="order:procurement:orderProDetail:view">
											<a href="#"
												onclick="openDialogView('查看采购单详情', '${ctx}/warehouses/warehouseGoodsInfo/form?id=${orderProDetail.id}','800px', '500px')"
												class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
												查看</a>
										</shiro:hasPermission></td>
								</tr>
							</c:forEach>
						</form:form>
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