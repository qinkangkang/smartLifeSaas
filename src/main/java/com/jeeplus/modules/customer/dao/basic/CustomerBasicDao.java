/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.dao.basic;

import com.jeeplus.common.persistence.CrudDao;

import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.customer.entity.basic.CustomerBasic;

/**
 * 客户基本信息DAO接口
 * @author diqiang
 * @version 2017-06-01
 */
@MyBatisDao
public interface CustomerBasicDao extends CrudDao<CustomerBasic> {

	CustomerBasic findByCellphone(CustomerBasic customerBasic);

	
}