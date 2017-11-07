/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.dao.ordermarketchargeback;

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketCha;
import com.jeeplus.modules.merchant.entity.management.Merchant;

/**
 * 销售退货单DAO接口
 * @author diqiang
 * @version 2017-06-18
 */
@MyBatisDao
public interface OrderMarketChaDao extends CrudDao<OrderMarketCha> {

	public List<CustomerBasic> findListBycustomerBasic(CustomerBasic customerBasic);
	public List<Warehouse> findListBywarehouse(Warehouse warehouse);
	public List<ClearingAccount> findListByclearingAccount(ClearingAccount clearingAccount);
	public List<AccountType> findListByaccountType(AccountType accountType);
//	public List<Merchant> findListByfsponsor(Merchant fsponsor);
	public List<SysModelType> findListByfmodeltype(SysModelType fmodeltype);
	
}