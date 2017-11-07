/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service.customersummary;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.customersummary.CustomerSummary;
import com.jeeplus.modules.account.dao.customersummary.CustomerSummaryDao;

/**
 * 客户基本信息Service
 * @author diqiang
 * @version 2017-06-07
 */
@Service
@Transactional(readOnly = true)
public class CustomerSummaryService extends CrudService<CustomerSummaryDao, CustomerSummary> {

	public CustomerSummary get(String id) {
		return super.get(id);
	}
	
	public List<CustomerSummary> findList(CustomerSummary customerSummary) {
		//权限过滤
		customDataScopeFilter(customerSummary,"dsf", "id=a.office_id", "id=create_by");
		return super.findList(customerSummary);
	}
	
	public Page<CustomerSummary> findPage(Page<CustomerSummary> page, CustomerSummary customerSummary) {
		//权限过滤
		customDataScopeFilter(customerSummary,"dsf", "id=a.office_id", "id=create_by");
		return super.findPage(page, customerSummary);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerSummary customerSummary) {
		super.save(customerSummary);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerSummary customerSummary) {
		super.delete(customerSummary);
	}
	
	public Page<CustomerCate> findPageBycustomerCate(Page<CustomerCate> page, CustomerCate customerCate) {
		//权限过滤
		customDataScopeFilter(customerCate,"dsf", "id=a.office_id", "id=create_by");
		customerCate.setPage(page);
		page.setList(dao.findListBycustomerCate(customerCate));
		return page;
	}
	
	
	
}