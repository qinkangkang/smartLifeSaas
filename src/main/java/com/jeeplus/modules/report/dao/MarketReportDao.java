/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.dao;


import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;

/**
 * 销售商品报表DAO接口
 * 
 * @author hxting
 * @version 2017-07-25
 */
@MyBatisDao
public interface MarketReportDao extends CrudDao<OrderMarketDetail> {

	/**
	 * 销售商品个数
	 * @param orderMarketDetail
	 * @return
	 */
	public int getMarketGoodsNum(OrderMarketDetail orderMarketDetail);

	/**
	 * 销售额
	 * @param orderMarketDetail
	 * @return
	 */
	public double getMarketValue(OrderMarketDetail orderMarketDetail);

	/**
	 * 销售毛利
	 * @param orderMarketDetail
	 * @return
	 */
	public double getMarketGrossMargin(OrderMarketDetail orderMarketDetail);
			

}