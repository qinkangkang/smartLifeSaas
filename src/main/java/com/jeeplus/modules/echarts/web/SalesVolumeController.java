package com.jeeplus.modules.echarts.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeeplus.modules.echarts.entity.ChinaWeatherDataBean;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.market.service.ordermarket.OrderMarketService;

@Controller
@RequestMapping(value = "${adminPath}/echarts/salesVolume")
public class SalesVolumeController {

	private static final long serialVersionUID = -6886697421555222670L;

	@Autowired
	private OrderMarketService orderMarketService;

	@SuppressWarnings("deprecation")
	@RequestMapping(value = { "index", "" })
	public String index(ChinaWeatherDataBean chinaWeatherDataBean, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		// X轴的数据
		List<String> xAxisData = new ArrayList<String>();
		
		// Y轴的数据
		Map<String, List<Double>> yAxisData = new HashMap<String, List<Double>>();
		
		List<Map<String, Object>> listMap = orderMarketService.findMonthPricList();
		
		//每月对应的销售额的list
		List<Double> totalMoney = new ArrayList<>();
		
		for (Map<String, Object> map : listMap) {
			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				//月份的list数据
				if("month".equals(key)){
					xAxisData.add(value);
				}
				//每月对应的销售额的list
				if("totalMoney".equals(key)){
					totalMoney.add(Double.valueOf(value));
				}
			}
		}
		yAxisData.put("销售额",totalMoney);
			
		request.getSession().setAttribute("xAxisData", xAxisData);
		request.getSession().setAttribute("yAxisData", yAxisData);
		
		return "modules/echarts/salesVolume";

	}

}
