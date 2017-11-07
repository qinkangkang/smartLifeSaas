/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketChaDetail;

/**
 * 销售退货商品报表DAO接口
 * 
 * @author hxting
 * @version 2017-07-25
 */
@MyBatisDao
public interface MarketBackReportDao extends CrudDao<OrderMarketChaDetail> {
	
	/**
	 * 销售退货商品个数
	 * @param orderMarketChaDetail
	 * @return
	 */
	public int getMarketBackGoodsNum(OrderMarketChaDetail orderMarketChaDetail);

	/**
	 * 退货额
	 * @param orderMarketChaDetail
	 * @return
	 */
	public double getMarketBackValue(OrderMarketChaDetail orderMarketChaDetail);

	/**
	 * 毛利
	 * @param orderMarketChaDetail
	 * @return
	 */
	public double getMarketBackGrossMargin(OrderMarketChaDetail orderMarketChaDetail);
		

}