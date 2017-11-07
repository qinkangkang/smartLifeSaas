/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.service.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.customer.dao.category.CustomerCateDao;
import com.jeeplus.modules.customer.entity.category.CustomerCate;

/**
 * 客户分类Service
 * @author diqiang
 * @version 2017-05-31
 */
@Service
@Transactional(readOnly = true)
public class CustomerCateService extends CrudService<CustomerCateDao,CustomerCate> {
	
	public CustomerCate get(String id) {
		return super.get(id);
	}
	
	public List<CustomerCate> findList(CustomerCate customerCate) {
		//权限过滤
		customDataScopeFilter(customerCate,"dsf","id=a.office_id", "id=a.create_by");
//		customerCate.getSqlMap().put("dsf", dataScopeFilter(customerCate.getCurrentUser(), "fsponsor", "createby"));
		return super.findList(customerCate);
	}
	
	public Page<CustomerCate> findPage(Page<CustomerCate> page, CustomerCate customerCate) {
		//权限过滤
		customDataScopeFilter(customerCate,"dsf","id=a.office_id", "id=a.create_by");
//		customerCate.getSqlMap().put("dsf", dataScopeFilter(customerCate.getCurrentUser(), "fsponsor", "createby"));
		return super.findPage(page, customerCate);
	}
	
	
	public List<CustomerCate> findAllList(CustomerCate customerCate){
		//权限过滤
		customDataScopeFilter(customerCate,"dsf","id=a.office_id", "id=a.create_by");
	//	customerCate.getSqlMap().put("dsf", dataScopeFilter(customerCate.getCurrentUser(), "fsponsor", "createby"));
		return super.findAllList(customerCate);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerCate customerCate) {
		super.save(customerCate);
	}
	/**
	 * 逻辑删除
	 */
	@Transactional(readOnly = false)
	public void delete(CustomerCate customerCate) {
		//super.delete(customerCate);
		super.deleteByLogic(customerCate);
	}
		
}