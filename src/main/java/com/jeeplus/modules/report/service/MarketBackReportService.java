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
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketChaDetail;
import com.jeeplus.modules.report.dao.MarketBackReportDao;

/**
 * 销售商品报表Service
 * @author hxteng
 * @version 2017-07-25
 */
@Service
@Transactional(readOnly = true)
public class MarketBackReportService extends CrudService<MarketBackReportDao, OrderMarketChaDetail> {
	
	@Autowired
	protected MarketBackReportDao marketBackReportDao;
	

	public Page<OrderMarketChaDetail> findMarketBackDetailList(Page<OrderMarketChaDetail> page,
			OrderMarketChaDetail orderMarketChaDetail) {
		List<OrderMarketChaDetail> list = marketBackReportDao.findList(orderMarketChaDetail);
		if(list != null && list.size() > 0){
			for (OrderMarketChaDetail omd : list) {
				BigDecimal fdiscountcountmoney = omd.getFdiscountcountmoney();
				BigDecimal grossProfit = omd.getGrossProfit();
				omd.setGrossProfitMargin(grossProfit.divide(fdiscountcountmoney, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
			}
		}
		page.setList(marketBackReportDao.findList(orderMarketChaDetail));
		return page;
	}
	
	public OrderMarketChaDetail get(String id) {
		OrderMarketChaDetail omcd = marketBackReportDao.get(id);
		if(omcd != null ){
			BigDecimal fdiscountcountmoney = omcd.getFdiscountcountmoney();
			BigDecimal grossProfit = omcd.getGrossProfit();
			omcd.setGrossProfitMargin(grossProfit.divide(fdiscountcountmoney, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
		}
		return omcd;
	}
	
	
	
	/**
	 * 销售退货商品统计
	 * @param orderMarketDetail
	 * @return
	 */
	public Map<String, Object> getMarketBackDetail(OrderMarketChaDetail orderMarketChaDetail) {
		Map<String, Object> map = new HashMap<>();
		
		int marketBackGoodsNum = marketBackReportDao.getMarketBackGoodsNum(orderMarketChaDetail);
		BigDecimal marketBackValue = new BigDecimal(marketBackReportDao.getMarketBackValue(orderMarketChaDetail)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal marketBackGrossMargin = new BigDecimal(marketBackReportDao.getMarketBackGrossMargin(orderMarketChaDetail)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal marketBackGrossProfitMargin = marketBackGrossMargin.divide(marketBackValue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
		map.put("marketBackGoodsNum", marketBackGoodsNum);
		map.put("marketBackValue", marketBackValue);
		map.put("marketBackGrossMargin", marketBackGrossMargin);
		map.put("marketBackGrossProfitMargin", marketBackGrossProfitMargin);
		
		return map;
	}
	
	
			
	
}