<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库调拨管理</title>
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
			            elem: '#ftransferDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
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
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="warehouseTransferOrder" action="${ctx}/warehouses/warehouseTransferOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">调拨单号：</label></td>
					<td class="width-35">
						<form:input path="ftransferOrder" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>调出仓库：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseTransferOrder/selectwarehouseOut" id="warehouseOut" name="warehouseOut.id"  value="${warehouseTransferOrder.warehouseOut.id}"  title="选择调出仓库" labelName="warehouseOut.fname" 
						 labelValue="${warehouseTransferOrder.warehouseOut.fname}" cssClass="form-control required" fieldLabels="调出仓库|备注" fieldKeys="fname|remarks" searchLabel="调出仓库" searchKey="fname" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>调入仓库：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseTransferOrder/selectfwarehouseIn" id="fwarehouseIn" name="fwarehouseIn.id"  value="${warehouseTransferOrder.fwarehouseIn.id}"  title="选择调入仓库" labelName="fwarehouseIn.fname" 
						 labelValue="${warehouseTransferOrder.fwarehouseIn.fname}" cssClass="form-control required" fieldLabels="调入仓库|备注" fieldKeys="fname|remarks" searchLabel="调入仓库" searchKey="fname" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>调拨日期：</label></td>
					<td class="width-35">
						<input id="ftransferDate" name="ftransferDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${warehouseTransferOrder.ftransferDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="fstatus" items="${fns:getDictList('transferorder_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">调拨商品：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#warehouseTransferGoodsList', warehouseTransferGoodsRowIdx, warehouseTransferGoodsTpl);warehouseTransferGoodsRowIdx = warehouseTransferGoodsRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>						
						<th>商品</th>
						<th>货号</th>
						<th>条码</th>
						<th>调拨数量</th>
						<th>商品单位</th>
						<th>调出仓库当前数量</th>
						<th>调入仓库当前数量</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="warehouseTransferGoodsList">
				</tbody>
			</table>
			<script type="text/template" id="warehouseTransferGoodsTpl">//<!--
				<tr id="warehouseTransferGoodsList{{idx}}">
					<td class="hide">
						<input id="warehouseTransferGoodsList{{idx}}_id" name="warehouseTransferGoodsList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="warehouseTransferGoodsList{{idx}}_delFlag" name="warehouseTransferGoodsList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td class="width-34">
						<sys:gridselect url="${ctx}/warehouses/warehouseTransferGoods/selectgoodsSpu" id="warehouseTransferGoodsList{{idx}}_goodsSpu" name="warehouseTransferGoodsList[{{idx}}].goodsSpu.id" value="{{row.goodsSpu.id}}" title="选择商品" labelName="goodsSpu.fgoodsname" 
						 labelValue="{{row.goodsSpu.fgoodsname}}" cssClass="form-control required" fieldLabels="商品名|备注" fieldKeys="fgoodsname|remarks" searchLabel="商品名" searchKey="fgoodsname" ></sys:gridselect>
					</td>
					<td class="width-34">
						<sys:gridselect url="${ctx}/warehouses/warehouseTransferGoods/selectgoodsSku" id="warehouseTransferGoodsList{{idx}}_goodsSku" name="warehouseTransferGoodsList[{{idx}}].goodsSku.id"  value="{{row.goodsSku.id}}"  title="选择商品货号" labelName="goodsSku.fgoodsnumber" 
						 labelValue="{{row.goodsSku.fgoodsnumber}}" cssClass="form-control required" fieldLabels="货号|备注" fieldKeys="fgoodsnumber|remarks" searchLabel="货号" searchKey="fgoodsnumber" ></sys:gridselect>
					</td>

					<td>
						<input id="warehouseTransferGoodsList{{idx}}_fbarcode" type="text" value="{{row.goodsSku.fbarcode}}"  disabled="disabled"  class="form-control"/>
					</td>														
					<td>
						<input id="warehouseTransferGoodsList{{idx}}_transferNum" name="warehouseTransferGoodsList[{{idx}}].transferNum" type="text" value="{{row.transferNum}}"    class="form-control required"/>
					</td>	
						
					<td>
						<input id="warehouseTransferGoodsList{{idx}}_goodsUnit" type="text" value="{{row.goodsSpu.goodsUnit.funitname}}"  disabled="disabled"  class="form-control"/>
					</td>	
					<td>
						<input id="warehouseTransferGoodsList{{idx}}_warehouseOutNum" type="text" value="{{row.ftransferOutBeforeNum}}"  disabled="disabled"  class="form-control"/>
					</td>
					<td>
						<input id="warehouseTransferGoodsList{{idx}}_warehouseInNum" type="text" value="{{row.ftransferInBeforeNum}}"  disabled="disabled"  class="form-control"/>
					</td>											
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#warehouseTransferGoodsList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var warehouseTransferGoodsRowIdx = 0, warehouseTransferGoodsTpl = $("#warehouseTransferGoodsTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(warehouseTransferOrder.warehouseTransferGoodsList)};
					for (var i=0; i<data.length; i++){
						addRow('#warehouseTransferGoodsList', warehouseTransferGoodsRowIdx, warehouseTransferGoodsTpl, data[i]);
						warehouseTransferGoodsRowIdx = warehouseTransferGoodsRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>