<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>销售单详细管理</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
		var validateForm;
		
		
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
		});
		
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){

			  $("#inputForm").submit();
			  return false;
		  }
	
		  return false;
		}
		
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
　　　　	alert("退货数量超出限制!");
　　　　	return false;
　　}
} 	
</script>


</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>销售单详细列表</h5>
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
						<form:form id="searchForm" modelAttribute="orderMarketDetail"
							action="${ctx}/market/ordermarket/orderMarketDetail/"
							method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>销售单号：</span>
								<sys:gridselect
									url="${ctx}/market/ordermarket/orderMarketDetail/selectorderMarket"
									id="orderMarket" name="orderMarket"
									value="${orderMarketDetail.orderMarket.id}" title="选择销售单"
									labelName="orderMarket.fordernum"
									labelValue="${orderMarketDetail.orderMarket.fordernum}"
									cssClass="form-control required" fieldLabels="销售单号|备注"
									fieldKeys="fordernum|remarks" searchLabel="销售单号"
									searchKey="fordernum"></sys:gridselect>
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							
							<!--  
							<shiro:hasPermission
								name="market:ordermarket:orderMarketDetail:edit">
								<table:editRow
									url="${ctx}/market/ordermarket/orderMarketDetail/form"
									title="销售单详细" id="contentTable"></table:editRow>
								 编辑按钮 -->
								 <!--
							</shiro:hasPermission>
							 -->
							<shiro:hasPermission
								name="market:ordermarket:orderMarketDetail:del">
								<table:marketReturnRow
									url="${ctx}/market/ordermarket/orderMarketDetail/deleteAll"
									id="contentTable"></table:marketReturnRow>
								
								
							</shiro:hasPermission>
							 <!-- 删除按钮
							<shiro:hasPermission
								name="market:ordermarket:orderMarketDetail:import">
								<table:importExcel
									url="${ctx}/market/ordermarket/orderMarketDetail/import"></table:importExcel>
								导入按钮 -->
								<!-- 
							</shiro:hasPermission>
							<shiro:hasPermission
								name="market:ordermarket:orderMarketDetail:export">
								<table:exportExcel
									url="${ctx}/market/ordermarket/orderMarketDetail/export"></table:exportExcel>
								
							</shiro:hasPermission>
							导出按钮 -->
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

							<th class="sort-column orderMarket.id">销售单</th>
							<th class="sort-column goodsSpu.id">商品名称</th>
							<th class="sort-column fgoodsnum">商品数量</th>
							<th class="sort-column fgoodsprice">商品单价</th>
							<th class="sort-column fgoodsdiscount">商品折扣</th>
							<th class="sort-column fdiscountprice">折后单价</th>
							<th class="sort-column fcountmoney">折扣前金额</th>
							<th class="sort-column fdiscountcountmoney">折后总额</th>
							<th class="sort-column goodsSku.id">商品货号</th>
							<th class="sort-column fstatus">数据状态</th>
							<th class="sort-column remarks">备注信息</th>
							<th class="sort-column fwarehousegoodsinfo.id">可用库存量</th>
							<th class="sort-column freturnnum">退货数量</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<form:form id="inputForm" modelAttribute="orderMarketDetail" action="javascript:marketreturnAll()" method="post" class="form-horizontal">
						<c:forEach items="${page.list}" var="orderMarketDetail" varStatus="status">
							<tr>
								<td><input type="checkbox" name="subcheck" id="${orderMarketDetail.id}" value="${orderMarketDetail.id}"
									class="i-checks" ></td>
								<td>${orderMarketDetail.orderMarket.fordernum}</td>
								<td>${orderMarketDetail.goodsSpu.fgoodsname}</td>
								<td>
								<input type="text" id="nownum${status.index+1}" name="nownum" value="${orderMarketDetail.fgoodsnum}" readonly />
								
								
								</td>
								
								<td>${orderMarketDetail.fgoodsprice}</td>
								<td>${orderMarketDetail.fgoodsdiscount}</td>
								<td>${orderMarketDetail.fdiscountprice}</td>
								<td>${orderMarketDetail.fcountmoney}</td>
								<td>${orderMarketDetail.fdiscountcountmoney}</td>
								<td><a href="#"
									onclick="openDialogView('查看商品详细', '${ctx}/market/ordermarket/orderMarketDetail/form?id=${orderMarketDetail.goodsSku.id}','800px', '500px')"></a>
									${orderMarketDetail.goodsSku.fgoodsnumber}</td>
								<td>${orderMarketDetail.fstatus}</td>
								<td>${orderMarketDetail.remarks}</td>
								<td >
								
								${orderMarketDetail.fwarehousegoodsinfo.finventory}
								
								</td>
								<td width="10">
									<input type="text" id="freturnnum${status.index+1}"  onblur="javascript:checkForm(${status.index+1})"/>
								</td>
								<td><shiro:hasPermission
										name="market:ordermarket:orderMarketDetail:view">
										<a href="#"
											onclick="openDialogView('查看销售单详细', '${ctx}/market/ordermarket/orderMarketDetail/form?id=${orderMarketDetail.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看</a>
									</shiro:hasPermission> 
									</td>
									<!--  
									<shiro:hasPermission
										name="market:ordermarket:orderMarketDetail:edit">
										<a href="#"
											onclick="openDialog('修改销售单详细', '${ctx}/market/ordermarket/orderMarketDetail/form?id=${orderMarketDetail.id}','800px', '500px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
											修改</a>
									</shiro:hasPermission> <shiro:hasPermission
										name="market:ordermarket:orderMarketDetail:del">
										<a
											href="${ctx}/market/ordermarket/orderMarketDetail/delete?id=${orderMarketDetail.id}"
											onclick="return confirmx('确认要删除该销售单详细吗？', this.href)"
											class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
											删除</a>
									</shiro:hasPermission>
									-->
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