/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service.barcode;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.entity.barcode.Barcode;
import com.jeeplus.modules.goods.dao.barcode.BarcodeDao;

/**
 * 生成条形码Service
 * @author maxiao
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class BarcodeService extends CrudService<BarcodeDao, Barcode> {

	public Barcode get(String id) {
		return super.get(id);
	}
	
	public List<Barcode> findList(Barcode barcode) {
		return super.findList(barcode);
	}
	
	public Page<Barcode> findPage(Page<Barcode> page, Barcode barcode) {
		return super.findPage(page, barcode);
	}
	
	@Transactional(readOnly = false)
	public void save(Barcode barcode) {
		super.save(barcode);
	}
	
	@Transactional(readOnly = false)
	public void delete(Barcode barcode) {
		super.delete(barcode);
	}
	
	
	
	
}