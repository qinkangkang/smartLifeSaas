/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao.accountmanagement;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;

/**
 * 账目类型管理DAO接口
 * @author 金圣智
 * @version 2017-06-05
 */
@MyBatisDao
public interface AccountTypeDao extends CrudDao<AccountType> {

	
}