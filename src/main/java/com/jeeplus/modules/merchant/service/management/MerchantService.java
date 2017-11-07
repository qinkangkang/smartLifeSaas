/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.merchant.service.management;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.merchant.dao.management.MerchantDao;

/**
 * 商户管理Service
 * @author diqiang
 * @version 2017-06-23
 */
@Service
@Transactional(readOnly = true)
public class MerchantService extends CrudService<MerchantDao, Merchant> {

	public Merchant get(String id) {
		return super.get(id);
	}
	
	public List<Merchant> findList(Merchant merchant) {
		return super.findList(merchant);
	}
	
	public Page<Merchant> findPage(Page<Merchant> page, Merchant merchant) {
		return super.findPage(page, merchant);
	}
	
	@Transactional(readOnly = false)
	public void save(Merchant merchant) {
		super.save(merchant);
	}
	
	@Transactional(readOnly = false)
	public void delete(Merchant merchant) {
		super.delete(merchant);
	}
	
	public Page<Area> findPageByarea(Page<Area> page, Area area) {
		area.setPage(page);
		page.setList(dao.findListByarea(area));
		return page;
	}
	
	
	
}