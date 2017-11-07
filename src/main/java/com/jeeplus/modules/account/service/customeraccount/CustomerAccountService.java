/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service.customeraccount;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.customeraccount.CustomerAccount;
import com.jeeplus.modules.account.dao.customeraccount.CustomerAccountDao;

/**
 * 客户对账单Service
 * @author 金圣智
 * @version 2017-06-06
 */
@Service
@Transactional(readOnly = true)
public class CustomerAccountService extends CrudService<CustomerAccountDao, CustomerAccount> {

	public CustomerAccount get(String id) {
		return super.get(id);
	}
	
	public List<CustomerAccount> findList(CustomerAccount customerAccount) {
		//权限过滤
		customDataScopeFilter(customerAccount,"dsf", "id=a.office_id", "id=create_by");
		return super.findList(customerAccount);
	}
	
	public Page<CustomerAccount> findPage(Page<CustomerAccount> page, CustomerAccount customerAccount) {
		//权限过滤
		customDataScopeFilter(customerAccount,"dsf", "id=a.office_id", "id=create_by");
		return super.findPage(page, customerAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerAccount customerAccount) {
		super.save(customerAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerAccount customerAccount) {
		super.delete(customerAccount);
	}
	
	public Page<CustomerBasic> findPageBycustomerBasic(Page<CustomerBasic> page, CustomerBasic customerBasic) {
		//权限过滤
		customDataScopeFilter(customerBasic,"dsf", "id=a.office_id", "id=create_by");
		customerBasic.setPage(page);
		page.setList(dao.findListBycustomerBasic(customerBasic));
		return page;
	}
	public Page<CustomerCate> findPageBycustomerCate(Page<CustomerCate> page, CustomerCate customerCate) {
		//权限过滤
		customDataScopeFilter(customerCate,"dsf", "id=a.office_id", "id=create_by");
		customerCate.setPage(page);
		page.setList(dao.findListBycustomerCate(customerCate));
		return page;
	}
	public Page<AccountType> findPageByaccountType(Page<AccountType> page, AccountType accountType) {
		//权限过滤
		customDataScopeFilter(accountType,"dsf", "id=a.office_id", "id=createby");
		accountType.setPage(page);
		page.setList(dao.findListByaccountType(accountType));
		return page;
	}
	public Page<ClearingAccount> findPageByclearingAccount(Page<ClearingAccount> page, ClearingAccount clearingAccount) {
		//权限过滤
		customDataScopeFilter(clearingAccount,"dsf", "id=a.office_id", "id=create_by");
		clearingAccount.setPage(page);
		page.setList(dao.findListByclearingAccount(clearingAccount));
		return page;
	}
	
	
	
}