/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.service;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.report.dao.MarketReportDao;

/**
 * 销售商品报表Service
 * @author hxteng
 * @version 2017-07-24
 */
@Service
@Transactional(readOnly = true)
public class MarketReportService extends CrudService<MarketReportDao, OrderMarketDetail> {
	
	@Autowired
	protected MarketReportDao marketReportDao;
	
	/**
	 * 销售商品列表
	 * @param page
	 * @param orderMarketDetail
	 * @return
	 */
	public Page<OrderMarketDetail> findMarketDetailList(Page<OrderMarketDetail> page, OrderMarketDetail orderMarketDetail) {
		orderMarketDetail.setPage(page);
		List<OrderMarketDetail> list = marketReportDao.findList(orderMarketDetail);
		if(list != null && list.size() > 0){
			for (OrderMarketDetail omd : list) {
				BigDecimal fdiscountcountmoney = omd.getFdiscountcountmoney();
				BigDecimal grossProfit = omd.getGrossProfit();
				omd.setGrossProfitMargin(grossProfit.divide(fdiscountcountmoney, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
			}
		}
		page.setList(marketReportDao.findList(orderMarketDetail));
		return page;
	}
	
	
	public OrderMarketDetail get(String id) {
		OrderMarketDetail omd = marketReportDao.get(id);
		if(omd != null ){
			BigDecimal fdiscountcountmoney = omd.getFdiscountcountmoney();
			BigDecimal grossProfit = omd.getGrossProfit();
			omd.setGrossProfitMargin(grossProfit.divide(fdiscountcountmoney, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
		}
		return omd;
	}
	
	
	/**
	 * 销售商品统计
	 * @param orderMarketDetail
	 * @return
	 */
	public Map<String, Object> getMarketDetail(OrderMarketDetail orderMarketDetail) {
		Map<String, Object> map = new HashMap<>();
		
		int marketGoodsNum = marketReportDao.getMarketGoodsNum(orderMarketDetail);
		BigDecimal marketValue = new BigDecimal(marketReportDao.getMarketValue(orderMarketDetail)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal marketGrossMargin = new BigDecimal(marketReportDao.getMarketGrossMargin(orderMarketDetail)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal marketGrossProfitMargin = marketGrossMargin.divide(marketValue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
		map.put("marketGoodsNum", marketGoodsNum);
		map.put("marketValue", marketValue);
		map.put("marketGrossMargin", marketGrossMargin);
		map.put("marketGrossProfitMargin", marketGrossProfitMargin);
		
		return map;
	}
	
	
	
			
	
}