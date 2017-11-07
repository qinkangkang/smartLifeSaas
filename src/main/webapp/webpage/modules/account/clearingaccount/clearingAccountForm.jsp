<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>结算账户管理</title>
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
		<form:form id="inputForm" modelAttribute="clearingAccount" action="${ctx}/account/clearingaccount/clearingAccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">账户名称：</label></td>
					<td class="width-35">
						<form:input path="faccountname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">账号：</label></td>
					<td class="width-35">
						<form:input path="faccountnumber" htmlEscape="false"    class="form-control  number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开户人：</label></td>
					<td class="width-35">
						<form:input path="faccountholder" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账户类型：</label></td>
					<td class="width-35">
						<form:select path="faccounttype" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('faccountt_ype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">当前余额：</label></td>
					<td class="width-35">
						<form:input path="fbalance" htmlEscape="false"    class="form-control number " maxlength="10"/>
					</td>
					<td class="width-15 active"><label class="pull-right">所属门店：</label></td>
					<td class="width-35"><sys:treeselect id="fstore"
							name="fstore.id" value="${clearingAccount.fstore.id}"
							labelName="fstore.name"
							labelValue="${clearingAccount.fstore.name}" title="门店"
							url="/sys/office/treeData?type=2"
							cssClass="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">启用状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="fstatus" items="${fns:getDictList('sys_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>