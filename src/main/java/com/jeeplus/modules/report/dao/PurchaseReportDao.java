/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.report.entity.CustomerSalse;

/**
 * 数据报表DAO接口
 * @author hxteng
 * @version 2017-06-01
 */
@MyBatisDao
public interface PurchaseReportDao {

	/*
	 *采购单商品明细
	 */
	public List<OrderProDetail> findOrderProDetailList(OrderProDetail orderProDetail);
	
	/**
	 * 根据Id查询采购单商品明细
	 * @param Id
	 * @return
	 */
	public OrderProDetail getOrderProDetailById(String id);
	
	/**
	 * 采购商品数
	 * @param orderProDetail
	 * @return
	 */
	public int getPurGoodsNum(OrderProDetail orderProDetail);
	
	/**
	 * 采购笔数
	 * @param orderProDetail
	 * @return
	 */
	public int getPurOrderNum(OrderProDetail orderProDetail);
	
	/**
	 * 采购金额
	 * @param orderProDetail
	 * @return
	 */
	public double getPurAmountOfMoney(OrderProDetail orderProDetail);
	
	/**
	 * 采购退单商品列表
	 * @param orderProChargeback
	 * @return
	 */
	public List<OrderProChaDetail> findOrderProChargebackList(OrderProChaDetail orderProChaDetail);
	
	
	
	/**
	 * 根据Id查询采购退货单商品明细
	 * @param Id
	 * @return
	 */
	public OrderProChaDetail getOrderProChaDetailById(String id);
	
	/**
	 * 采购退单商品数
	 * @param orderProDetail
	 * @return
	 */
	public int getPurBackGoodsNum(OrderProChaDetail orderProChaDetail);
	
	/**
	 * 采购退单笔数
	 * @param orderProDetail
	 * @return
	 */
	public int getPurBackOrderNum(OrderProChaDetail orderProChaDetail);
	
	/**
	 * 采购退单金额
	 * @param orderProDetail
	 * @return
	 */
	public double getPurBackAmountOfMoney(OrderProChaDetail orderProChaDetail);
	
	
	
}