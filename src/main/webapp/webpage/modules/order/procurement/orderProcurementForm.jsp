<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购单管理</title>
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
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
	<script type="text/javascript">
	//折扣JS
	
	function checkdiscount(){
		var discount=$("#forderdiscount").val();
		if(discount<=0||discount>100){
			alert('折扣不能小于等于0或大于100');
			$("#forderdiscount").val(100);
			 return false; 
		}
		
	}
	</script>
	
	
	
	
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="orderProcurement"
		action="${ctx}/order/procurement/orderProcurement/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>供应商：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProcurement/selectfsupplier"
							id="fsupplier" name="fsupplier.id"
							value="${orderProcurement.fsupplier.id}" title="选择供应商"
							labelName="fsupplier.fname"
							labelValue="${orderProcurement.fsupplier.fname}"
							cssClass="form-control required" fieldLabels="供应商名|备注"
							fieldKeys="fname|remarks" searchLabel="供应商" searchKey="fname"></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>仓库：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProcurement/selectfwarehose"
							id="fwarehose" name="fwarehose.id"
							value="${orderProcurement.fwarehose.id}" title="选择仓库"
							labelName="fwarehose.fname"
							labelValue="${orderProcurement.fwarehose.fname}"
							cssClass="form-control required" fieldLabels="仓库名|备注"
							fieldKeys="fname|remarks" searchLabel="所属仓库" searchKey="fname"></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>订单类型 ：</label></td>
					<td class="width-35"><form:select path="fordertype"
							class="form-control required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('procurement_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>订单状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('procurement_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>结算账户：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProcurement/selectfdclearingaccountid"
							id="fdclearingaccountid" name="fdclearingaccountid.id"
							value="${orderProcurement.fdclearingaccountid.id}" title="选择结算账户"
							labelName="fdclearingaccountid.faccountname"
							labelValue="${orderProcurement.fdclearingaccountid.faccountname}"
							cssClass="form-control required" fieldLabels="账户名称|备注"
							fieldKeys="faccountname|remarks" searchLabel="结算账户"
							searchKey="faccountname"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账目类型：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProcurement/selectfdaccounttype"
							id="fdaccounttype" name="fdaccounttype.id"
							value="${orderProcurement.fdaccounttype.id}" title="选择账目类型"
							labelName="fdaccounttype.ftypename"
							labelValue="${orderProcurement.fdaccounttype.ftypename}"
							cssClass="form-control required" fieldLabels="账目类型|备注"
							fieldKeys="ftypename|remarks" searchLabel="其他费用类型"
							searchKey="ftypename"></sys:gridselect></td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">其他费用：</label></td>
					<td class="width-35"><form:input path="fothermoney"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">抹零：</label></td>
					<td class="width-35"><form:input path="fcutsmall"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单折扣(1~100)%：</label></td>
					<td class="width-35"><form:input path="forderdiscount"
							id="forderdiscount" htmlEscape="false" class="form-control number " onblur="checkdiscount()"/></td>
					<td class="width-15 active"><label class="pull-right">实付款：</label></td>
					<td class="width-35"><form:input path="factualpayment"
							htmlEscape="false" class="form-control " /></td>		
				</tr>	
				<tr>
					
					<td class="width-15 active"><label class="pull-right">是否打印 ：</label></td>
					<td class="width-35"><form:select path="fprint"
							class="form-control ">
							<form:option value="1" label="不打印" />
							<form:option value="0" label="打印" />
						</form:select></td>
					<td class="width-15 active"><label class="pull-right">打印模板：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/procurement/orderProcurement/selectfmodeltype"
							id="fmodeltype" name="fmodeltype.id"
							value="${orderProcurement.fmodeltype.id}" title="选择打印模板"
							labelName="fmodeltype.fname"
							labelValue="${orderProcurement.fmodeltype.fname}"
							cssClass="form-control" fieldLabels="模板|备注"
							fieldKeys="fname|remarks" searchLabel="打印模板" searchKey="fname"></sys:gridselect>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">执行人：</label></td>
							<td class="width-35">
							<sys:treeselect id="fexecutor"
							name="fexecutor.id" value="${orderProcurement.fexecutor.id}"
							labelName="fexecutor.loginName"
							labelValue="${orderProcurement.fexecutor.loginName}" title="执行人"
							url="/sys/office/treeData?type=3"
							cssClass="form-control" /></td>	
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control "  /></td>
				</tr>			
			</tbody>
		</table>

		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1"
					aria-expanded="true">采购单详细表：</a></li>
			</ul>
			<div class="tab-content">
				<div id="tab-1" class="tab-pane active">
					<a class="btn btn-white btn-sm"
						onclick="addRow('#orderProDetailList', orderProDetailRowIdx, orderProDetailTpl);orderProDetailRowIdx = orderProDetailRowIdx + 1;"
						title="新增"><i class="fa fa-plus"></i> 新增</a>
					<table id="contentTable"
						class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>商品</th>
								<th>商品货号</th>
								<th>商品数量</th>
								<th>商品单价</th>
								<th>商品折扣(1~100)%</th>
								<th>备注信息</th>
								<th width="10">&nbsp;</th>
							</tr>
						</thead>
						<tbody id="orderProDetailList">
						</tbody>
					</table>
					<script type="text/template" id="orderProDetailTpl">//<!--
				<tr id="orderProDetailList{{idx}}">
					<td class="hide">
						<input id="orderProDetailList{{idx}}_id" name="orderProDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="orderProDetailList{{idx}}_delFlag" name="orderProDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td class="width-20">
							<sys:gridselect url="${ctx}/order/procurement/orderProcurement/selectfspu" id="orderProDetailList{{idx}}_fspu" name="orderProDetailList[{{idx}}].fspu.id"  value="{{row.fspu.id}}"  title="选择商品" labelName="orderProDetailList{{idx}}.fspu.fgoodsname" 
						 labelValue="{{row.fspu.fgoodsname}}" cssClass="form-control required" fieldLabels="商品名称|备注" fieldKeys="fgoodsname|remarks" searchLabel="商品名称" searchKey="fgoodsname" ></sys:gridselect>
					</td>	
					<td class="width-20">
							<sys:gridselect url="${ctx}/order/procurement/orderProcurement/selectfsku" id="orderProDetailList{{idx}}_fsku" name="orderProDetailList[{{idx}}].fsku.id"  value="{{row.fsku.id}}"  title="选择商品货号" labelName="orderProDetailList{{idx}}.fsku.fgoodsnumber" 
						 labelValue="{{row.fsku.fgoodsnumber}}" cssClass="form-control required" fieldLabels="商品货号|备注" fieldKeys="fgoodsnumber|remarks" searchLabel="货号" searchKey="fgoodsnumber"></sys:gridselect>
					</td>
					
					<td>
						<input id="orderProDetailList{{idx}}_fgoodsnum" name="orderProDetailList[{{idx}}].fgoodsnum" type="text" value="{{row.fgoodsnum}}"    class="form-control required"/>
					</td>	
					<td>
						<input id="orderProDetailList{{idx}}_fgoodsprice" name="orderProDetailList[{{idx}}].fgoodsprice" type="text" value="{{row.fgoodsprice}}"    class="form-control required"/>
					</td>
					<td>
						<input id="orderProDetailList{{idx}}_fgoodsdiscount" name="orderProDetailList[{{idx}}].fgoodsdiscount" type="text" value="{{row.fgoodsdiscount}}"    class="form-control number required"/>
					</td>
					<td>
						<textarea id="orderProDetailList{{idx}}_remarks" name="orderProDetailList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#orderProDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
					<script type="text/javascript">
				var orderProDetailRowIdx = 0, orderProDetailTpl = $("#orderProDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(orderProcurement.orderProDetailList)};
					
					for (var i=0; i<data.length; i++){
						addRow('#orderProDetailList', orderProDetailRowIdx, orderProDetailTpl, data[i]);
						orderProDetailRowIdx = orderProDetailRowIdx + 1;
					}
				});
			</script>
				</div>
			</div>



		</div>
	</form:form>
</body>
</html>