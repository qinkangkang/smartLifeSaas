<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>销售退货单管理</title>
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
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="orderMarketCha"
		action="${ctx}/market/ordermarketchargeback/orderMarketCha/save"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">销售退单单号：</label></td>
					<td class="width-35"><form:input path="fordernum"
							htmlEscape="false" class="form-control " readonly="true" /></td>
					<td class="width-15 active"><label class="pull-right">客户名称：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectcustomerBasic"
							id="customerBasic" name="customerBasic.id"
							value="${orderMarketCha.customerBasic.id}" title="选择客户名称"
							labelName="customerBasic.fname"
							labelValue="${orderMarketCha.customerBasic.fname}"
							cssClass="form-control" fieldLabels="客户姓名|备注"
							fieldKeys="fname|remarks" searchLabel="客户名称" searchKey="fname"></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">仓库名称：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectwarehouse"
							id="warehouse" name="warehouse.id"
							value="${orderMarketCha.warehouse.id}" title="选择仓库名称"
							labelName="warehouse.fname"
							labelValue="${orderMarketCha.warehouse.fname}"
							cssClass="form-control required" fieldLabels="仓库名称|备注"
							fieldKeys="fname|remarks" searchLabel="仓库名称" searchKey="fname"></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">结算账户：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectclearingAccount"
							id="clearingAccount" name="clearingAccount.id"
							value="${orderMarketCha.clearingAccount.id}" title="选择结算账户"
							labelName="clearingAccount.faccountname"
							labelValue="${orderMarketCha.clearingAccount.faccountname}"
							cssClass="form-control" fieldLabels="结算账户|备注"
							fieldKeys="faccountname|remarks" searchLabel="结算账户"
							searchKey="faccountname"></sys:gridselect></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">发货方式
							：</label></td>
					<td class="width-35"><form:select path="fshipmenttype"
							class="form-control ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('shipment_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
					<td class="width-15 active"><label class="pull-right">订单类型
							：</label></td>
					<td class="width-35"><form:select path="fordertype"
							class="form-control  required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('chargeback_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单状态：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control  required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('market_charge_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
					<td class="width-15 active"><label class="pull-right">销售单结束时间：</label></td>
					<td class="width-35"><input id="fendtime" name="fendtime"
						type="text" maxlength="20"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${orderMarketCha.fendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>

				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">其他费用：</label></td>
					<td class="width-35"><form:input path="fothermoney"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">账目类型：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectaccountType"
							id="accountType" name="accountType.id"
							value="${orderMarketCha.accountType.id}" title="选择账目类型"
							labelName="accountType.ftypename"
							labelValue="${orderMarketCha.accountType.ftypename}"
							cssClass="form-control" fieldLabels="账目类型|备注"
							fieldKeys="ftypename|remarks" searchLabel="账目类型"
							searchKey="ftypename"></sys:gridselect></td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品总价：</label></td>
					<td class="width-35"><form:input path="fcountprice"
							htmlEscape="false" class="form-control " readonly="true"/></td>
					<td class="width-15 active"><label class="pull-right">抹零：</label></td>
					<td class="width-35"><form:input path="fcutsmall"
							htmlEscape="false" class="form-control " /></td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单总额：</label></td>
					<td class="width-35"><form:input path="fordercountprice"
							htmlEscape="false" class="form-control " readonly="true"/></td>
					<td class="width-15 active"><label class="pull-right">折扣(%)：</label></td>
					<td class="width-35"><form:input path="forderdiscount"
							htmlEscape="false" class="form-control " /></td>
					
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">折后金额：</label></td>
					<td class="width-35"><form:input path="fdiscountprice"
							htmlEscape="false" class="form-control " readonly="true"/></td>
					<td class="width-15 active"><label class="pull-right">客户折扣：</label></td>
					<td class="width-35"><form:input path="customerBasic.fdiscount"
							htmlEscape="false" class="form-control " readonly="true"/></td>
					
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right">实付款：</label></td>
					
					<td class="width-35"><form:input path="fproceeds"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">欠款：</label></td>
					<td class="width-35"><form:input path="fdebt"
							htmlEscape="false" class="form-control " readonly="true"/></td>
					
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">是否打印：</label></td>
					<td class="width-35"><form:select path="fprint"
							class="form-control required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('print_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
					<td class="width-15 active"><label class="pull-right">打印模板：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectfmodeltype"
							id="fmodeltype" name="fmodeltype.id"
							value="${orderMarketCha.fmodeltype.id}" title="选择打印模板"
							labelName="fmodeltype.fname"
							labelValue="${orderMarketCha.fmodeltype.fname}"
							cssClass="form-control" fieldLabels="打印模板|备注"
							fieldKeys="fname|remarks" searchLabel="打印模板" searchKey="fname"></sys:gridselect>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单形式：</label></td>
					<td class="width-35"><form:select path="fordermodel"
							class="form-control  required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('order_model')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
					<!-- 
					<td class="width-15 active"><label class="pull-right">商户：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectfsponsor"
							id="fsponsor" name="fsponsor.id"
							value="${orderMarketCha.fsponsor.id}" title="选择商户"
							labelName="fsponsor.name"
							labelValue="${orderMarketCha.fsponsor.name}"
							cssClass="form-control required" fieldLabels="商户名|备注"
							fieldKeys="name|remarks" searchLabel="所属商户" searchKey="name"></sys:gridselect>
					</td>
					 -->
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea row="4" path="remarks"
							htmlEscape="false" class="form-control " /></td>
				</tr>
			</tbody>
		</table>

		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1"
					aria-expanded="true">销售退单详细表：</a></li>
			</ul>
			<div class="tab-content">
				<div id="tab-1" class="tab-pane active">
					<a class="btn btn-white btn-sm"
						onclick="addRow('#orderMarketChaDetailList', orderMarketChaDetailRowIdx, orderMarketChaDetailTpl);orderMarketChaDetailRowIdx = orderMarketChaDetailRowIdx + 1;"
						title="新增"><i class="fa fa-plus"></i> 新增</a>
					<a class="btn btn-white btn-sm"
						href="${ctx}/market/ordermarketchargeback/orderMarketCha/exportmarketChaDetail?id=${orderMarketCha.id}"
						title="导出"><i class="fa fa-plus"></i> 导出</a>
					<table id="contentTable"
						class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>销售单</th>
								<th>商品</th>
								<th>商品货号</th>
								<th>商品颜色</th>
								<th>商品尺寸</th>
								<th>商品数量</th>
								<th>商品单价</th>
								<th>商品折扣</th>
								<th>折后单价</th>
								<th>折扣前金额</th>
								<th>折后总额</th>
								<th>备注信息</th>
								<th width="10">&nbsp;</th>
							</tr>
						</thead>
						<tbody id="orderMarketChaDetailList">
						</tbody>
					</table>
					<script type="text/template" id="orderMarketChaDetailTpl">//<!--
				<tr id="orderMarketChaDetailList{{idx}}">
					<td class="hide">
						<input id="orderMarketChaDetailList{{idx}}_id" name="orderMarketChaDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="orderMarketChaDetailList{{idx}}_delFlag" name="orderMarketChaDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td class="width-20">
							<sys:gridselect url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectfordermarket" id="orderMarketChaDetailList{{idx}}_fordermarket" name="orderMarketChaDetailList[{{idx}}].fordermarket.id"  value="{{row.fordermarket.id}}"  title="选择销售单" labelName="orderMarketChaDetailList{{idx}}.fordermarket.fordernum" 
						 labelValue="{{row.fordermarket.fordernum}}" cssClass="form-control required" fieldLabels="销售单号|备注" fieldKeys="fordernum|remarks" searchLabel="销售单号" searchKey="fordernum" ></sys:gridselect>
					</td>
					
					<td class="width-20">
							<sys:gridselect url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectfspu" id="orderMarketChaDetailList{{idx}}_fspu" name="orderMarketChaDetailList[{{idx}}].fspu.id"  value="{{row.fspu.id}}"  title="选择商品" labelName="orderMarketChaDetailList{{idx}}.fspu.fgoodsname" 
						 labelValue="{{row.fspu.fgoodsname}}" cssClass="form-control required" fieldLabels="商品名称|备注" fieldKeys="fgoodsname|remarks" searchLabel="商品名称" searchKey="fgoodsname" ></sys:gridselect>
					</td>
					<td class="width-20">
							<sys:gridselect url="${ctx}/market/ordermarketchargeback/orderMarketCha/selectfsku" id="orderMarketChaDetailList{{idx}}_fsku" name="orderMarketChaDetailList[{{idx}}].fsku.id"  value="{{row.fsku.id}}"  title="选择商品详情" labelName="orderMarketChaDetailList{{idx}}.fsku.fgoodsnumber" 
						 labelValue="{{row.fsku.fgoodsnumber}}" cssClass="form-control required" fieldLabels="商品详情|备注" fieldKeys="fgoodsnumber|remarks" searchLabel="货号" searchKey="fgoodsnumber" ></sys:gridselect>
					</td>
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fsku" name="orderMarketChaDetailList[{{idx}}].fsku.colors.fcolorname" type="text" value="{{row.fsku.colors.fcolorname}}"    class="form-control " readonly/>
					</td>
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fsku" name="orderMarketChaDetailList[{{idx}}].fsku.size.fsizename" type="text" value="{{row.fsku.size.fsizename}}"    class="form-control " readonly/>
					</td>
					
					
					
					
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fgoodsnum" name="orderMarketChaDetailList[{{idx}}].fgoodsnum" type="text" value="{{row.fgoodsnum}}"    class="form-control required "/>
					</td>
					
					
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fgoodsprice" name="orderMarketChaDetailList[{{idx}}].fgoodsprice" type="text" value="{{row.fgoodsprice}}"    class="form-control required " />
					</td>
					
					
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fgoodsdiscount" name="orderMarketChaDetailList[{{idx}}].fgoodsdiscount" type="text" value="{{row.fgoodsdiscount}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fdiscountprice" name="orderMarketChaDetailList[{{idx}}].fdiscountprice" type="text" value="{{row.fdiscountprice}}"    class="form-control " readonly/>
					</td>
					
					
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fcountmoney" name="orderMarketChaDetailList[{{idx}}].fcountmoney" type="text" value="{{row.fcountmoney}}"    class="form-control " readonly/>
					</td>
					
					
					<td>
						<input id="orderMarketChaDetailList{{idx}}_fdiscountcountmoney" name="orderMarketChaDetailList[{{idx}}].fdiscountcountmoney" type="text" value="{{row.fdiscountcountmoney}}"    class="form-control " readonly/>
					</td>
					<td>
						<textarea id="orderMarketChaDetailList{{idx}}_remarks" name="orderMarketChaDetailList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#orderMarketChaDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
					<script type="text/javascript">
				var orderMarketChaDetailRowIdx = 0, orderMarketChaDetailTpl = $("#orderMarketChaDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(orderMarketCha.orderMarketChaDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#orderMarketChaDetailList', orderMarketChaDetailRowIdx, orderMarketChaDetailTpl, data[i]);
						orderMarketChaDetailRowIdx = orderMarketChaDetailRowIdx + 1;
					}
				});
			</script>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>