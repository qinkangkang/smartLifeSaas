/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service.supplieraccount;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.supplieraccount.SupplierAccount;
import com.jeeplus.modules.account.dao.supplieraccount.SupplierAccountDao;

/**
 * 供应商对账Service
 * @author 金圣智
 * @version 2017-06-06
 */
@Service
@Transactional(readOnly = true)
public class SupplierAccountService extends CrudService<SupplierAccountDao, SupplierAccount> {

	public SupplierAccount get(String id) {
		return super.get(id);
	}
	
	public List<SupplierAccount> findList(SupplierAccount supplierAccount) {
		//权限数据过滤
		customDataScopeFilter(supplierAccount, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findList(supplierAccount);
	}
	
	public Page<SupplierAccount> findPage(Page<SupplierAccount> page, SupplierAccount supplierAccount) {
		//权限数据过滤
		customDataScopeFilter(supplierAccount, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findPage(page, supplierAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(SupplierAccount supplierAccount) {
		super.save(supplierAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(SupplierAccount supplierAccount) {
		super.delete(supplierAccount);
	}
	
	public Page<SupplierBasic> findPageBysupplierBasic(Page<SupplierBasic> page, SupplierBasic supplierBasic) {
		//权限数据过滤
		customDataScopeFilter(supplierBasic, "dsf", "id=a.office_id", "id=a.create_by");
		supplierBasic.setPage(page);
		page.setList(dao.findListBysupplierBasic(supplierBasic));
		return page;
	}
	public Page<AccountType> findPageByaccountType(Page<AccountType> page, AccountType accountType) {
		//权限数据过滤
		customDataScopeFilter(accountType, "dsf", "id=a.office_id", "id=a.createby");
		accountType.setPage(page);
		page.setList(dao.findListByaccountType(accountType));
		return page;
	}
	public Page<ClearingAccount> findPageByclearingAccount(Page<ClearingAccount> page, ClearingAccount clearingAccount) {
		//权限数据过滤
		customDataScopeFilter(clearingAccount, "dsf", "id=a.office_id", "id=a.create_by");
		clearingAccount.setPage(page);
		page.setList(dao.findListByclearingAccount(clearingAccount));
		return page;
	}
	
	
	
}