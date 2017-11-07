<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>供应商信息管理</title>
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
	
	function checkphone(){
		var myreg = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[7])|(18[0-9]))\\d{8}$";
		var re = new RegExp(myreg);
		
		if(!re.test($("#fcellphone").val()))
		{ 
		    alert('请输入有效的手机号码！'); 
		    return false; 
		} 
	}
	</script>

<script type="text/javascript">
	
	function checkphone2(){
		var myreg = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[7])|(18[0-9]))\\d{8}$";
		var re = new RegExp(myreg);
		
		if(!re.test($("#fbankphone").val()))
		{ 
		    alert('请输入有效的手机号码！'); 
		    return false; 
		} 
	}
	</script>

<script type="text/javascript">
	
	
	function checkdiscount(){
		//var myreg = "^(([0-9]\.[0-9])|[1,2,3,4,5,6,7,8,9,10])$";
		//var re = new RegExp(myreg);
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
	<form:form id="inputForm" modelAttribute="supplierBasic"
		action="${ctx}/supplier/supplierbasic/supplierBasic/save"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">供应商名称：</label></td>
					<td class="width-35"><form:input path="fname"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">全名：</label></td>
					<td class="width-35"><form:input path="ffullname"
							htmlEscape="false" class="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">简介：</label></td>
					<td class="width-35"><form:input path="fbrief"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">网址：</label></td>
					<td class="width-35"><form:input path="fwebsite"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开户银行：</label></td>
					<td class="width-35"><form:select path="fbankid"
							class="form-control ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('sys_bank')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select> <!-- 
						<form:input path="fbankid" htmlEscape="false"    class="form-control "/>
						 --></td>
					<td class="width-15 active"><label class="pull-right">开户支行：</label></td>
					<td class="width-35"><form:input path="fbank"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开户行账号：</label></td>
					<td class="width-35"><form:input path="fbankaccoun"
							htmlEscape="false" class="form-control  number" /></td>
					<td class="width-15 active"><label class="pull-right">开户人姓名：</label></td>
					<td class="width-35"><form:input path="fbankaccountname"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开户人身份证：</label></td>
					<td class="width-35"><form:input path="fbankaccountpersonId"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">银行预留手机：</label></td>
					<td class="width-35"><form:input id="fbankphone"
							path="fbankphone" htmlEscape="false" class="form-control  number"
							maxlength="11" minlength="11" onblur="checkphone2()" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">地址：</label></td>
					<td class="width-35"><form:input path="faddress"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">负责人：</label></td>
					<td class="width-35"><form:input path="fbdid"
							htmlEscape="false" class="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">温馨提示：</label></td>
					<td class="width-35"><form:input path="freminder"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35"><form:input path="femail"
							htmlEscape="false" class="form-control  email" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">排序：</label></td>
					<td class="width-35"><form:input path="fsort"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">传真：</label></td>
					<td class="width-35"><form:input path="ffax"
							htmlEscape="false" class="form-control number" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">折扣(%)：</label></td>
					<td class="width-35"><form:input id="fdiscount"
							path="fdiscount" htmlEscape="false"
							class="form-control number required" onblur="checkdiscount()" />
					</td>
					<td class="width-15 active"><label class="pull-right">欠款：</label></td>
					<td class="width-35"><form:input path="fdebt"
							htmlEscape="false" class="form-control number" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">联系人：</label></td>
					<td class="width-35"><form:input path="flinkman"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">联系人手机：</label></td>
					<td class="width-35"><form:input id="fcellphone"
							path="fcellphone" htmlEscape="false"
							class="form-control required  number" maxlength="11"
							minlength="11" onblur="checkphone()" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">供应商状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control required ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('sys_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<!-- 
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>所属商户：</label></td>
					<td class="width-35"><sys:treeselect id="fsponsor"
							name="fsponsor.id" value="${supplierBasic.fsponsor.id}"
							labelName="fsponsor.name"
							labelValue="${supplierBasic.fsponsor.name}" title="商户"
							url="/sys/office/treeData?type=2"
							cssClass="form-control required" /></td>
				</tr>
				 -->
			</tbody>
		</table>
	</form:form>
</body>
</html>