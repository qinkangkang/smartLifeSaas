/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao.capitalaccount;

import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;

/**
 * 资金流水账DAO接口
 * @author 金圣智
 * @version 2017-06-05
 */
@MyBatisDao
public interface CapitalAccountDao extends CrudDao<CapitalAccount> {

	public List<AccountType> findListByaccountType(AccountType accountType);
	public List<ClearingAccount> findListByclearingAccount(ClearingAccount clearingAccount);
	
}