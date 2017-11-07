/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.warehouses.dao.WarehouseDao;

/**
 * 仓库Service
 * @author Hxting
 * @version 2017-07-04
 */
@Service
@Transactional(readOnly = true)
public class WarehouseService extends CrudService<WarehouseDao, Warehouse> {
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private OfficeService officeService;

	public Warehouse get(String id) {
		return super.get(id);
	}
	
	public List<Warehouse> findList(Warehouse warehouse) {
		return super.findList(warehouse);
	}
	
	public Page<Warehouse> findPage(Page<Warehouse> page, Warehouse warehouse) {
		warehouse.getSqlMap().put("dsf", dataScopeFilter(warehouse.getCurrentUser(), "o", "createby"));
		warehouse.setPage(page);
		page.setList(warehouseDao.findList(warehouse));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Warehouse warehouse) {
		if(warehouse.getOffice() != null){
			Office office = officeService.get(warehouse.getOffice().getId());
			if(office != null && office.getParentId() != null){
				warehouse.setCompany(officeService.get(office.getParentId()));
			}
		}
		super.save(warehouse);
	}
	
	@Transactional(readOnly = false)
	public void delete(Warehouse warehouse) {
		super.delete(warehouse);
	}
	
	
	
	
}