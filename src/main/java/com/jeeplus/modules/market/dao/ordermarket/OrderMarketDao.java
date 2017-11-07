/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.dao.ordermarket;

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.merchant.entity.management.Merchant;

/**
 * 销售订单管理DAO接口
 * @author diqiang
 * @version 2017-06-18
 */
@MyBatisDao
public interface OrderMarketDao extends CrudDao<OrderMarket> {

	public List<CustomerBasic> findListBycustomerBasic(CustomerBasic customerBasic);
	public List<Warehouse> findListBywarehouse(Warehouse warehouse);
	public List<AccountType> findListByaccountType(AccountType accountType);
	public List<ClearingAccount> findListByclearingAccount(ClearingAccount clearingAccount);
//	public List<Merchant> findListByfsponsor(Merchant fsponsor);
	public List<SysModelType> findListBysysModelType(SysModelType sysModelType);
	/**
	 * @author maxiao
	 * @return
	 * 查找每月销售额
	 */
	public List<Map<String, Object>> findMonthPricList();
	/**
	 * @author maxiao
	 * @return
	 * 查找未发货的订单
	 */
	public int findCountByStatus();
	
}