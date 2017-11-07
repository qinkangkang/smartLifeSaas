/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.service.procurement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.order.dao.procurement.OrderProDetailDao;

/**
 * 采购单详情Service采购退单辅助
 * 
 * @author diqiang
 * @version 2017-06-15
 */
@Service
@Transactional(readOnly = true)
public class OrderProDetailService extends CrudService<OrderProDetailDao, OrderProDetail> {

	public OrderProDetail get(String id) {
		return super.get(id);
	}

	public List<OrderProDetail> findList(OrderProDetail orderProDetail) {
		return super.findList(orderProDetail);
	}

	public Page<OrderProDetail> findPage(Page<OrderProDetail> page, OrderProDetail orderProDetail) {
		return super.findPage(page, orderProDetail);
	}

	@Transactional(readOnly = false)
	public void save(OrderProDetail orderProDetail) {
		super.save(orderProDetail);
	}

	@Transactional(readOnly = false)
	public void delete(OrderProDetail orderProDetail) {
		super.delete(orderProDetail);
	}

	public Page<OrderProcurement> findPageByforderprocurement(Page<OrderProcurement> page,
			OrderProcurement forderprocurement) {
		//权限过滤
		customDataScopeFilter(forderprocurement, "dsf","id=a.office_id","id=a.create_by");
//		forderprocurement.getSqlMap().put("dsf", dataScopeFilter(forderprocurement.getCurrentUser(), "fsponsor", "createby"));
		forderprocurement.setPage(page);
		page.setList(dao.findListByforderprocurement(forderprocurement));
		return page;
	}

	public Page<GoodsSpu> findPageByfspu(Page<GoodsSpu> page, GoodsSpu fspu) {
		//权限过滤
//		customDataScopeFilter(fspu, "dsf","id=a.office_id","id=a.create_by");
//		fspu.getSqlMap().put("dsf", dataScopeFilter(fspu.getCurrentUser(), "fsponsor", "createby"));
			fspu.setPage(page);
			page.setList(dao.findListByfspu(fspu));
			return page;	
	}

	public Page<GoodsSku> findPageByfsku(Page<GoodsSku> page, GoodsSku fsku) {
		//权限过滤
//		customDataScopeFilter(fsku, "dsf","id=a.office_id","id=a.create_by");
//		fsku.getSqlMap().put("dsf", dataScopeFilter(fsku.getCurrentUser(), "fsponsor", "createby"));
		fsku.setPage(page);
		page.setList(dao.findListByfsku(fsku));
		return page;
	}

	public Page<Colors> findPageBycolors(Page<Colors> page, Colors colors) {
		//权限过滤
//		customDataScopeFilter(colors, "dsf","id=a.office_id","id=a.create_by");
//		colors.getSqlMap().put("dsf", dataScopeFilter(colors.getCurrentUser(), "fsponsor", "createby"));
		colors.setPage(page);
		page.setList(dao.findListBycolors(colors));
		return page;
	}

	public Page<Size> findPageBySize(Page<Size> page, Size size) {
		//权限过滤
//		customDataScopeFilter(size, "dsf","id=a.office_id","id=a.create_by");
//		size.getSqlMap().put("dsf", dataScopeFilter(size.getCurrentUser(), "fsponsor", "createby"));
		size.setPage(page);
		page.setList(dao.findListBysize(size));
		return page;
	}

	public Page<SupplierBasic> findPageBySupplier(Page<SupplierBasic> page, SupplierBasic supplierBasic) {
		// TODO Auto-generated method stub
		//权限过滤
		customDataScopeFilter(supplierBasic, "dsf","id=a.office_id","id=a.create_by");
//		supplierBasic.getSqlMap().put("dsf", dataScopeFilter(supplierBasic.getCurrentUser(), "fsponsor", "createby"));
		supplierBasic.setPage(page);
		page.setList(dao.findBySupplier(supplierBasic));
		return page;
	}

	public Page<Warehouse> findPageByWarehouse(Page<Warehouse> page, Warehouse warehouse) {
		// TODO Auto-generated method stub
		//权限过滤
		customDataScopeFilter(warehouse, "dsf","id=a.office_id","id=a.create_by");
//		supplierBasic.getSqlMap().put("dsf", dataScopeFilter(supplierBasic.getCurrentUser(), "fsponsor", "createby"));
		warehouse.setPage(page);
		page.setList(dao.findByWarehouse(warehouse));
		return page;
	}

}