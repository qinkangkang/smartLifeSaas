/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.dao.ordermarket;

import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;

/**
 * 销售订单管理DAO接口
 * @author diqiang
 * @version 2017-06-18
 */
@MyBatisDao
public interface OrderMarketDetailDao extends CrudDao<OrderMarketDetail> {

	public List<OrderMarket> findListByorderMarket(OrderMarket orderMarket);
	public List<GoodsSpu> findListBygoodsSpu(GoodsSpu goodsSpu);
	public List<GoodsSku> findListBygoodsSku(GoodsSku goodsSku);
	public List<Colors> findListBycolors(Colors colors);
	public List<Size> findListBysize(Size size);
	public List<WarehouseGoodsInfo> findPageBywarehouseGoodesInfo(WarehouseGoodsInfo warehouseGoodsInfo);
	
}