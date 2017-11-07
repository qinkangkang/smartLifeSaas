/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service.cashflow;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.cashflow.CashFlow;
import com.jeeplus.modules.account.dao.cashflow.CashFlowDao;

/**
 * 收款流水(线上)Service
 * @author 金圣智
 * @version 2017-06-02
 */
@Service
@Transactional(readOnly = true)
public class CashFlowService extends CrudService<CashFlowDao, CashFlow> {

	public CashFlow get(String id) {
		return super.get(id);
	}
	
	public List<CashFlow> findList(CashFlow cashFlow) {
		return super.findList(cashFlow);
	}
	
	public Page<CashFlow> findPage(Page<CashFlow> page, CashFlow cashFlow) {
		return super.findPage(page, cashFlow);
	}
	
	@Transactional(readOnly = false)
	public void save(CashFlow cashFlow) {
		super.save(cashFlow);
	}
	
	@Transactional(readOnly = false)
	public void delete(CashFlow cashFlow) {
		super.delete(cashFlow);
	}
	
	
	
	
}