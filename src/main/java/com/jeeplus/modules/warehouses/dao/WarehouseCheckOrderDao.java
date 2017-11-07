/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.dao;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckOrder;

/**
 * 仓库盘点DAO接口
 * @author hxting
 * @version 2017-06-19
 */
@MyBatisDao
public interface WarehouseCheckOrderDao extends CrudDao<WarehouseCheckOrder> {

	public List<Warehouse> findListBywarehouse(Warehouse warehouse);
	
}