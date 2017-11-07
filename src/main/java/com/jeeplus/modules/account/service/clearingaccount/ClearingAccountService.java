/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service.clearingaccount;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.dao.clearingaccount.ClearingAccountDao;

/**
 * 结算账户Service
 * @author 金圣智
 * @version 2017-06-01
 */
@Service
@Transactional(readOnly = true)
public class ClearingAccountService extends CrudService<ClearingAccountDao, ClearingAccount> {

	public ClearingAccount get(String id) {
		return super.get(id);
	}
	
	public List<ClearingAccount> findList(ClearingAccount clearingAccount) {
		customDataScopeFilter(clearingAccount, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findList(clearingAccount);
	}
	
	public Page<ClearingAccount> findPage(Page<ClearingAccount> page, ClearingAccount clearingAccount) {
		customDataScopeFilter(clearingAccount, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findPage(page, clearingAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(ClearingAccount clearingAccount) {
		super.save(clearingAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(ClearingAccount clearingAccount) {
		super.delete(clearingAccount);
	}
	
	
	
	
}