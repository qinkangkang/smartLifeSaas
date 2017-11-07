/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.dao.storemanage;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.store.entity.storemanage.Store;

/**
 * 门店管理DAO接口
 * @author 金圣智
 * @version 2017-06-23
 */
@MyBatisDao
public interface StoreDao extends CrudDao<Store> {

	
}