/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao.customersummary;

import com.jeeplus.modules.customer.entity.category.CustomerCate;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.customersummary.CustomerSummary;

/**
 * 客户基本信息DAO接口
 * @author diqiang
 * @version 2017-06-07
 */
@MyBatisDao
public interface CustomerSummaryDao extends CrudDao<CustomerSummary> {

	public List<CustomerCate> findListBycustomerCate(CustomerCate customerCate);
	
}