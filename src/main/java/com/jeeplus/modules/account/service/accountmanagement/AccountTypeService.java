/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service.accountmanagement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.dao.accountmanagement.AccountTypeDao;

/**
 * 账目类型管理Service
 * @author 金圣智
 * @version 2017-06-05
 */
@Service
@Transactional(readOnly = true)
public class AccountTypeService extends CrudService<AccountTypeDao, AccountType> {

	public AccountType get(String id) {
		return super.get(id);
	}
	
	public List<AccountType> findList(AccountType accountType) {
		customDataScopeFilter(accountType, "dsf", "id=a.office_id", "id=a.createby");
		return super.findList(accountType);
	}
	
	public Page<AccountType> findPage(Page<AccountType> page, AccountType accountType) {
		customDataScopeFilter(accountType, "dsf", "id=a.office_id", "id=a.createby");
		return super.findPage(page, accountType);
	}
	
	@Transactional(readOnly = false)
	public void save(AccountType accountType) {
		super.save(accountType);
	}
	
	@Transactional(readOnly = false)
	public void delete(AccountType accountType) {
		super.delete(accountType);
	}
	
	
	
	
}