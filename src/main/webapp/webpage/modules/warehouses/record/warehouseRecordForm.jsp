<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库流水管理</title>
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
			
					laydate({
			            elem: '#businessTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="warehouseRecord" action="${ctx}/warehouses/warehouseRecord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">仓库：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseRecord/selectwarehouse" id="warehouse" name="warehouse.id"  value="${warehouseRecord.warehouse.id}"  title="选择仓库" labelName="warehouse.fname" 
						 labelValue="${warehouseRecord.warehouse.fname}" cssClass="form-control required" fieldLabels="仓库|备注" fieldKeys="fname|remarks" searchLabel="仓库" searchKey="fname" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品SPU：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseRecord/selectgoodsSpu" id="goodsSpu" name="goodsSpu.id"  value="${warehouseRecord.goodsSpu.id}"  title="选择商品SPU" labelName="goodsSpu.fgoodsname" 
						 labelValue="${warehouseRecord.goodsSpu.fgoodsname}" cssClass="form-control required" fieldLabels="商品名|备注" fieldKeys="fgoodsname|remaks" searchLabel="商品名" searchKey="fgoodsname" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">商品SKU：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseRecord/selectgoodsSku" id="goodsSku" name="goodsSku.id"  value="${warehouseRecord.goodsSku.id}"  title="选择商品SKU" labelName="goodsSku.fgoodsnumber" 
						 labelValue="${warehouseRecord.goodsSku.fgoodsnumber}" cssClass="form-control required" fieldLabels="货号|备注" fieldKeys="fgoodsnumber|remarks" searchLabel="货号" searchKey="fgoodsnumber" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">业务时间：</label></td>
					<td class="width-35">
						<input id="businessTime" name="businessTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${warehouseRecord.businessTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">业务类型：</label></td>
					<td class="width-35">
						<form:input path="businessType" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">业务单号：</label></td>
					<td class="width-35">
						<form:input path="businessorderNumber" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">改变数量：</label></td>
					<td class="width-35">
						<form:input path="changeNum" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">剩余数量：</label></td>
					<td class="width-35">
						<form:input path="remainingNum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>