<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>生成条形码管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").submit(function(e){
				if($("#fstrCode").val()=="" || $("#fstrCode").val().length!=8){
					alert("厂商代码不正确,请输入八位厂商代码");
					return false;
				}
				if($("#fproductCode").val()=="" ){
					alert("产品代码起始值不可为空");
					return false;
				} else if($("#fproductCode").val().length>4){
					alert("产品代码起始值不可大于四位数");
					return false;
				}
			});
			
			//校验
			function validataRules(){
				if($("#fstrCode").val()=="" || $("#fstrCode").val().length!=8){
					alert("厂商代码不正确,请输入八位厂商代码");
					return false;
				}
				if($("#fproductCode").val()=="" ){
					alert("产品代码起始值不可为空");
					return false;
				} else if($("#fproductCode").val().length>4){
					alert("产品代码起始值不可大于四位数");
					return false;
				}
			}
			
			//点击触发异步
			$("#submit").click(function(){
				//$("#inputForm").submit();
				validataRules();
				$.ajax({
		            url: "${ctx}/goods/barcode/barcode/create",
		            data: {
		            	fstrCode: $("#fstrCode").val(),
		            	fproductCode: $("#fproductCode").val(),
		            },
		            async: true,   
		            type: "POST", 
		            dataType: "json",   
		            success: function(data){  
		                if(data=="1"){
		                    alert("条形码生成成功");
		                }else if(data=="0"){
		                    alert("条形码生成失败");
		                }else{
		                	alert("不存在无条码商品")
		                }
		            }
		          });
			});
			});
		
	</script>
</head>
<body  >
	<div  style="width: 600px; margin-left:10px; margin-top: 10px; text-align: center;  ">
		<form:form id="inputForm" modelAttribute="barcode"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">条码    类型：</label></td>
					<td class="width-35">
						<form:select path="codeType" class="form-control ">
							<form:option value="0" label="EAN-13"/>
							<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right">八位厂商代码：</label></td>
					<td class="width-35">
						<form:input path="fstrCode" htmlEscape="false"   id="fstrCode" class="form-control "/><font style="color: red;">* 请输入八位厂商代码</font>
					</td>
				</tr>
				<tr> 	
					<td class="width-15 active"><label class="pull-right">产品代码起始值：</label></td>
					<td class="width-35">
						<form:input path="fproductCode" htmlEscape="false"  id="fproductCode"  class="form-control "/><font style="color: red;">* 产品代码不可超过4位</font>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
	<br></br>
		 <input  id="submit"  type="submit" style="width:180px;height: 40px;text-align: center; "  value="为无条码商品生产条码" />
	</div>	 
	
</body>
</html>