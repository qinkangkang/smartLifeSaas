/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.dao;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;

/**
 * 仓库流水DAO接口
 * @author hxting
 * @version 2017-06-15
 */
@MyBatisDao
public interface WarehouseRecordDao extends CrudDao<WarehouseRecord> {

	public List<Warehouse> findListBywarehouse(Warehouse warehouse);
	public List<GoodsSpu> findListBygoodsSpu(GoodsSpu goodsSpu);
	public List<GoodsSku> findListBygoodsSku(GoodsSku goodsSku);
	
}