/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.dao.procurement;

import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;

/**
 * 采购单DAO接口
 * @author diqiang
 * @version 2017-06-12
 */
@MyBatisDao
public interface OrderProDetailDao extends CrudDao<OrderProDetail> {

	public List<OrderProcurement> findListByforderprocurement(OrderProcurement forderprocurement);
	public List<GoodsSpu> findListByfspu(GoodsSpu fspu);
	public List<GoodsSku> findListByfsku(GoodsSku fsku);
	public List<Colors> findListBycolors(Colors colors);
	public List<Size> findListBysize(Size size);
	public List<SupplierBasic> findBySupplier(SupplierBasic supplierBasic);
	public List<Warehouse> findByWarehouse(Warehouse warehouse);
	
}