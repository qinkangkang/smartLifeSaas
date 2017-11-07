<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/echarts.jsp"%>
<meta name="decorator" content="default"/>
	单轴：
	<div id="line_normal"  class="main000"></div>
    <echarts:line 
        id="line_normal"
		title="2017年月订单销售额度对比曲线" 
		subtitle="月订单的销售额度对比曲线"
		xAxisData="${xAxisData}" 
		yAxisData="${yAxisData}" 
		xAxisName="月份"
		yAxisName="销售额度(元)" />
		
	<%-- 双轴：
	<div id="line_yAxisIndex"  class="main000"></div>
	<echarts:line 
	    id="line_yAxisIndex"
		title="2011年温度对比曲线" 
		subtitle="主要城市的温度对比曲线"
		xAxisData="${xAxisData}" 
		yAxisData="${yAxisData}" 
		xAxisName="预测时间"
		yAxisName="最高温度(℃),最低温度(℃)" 
		yAxisIndex="${yAxisIndex}"/> --%>
