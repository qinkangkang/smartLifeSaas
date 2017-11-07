/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.service.employee;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.store.entity.storemanage.Store;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.store.entity.employee.Employee;
import com.jeeplus.modules.store.dao.employee.EmployeeDao;

/**
 * 员工账号管理Service
 * @author 金圣智
 * @version 2017-06-23
 */
@Service
@Transactional(readOnly = true)
public class EmployeeService extends CrudService<EmployeeDao, Employee> {

	public Employee get(String id) {
		return super.get(id);
	}
	
	public List<Employee> findList(Employee employee) {
		return super.findList(employee);
	}
	
	public Page<Employee> findPage(Page<Employee> page, Employee employee) {
		return super.findPage(page, employee);
	}
	
	@Transactional(readOnly = false)
	public void save(Employee employee) {
		super.save(employee);
	}
	
	@Transactional(readOnly = false)
	public void delete(Employee employee) {
		super.delete(employee);
	}
	
	public Page<Store> findPageBystore(Page<Store> page, Store store) {
		store.setPage(page);
		page.setList(dao.findListBystore(store));
		return page;
	}
	
	
	
}