/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.dao.prochargeback;

import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;

/**
 * 采购退单DAO接口
 * @author diqiang
 * @version 2017-06-13
 */
@MyBatisDao
public interface OrderProChaDetailDao extends CrudDao<OrderProChaDetail> {

	public List<GoodsSpu> findListByfspu(GoodsSpu fspu);
	public List<GoodsSku> findListByfsku(GoodsSku fsku);
	public List<OrderProcurement> findListByfprocurement(OrderProcurement fprocurement);
	
}