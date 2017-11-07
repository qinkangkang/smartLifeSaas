<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>销售单详细管理</title>
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
	<form:form id="inputForm" modelAttribute="orderMarketDetail"
		action="${ctx}/market/ordermarket/orderMarketDetail/save"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">销售单：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarketDetail/selectorderMarket"
							id="orderMarket" name="orderMarket.id"
							value="${orderMarketDetail.orderMarket.id}" title="选择销售单"
							labelName="orderMarket.fordernum"
							labelValue="${orderMarketDetail.orderMarket.fordernum}"
							cssClass="form-control required" fieldLabels="销售单号|备注"
							fieldKeys="fordernum|remarks" searchLabel="销售单号"
							searchKey="fordernum"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right">商品名称：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarketDetail/selectgoodsSpu"
							id="goodsSpu" name="goodsSpu.id"
							value="${orderMarketDetail.goodsSpu.id}" title="选择商品名称"
							labelName="goodsSpu.fgoodsname"
							labelValue="${orderMarketDetail.goodsSpu.fgoodsname}"
							cssClass="form-control required" fieldLabels="商品名称|备注"
							fieldKeys="fgoodsname|remarks" searchLabel="商品名称"
							searchKey="fgoodsname"></sys:gridselect></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品数量：</label></td>
					<td class="width-35"><form:input path="fgoodsnum"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">商品单价：</label></td>
					<td class="width-35"><form:input path="fgoodsprice"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品折扣：</label></td>
					<td class="width-35"><form:input path="fgoodsdiscount"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">折后单价：</label></td>
					<td class="width-35"><form:input path="fdiscountprice"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">折扣前金额：</label></td>
					<td class="width-35"><form:input path="fcountmoney"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">折后总额：</label></td>
					<td class="width-35"><form:input path="fdiscountcountmoney"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品详情：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarketDetail/selectgoodsSku"
							id="goodsSku" name="goodsSku.id"
							value="${orderMarketDetail.goodsSku.id}" title="选择商品详情"
							labelName="goodsSku.fgoodsnumber"
							labelValue="${orderMarketDetail.goodsSku.fgoodsnumber}"
							cssClass="form-control required" fieldLabels="商品详情|备注"
							fieldKeys="fgoodsnumber|remarks" searchLabel="商品详情"
							searchKey="fgoodsnumber"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right">数据状态：</label></td>
					<td class="width-35"><form:input path="fstatus"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">退货数量：</label></td>
					<td class="width-35"><form:input path="freturnnum"
							htmlEscape="false" class="form-control " /></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>