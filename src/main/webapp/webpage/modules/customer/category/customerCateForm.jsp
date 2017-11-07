<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>客户分类管理</title>
<meta name="decorator" content="default" />
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
	
	<script type="text/javascript">
	
	
	function checkdiscount(){
		var discount=$("#fdiscount").val();
		if(discount<=0||discount>100){
			alert('折扣不能小于等于0或大于100');
			$("#fdiscount").val(100);
			 return false; 
		}
		
	}
	</script>
	
	
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="customerCate"
		action="${ctx}/customer/category/customerCate/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>分类名称：</label></td>
					<td class="width-35"><form:input path="fname"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">客户分类状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control ">
							<form:option value="1" label="启用" />
							<form:option value="2" label="失效" />
							<!--  
							<form:options items="${fns:getDictList('select')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							-->
						</form:select></td>


				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>折扣(%)：</label></td>
					<td class="width-35"><form:input path="fdiscount"
							htmlEscape="false" class="form-control required" id="fdiscount"  onblur="checkdiscount()"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>

				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>