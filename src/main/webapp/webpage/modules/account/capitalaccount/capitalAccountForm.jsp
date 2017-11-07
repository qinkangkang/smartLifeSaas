<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>资金流水账管理</title>
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
			
					laydate({
			            elem: '#fbusinessHours', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="capitalAccount" action="${ctx}/account/capitalaccount/capitalAccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">单据编号：</label></td>
					<td class="width-35">
						<form:input path="foddNumbers" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">业务时间：</label></td>
					<td class="width-35">
						<input id="fbusinessHours" name="fbusinessHours" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${capitalAccount.fbusinessHours}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">账目类型：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/account/capitalaccount/capitalAccount/selectaccountType" id="accountType" name="accountType.id"  value="${capitalAccount.accountType.id}"  title="选择账目类型" labelName="accountType.ftypename" 
						 labelValue="${capitalAccount.accountType.ftypename}" cssClass="form-control required" fieldLabels="账目类型|备注" fieldKeys="ftypename|remarks" searchLabel="账目类型" searchKey="ftypename" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">结算账户：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/account/capitalaccount/capitalAccount/selectclearingAccount" id="clearingAccount" name="clearingAccount.id"  value="${capitalAccount.clearingAccount.id}"  title="选择结算账户" labelName="clearingAccount.faccountname" 
						 labelValue="${capitalAccount.clearingAccount.faccountname}" cssClass="form-control required" fieldLabels="账户名称|备注" fieldKeys="faccountname|remarks" searchLabel="结算账户" searchKey="faccountname" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">交易人：</label></td>
					<td class="width-35">
						<form:input path="ftrader" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">收入：</label></td>
					<td class="width-35">
						<form:input path="fincome" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">支出：</label></td>
					<td class="width-35">
						<form:input path="fexpenditure" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">盈利：</label></td>
					<td class="width-35">
						<form:input path="fprofit" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">账号余额：</label></td>
					<td class="width-35">
						<form:input path="faccountBalance" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">期初金额：</label></td>
					<td class="width-35">
						<form:input path="finitialamount" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收入/支出：</label></td>
					<td class="width-35">
						<form:select path="fexpenditureflag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('fund_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>