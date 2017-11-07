/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.report.dao.TransferReportDao;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;

/**
 * 调拨商品报表Service
 * 
 * @author hxteng
 * @version 2017-07-24
 */
@Service
@Transactional(readOnly = true)
public class TransferReportService extends CrudService<TransferReportDao, WarehouseTransferGoods> {

	@Autowired
	protected TransferReportDao transferReportDao;
	
	public Page<WarehouseTransferGoods> findPage(Page<WarehouseTransferGoods> page, WarehouseTransferGoods WarehouseTransferGoods) {
		List<WarehouseTransferGoods> list = transferReportDao.findList(WarehouseTransferGoods);
		page.setList(list);
		return page;
	}

	/**
	 * 调拨商品统计
	 * @param warehouseTransferGoods
	 * @return
	 */
	public Map<String, Object> getTransferDetail(WarehouseTransferGoods warehouseTransferGoods) {
		Map<String, Object> map = new HashMap<>();
		
		return map;
	}
	
	
	

}