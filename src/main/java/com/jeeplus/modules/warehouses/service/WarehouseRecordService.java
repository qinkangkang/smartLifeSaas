/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.dao.WarehouseRecordDao;

/**
 * 仓库流水Service
 * @author hxting
 * @version 2017-06-15
 */
@Service
@Transactional(readOnly = true)
public class WarehouseRecordService extends CrudService<WarehouseRecordDao, WarehouseRecord> {

	public WarehouseRecord get(String id) {
		return super.get(id);
	}
	
	public List<WarehouseRecord> findList(WarehouseRecord warehouseRecord) {
		return super.findList(warehouseRecord);
	}
	
	public Page<WarehouseRecord> findPage(Page<WarehouseRecord> page, WarehouseRecord warehouseRecord) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		warehouseRecord.getSqlMap().put("dsf", dataScopeFilter(warehouseRecord.getCurrentUser(), "o", "createby"));
		return super.findPage(page, warehouseRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(WarehouseRecord warehouseRecord) {
		User currentUser = warehouseRecord.getCurrentUser();
		if(!currentUser.isAdmin()){
			Office office = currentUser.getOffice();
			Office company = currentUser.getCompany();
			warehouseRecord.setCompany(company);
			warehouseRecord.setOffice(office);
		}
		super.save(warehouseRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(WarehouseRecord warehouseRecord) {
		super.delete(warehouseRecord);
	}
	
	public Page<Warehouse> findPageBywarehouse(Page<Warehouse> page, Warehouse warehouse) {
		warehouse.setPage(page);
		page.setList(dao.findListBywarehouse(warehouse));
		return page;
	}
	public Page<GoodsSpu> findPageBygoodsSpu(Page<GoodsSpu> page, GoodsSpu goodsSpu) {
		goodsSpu.setPage(page);
		page.setList(dao.findListBygoodsSpu(goodsSpu));
		return page;
	}
	public Page<GoodsSku> findPageBygoodsSku(Page<GoodsSku> page, GoodsSku goodsSku) {
		goodsSku.setPage(page);
		page.setList(dao.findListBygoodsSku(goodsSku));
		return page;
	}
	
	
	
}