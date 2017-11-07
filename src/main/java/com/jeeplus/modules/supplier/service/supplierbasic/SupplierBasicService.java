/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.supplier.service.supplierbasic;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.supplier.dao.supplierbasic.SupplierBasicDao;

/**
 * 供应商管理Service
 * @author 金圣智
 * @version 2017-06-06
 */
@Service
@Transactional(readOnly = true)
public class SupplierBasicService extends CrudService<SupplierBasicDao, SupplierBasic> {

	public SupplierBasic get(String id) {
		return super.get(id);
	}
	
	public List<SupplierBasic> findList(SupplierBasic supplierBasic) {
		customDataScopeFilter(supplierBasic, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findList(supplierBasic);
	}
	
	public Page<SupplierBasic> findPage(Page<SupplierBasic> page, SupplierBasic supplierBasic) {
		customDataScopeFilter(supplierBasic, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findPage(page, supplierBasic);
	}
	
	@Transactional(readOnly = false)
	public void save(SupplierBasic supplierBasic) {
		super.save(supplierBasic);
	}
	
	@Transactional(readOnly = false)
	public void delete(SupplierBasic supplierBasic) {
		super.delete(supplierBasic);
	}
	
	
	
	
}