/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.service.storemanage;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.store.entity.storemanage.Store;
import com.jeeplus.modules.store.dao.storemanage.StoreDao;

/**
 * 门店管理Service
 * @author 金圣智
 * @version 2017-06-23
 */
@Service
@Transactional(readOnly = true)
public class StoreService extends CrudService<StoreDao, Store> {

	public Store get(String id) {
		return super.get(id);
	}
	
	public List<Store> findList(Store store) {
		return super.findList(store);
	}
	
	public Page<Store> findPage(Page<Store> page, Store store) {
		return super.findPage(page, store);
	}
	
	@Transactional(readOnly = false)
	public void save(Store store) {
		super.save(store);
	}
	
	@Transactional(readOnly = false)
	public void delete(Store store) {
		super.delete(store);
	}
	
	
	
	
}