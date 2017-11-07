/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service.size;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.goods.dao.size.SizeDao;

/**
 * 尺寸设置Service
 * @author maxiao
 * @version 2017-06-05
 */
@Service
@Transactional(readOnly = true)
public class SizeService extends CrudService<SizeDao, Size> {

	public Size get(String id) {
		return super.get(id);
	}
	
	public List<Size> findList(Size size) {
		return super.findList(size);
	}
	
	public Page<Size> findPage(Page<Size> page, Size size) {
		size.getSqlMap().put("dsf", dataScopeFilter(size.getCurrentUser(),"c","createby"));
		User currentuser = UserUtils.getUser();
		if(!"admin".equals(currentuser.getLoginName())){
			// 公司:公司的员工可以看到该公司的产品
			Office company = currentuser.getCompany();
			size.setFsponsorid(company);
		}
		return super.findPage(page, size);
	}
	
	@Transactional(readOnly = false)
	public void save(Size size) {
		super.save(size);
	}
	
	@Transactional(readOnly = false)
	public void delete(Size size) {
		super.delete(size);
	}
	
	
	
	
}