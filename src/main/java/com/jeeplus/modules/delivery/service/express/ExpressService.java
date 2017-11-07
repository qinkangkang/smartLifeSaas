/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.delivery.service.express;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.delivery.entity.express.Express;
import com.jeeplus.modules.delivery.dao.express.ExpressDao;

/**
 * 物流信息Service
 * @author qkk
 * @version 2017-06-01
 */
@Service
@Transactional(readOnly = true)
public class ExpressService extends CrudService<ExpressDao, Express> {
	
	@Autowired
	protected ExpressDao expressDao;

	public Express get(String id) {
		return super.get(id);
	}
	
	public List<Express> findList(Express express) {
		return super.findList(express);
	}
	
	public Page<Express> findPage(Page<Express> page, Express express) {
		return super.findPage(page, express);
	}
	
	@Transactional(readOnly = false)
	public void save(Express express) {
		super.save(express);
	}
	
	@Transactional(readOnly = false)
	public void delete(Express express) {
		super.delete(express);
	}
	
	@Transactional(readOnly = false)
	public void deleteByStatus(Express express) {
		expressDao.deleteByStatus(express);
	}
	
	
	
	
}