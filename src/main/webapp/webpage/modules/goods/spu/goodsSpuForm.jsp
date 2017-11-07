<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品管理管理</title>
	<meta name="decorator" content="default"/>
	 <link href="${ctxStatic}/summernote/summernote.css" rel="stylesheet">
	 <link href="${ctxStatic}/summernote/summernote-bs3.css" rel="stylesheet">
	 <script src="${ctxStatic}/summernote/summernote.min.js"></script>
	 <script src="${ctxStatic}/summernote/summernote-zh-CN.js"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
//		   	  $("#fimage1").val($("#rich_fimage1").next().find(".note-editable").html());//取富文本的值 
//		   	  $("#fimage2").val($("#rich_fimage2").next().find(".note-editable").html());//取富文本的值
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
			
					//富文本初始化
// 			 $('#rich_fimage1').summernote({
//                 lang: 'zh-CN'
//             });

// 			$("#rich_fimage1").next().find(".note-editable").html(  $("#fimage1").val());

// 			$("#rich_fimage1").next().find(".note-editable").html(  $("#rich_fimage1").next().find(".note-editable").text()); 
				
					//富文本初始化
// 			$('#rich_fimage2').summernote({
//                 lang: 'zh-CN'
//             });

// 			$("#rich_fimage2").next().find(".note-editable").html(  $("#fimage2").val());

