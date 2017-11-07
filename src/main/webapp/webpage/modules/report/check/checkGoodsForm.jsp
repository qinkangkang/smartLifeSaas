<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>盘点商品详情</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			$("#inputForm").submit();
			return true;
		}

		return false;
	}
	$(document).ready(
			function() {
				validateForm = $("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});

			});
</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="warehouseCheckGoods"
		action="" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">单号：</label></td>
					<td class="width-35">
						<form:input path="checkOrder.fcheckOrder" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">商品名称：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.fgoodsname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">货号：</label></td>
					<td class="width-35">
						<form:input path="goodsSku.fgoodsnumber" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">条码：</label></td>
					<td class="width-35">
						<form:input path="goodsSku.fbarcode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">分类：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.categorys.name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">品牌：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.brand.fbrandname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">单位：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.goodsUnit.funitname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">仓库：</label></td>
					<td class="width-35">
						<form:input path="checkOrder.warehouse.fname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">盘点数量：</label></td>
					<td class="width-35">
						<form:input path="checkNum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">盘点前数量：</label></td>
					<td class="width-35">
						<form:input path="checkBeforeNum" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">盈亏数量：</label></td>
					<td class="width-35">
						<form:input path="profitLossNum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">盈亏金额：</label></td>
					<td class="width-35">
						<form:input path="profitLossMoney" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">盈亏状况：</label></td>
					<td class="width-35">
						<form:input path="profitLossMoney" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">盘点人：</label></td>
					<td class="width-35">
						<form:input path="checkOrder.updateBy.name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">盘点时间：</label></td>
					<td class="width-35">
						<form:input path="checkOrder.checkDate" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
			</tbody>
		</table>
	</form:form>
</body>
</html>  