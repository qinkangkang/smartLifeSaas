<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>客户基本信息管理</title>
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
			
					laydate({
			            elem: '#fBirthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
	
	<script type="text/javascript">
	
	function checkphone(){
		var myreg = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[7])|(18[0-9]))\\d{8}$";
		var re = new RegExp(myreg);
		
		if(!re.test($("#shouji").val()))
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
	<form:form id="inputForm" modelAttribute="customerBasic"
		action="${ctx}/customer/basic/customerBasic/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>客户名称：</label></td>
					<td class="width-35"><form:input path="fname"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">客户性别：</label></td>
					<td class="width-35"><form:select path="fsex"
							class="form-control ">
							<form:option value="" label="请选择" selected="selected" />
							<form:option value="1" label="男" />
							<form:option value="2" label="女" />

						</form:select></td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">客户分类：</label></td>
					<td class="width-35">
						<!-- 
						<form:input path="fcategoryId" htmlEscape="false"    class="form-control "/>
					 --> <form:select path="fcategoryId" class="form-control ">
							<form:option value="" label="请选择" selected="selected" />
							<c:forEach items="${customerCateList }" var="cate">
								<form:option value="${cate.id }" label="${cate.fname }" />
							</c:forEach>

						</form:select>

					</td>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>手机：</label></td>
					<td class="width-35"><form:input path="fcellphone" id="shouji"
							htmlEscape="false" maxlength="11" minlength="11"
							class="form-control required number" onblur="checkphone()"/></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">客户生日：</label></td>
					<td class="width-35"><input id="fbirthday" name="fbirthday"
						type="text" maxlength="20"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${customerBasic.fbirthday}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>联系人：</label></td>
					<td class="width-35"><form:input path="flinkman"
							htmlEscape="false" class="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">电话：</label></td>
					<td class="width-35"><form:input path="fphone"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35"><form:input path="femail"
							htmlEscape="false" class="form-control  email" /></td>
					<td class="width-15 active"><label class="pull-right">传真：</label></td>
					<td class="width-35"><form:input path="ffax"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">银行账户：</label></td>
					<td class="width-35"><form:input path="fbankaccount"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">银行账号：</label></td>
					<td class="width-35"><form:input path="fbankaccountnum"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开户行：</label></td>
					<td class="width-35"><form:input path="fbank"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">开户人姓名：</label></td>
					<td class="width-35"><form:input path="fbankaccountname"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开户人身份证：</label></td>
					<td class="width-35"><form:input path="fbankaccountpersonid"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">开户预留手机号：</label></td>
					<td class="width-35"><form:input id="fbankphone" path="fbankphone"
							htmlEscape="false" class="form-control " onblur="checkphone2()"/></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">支付方式：</label></td>
					<td class="width-35">
						<!-- 
						<form:input path="fpaytype" htmlEscape="false"    class="form-control "/>
					 --> <form:select path="fpaytype" class="form-control">
							<form:option value="" label="" />

							<form:options items="${fns:getDictList('pay_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />

						</form:select>

					</td>
					<td class="width-15 active"><label class="pull-right">排序：</label></td>
					<td class="width-35"><form:input path="fsort"
							htmlEscape="false" class="form-control  digits" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">客户地址：</label></td>
					<td class="width-35"><form:input path="faddress"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">发货方式：</label></td>
					<td class="width-35"><form:select path="fshipmenttype"
							class="form-control ">
							<form:option value="" label="请选择" selected="selected" />
							<form:option value="0" label="自提" />
							<form:option value="1" label="车送" />
							<form:option value="2" label="物流" />
							<!--  
							<form:options items="${fns:getDictList('fshipmentype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							-->
						</form:select></td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">是否有关联账号：</label></td>
					<td class="width-35"><form:select path="fisrelate"
							class="form-control ">
							<form:option value="" label="请选择" selected="selected" />
							<form:option value="0" label="无" />
							<form:option value="1" label="有" />

						</form:select></td>

					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('sys_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />

						</form:select></td>
				</tr>
				<tr>
					<!--  
					<td class="width-15 active"><label class="pull-right">关联账号信息ID：</label></td>
					<td class="width-35">
						<form:input path="fcustomerinfo" htmlEscape="false"    class="form-control "/>
					</td>
					-->
					<!-- 
					<td class="width-15 active"><label class="pull-right">客户欠款：</label></td>
					<td class="width-35">
						<form:input path="fdebt" htmlEscape="false"    class="form-control  number"/>
					</td>
					 -->
				</tr>
				<!-- 
				<tr>
					<td class="width-15 active"><label class="pull-right">折扣：</label></td>
					<td class="width-35">
						<form:input path="fdiscount" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 		 -->
			</tbody>
		</table>
	</form:form>
</body>
</html>