/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service.modeltype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.dao.modeltype.SysModelTypeDao;

/**
 * 打印模板Service
 * @author diqiang
 * @version 2017-06-08
 */
@Service
@Transactional(readOnly = true)
public class SysModelTypeService extends CrudService<SysModelTypeDao, SysModelType> {

	public SysModelType get(String id) {
		return super.get(id);
	}
	
	public List<SysModelType> findList(SysModelType sysModelType) {
		return super.findList(sysModelType);
	}
	
	public Page<SysModelType> findPage(Page<SysModelType> page, SysModelType sysModelType) {
		return super.findPage(page, sysModelType);
	}
	
	@Transactional(readOnly = false)
	public void save(SysModelType sysModelType) {
		super.save(sysModelType);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysModelType sysModelType) {
		super.delete(sysModelType);
	}
	
	
	
	
}