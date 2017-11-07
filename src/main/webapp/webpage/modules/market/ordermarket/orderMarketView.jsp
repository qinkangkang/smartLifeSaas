<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>销售订单管理</title>
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
			            elem: '#fendtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
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
	<form:form id="inputForm" modelAttribute="orderMarket"
		action="${ctx}/market/ordermarket/orderMarket/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			
			
				
				<tr>
					<td class="width-15 active"><label class="pull-right">销售单号：</label></td>
					<td class="width-35"><form:input path="fordernum"
							htmlEscape="false" class="form-control " readonly="true" /></td>
					<td class="width-15 active"><label class="pull-right">客户名称：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarket/selectcustomerBasic"
							id="customerBasic" name="customerBasic.id"
							value="${orderMarket.customerBasic.id}" title="选择客户名称"
							labelName="customerBasic.fname"
							labelValue="${orderMarket.customerBasic.fname}"
							cssClass="form-control" fieldLabels="客户名称|备注信息"
							fieldKeys="fname|remarks" searchLabel="客户名称" searchKey="fname"></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>仓库名称：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarket/selectwarehouse"
							id="warehouse" name="warehouse.id"
							value="${orderMarket.warehouse.id}" title="选择仓库名称"
							labelName="warehouse.fname"
							labelValue="${orderMarket.warehouse.fname}"
							cssClass="form-control required" fieldLabels="仓库名称|备注信息"
							fieldKeys="fname|remarks" searchLabel="仓库名称" searchKey="fname"></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>账目类型：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarket/selectaccountType"
							id="accountType" name="accountType.id"
							value="${orderMarket.accountType.id}" title="选择账目类型"
							labelName="accountType.ftypename"
							labelValue="${orderMarket.accountType.ftypename}"
							cssClass="form-control required" fieldLabels="账目类型|备注信息"
							fieldKeys="ftypename|remarks" searchLabel="账目类型"
							searchKey="ftypename"></sys:gridselect></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>结算账户：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarket/selectclearingAccount"
							id="clearingAccount" name="clearingAccount.id"
							value="${orderMarket.clearingAccount.id}" title="选择结算账户"
							labelName="clearingAccount.faccountname"
							labelValue="${orderMarket.clearingAccount.faccountname}"
							cssClass="form-control required" fieldLabels="账目类型|备注信息"
							fieldKeys="faccountname|remarks" searchLabel="账目类型"
							searchKey="faccountname"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right">发货方式：</label></td>
					<td class="width-35"><form:select path="fshipmenttype"
							class="form-control ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('shipment_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>订单类型：</label></td>
					<td class="width-35"><form:select path="fordertype"
							class="form-control  required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('sdelas_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>订单状态
							：</label></td>
					<td class="width-35"><form:select path="fstatus"
							class="form-control required ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('sdeals_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">销售单结束时间：</label></td>
					<td class="width-35"><input id="fendtime" name="fendtime"
						type="text" maxlength="20"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${orderMarket.fendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
					<td class="width-15 active"><label class="pull-right">其他费用：</label></td>
					<td class="width-35"><form:input path="fothermoney"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品总价：</label></td>
					<td class="width-35"><form:input path="fcountprice"
							htmlEscape="false" class="form-control " readonly="true" /></td>
					<td class="width-15 active"><label class="pull-right">抹零：</label></td>
					<td class="width-35"><form:input path="fcutsmall"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单总额：</label></td>
					<td class="width-35"><form:input path="fordercountprice"
							htmlEscape="false" class="form-control " readonly="true" /></td>
					<td class="width-15 active"><label class="pull-right">折扣(%)：</label></td>
					<td class="width-35"><form:input path="forderdiscount"
							htmlEscape="false" class="form-control " onblur="checkdiscount()"/></td>		

				</tr>
				<tr>
				
					<td class="width-15 active"><label class="pull-right">折后金额：</label></td>
					<td class="width-35"><form:input id="fdiscountprice" path="fdiscountprice"
							htmlEscape="false" class="form-control " readonly="true"/></td>
					<td class="width-15 active"><label class="pull-right">客户折扣：</label></td>
					<td class="width-35"><form:input path="customerBasic.fdiscount" 
							htmlEscape="false" class="form-control "  readonly="true"/></td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">实收款：</label></td>
					<td class="width-35"><form:input path="fproceeds"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">欠款：</label></td>
					<td class="width-35"><form:input path="fdebt"
							htmlEscape="false" class="form-control " readonly="true"/></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否打印：</label></td>
					<td class="width-35"><form:select path="fprint"
							class="form-control required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('print_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
					<td class="width-15 active"><label class="pull-right">打印模板：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarket/selectsysModelType"
							id="sysModelType" name="sysModelType.id"
							value="${orderMarket.sysModelType.id}" title="选择打印模板"
							labelName="sysModelType.fname"
							labelValue="${orderMarket.sysModelType.fname}"
							cssClass="form-control" fieldLabels="模板名称|备注信息"
							fieldKeys="fname|remarks" searchLabel="模板名称" searchKey="fname"></sys:gridselect>
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>订单形式：</label></td>
					<td class="width-35"><form:select path="fordermodel"
							class="form-control  required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('order_model')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				<!-- 
					<td class="width-15 active"><label class="pull-right">商户：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/market/ordermarket/orderMarket/selectfsponsor"
							id="fsponsor" name="fsponsor.id"
							value="${orderMarket.fsponsor.id}" title="选择商户名称"
							labelName="fsponsor.name"
							labelValue="${orderMarket.fsponsor.name}"
							cssClass="form-control" fieldLabels="商户名称|备注信息"
							fieldKeys="name|remarks" searchLabel="商户名称" searchKey="name"></sys:gridselect>
					</td>
				 -->
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="2" class="form-control " /></td>
				</tr>
			</tbody>
		</table>

		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1"
					aria-expanded="true">销售单详细表：</a></li>
			</ul>
			<div class="tab-content">
				<div id="tab-1" class="tab-pane active">
					<a class="btn btn-white btn-sm"
						onclick="addRow('#orderMarketDetailList', orderMarketDetailRowIdx, orderMarketDetailTpl);orderMarketDetailRowIdx = orderMarketDetailRowIdx + 1;"
						title="新增"><i class="fa fa-plus"></i> 新增</a>
					<a class="btn btn-white btn-sm"
						href="${ctx}/market/ordermarket/orderMarket/exportmarketDetail?id=${orderMarket.id}"
						title="导出"><i class="fa fa-plus"></i> 导出</a>
					<table id="contentTable"
						class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>商品</th>	
								<th>商品货号</th>
								<th>商品数量</th>
								<th>商品原价</th>
								<th>折扣(%)</th>
								<th>折后单价</th>
								<th>折扣前金额</th>
								<th>折后总额</th>
								<th>状态</th>
								<th>备注信息</th>
								<th width="10">&nbsp;</th>
							</tr>
						</thead>
						<tbody id="orderMarketDetailList">
						</tbody>
					</table>
					<script type="text/template" id="orderMarketDetailTpl">//<!--
				<tr id="orderMarketDetailList{{idx}}">
					<td class="hide">
						<input id="orderMarketDetailList{{idx}}_id" name="orderMarketDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="orderMarketDetailList{{idx}}_delFlag" name="orderMarketDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td class="width-20">
							<sys:gridselect url="${ctx}/market/ordermarket/orderMarket/selectfspu" id="orderMarketDetailList{{idx}}_wgi" name="orderMarketDetailList[{{idx}}].goodsSpu.id"  value="{{row.goodsSpu.id}}"  title="选择商品" labelName="orderMarketDetailList{{idx}}.goodsSpu.fgoodsname" 
						 labelValue="{{row.goodsSpu.fgoodsname}}" cssClass="form-control required" fieldLabels="商品名称|备注" fieldKeys="fgoodsname|remarks" searchLabel="商品名称" searchKey="fgoodsname" ></sys:gridselect>
					</td>					
					<td class="width-20">
							<sys:gridselect url="${ctx}/market/ordermarket/orderMarket/selectfsku" id="orderMarketDetailList{{idx}}_goodsSku" name="orderMarketDetailList[{{idx}}].goodsSku.id"  value="{{row.goodsSku.id}}"  title="选择商品详情" labelName="orderMarketDetailList{{idx}}.goodsSku.fgoodsnumber" 
						 labelValue="{{row.goodsSku.fgoodsnumber}}" cssClass="form-control required" fieldLabels="商品详情|备注" fieldKeys="fgoodsnumber|remarks" searchLabel="货号" searchKey="fgoodsnumber" ></sys:gridselect>
					</td>
					<td>
						<input id="orderMarketDetailList{{idx}}_fgoodsnum" name="orderMarketDetailList[{{idx}}].fgoodsnum" type="text" value="{{row.fgoodsnum}}"    class="form-control required " size="5"/>
					</td>
					
					
					<td>
						<input id="orderMarketDetailList{{idx}}_fgoodsprice" name="orderMarketDetailList[{{idx}}].fgoodsprice" type="text" value="{{row.fgoodsprice}}"    class="form-control required " size="5"/>
					</td>
					
					
					<td>
						<input id="orderMarketDetailList{{idx}}_fgoodsdiscount" name="orderMarketDetailList[{{idx}}].fgoodsdiscount" type="text" value="{{row.fgoodsdiscount}}"    class="form-control required" size="5"/>
					</td>
					
					
					<td>
						<input id="orderMarketDetailList{{idx}}_fdiscountprice" name="orderMarketDetailList[{{idx}}].fdiscountprice" type="text" value="{{row.fdiscountprice}}"    class="form-control " readonly size="5"/>
					</td>
					
					
					<td>
						<input id="orderMarketDetailList{{idx}}_fcountmoney" name="orderMarketDetailList[{{idx}}].fcountmoney" type="text" value="{{row.fcountmoney}}"    class="form-control " readonly size="5"/>
					</td>
					
					
					<td>
						<input id="orderMarketDetailList{{idx}}_fdiscountcountmoney" name="orderMarketDetailList[{{idx}}].fdiscountcountmoney" type="text" value="{{row.fdiscountcountmoney}}"    class="form-control " readonly size="5"/>
					</td>
					
					
					
					
					<td>
						<select style="width:75px;" id="orderMarketDetailList{{idx}}_fstatus" name="orderMarketDetailList[{{idx}}].fstatus" data-value="{{row.fstatus}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('sys_status')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<textarea id="orderMarketDetailList{{idx}}_remarks" name="orderMarketDetailList[{{idx}}].remarks" rows="2"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#orderMarketDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
					
				</tr>//-->
			</script>
					<script type="text/javascript">
				var orderMarketDetailRowIdx = 0, orderMarketDetailTpl = $("#orderMarketDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(orderMarket.orderMarketDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#orderMarketDetailList', orderMarketDetailRowIdx, orderMarketDetailTpl, data[i]);
						orderMarketDetailRowIdx = orderMarketDetailRowIdx + 1;
					}
				});
			</script>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>