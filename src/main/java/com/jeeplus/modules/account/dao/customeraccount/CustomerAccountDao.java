/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao.customeraccount;

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.customeraccount.CustomerAccount;

/**
 * 客户对账单DAO接口
 * @author 金圣智
 * @version 2017-06-06
 */
@MyBatisDao
public interface CustomerAccountDao extends CrudDao<CustomerAccount> {

	public List<CustomerBasic> findListBycustomerBasic(CustomerBasic customerBasic);
	public List<CustomerCate> findListBycustomerCate(CustomerCate customerCate);
	public List<AccountType> findListByaccountType(AccountType accountType);
	public List<ClearingAccount> findListByclearingAccount(ClearingAccount clearingAccount);
	
}