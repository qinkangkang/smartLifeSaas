<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>仓库盘点管理</title>
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
			            elem: '#checkDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
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
	<form:form id="inputForm" modelAttribute="warehouseCheckOrder" action="${ctx}/warehouses/warehouseCheckOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">盘点单号：</label></td>
					<td class="width-35">
						${warehouseCheckOrder.fcheckOrder }
						<%-- <form:input path="fcheckOrder" htmlEscape="false"  disabled="disabled"   class="form-control "/> --%>
					</td>
					<td class="width-15 active"><label class="pull-right">盘点总数：</label></td>
					<td class="width-35">
						${warehouseCheckOrder.checkTotalNum }
						<%-- <form:input path="checkTotalNum" htmlEscape="false"  class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>仓库：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/warehouses/warehouseCheckOrder/selectwarehouse" id="warehouse" name="warehouse.id"  value="${warehouseCheckOrder.warehouse.id}"  title="选择仓库" labelName="warehouse.fname" 
						 labelValue="${warehouseCheckOrder.warehouse.fname}" cssClass="form-control required" fieldLabels="仓库|备注" fieldKeys="fname|remarks" searchLabel="仓库" searchKey="fname" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">盈亏总数：</label></td>
					<td class="width-35">
						${warehouseCheckOrder.profitLossTotalNum }
						<%-- <form:input path="profitLossTotalNum" htmlEscape="false"  class="form-control "/> --%>
					</td>
				</tr>
				<tr>
				 	<td class="width-15 active"><label class="pull-right">盘点日期：</label></td>
					<td class="width-35">
						 <input id="checkDate" name="checkDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${warehouseCheckOrder.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td> 
					<%-- <td class="width-15 active"><label class="pull-right">盈亏总金额：</label></td>
					<td class="width-35">
						${warehouseCheckOrder.profitLossTotalMoney }
						<form:input path="profitLossTotalMoney" htmlEscape="false"  class="form-control "/>
					</td> --%>			
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="fstatus" items="${fns:getDictList('checkorder_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
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
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">盘点商品：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<!-- <a class="btn btn-white btn-sm" onclick="addRow('#warehouseCheckGoodsList', warehouseCheckGoodsRowIdx, warehouseCheckGoodsTpl);warehouseCheckGoodsRowIdx = warehouseCheckGoodsRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<a class="btn btn-white btn-sm" href="${ctx}/warehouses/warehouseCheckGoods/export?checkOrder.id=${warehouseCheckOrder.id }" title="新增"><i class="fa fa-plus"></i> 导出</a>
	       		<%-- <table:exportExcel url="${ctx}/warehouses/warehouseCheckGoods/export?checkOrder.id=${warehouseCheckOrder.id }"></table:exportExcel><!-- 导出按钮 --> --%>
	       		
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>商品名称</th>
						<th>商品货号</th>
						<th>商品品牌</th>
						<th>商品分类</th>
						<th>单位</th>
						<th>盘点数量</th>
						<th>盘点前数量</th>
						<th>盈亏数量</th>
						<!-- <th>盈亏金额（元）</th> -->
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="warehouseCheckGoodsList">
				</tbody>
			</table>
			<script type="text/template" id="warehouseCheckGoodsTpl">//<!--
				<tr id="warehouseCheckGoodsList{{idx}}">
					<td class="hide">
						<input id="warehouseCheckGoodsList{{idx}}_id" name="warehouseCheckGoodsList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="warehouseCheckGoodsList{{idx}}_delFlag" name="warehouseCheckGoodsList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_goodsSpu" type="text" value="{{row.goodsSpu.fgoodsname}}"  disabled="disabled"  class="form-control"/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_goodsSku" type="text" value="{{row.goodsSku.fgoodsnumber}}"  disabled="disabled"  class="form-control"/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_goodsSku_brand" type="text" value="{{row.goodsSpu.brand.fbrandname}}"  disabled="disabled"  class="form-control"/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_goodsSku_category" type="text" value="{{row.goodsSpu.categorys.name}}"  disabled="disabled"  class="form-control"/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_goodsUnit" type="text" value="{{row.goodsSpu.goodsUnit.funitname}}"  disabled="disabled"   class="form-control"/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_checkNum" name="warehouseCheckGoodsList[{{idx}}].checkNum" type="text" value="{{row.checkNum}}"    class="form-control required"/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_checkBeforeNum" name="warehouseCheckGoodsList[{{idx}}].checkBeforeNum" type="text" value="{{row.checkBeforeNum}}"  disabled="disabled"  class="form-control "/>
					</td>
					<td>
						<input id="warehouseCheckGoodsList{{idx}}_profitLossNum" name="warehouseCheckGoodsList[{{idx}}].profitLossNum" type="text" value="{{row.profitLossNum}}"  disabled="disabled"  class="form-control "/>
					</td>
				
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#warehouseCheckGoodsList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var warehouseCheckGoodsRowIdx = 0, warehouseCheckGoodsTpl = $("#warehouseCheckGoodsTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(warehouseCheckOrder.warehouseCheckGoodsList)};
					for (var i=0; i<data.length; i++){
						addRow('#warehouseCheckGoodsList', warehouseCheckGoodsRowIdx, warehouseCheckGoodsTpl, data[i]);
						warehouseCheckGoodsRowIdx = warehouseCheckGoodsRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>