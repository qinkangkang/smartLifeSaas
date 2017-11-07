/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao.clearingaccount;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;

/**
 * 结算账户DAO接口
 * @author 金圣智
 * @version 2017-06-01
 */
@MyBatisDao
public interface ClearingAccountDao extends CrudDao<ClearingAccount> {

	
}