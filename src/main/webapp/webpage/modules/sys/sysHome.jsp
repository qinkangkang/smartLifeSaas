<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/echarts.jsp"%>
<%@ include file="/webpage/include/head.jsp"%>
	<script src="${ctxStatic}/common/inspinia.js?v=3.2.0"></script>
	<script src="${ctxStatic}/common/contabs.js"></script>
	<title>首页</title>
	<style>
		.div1{float:left}
	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		     WinMove();
		});
	</script>
	<div class="wrapper wrapper-content" style="border: 0px;margin-left: 15px;">
	
	   <div class="row " style="font-size: 14px;margin-bottom: 15px;" align="center">
	          <div class="col-sm-3 border-bottom  dashboard-header" style="background-color:#FFFFFF; margin-right: 15px;" >
	        	<div class="div1">
	        	<img src="${pageContext.request.contextPath}/webpage/modules/sys/images/Group 4.png">
	        	</div>&nbsp;&nbsp;
	        	 <div class="div1" style="margin-left: 20px;">
	        	 	<a  href="#" onclick="openDialogView('查看调拨单', '${ctx}/warehouses/warehouseTransferOrder','800px', '500px')" >
		        	 	<div style="margin-left: -30px; margin-top:13px;"><font size="10px" color="#0C77D8">${count}</font>&nbsp;&nbsp;<font color="#000">件</font></div>
			        	<div style="margin-top:-10px;"><font color="#000">未处理调拨单</font></div>
	        	 	</a>
	        	 	<%-- <a href="#" onclick="openDialogView('查看挑拨单', '${ctx}/warehouses/warehouseTransferOrder','800px', '500px')" >
			        </a> --%>
	        	 <%-- <div><font size="14px" color="#0C77D8">${count}</font>件</span></div>
	        	 <div><a href="#" onclick="openDialogView('查看挑拨单', '${ctx}/warehouses/warehouseTransferOrder','800px', '500px')" ><span><font color="#000000">未处理调拨单</font></span>   </a></div> --%>
	        	<!-- <script type="text/javascript">
	            	var divM = document.getElementById("demo1");
	            </script> -->
	        	</div>
	        </div> 
	          <div class="col-sm-3 border-bottom  dashboard-header" style="background-color:#FFFFFF; margin-right: 15px;" >
	        	<div class="div1">
	        	 <img src="${pageContext.request.contextPath}/webpage/modules/sys/images/Group 5.png">
	        	</div>
	        	
	        	<div class="div1" style="margin-left: 20px;">
	        		<a href="#" onclick="openDialogView('查看采购退单详情', '${ctx}/order/prochargeback/orderProChargeback/list','800px', '500px')">
		        	<div style="margin-left: -30px;margin-top:13px"><font size="10px" color="#f00">${countChangeBack}</font>&nbsp;&nbsp;<font color="#000">件</font></div>
		        	<div style="margin-top:-10px;"><font color="#000">未处理采购退单</font></div></a>
	        	</div>
	        </div> 
	        <div class="col-sm-3 border-bottom  dashboard-header" style="background-color:#FFFFFF; margin-right: 15px;" >
	        	<div class="div1">
	        	 <img src="${pageContext.request.contextPath}/webpage/modules/sys/images/Group 6.png">
	        	</div>
	        	<div class="div1" style="margin-left: 20px;">
	        		<a href="#" onclick="openDialogView('查看订单详情', '${ctx}/market/ordermarket/orderMarket','800px', '500px')">
	        		<div style="margin-left: -30px;margin-top:13px"><font size="10px" color="#1CA12A">${marketCount}</font >&nbsp;&nbsp;<font color="#000">件</font></div>
		        	<div style="margin-top:-10px;font-color:#000;"><font color="#000">未处理订单</font></div>
		        	</a>
	        	</div>
	        </div> 
	        <div class="col-sm-3 border-bottom  dashboard-header" style="width: 218px;height: 143px;background-color:#FFFFFF; margin-right: 15px;    text-align: left;">
		        <div style="margin-top:-10px;padding-bottom: 4px;border-bottom: 0.2px solid #f3f3f4;">
			        <font size="4px" face="黑体">公司公告</font>
			        <a href="#" style="float: right;" onclick="openDialogView('查看更多广告', '${ctx}//oa/oaNotify','800px', '500px')" class="btn btn-info btn-xs" > 更多</a>
		        </div>
		        <c:forEach items="${gonggaoList}" var="oaNotify">
		        	<span style="font-size:20px;color:#0C77D8;">•</span>
		        	<tr>
		        		<td> ${oaNotify.title}</td>
		        		<td> <fmt:formatDate value="${brand.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		        	</tr><br>
		        </c:forEach>
	        
	        </div> 
	         
	    </div>	
		<div id="line_normal"  class="main000" ></div> 
	    <echarts:line 
	        id="line_normal"
			title="2017年月订单销售额度对比曲线" 
			subtitle="月订单的销售额度对比曲线"
			xAxisData="${xAxisData}" 
			yAxisData="${yAxisData}" 
			xAxisName="月份"
			yAxisName="销售额度(元)" />
	  
	  </div>  
