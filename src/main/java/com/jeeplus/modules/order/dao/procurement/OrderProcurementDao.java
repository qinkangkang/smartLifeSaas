/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.dao.procurement;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;

/**
 * 采购单DAO接口
 * @author diqiang
 * @version 2017-06-12
 */
@MyBatisDao
public interface OrderProcurementDao extends CrudDao<OrderProcurement> {

	public List<SupplierBasic> findListByfsupplier(SupplierBasic fsupplier);
	public List<Warehouse> findListByfwarehose(Warehouse fwarehose);
//	public List<User> findListByfseniorarchirist(User fseniorarchirist);
//	public List<User> findListByfexecutor(User fexecutor);
	public List<ClearingAccount> findListByfdclearingaccountid(ClearingAccount fdclearingaccountid);
	public List<AccountType> findListByfdaccounttype(AccountType fdaccounttype);
//	public List<Office> findListByfsponsor(Office fsponsor);
	public List<SysModelType> findListByfmodeltype(SysModelType fmodeltype);
	
}