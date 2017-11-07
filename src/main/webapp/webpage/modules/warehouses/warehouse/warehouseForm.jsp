<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>仓库管理</title>
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
	<form:form id="inputForm" modelAttribute="warehouse"
		action="${ctx}/warehouses/warehouse/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>

				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>仓库名：</label></td>
					<td class="width-35"><form:input path="fname"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">仓库状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('sys_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<tr>

					<td class="active"><label class="pull-right"><font
							color="red">*</font>所属门店:</label></td>
					<td><sys:treeselect id="office" name="office.id"
							value="${warehouse.office.id}" labelName="office.name"
							labelValue="${warehouse.office.name}" title="部门"
							url="/sys/office/treeData?type=2"
							cssClass="form-control required" notAllowSelectParent="true" /></td>


					<td class="width-15" class="active"><label class="pull-right">仓库负责人:</label></td>
					<td class="width-35"><sys:treeselect id="chargePerson"
							name="chargePerson.id" value="${warehouse.chargePerson.id}"
							labelName="chargePerson.name"
							labelValue="${warehouse.chargePerson.name}" title="用户"
							url="/sys/office/treeData?type=3" cssClass="form-control"
							allowClear="true" notAllowSelectParent="true" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
				</tr>

			</tbody>
		</table>
	</form:form>
</body>
</html>