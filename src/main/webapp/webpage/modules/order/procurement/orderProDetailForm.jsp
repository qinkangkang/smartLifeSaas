<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购单详情管理</title>
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
	<form:form id="inputForm" modelAttribute="orderProDetail"
		action="${ctx}/order/procurement/orderProDetail/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">采购单：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProDetail/selectforderprocurement"
							id="forderprocurement" name="forderprocurement.id"
							value="${orderProDetail.forderprocurement.id}" title="选择采购单"
							labelName="forderprocurement.fordernum"
							labelValue="${orderProDetail.forderprocurement.fordernum}"
							cssClass="form-control required" fieldLabels="采购单号|备注"
							fieldKeys="fordernum|remarks" searchLabel="采购单号"
							searchKey="fordernum"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right">商品：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProDetail/selectfspu"
							id="fspu" name="fspu.id" value="${orderProDetail.fspu.id}"
							title="选择商品" labelName="fspu.fgoodsname"
							labelValue="${orderProDetail.fspu.fgoodsname}"
							cssClass="form-control required" fieldLabels="商品名称|备注"
							fieldKeys="fgoodsname|remarks" searchLabel="商品名称"
							searchKey="fgoodsname"></sys:gridselect></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品详情：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProDetail/selectfsku"
							id="fsku" name="fsku.id" value="${orderProDetail.fsku.id}"
							title="选择商品详情" labelName="fsku.fgoodsnumber"
							labelValue="${orderProDetail.fsku.fgoodsnumber}"
							cssClass="form-control required" fieldLabels="商品货号|备注"
							fieldKeys="fgoodsnumber|remarks" searchLabel="商品货号"
							searchKey="fgoodsnumber"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right">商品数量：</label></td>
					<td class="width-35"><form:input path="fgoodsnum"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品单价：</label></td>
					<td class="width-35"><form:input path="fgoodsprice"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">商品折扣：</label></td>
					<td class="width-35"><form:input path="fgoodsdiscount"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">折后单价：</label></td>
					<td class="width-35"><form:input path="fdiscountprice"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">折扣前总额：</label></td>
					<td class="width-35"><form:input path="fcountmoney"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">折后总额：</label></td>
					<td class="width-35"><form:input path="fdiscountcountmoney"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('sys_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
					<td class="width-15 active"></td>
					<td class="width-35"></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>