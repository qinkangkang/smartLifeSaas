/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service.categorys;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.goods.dao.categorys.CategorysDao;

/**
 * 商品的分类Service
 * @author maxiao
 * @version 2017-06-07
 */
@Service
@Transactional(readOnly = true)
public class CategorysService extends TreeService<CategorysDao, Categorys> {

	public Categorys get(String id) {
		return super.get(id);
	}
	
	public List<Categorys> findList(Categorys categorys) {
		categorys.getSqlMap().put("dsf", dataScopeFilter(categorys.getCurrentUser(),"c","createby"));
		if (StringUtils.isNotBlank(categorys.getParentIds())){
			categorys.setParentIds(","+categorys.getParentIds()+",");
		}
		
		User currentuser = UserUtils.getUser();
		if(!"admin".equals(currentuser.getLoginName())){
			// 公司:公司的员工可以看到该公司的分类
			Office company = currentuser.getCompany();
			categorys.setFsponsorId(company);	
		}
		return super.findList(categorys);
	}
	
	@Transactional(readOnly = false)
	public void save(Categorys categorys) {
		super.save(categorys);
	}
	
	@Transactional(readOnly = false)
	public void delete(Categorys categorys) {
		super.delete(categorys);
	}
	
}