/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.Warehouse;

/**
 * 仓库DAO接口
 * @author Hxting
 * @version 2017-07-04
 */
@MyBatisDao
public interface WarehouseDao extends CrudDao<Warehouse> {

	
}