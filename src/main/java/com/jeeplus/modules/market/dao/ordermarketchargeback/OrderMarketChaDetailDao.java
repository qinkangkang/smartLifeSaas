/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.dao.ordermarketchargeback;

import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketCha;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketChaDetail;

/**
 * 销售退货单DAO接口
 * @author diqiang
 * @version 2017-06-18
 */
@MyBatisDao
public interface OrderMarketChaDetailDao extends CrudDao<OrderMarketChaDetail> {

	public List<OrderMarket> findListByfordermarket(OrderMarket fordermarket);
	public List<GoodsSpu> findListByfspu(GoodsSpu fspu);
	public List<GoodsSku> findListByfsku(GoodsSku fsku);
	public List<OrderMarketCha> findListByfordermarketchargeback(OrderMarketCha fordermarketchargeback);
	
}