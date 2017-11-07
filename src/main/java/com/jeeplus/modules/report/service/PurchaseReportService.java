/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.BaseService;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.report.dao.PurchaseReportDao;
import com.jeeplus.modules.report.entity.CustomerSalse;

/**
 * 数据报表Service
 * @author hxteng
 * @version 2017-06-01
 */
@Service
@Transactional(readOnly = true)
public class PurchaseReportService extends BaseService {
	
	@Autowired
	protected PurchaseReportDao reportDao;
			
	public OrderProDetail getOrderProDetailById(String id) {
		return reportDao.getOrderProDetailById(id);
	}
	
	public Page<OrderProDetail> findOrderProDetailList(Page<OrderProDetail> page, OrderProDetail orderProDetail) {
		orderProDetail.setPage(page);
		page.setList(reportDao.findOrderProDetailList(orderProDetail));
		return page;
	}
	
	public Map<String, Object> getPurDetail(OrderProDetail orderProDetail) {
		Map<String, Object> map = new HashMap<>();
		map.put("purchaseGoodsNum", reportDao.getPurGoodsNum(orderProDetail));
		map.put("purchaseOrderNum", reportDao.getPurOrderNum(orderProDetail));
		map.put("purchaseAmountOfMoney", reportDao.getPurAmountOfMoney(orderProDetail));
		return map;
	}

	public Page<OrderProChaDetail> findOrderProChaDetailList(Page<OrderProChaDetail> page,
			OrderProChaDetail orderProChaDetail) {
		orderProChaDetail.setPage(page);
		page.setList(reportDao.findOrderProChargebackList(orderProChaDetail));
		return page;
	}
	 
	public OrderProChaDetail getOrderProChaDetailById(String id) {
		return reportDao.getOrderProChaDetailById(id);
	}
	
	public Map<String, Object> getPurBackDetail(OrderProChaDetail orderProChaDetail) {
		Map<String, Object> map = new HashMap<>();
		map.put("purchaseBackGoodsNum", reportDao.getPurBackGoodsNum(orderProChaDetail));
		map.put("purchaseBackOrderNum", reportDao.getPurBackOrderNum(orderProChaDetail));
		map.put("purchaseBackAmountOfMoney", reportDao.getPurBackAmountOfMoney(orderProChaDetail));
		return map;
	}
	
}