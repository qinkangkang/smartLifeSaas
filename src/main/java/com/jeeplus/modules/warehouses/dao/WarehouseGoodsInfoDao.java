/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.dao;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;

/**
 * 仓库商品DAO接口
 * @author hxting
 * @version 2017-06-07
 */
@MyBatisDao
public interface WarehouseGoodsInfoDao extends CrudDao<WarehouseGoodsInfo> {

	public List<Warehouse> findListBywarehouse(Warehouse warehouse);
	public List<GoodsSpu> findListBygoodsSpu(GoodsSpu goodsSpu);
	public List<GoodsSku> findListBygoodsSku(GoodsSku goodsSku);
	public List<Brand> findListBygoodsBrand(Brand goodsBrand);
	public List<Categorys> findListBygoodsCategory(Categorys goodsCategory);
	public List<GoodsUnit> findListBygoodsUnit(GoodsUnit goodsUnit);
	
}