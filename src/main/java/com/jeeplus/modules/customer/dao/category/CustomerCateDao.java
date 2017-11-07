/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.dao.category;



import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.customer.entity.category.CustomerCate;


/**
 * 客户分类DAO接口
 * @author diqiang
 * @version 2017-05-31
 */
@MyBatisDao

public interface CustomerCateDao extends CrudDao<CustomerCate> {
	
	public List<CustomerCate> findAllList(CustomerCate customerCate);
}