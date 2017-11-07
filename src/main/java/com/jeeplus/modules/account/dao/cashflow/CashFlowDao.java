/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao.cashflow;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.cashflow.CashFlow;

/**
 * 收款流水(线上)DAO接口
 * @author 金圣智
 * @version 2017-06-02
 */
@MyBatisDao
public interface CashFlowDao extends CrudDao<CashFlow> {

	
}