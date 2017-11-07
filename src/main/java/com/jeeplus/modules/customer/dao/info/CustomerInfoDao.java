/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.dao.info;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.customer.entity.info.CustomerInfo;

/**
 * 线上客户DAO接口
 * @author diqiang
 * @version 2017-06-05
 */
@MyBatisDao
public interface CustomerInfoDao extends CrudDao<CustomerInfo> {

	
	public CustomerInfo findByCellphone(CustomerInfo info);
	
}