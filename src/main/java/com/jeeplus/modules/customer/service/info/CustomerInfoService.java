/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.service.info;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.customer.entity.info.CustomerInfo;
import com.jeeplus.modules.customer.dao.info.CustomerInfoDao;

/**
 * 线上客户Service
 * @author diqiang
 * @version 2017-06-05
 */
@Service
@Transactional(readOnly = true)
public class CustomerInfoService extends CrudService<CustomerInfoDao, CustomerInfo> {

	
	@Autowired
	private CustomerInfoDao customerInfoDao;
	
	public CustomerInfo get(String id) {
		return super.get(id);
	}
	
	public List<CustomerInfo> findList(CustomerInfo customerInfo) {
		return super.findList(customerInfo);
	}
	
	public Page<CustomerInfo> findPage(Page<CustomerInfo> page, CustomerInfo customerInfo) {
		return super.findPage(page, customerInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerInfo customerInfo) {
		super.save(customerInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerInfo customerInfo) {
		super.delete(customerInfo);
	}
	/**
	 * 通过电话号查询
	 * @param customerInfo
	 * @return
	 */
	public CustomerInfo findByCellphone(CustomerInfo customerInfo){
		return customerInfoDao.findByCellphone(customerInfo);
	}
	
}