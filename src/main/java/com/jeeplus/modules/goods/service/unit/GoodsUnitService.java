/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service.unit;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.goods.dao.unit.GoodsUnitDao;

/**
 * 单位管理Service
 * @author maxiao
 * @version 2017-06-05
 */
@Service
@Transactional(readOnly = true)
public class GoodsUnitService extends CrudService<GoodsUnitDao, GoodsUnit> {

	public GoodsUnit get(String id) {
		return super.get(id);
	}
	
	public List<GoodsUnit> findList(GoodsUnit goodsUnit) {
		return super.findList(goodsUnit);
	}
	
	public Page<GoodsUnit> findPage(Page<GoodsUnit> page, GoodsUnit goodsUnit) {
		goodsUnit.getSqlMap().put("dsf", dataScopeFilter(goodsUnit.getCurrentUser(),"c","createby"));
		User currentuser = UserUtils.getUser();
		if(!"admin".equals(currentuser.getLoginName())){
			// 公司:公司的员工可以看到该公司的产品
			Office company = currentuser.getCompany();
			goodsUnit.setFsponsorid(company);	
		}
		return super.findPage(page, goodsUnit);
	}
	
	@Transactional(readOnly = false)
	public void save(GoodsUnit goodsUnit) {
		super.save(goodsUnit);
	}
	
	@Transactional(readOnly = false)
	public void delete(GoodsUnit goodsUnit) {
		super.delete(goodsUnit);
	}
	
	
	
	
}