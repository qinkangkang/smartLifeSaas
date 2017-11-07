/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.service.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.customer.dao.basic.CustomerBasicDao;
import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 客户基本信息Service
 * @author diqiang
 * @version 2017-06-01
 */
@Service
@Transactional(readOnly = true)
public class CustomerBasicService extends CrudService<CustomerBasicDao, CustomerBasic> {
	@Autowired
	private CustomerBasicDao customerBasicDao;
	public CustomerBasic get(String id) {
		return super.get(id);
	}
	
	public List<CustomerBasic> findList(CustomerBasic customerBasic) {
		//权限过滤
		customDataScopeFilter(customerBasic,"dsf","id=a.office_id", "id=a.create_by");
//		customerBasic.getSqlMap().put("dsf", dataScopeFilter(customerBasic.getCurrentUser(), "fsponsor", "createby"));
		return super.findList(customerBasic);
	}
	
	public Page<CustomerBasic> findPage(Page<CustomerBasic> page, CustomerBasic customerBasic) {
		//权限过滤
		customDataScopeFilter(customerBasic,"dsf","id=a.office_id", "id=a.create_by");
//		customerBasic.getSqlMap().put("dsf", dataScopeFilter(customerBasic.getCurrentUser(), "fsponsor", "createby"));
		return super.findPage(page, customerBasic);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerBasic customerBasic) {
		super.save(customerBasic);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerBasic customerBasic) {
		super.deleteByLogic(customerBasic);
	}	
	
	public CustomerBasic findByCellphone(CustomerBasic customerBasic){
		//权限过滤
//		customDataScopeFilter(customerBasic,"dsf","id=a.office_id", "id=a.create_by");
//		customerBasic.getSqlMap().put("dsf", dataScopeFilter(customerBasic.getCurrentUser(), "fsponsor", "createby"));
		return customerBasicDao.findByCellphone(customerBasic);
	}
	
	
	
}