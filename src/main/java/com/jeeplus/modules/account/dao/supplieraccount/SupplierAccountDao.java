/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao.supplieraccount;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.supplieraccount.SupplierAccount;

/**
 * 供应商对账DAO接口
 * @author 金圣智
 * @version 2017-06-06
 */
@MyBatisDao
public interface SupplierAccountDao extends CrudDao<SupplierAccount> {

	public List<SupplierBasic> findListBysupplierBasic(SupplierBasic supplierBasic);
	public List<AccountType> findListByaccountType(AccountType accountType);
	public List<ClearingAccount> findListByclearingAccount(ClearingAccount clearingAccount);
	
}