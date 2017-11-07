/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.dao;


import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;

/**
 * 盘点商品报表DAO接口
 * 
 * @author hxting
 * @version 2017-07-25
 */
@MyBatisDao
public interface TransferReportDao extends CrudDao<WarehouseTransferGoods> {

	
	

}