/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckGoods;
import com.jeeplus.modules.warehouses.dao.WarehouseCheckGoodsDao;

/**
 * 盘点商品Service
 * @author hxting
 * @version 2017-06-15
 */
@Service
@Transactional(readOnly = true)
public class WarehouseCheckGoodsService extends CrudService<WarehouseCheckGoodsDao, WarehouseCheckGoods> {

	public WarehouseCheckGoods get(String id) {
		return super.get(id);
	}
	
	public List<WarehouseCheckGoods> findList(WarehouseCheckGoods warehouseCheckGoods) {
		return super.findList(warehouseCheckGoods);
	}
	
	public Page<WarehouseCheckGoods> findPage(Page<WarehouseCheckGoods> page, WarehouseCheckGoods warehouseCheckGoods) {
		return super.findPage(page, warehouseCheckGoods);
	}
	
	@Transactional(readOnly = false)
	public void save(WarehouseCheckGoods warehouseCheckGoods) {
		super.save(warehouseCheckGoods);
	}
	
	@Transactional(readOnly = false)
	public void delete(WarehouseCheckGoods warehouseCheckGoods) {
		super.delete(warehouseCheckGoods);
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