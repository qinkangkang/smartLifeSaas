/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.dao.WarehouseGoodsInfoDao;

/**
 * 仓库商品Service
 * @author hxting
 * @version 2017-06-07
 */
@Service
@Transactional(readOnly = true)
public class WarehouseGoodsInfoService extends CrudService<WarehouseGoodsInfoDao, WarehouseGoodsInfo> {

	@Autowired
	private WarehouseService warehouseService;
	public WarehouseGoodsInfo get(String id) {
		return super.get(id);
	}
	
	public List<WarehouseGoodsInfo> findList(WarehouseGoodsInfo warehouseGoodsInfo) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		warehouseGoodsInfo.getSqlMap().put("dsf", dataScopeFilter(warehouseGoodsInfo.getCurrentUser(), "o", "createby"));
		return super.findList(warehouseGoodsInfo);
	}
	
	public Page<WarehouseGoodsInfo> findPage(Page<WarehouseGoodsInfo> page, WarehouseGoodsInfo warehouseGoodsInfo) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		warehouseGoodsInfo.getSqlMap().put("dsf", dataScopeFilter(warehouseGoodsInfo.getCurrentUser(), "o", "createby"));
		return super.findPage(page, warehouseGoodsInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WarehouseGoodsInfo warehouseGoodsInfo) {
		User currentUser = warehouseGoodsInfo.getCurrentUser();
		if(!currentUser.isAdmin()){
			Office office = currentUser.getOffice();
			Office company = currentUser.getCompany();
			warehouseGoodsInfo.setCompan(company);
			warehouseGoodsInfo.setOffice(office); 
		}
		super.save(warehouseGoodsInfo);
	}	
	@Transactional(readOnly = false)
	public void delete(WarehouseGoodsInfo warehouseGoodsInfo) {
		super.delete(warehouseGoodsInfo);
	}
	
	public Page<Warehouse> findPageBywarehouse(Page<Warehouse> page, Warehouse warehouse) {
		return warehouseService.findPage(page, warehouse);
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
	public Page<Brand> findPageBygoodsBrand(Page<Brand> page, Brand goodsBrand) {
		goodsBrand.setPage(page);
		page.setList(dao.findListBygoodsBrand(goodsBrand));
		return page;
	}
	public Page<Categorys> findPageBygoodsCategory(Page<Categorys> page, Categorys goodsCategory) {
		goodsCategory.setPage(page);
		page.setList(dao.findListBygoodsCategory(goodsCategory));
		return page;
	}
	public Page<GoodsUnit> findPageBygoodsUnit(Page<GoodsUnit> page, GoodsUnit goodsUnit) {
		goodsUnit.setPage(page);
		page.setList(dao.findListBygoodsUnit(goodsUnit));
		return page;
	}
	
	
	
}