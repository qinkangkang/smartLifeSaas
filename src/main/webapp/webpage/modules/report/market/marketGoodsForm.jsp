<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>销售详情</title>
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
		action="" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品名：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.fgoodsname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">货号：</label></td>
					<td class="width-35">
						<form:input path="goodsSku.fgoodsnumber" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">条码：</label></td>
					<td class="width-35">
						<form:input path="goodsSku.fbarcode" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">品牌：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.brand.fbrandname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">分类：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.categorys.name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单位：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.goodsUnit.funitname" htmlEscape="false"    class="form-control "/>
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
						<form:input path="fgoodsdiscount" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">折后价：</label></td>
					<td class="width-35">
						<form:input path="fdiscountprice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">金额：</label></td>
					<td class="width-35">
						<form:input path="fcountmoney" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">折后金额：</label></td>
					<td class="width-35">
						<form:input path="fdiscountcountmoney" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">采购均价：</label></td>
					<td class="width-35">
						<form:input path="goodsSpu.fbuyprice" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">采购成本：</label></td>
					<td class="width-35">
						<form:input path="purchasingCost" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">毛利润：</label></td>
					<td class="width-35">
						<form:input path="grossProfit" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">毛利率(%)：</label></td>
					<td class="width-35">
						<form:input path="grossProfitMargin" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">单据编号：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.fordernum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">制单人：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.createBy.name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">销售员：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.updateBy.name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">客户：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.customerBasic.fname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">仓库：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.warehouse.fname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">结算账户：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.clearingAccount.faccountname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">单据总金额：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.fordercountprice" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">实收金额：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.fdiscountprice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">单据日期：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.fendtime" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">销售员所属门店：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.updateBy.office.name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">仓库所属门店：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.warehouse.office.name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单据备注：</label></td>
					<td class="width-35">
						<form:input path="orderMarket.remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">商品备注：</label></td>
					<td class="width-35">
						<form:input path="goodsSku.remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr> 
			</tbody>
		</table>
	</form:form>
</body>
</html>  