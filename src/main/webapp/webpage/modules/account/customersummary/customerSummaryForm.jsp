<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户基本信息管理</title>
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
		<form:form id="inputForm" modelAttribute="customerSummary" action="${ctx}/account/customersummary/customerSummary/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户名称：</label></td>
					<td class="width-35">
						<form:input path="fname" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">客户分类：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/account/customersummary/customerSummary/selectcustomerCate" id="customerCate" name="customerCate.id"  value="${customerSummary.customerCate.id}"  title="选择客户分类" labelName="customerCate.fname" 
						 labelValue="${customerSummary.customerCate.fname}" cssClass="form-control required" fieldLabels="客户分类|备注" fieldKeys="fname|remarks" searchLabel="客户类别" searchKey="fname" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系人：</label></td>
					<td class="width-35">
						<form:input path="flinkman" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>手机：</label></td>
					<td class="width-35">
						<form:input path="fcellphone" htmlEscape="false" maxlength="11"  minlength="11"   class="form-control required number"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>