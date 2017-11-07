/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.dao;


import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckGoods;

/**
 * 盘点商品报表DAO接口
 * 
 * @author hxting
 * @version 2017-07-25
 */
@MyBatisDao
public interface CheckReportDao extends CrudDao<WarehouseCheckGoods> {

	/**
	 * 盘点商品数
	 * @param warehouseCheckGoods
	 * @return
	 */
	public int getCheckGoodsNum(WarehouseCheckGoods warehouseCheckGoods);

	/**
	 * 盘点仓库数
	 * @param warehouseCheckGoods
	 * @return
	 */
	public int getCheckWarehouseNum(WarehouseCheckGoods warehouseCheckGoods);

	/**
	 * 盘点盈亏数
	 * @param warehouseCheckGoods
	 * @return
	 */
	public int getCheckProfitAndLossNum(WarehouseCheckGoods warehouseCheckGoods);

	/**
	 * 盘点盈亏金额
	 * @param warehouseCheckGoods
	 * @return
	 */
	public double getCheckProfitAndLossMoney(WarehouseCheckGoods warehouseCheckGoods);
	

}