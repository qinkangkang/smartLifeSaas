<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库商品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
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
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="warehouseGoodsInfo" action="${ctx}/warehouses/warehouseGoodsInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">仓库：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseGoodsInfo/selectwarehouse" id="warehouse" name="warehouse.id"  value="${warehouseGoodsInfo.warehouse.id}"  title="选择仓库" labelName="warehouse.fname" 
						 labelValue="${warehouseGoodsInfo.warehouse.fname}" cssClass="form-control required" fieldLabels="仓库|备注" fieldKeys="fname|remarks" searchLabel="仓库" searchKey="fname" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">商品品牌：</label></td>
					<td class="width-35">
						${warehouseGoodsInfo.goodsSpu.brand.fbrandname }
						<%-- <form:input path="goodsSpu.brand.fbrandname" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品名称：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseGoodsInfo/selectgoodsSpu" id="goodsSpu" name="goodsSpu.id"  value="${warehouseGoodsInfo.goodsSpu.id}"  title="选择商品SPU" labelName="goodsSpu.fgoodsname" 
						 labelValue="${warehouseGoodsInfo.goodsSpu.fgoodsname}" cssClass="form-control required" fieldLabels="商品名|备注" fieldKeys="fgoodsname|remarks" searchLabel="商品名" searchKey="fgoodsname" ></sys:gridselect>
					</td>
					
					
					<td class="width-15 active"><label class="pull-right">单位：</label></td>
					<td class="width-35">
						${warehouseGoodsInfo.goodsSpu.goodsUnit.funitname }
						<%-- <form:input path="goodsSpu.goodsUnit.funitname" htmlEscape="false"    class="form-control "/> --%>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品货号：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseGoodsInfo/selectgoodsSku" id="goodsSku" name="goodsSku.id"  value="${warehouseGoodsInfo.goodsSku.id}"  title="选择商品SKU" labelName="goodsSku.fgoodsnumber" 
						 labelValue="${warehouseGoodsInfo.goodsSku.fgoodsnumber}" cssClass="form-control required" fieldLabels="货号|备注" fieldKeys="fgoodsnumber|remarks" searchLabel="货号" searchKey="fgoodsnumber" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">可用库存量：</label></td>
					<td class="width-35">
						<form:input path="finventory" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品分类：</label></td>
					<td class="width-35">
						${warehouseGoodsInfo.goodsSpu.categorys.name}
						<%-- <form:input path="goodsSpu.categorys.name" htmlEscape="false"    class="form-control "/> --%>
					</td>
					<td class="width-15 active"><label class="pull-right">锁定库存量：</label></td>
					<td class="width-35">
						<form:input path="flockinventory" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">库存总量：</label></td>
					<td class="width-35">
						<form:input path="ftotalinventory" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>