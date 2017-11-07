/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferOrder;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;
import com.jeeplus.modules.warehouses.dao.WarehouseTransferGoodsDao;

/**
 * 调拨商品Service
 * @author hxting
 * @version 2017-06-09
 */
@Service
@Transactional(readOnly = true)
public class WarehouseTransferGoodsService extends CrudService<WarehouseTransferGoodsDao, WarehouseTransferGoods> {

	public WarehouseTransferGoods get(String id) {
		return super.get(id);
	}
	
	public List<WarehouseTransferGoods> findList(WarehouseTransferGoods warehouseTransferGoods) {
		return super.findList(warehouseTransferGoods);
	}
	
	public Page<WarehouseTransferGoods> findPage(Page<WarehouseTransferGoods> page, WarehouseTransferGoods warehouseTransferGoods) {
		return super.findPage(page, warehouseTransferGoods);
	}
	
	@Transactional(readOnly = false)
	public void save(WarehouseTransferGoods warehouseTransferGoods) {
		super.save(warehouseTransferGoods);
	}
	
	@Transactional(readOnly = false)
	public void delete(WarehouseTransferGoods warehouseTransferGoods) {
		super.delete(warehouseTransferGoods);
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
	public Page<GoodsUnit> findPageBygoodsUnit(Page<GoodsUnit> page, GoodsUnit goodsUnit) {
		goodsUnit.setPage(page);
		page.setList(dao.findListBygoodsUnit(goodsUnit));
		return page;
	}
	public Page<WarehouseTransferOrder> findPageBytransferOrder(Page<WarehouseTransferOrder> page, WarehouseTransferOrder transferOrder) {
		transferOrder.setPage(page);
		page.setList(dao.findListBytransferOrder(transferOrder));
		return page;
	}
	
	
	
}