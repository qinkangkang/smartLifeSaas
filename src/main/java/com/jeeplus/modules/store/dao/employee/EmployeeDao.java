/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.dao.employee;

import com.jeeplus.modules.store.entity.storemanage.Store;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.store.entity.employee.Employee;

/**
 * 员工账号管理DAO接口
 * @author 金圣智
 * @version 2017-06-23
 */
@MyBatisDao
public interface EmployeeDao extends CrudDao<Employee> {

	public List<Store> findListBystore(Store store);
	
}