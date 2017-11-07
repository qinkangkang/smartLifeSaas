/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.dao;

import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferOrder;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;

/**
 * 仓库调拨DAO接口
 * @author hxting
 * @version 2017-06-12
 */
@MyBatisDao
public interface WarehouseTransferGoodsDao extends CrudDao<WarehouseTransferGoods> {

	public List<GoodsSpu> findListBygoodsSpu(GoodsSpu goodsSpu);
	public List<GoodsSku> findListBygoodsSku(GoodsSku goodsSku);
	public List<GoodsUnit> findListBygoodsUnit(GoodsUnit goodsUnit);
	public List<WarehouseTransferOrder> findListBytransferOrder(WarehouseTransferOrder transferOrder);
	
}