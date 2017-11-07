/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service.capitalaccount;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;
import com.jeeplus.modules.account.dao.capitalaccount.CapitalAccountDao;

/**
 * 资金流水账Service
 * @author 金圣智
 * @version 2017-06-05
 */
@Service
@Transactional(readOnly = true)
public class CapitalAccountService extends CrudService<CapitalAccountDao, CapitalAccount> {

	public CapitalAccount get(String id) {
		return super.get(id);
	}
	
	public List<CapitalAccount> findList(CapitalAccount capitalAccount) {
		customDataScopeFilter(capitalAccount, "dsf", "id=a.office_id", "id=create_by");
		return super.findList(capitalAccount);
	}
	
	public Page<CapitalAccount> findPage(Page<CapitalAccount> page, CapitalAccount capitalAccount) {
		customDataScopeFilter(capitalAccount, "dsf", "id=a.office_id", "id=create_by");
		return super.findPage(page, capitalAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(CapitalAccount capitalAccount) {
		super.save(capitalAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(CapitalAccount capitalAccount) {
		super.delete(capitalAccount);
	}
	
	public Page<AccountType> findPageByaccountType(Page<AccountType> page, AccountType accountType) {
		customDataScopeFilter(accountType, "dsf", "id=a.office_id", "id=createby");
		accountType.setPage(page);
		page.setList(dao.findListByaccountType(accountType));
		return page;
	}
	public Page<ClearingAccount> findPageByclearingAccount(Page<ClearingAccount> page, ClearingAccount clearingAccount) {
		customDataScopeFilter(clearingAccount, "dsf", "id=a.office_id", "id=create_by");
		clearingAccount.setPage(page);
		page.setList(dao.findListByclearingAccount(clearingAccount));
		return page;
	}
	
	
	
}