/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.report.dao.CheckReportDao;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckGoods;

/**
 * 盘点商品报表Service
 * 
 * @author hxteng
 * @version 2017-07-24
 */
@Service
@Transactional(readOnly = true)
public class CheckReportService extends CrudService<CheckReportDao, WarehouseCheckGoods> {

	@Autowired
	protected CheckReportDao checkReportDao;
	
	public Page<WarehouseCheckGoods> findPage(Page<WarehouseCheckGoods> page, WarehouseCheckGoods warehouseCheckGoods) {
		List<WarehouseCheckGoods> list = checkReportDao.findList(warehouseCheckGoods);
		page.setList(list);
		return page;
	}

	/**
	 * 盘点商品统计
	 * @param warehouseCheckGoods
	 * @return
	 */
	public Map<String, Object> getCheckGoodsDetail(WarehouseCheckGoods warehouseCheckGoods) {
		Map<String, Object> map = new HashMap<>();
		int checkGoodsNum = checkReportDao.getCheckGoodsNum(warehouseCheckGoods);
		int checkWarehouseNum = checkReportDao.getCheckWarehouseNum(warehouseCheckGoods);
		int checkProfitAndLossNum = checkReportDao.getCheckProfitAndLossNum(warehouseCheckGoods);
		BigDecimal checkProfitAndLossMoney = new BigDecimal(checkReportDao.getCheckProfitAndLossMoney(warehouseCheckGoods)).setScale(2, BigDecimal.ROUND_HALF_UP);
		map.put("checkGoodsNum", checkGoodsNum);
		map.put("checkWarehouseNum", checkWarehouseNum);
		map.put("checkProfitAndLossNum", checkProfitAndLossNum);
		map.put("checkProfitAndLossMoney", checkProfitAndLossMoney);
		return map;
	}
	
	
	

}