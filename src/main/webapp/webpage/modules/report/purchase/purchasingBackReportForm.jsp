<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购退单明细详情</title>
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
		<form:form id="inputForm" modelAttribute="orderProChaDetail" action="${ctx}/warehouses/warehouseGoodsInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品名：</label></td>
					<td class="width-35">
						<form:input path="fspu.fgoodsname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">货号：</label></td>
					<td class="width-35">
						<form:input path="fsku.fgoodsnumber" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">条码：</label></td>
					<td class="width-35">
						<form:input path="fsku.fbarcode" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">商品品牌：</label></td>
					<td class="width-35">
						<form:input path="fspu.brand.fbrandname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分类：</label></td>
					<td class="width-35">
						<form:input path="fspu.categorys.name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单位：</label></td>
					<td class="width-35">
						<form:input path="fspu.goodsUnit.funitname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">数量：</label></td>
					<td class="width-35">
						<form:input path="fgoodsnum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单价：</label></td>
					<td class="width-35">
						<form:input path="fgoodsprice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">折扣(%)：</label></td>
					<td class="width-35">
						<form:input path="fdiscount" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">折后单价：</label></td>
					<td class="width-35">
						<form:input path="fdiscountPrice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">金额：</label></td>
					<td class="width-35">
						<form:input path="fcountprice" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">折后金额：</label></td>
					<td class="width-35">
						<form:input path="fdiscountcountprice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">单据编号：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.fordernum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">采购员：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.fexecutor.name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">供应商：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.fsupplier.fname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">仓库：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.fwarehose.fname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">结算账户：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.fdclearingaccount.faccountname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单据总金额：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.fordercountprice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">实付金额：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.factualpayment" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单据日期：</label></td>
					<td class="width-35">
					<fmt:formatDate value="${orderProDetail.fprocurementchargeback.fendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<%-- <form:input path="forderprocurement.fendtime" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">单据备注：</label></td>
					<td class="width-35">
						<form:input path="fprocurementchargeback.remarks" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">商品备注：</label></td>
					<td class="width-35">
						<form:input path="fsku.remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>