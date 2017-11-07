<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>供应商对账/付款管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		
		var validateForm;
		function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if (window.confirm('确定金额输入无误吗？请谨慎操作哦！')) {
				//alert("确定");
				if (validateForm.form()) {
					$("#inputForm").submit();
					return true;
				}
			} else {
				//alert("取消");
				return false;
			}
			return false;
		}
		$(document).ready(
				function() {
					validateForm = $("#inputForm").validate(
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

					laydate({
						elem : '#fbusinessHours', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
						event : 'focus' //响应事件。如果没有传入event，则按照默认的click
					});
				});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="supplierAccount" action="${ctx}/account/supplieraccount/supplierAccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>供应商名称：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/account/supplieraccount/supplierAccount/selectsupplierBasic" id="supplierBasic" name="supplierBasic.id"  value="${supplierAccount.supplierBasic.id}"  title="选择供应商名称" labelName="supplierBasic.fname" 
						 labelValue="${supplierAccount.supplierBasic.fname}" cssClass="form-control required" fieldLabels="供应商|备注" fieldKeys="fname|remarks" searchLabel="供应商名称" searchKey="fname" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">单据编号：</label></td>
					<td class="width-35">
						<form:input path="foddNumbers" htmlEscape="false"   class="form-control " readonly="true"/>
						<font color="#999999">单号自动生成</font>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账目类型：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/account/supplieraccount/supplierAccount/selectaccountType" id="accountType" name="accountType.id"  value="${supplierAccount.accountType.id}"  title="选择账目类型" labelName="accountType.ftypename" 
						 labelValue="${supplierAccount.accountType.ftypename}" cssClass="form-control required" fieldLabels="账目类型|备注" fieldKeys="ftypename|remarks" searchLabel="账目类型" searchKey="ftypename" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>结算账户：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/account/supplieraccount/supplierAccount/selectclearingAccount" id="clearingAccount" name="clearingAccount.id"  value="${supplierAccount.clearingAccount.id}"  title="选择结算账户" labelName="clearingAccount.faccountname" 
						 labelValue="${supplierAccount.clearingAccount.faccountname}" cssClass="form-control required " fieldLabels="账户名称|备注" fieldKeys="faccountname|remarks" searchLabel="结算账户" searchKey="faccountname" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">业务时间：</label></td>
					<td class="width-35">
						<input id="fbusinessHours" name="fbusinessHours" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${supplierAccount.fbusinessHours}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
						<font color="#999999">时间自动生成</font>	
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>应付金额：</label></td>
					<td class="width-35">
						<form:input path="famountPay" htmlEscape="false"    class="form-control required number"/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>优惠金额：</label></td>
					<td class="width-35">
						<form:input path="fpreferentialAmount" htmlEscape="false"    class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>实付金额：</label></td>
					<td class="width-35">
						<form:input path="fpayAmount" htmlEscape="false"    class="form-control required number"/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">本单应付余额：</label></td>
					<td class="width-35">
						<form:input path="fsolehandlingAmount" htmlEscape="false"    class="form-control number" readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>