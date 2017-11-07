/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.dao;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferOrder;

/**
 * 仓库调拨DAO接口
 * @author hxting
 * @version 2017-06-12
 */
@MyBatisDao
public interface WarehouseTransferOrderDao extends CrudDao<WarehouseTransferOrder> {

	public List<Warehouse> findListBywarehouseOut(Warehouse warehouseOut);
	public List<Warehouse> findListByfwarehouseIn(Warehouse fwarehouseIn);
	
	/**
	 * @author maxiao
	 * @return
	 */
	public int findCountByType();
	
}