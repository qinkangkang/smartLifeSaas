<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商户管理管理</title>
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
	
	<script type="text/javascript">
	
	function checkPhone(){ 
	    var phone = document.getElementById('cellphone').value;
	    if(!(/^1(3|5|7|8)\d{9}$/.test(phone))){ 
	        alert("手机号码有误,请重新填写");  
	        return false; 
	    } 
	}
	
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="merchant" action="${ctx}/merchant/management/merchant/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属区域：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/merchant/management/merchant/selectarea" id="area" name="area.id"  value="${merchant.area.id}"  title="选择所属区域" labelName="area.name" 
						 labelValue="${merchant.area.name}" cssClass="form-control required" fieldLabels="所属区域|备注" fieldKeys="name|remarks" searchLabel="区域名" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">商户名称：</label></td>
					<td class="width-35">
						<form:input path="fname" htmlEscape="false"    class="form-control required "/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">负责人：</label></td>
					<td class="width-35">
						<form:input path="fmaster" htmlEscape="false"    class="form-control required "/>
					</td>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35">
						<form:input path="femail" htmlEscape="false"    class="form-control email"/>
					</td>
					<!-- 
					<td class="width-15 active"><label class="pull-right">区域编号：</label></td>
					<td class="width-35">
						<form:input path="code" htmlEscape="false"    class="form-control "/>
					</td>
					 -->
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">手机：</label></td>
					<td class="width-35">
						<form:input id="cellphone" path="cellphone" htmlEscape="false" maxlength="11" minlength="11" class="form-control required number " onblur="checkPhone()"/>
					</td>
					<td class="width-15 active"><label class="pull-right">是否启用</label></td>
					<td class="width-35">
						<form:select path="fstatus" class="form-control required ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('sys_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					
				</tr>
				<tr>
					
					
					<td class="width-15 active"><label class="pull-right">排序：</label></td>
					<td class="width-35">
						<form:input path="fsort" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
					
				</tr>
				<!--  
				<tr>
					
		  		</tr>
		  		-->
		 	</tbody>
		</table>
	</form:form>
</body>
</html>