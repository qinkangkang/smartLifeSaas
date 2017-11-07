/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service.color;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.goods.dao.color.ColorsDao;

/**
 * 颜色管理Service
 * @author maxiao
 * @version 2017-06-05
 */
@Service
@Transactional(readOnly = true)
public class ColorsService extends CrudService<ColorsDao, Colors> {

	public Colors get(String id) {
		return super.get(id);
	}
	
	public List<Colors> findList(Colors colors) {
		return super.findList(colors);
	}
	
	public Page<Colors> findPage(Page<Colors> page, Colors colors) {
		colors.getSqlMap().put("dsf", dataScopeFilter(colors.getCurrentUser(),"c","createby"));
		User currentuser = UserUtils.getUser();
		if(!"admin".equals(currentuser.getLoginName())){
			// 公司:公司的员工可以看到该公司的产品
			Office company = currentuser.getCompany();
			colors.setFsponsorid(company);	
		}
		return super.findPage(page, colors);
	}
	
	@Transactional(readOnly = false)
	public void save(Colors colors) {
		super.save(colors);
	}
	
	@Transactional(readOnly = false)
	public void delete(Colors colors) {
		super.delete(colors);
	}
	
	
	
	
}