// 			$("#rich_fimage2").next().find(".note-editable").html(  $("#rich_fimage2").next().find(".note-editable").text());
				
					laydate({
			            elem: '#fonsalestime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#foffsalestime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
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
	<form:form id="inputForm" modelAttribute="goodsSpu" action="${ctx}/goods/spu/goodsSpu/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品名称：</label></td>
					<td class="width-35">
						<form:input path="fgoodsname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">采购价：</label></td>
					<td class="width-35">
						<form:input path="fbuyprice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">商品单位：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/goods/spu/goodsSpu/selectgoodsUnit" id="goodsUnit" name="goodsUnit.id"  value="${goodsSpu.goodsUnit.id}"  title="选择商品单位" labelName="goodsUnit.funitname" 
						 labelValue="${goodsSpu.goodsUnit.funitname}" cssClass="form-control required" fieldLabels="商品单位|备注" fieldKeys="funitname|remarks" searchLabel="商品单位" searchKey="funitname" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>商品品牌：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/goods/spu/goodsSpu/selectbrand" id="brand" name="brand.id"  value="${goodsSpu.brand.id}"  title="选择商品所属品牌" labelName="brand.fbrandname" 
						 labelValue="${goodsSpu.brand.fbrandname}" cssClass="form-control required" fieldLabels="商品品牌|备注" fieldKeys="fbrandname|remarks" searchLabel="商品品牌" searchKey="fbrandname" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>商品分类：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/goods/spu/goodsSpu/selectcategorys" id="categorys" name="categorys.id"  value="${goodsSpu.categorys.id}"  title="选择商品所属分类" labelName="categorys.name" 
						 labelValue="${goodsSpu.categorys.name}" cssClass="form-control required" fieldLabels="商品类别|备注" fieldKeys="name|remarks" searchLabel="商品类别" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">启用状态：</label></td>
					<td class="width-35">
						<form:select path="fstatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('sys_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品主图：</label></td>
					<td class="width-35">
						<form:hidden id="fimage1" path="fimage1" htmlEscape="false" class="form-control"/>
						<sys:ckfinder input="fimage1" type="files" uploadPath="/smartLife/image" selectMultiple="true"/>
						
<%-- 						<form:hidden path="fimage1"/> --%>
<!-- 						<div id="rich_fimage1"> -->
						
						
<!--                         </div>  -->
                         
					</td>
					<td class="width-15 active"><label class="pull-right">商品细节图：</label></td>
					<td class="width-35">
						 <form:hidden id="fimage2" path="fimage2" htmlEscape="false" class="form-control"/>
						<sys:ckfinder input="fimage2" type="files" uploadPath="/smartLife/image" selectMultiple="true"/>

<%-- 						<form:hidden path="fimage2"/> --%>
<!-- 						<div id="rich_fimage2"> -->
                           

<!--                         </div> -->
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">上架时间：</label></td>
					<td class="width-35">
						<input id="fonsalestime" name="fonsalestime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${goodsSpu.fonsalestime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">下架时间：</label></td>
					<td class="width-35">
						<input id="foffsalestime" name="foffsalestime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${goodsSpu.foffsalestime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属季节：</label></td>
						<td class="width-35">
							<form:select path="fseason" class="form-control ">
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('season_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">商品库存表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#goodsSkuList', goodsSkuRowIdx, goodsSkuTpl);goodsSkuRowIdx = goodsSkuRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>商品货号</th>
						<th>颜色</th>
						<th>尺码</th>
						<th>原价</th>
						<th>销售价</th>
						<th>商品条形码</th>
						<th>排序</th>
						<th>启用状态</th>
						<th>库存下限</th>
						<th>库存上限</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="goodsSkuList">
				</tbody>
			</table>
			<script type="text/template" id="goodsSkuTpl">//<!--
				<tr id="goodsSkuList{{idx}}">
					<td class="hide">
						<input id="goodsSkuList{{idx}}_id" name="goodsSkuList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="goodsSkuList{{idx}}_delFlag" name="goodsSkuList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="goodsSkuList{{idx}}_fgoodsnumber" name="goodsSkuList[{{idx}}].fgoodsnumber" type="text" value="{{row.fgoodsnumber}}"    class="form-control "/>
					</td>
					
					<td >
						<sys:gridselect url="${ctx}/goods/sku/goodsSku/selectcolors" id="goodsSkuList{{idx}}_colors" name="goodsSkuList[{{idx}}].colors.id"  value="${row.colors.id}"  title="选择颜色" labelName="colors.fcolorname" 
							labelValue="{{row.colors.fcolorname}}" cssClass="form-control required" fieldLabels="颜色名称|备注" fieldKeys="fcolorname|remarks" searchLabel="颜色" searchKey="fcolorname" ></sys:gridselect>
					</td>
					
					<td >
						<sys:gridselect url="${ctx}/goods/sku/goodsSku/selectsize" id="goodsSkuList{{idx}}_size" name="goodsSkuList[{{idx}}].size.id"  value="${row.size.id}"  title="选择尺码" labelName="size.fsizename" 
							labelValue="{{row.size.fsizename}}" cssClass="form-control required" fieldLabels="尺码名称|备注" fieldKeys="fsizename|remarks" searchLabel="尺码" searchKey="fsizename" ></sys:gridselect>
					</td>
					
					
					
					<td>
						<input id="goodsSkuList{{idx}}_fprice" name="goodsSkuList[{{idx}}].fprice" type="text" value="{{row.fprice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="goodsSkuList{{idx}}_fonsalesprice" name="goodsSkuList[{{idx}}].fonsalesprice" type="text" value="{{row.fonsalesprice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="goodsSkuList{{idx}}_fbarcode" name="goodsSkuList[{{idx}}].fbarcode" type="text" value="{{row.fbarcode}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="goodsSkuList{{idx}}_fsort" name="goodsSkuList[{{idx}}].fsort" type="text" value="{{row.fsort}}"    class="form-control "/>
					</td>
					
					
					<td>
						<select id="goodsSkuList{{idx}}_fstatus" name="goodsSkuList[{{idx}}].fstatus" data-value="{{row.fstatus}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('sys_status')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="goodsSkuList{{idx}}_fstorelowerno" name="goodsSkuList[{{idx}}].fstorelowerno" type="text" value="{{row.fstorelowerno}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="goodsSkuList{{idx}}_fstoreupperno" name="goodsSkuList[{{idx}}].fstoreupperno" type="text" value="{{row.fstoreupperno}}"    class="form-control "/>
					</td>

					<td>
						<textarea id="goodsSkuList{{idx}}_remarks" name="goodsSkuList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#goodsSkuList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var goodsSkuRowIdx = 0, goodsSkuTpl = $("#goodsSkuTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(goodsSpu.goodsSkuList)};
					for (var i=0; i<data.length; i++){
						addRow('#goodsSkuList', goodsSkuRowIdx, goodsSkuTpl, data[i]);
						goodsSkuRowIdx = goodsSkuRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>