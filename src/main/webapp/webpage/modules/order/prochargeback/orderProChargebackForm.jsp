<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>采购退单管理</title>
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
	<form:form id="inputForm" modelAttribute="orderProChargeback"
		action="${ctx}/order/prochargeback/orderProChargeback/save"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">采购退单单号：</label></td>
					<td class="width-35"><form:input path="fordernum"
							htmlEscape="false" class="form-control " readonly="true"/></td>
					<td class="width-15 active"><label class="pull-right">退单结束时间：</label></td>
					<td class="width-35"><input id="fendtime" name="fendtime"
						type="text" maxlength="20"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${orderProChargeback.fendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
						<font color="#999999">订单完毕时会自动生成</font>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>供应商：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/prochargeback/orderProChargeback/selectfsupplier"
							id="fsupplier" name="fsupplier.id"
							value="${orderProChargeback.fsupplier.id}" title="选择供应商"
							labelName="fsupplier.fname"
							labelValue="${orderProChargeback.fsupplier.fname}"
							cssClass="form-control required" fieldLabels="供应商|备注"
							fieldKeys="fname|remarks" searchLabel="供应商名称" searchKey="fname"></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>仓库：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/prochargeback/orderProChargeback/selectfwarehose"
							id="fwarehose" name="fwarehose.id"
							value="${orderProChargeback.fwarehose.id}" title="选择仓库"
							labelName="fwarehose.fname"
							labelValue="${orderProChargeback.fwarehose.fname}"
							cssClass="form-control required" fieldLabels="仓库|备注"
							fieldKeys="fname|remarks" searchLabel="仓库名 " searchKey="fname"></sys:gridselect>
					</td>
					<!-- 
					<td class="width-15 active"><label class="pull-right">审批人：</label></td>
					<td class="width-35"><sys:treeselect id="fseniorarchirist"
							name="fseniorarchirist.id"
							value="${orderProChargeback.fseniorarchirist.id}"
							labelName="fseniorarchirist.loginName"
							labelValue="${orderProChargeback.fseniorarchirist.loginName}"
							title="用户" url="/sys/office/treeData?type=3"
							cssClass="form-control " allowClear="true"
							notAllowSelectParent="true" /></td>
					 -->
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>采购退单状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control required">
							<form:option value="" label="" />
							<form:options
								items="${fns:getDictList('procurement_charge_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>采购退单类型：</label></td>
					<td class="width-35"><form:select path="fordertype"
							class="form-control required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('chargeback_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<tr>
					
					
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>结算账户：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/prochargeback/orderProChargeback/selectfdclearingaccount"
							id="fdclearingaccount" name="fdclearingaccount"
							value="${orderProChargeback.fdclearingaccount.id}"
							title="选择结算账户" labelName="fdclearingaccount.faccountname"
							labelValue="${orderProChargeback.fdclearingaccount.faccountname}"
							cssClass="form-control required" fieldLabels="账号名称|备注"
							fieldKeys="faccountname|remarks" searchLabel="账户名"
							searchKey="faccountname"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账目类型：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/order/prochargeback/orderProChargeback/selectfdaccounttype"
							id="fdaccounttype" name="fdaccounttype"
							value="${orderProChargeback.fdaccounttype.id}"
							title="选择其他费用类型"
							labelName="fdaccounttype.ftypename"
							labelValue="${orderProChargeback.fdaccounttype.ftypename}"
							cssClass="form-control required" fieldLabels="账目类型|备注"
							fieldKeys="ftypename|remarks" searchLabel="账目类型"
							searchKey="ftypename"></sys:gridselect></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">其他费用：</label></td>
					<td class="width-35"><form:input path="fothermoney"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">商品总额：</label></td>
					<td class="width-35"><form:input path="fcountprice"
							htmlEscape="false" class="form-control " readonly="true"/></td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">订单折扣(1~100)%：</label></td>
					<td class="width-35"><form:input path="forderdiscount"
							id="forderdiscount" htmlEscape="false" class="form-control number " onblur="checkdiscount()"/></td>
					<td class="width-15 active"><label class="pull-right">订单总价：</label></td>
					<td class="width-35"><form:input path="fordercountprice"
							htmlEscape="false" class="form-control " readonly="true"/></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">抹零：</label></td>
					<td class="width-35"><form:input path="fcutsmall"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">最终金额：</label></td>
					<td class="width-35"><form:input path="fdiscountprice"
							htmlEscape="false" class="form-control " readonly="true"/></td>
	
					<!-- 
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>商户：</label></td>
					<td class="width-35"><sys:treeselect id="fsponsor"
							name="fsponsor.id"
							value="${orderProChargeback.fsponsor.id}"
							labelName="fsponsor.name"
							labelValue="${orderProChargeback.fsponsor.name}"
							title="用户" url="/sys/office/treeData?type=1"
							cssClass="form-control " allowClear="true"
							notAllowSelectParent="true" /></td>
					 -->
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">实收款：</label></td>
					<td class="width-35"><form:input path="factualpayment"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">欠款：</label></td>
					<td class="width-35"><form:input path="fdebt"
							htmlEscape="false" class="form-control " readonly="true"/></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>执行人：</label></td>
					<td class="width-35"><sys:treeselect id="fexecutor"
							name="fexecutor.id" value="${orderProChargeback.fexecutor.id}"
							labelName="fexecutor.loginName"
							labelValue="${orderProChargeback.fexecutor.loginName}" title="用户"
							url="/sys/office/treeData?type=3" cssClass="form-control required"
							allowClear="true" notAllowSelectParent="true" /></td>
					<td class="width-15 active"><label class="pull-right">是否打印：</label></td>
					<td class="width-35"><form:select path="fprint"
							class="form-control">
							<form:option value="1" label="不打印" />
							<form:option value="0" label="打印" />
						</form:select></td>
					
				</tr>
				
				<tr>
					
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="2" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">打印模板：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarket/selectsysModelType"
							id="sysModelType" name="sysModelType.id"
							value="${orderProChargeback.fmodeltype.id}" title="选择打印模板"
							labelName="sysModelType.fname"
							labelValue="${orderProChargeback.fmodeltype.fname}"
							cssClass="form-control" fieldLabels="模板名称|备注信息"
							fieldKeys="fname|remarks" searchLabel="模板名称" searchKey="fname"></sys:gridselect>
					</td>
				</tr>
			</tbody>
		</table>

		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1"
					aria-expanded="true">采购退单详细表：</a></li>
			</ul>
			<div class="tab-content">
				<div id="tab-1" class="tab-pane active">
				<!-- -->
					<a class="btn btn-white btn-sm"
						onclick="addRow('#orderProChaDetailList', orderProChaDetailRowIdx, orderProChaDetailTpl);orderProChaDetailRowIdx = orderProChaDetailRowIdx + 1;"
						title="新增"><i class="fa fa-plus"></i> 新增</a>
				 	<a class="btn btn-white btn-sm"
						href="${ctx}/order/prochargeback/orderProChargeback/exportproChaDetail?id=${orderProChargeback.id}"
						title="导出"><i class="fa fa-plus"></i> 导出</a>
					<table id="contentTable"
						class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								
								<th>商品</th>
								<th>商品货号</th>
								<!-- 
								<th>商品颜色</th>
								<th>商品尺寸</th>
								 -->
								<th>商品数量</th>
								<th>商品单价</th>
								<th>折扣</th>
								<th>折后单价</th>
								<th>折扣前总额</th>
								<th>折扣后总额</th>
								<th>状态<th>
								<th>备注信息</th>
								<th width="10">&nbsp;</th>
							</tr>
						</thead>
						<tbody id="orderProChaDetailList">
						</tbody>
					</table>
					<script type="text/template" id="orderProChaDetailTpl">//<!--
				<tr id="orderProChaDetailList{{idx}}">
					<td class="hide">
						<input id="orderProChaDetailList{{idx}}_id" name="orderProChaDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="orderProChaDetailList{{idx}}_delFlag" name="orderProChaDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					 
					<td class="width-25">
							<sys:gridselect url="${ctx}/order/prochargeback/orderProChargeback/selectfspu" id="orderProChaDetailList{{idx}}_fspu" name="orderProChaDetailList[{{idx}}].fspu.id"  value="{{row.fspu.id}}"  title="选择商品" labelName="orderProChaDetailList{{idx}}.fspu.fgoodsname" 
						 labelValue="{{row.fspu.fgoodsname}}" cssClass="form-control required" fieldLabels="商品名称|备注" fieldKeys="fgoodsname|remarks" searchLabel="商品名称" searchKey="fgoodsname" ></sys:gridselect>
					</td>
					<td class="width-25">
							<sys:gridselect url="${ctx}/order/prochargeback/orderProChargeback/selectfsku" id="orderProChaDetailList{{idx}}_fsku" name="orderProChaDetailList[{{idx}}].fsku.id"  value="{{row.fsku.id}}"  title="选择商品详情" labelName="orderProChaDetailList{{idx}}.fsku.fgoodsnumber" 
						 labelValue="{{row.fsku.fgoodsnumber}}" cssClass="form-control required" fieldLabels="商品详情|备注" fieldKeys="fgoodsnumber|remarks" searchLabel="货号" searchKey="fgoodsnumber" ></sys:gridselect>
					</td>
					<!--
					<td>
						<input id="orderProChaDetailList{{idx}}_fspu" name="orderProChaDetailList[{{idx}}].fspu.fgoodsname" type="text" value="{{row.fspu.fgoodsname}}"    class="form-control " readonly/>
					</td>
					<td>
						<input id="orderProChaDetailList{{idx}}_fsku" name="orderProChaDetailList[{{idx}}].fsku.fgoodsnumber" type="text" value="{{row.fsku.fgoodsnumber}}"    class="form-control " readonly/>
					</td>
					
					<td>
						<input id="orderProChaDetailList{{idx}}_fsku" name="orderProChaDetailList[{{idx}}].fsku.colors.fcolorname" type="text" value="{{row.fsku.colors.fcolorname}}"    class="form-control " readonly/>
					</td>
					<td>
						<input id="orderProChaDetailList{{idx}}_fsku" name="orderProChaDetailList[{{idx}}].fsku.size.fsizename" type="text" value="{{row.fsku.size.fsizename}}"    class="form-control " readonly/>
					</td>
					-->	
					<td>
						<input id="orderProChaDetailList{{idx}}_fgoodsnum" name="orderProChaDetailList[{{idx}}].fgoodsnum" type="text" value="{{row.fgoodsnum}}"    class="form-control required "/>
					</td>
					
					
					<td>
						<input id="orderProChaDetailList{{idx}}_fgoodsprice" name="orderProChaDetailList[{{idx}}].fgoodsprice" type="text" value="{{row.fgoodsprice}}"    class="form-control required "/>
					</td>
					
					
					<td>
						<input id="orderProChaDetailList{{idx}}_fdiscount" name="orderProChaDetailList[{{idx}}].fdiscount" type="text" value="{{row.fdiscount}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="orderProChaDetailList{{idx}}_fdiscountPrice" name="orderProChaDetailList[{{idx}}].fdiscountPrice" type="text" value="{{row.fdiscountPrice}}"    class="form-control " readonly/>
					</td>
					
				

					<td>
						<input id="orderProChaDetailList{{idx}}_fcountprice" name="orderProChaDetailList[{{idx}}].fcountprice" type="text" value="{{row.fcountprice}}"    class="form-control " readonly/>
					</td>
					
					
					<td>
						<input id="orderProChaDetailList{{idx}}_fdiscountcountprice" name="orderProChaDetailList[{{idx}}].fdiscountcountprice" type="text" value="{{row.fdiscountcountprice}}"    class="form-control " readonly/>
					</td>	
					<td>
						<select style="width:75px;" id="orderProChaDetailList{{idx}}_fstatus" name="orderProChaDetailList[{{idx}}].fstatus" data-value="{{row.fstatus}}" class="form-control m-b  ">
							<option value="1">启用</option>
							<option value="2">禁用</option>
							
						</select>
					</td>				
					<td>
						<textarea id="orderProChaDetailList{{idx}}_remarks" name="orderProChaDetailList[{{idx}}].remarks" rows="2"    class="form-control ">{{row.remarks}}</textarea>
					</td>					

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#orderProChaDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
					<script type="text/javascript">
				var orderProChaDetailRowIdx = 0, orderProChaDetailTpl = $("#orderProChaDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(orderProChargeback.orderProChaDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#orderProChaDetailList', orderProChaDetailRowIdx, orderProChaDetailTpl, data[i]);
						orderProChaDetailRowIdx = orderProChaDetailRowIdx + 1;
					}
				});
			</script>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>