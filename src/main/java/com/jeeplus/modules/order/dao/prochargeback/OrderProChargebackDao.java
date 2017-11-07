/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.dao.prochargeback;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChargeback;

/**
 * 采购退单DAO接口
 * @author diqiang
 * @version 2017-06-13
 */
@MyBatisDao
public interface OrderProChargebackDao extends CrudDao<OrderProChargeback> {

	public List<SupplierBasic> findListByfsupplier(SupplierBasic fsupplier);
	public List<Warehouse> findListByfwarehose(Warehouse fwarehose);
//	public List<User> findListByfseniorarchirist(User fseniorarchirist);
//	public List<User> findListByfexecutor(User fexecutor);
	public List<ClearingAccount> findListByfdclearingaccount(ClearingAccount fdclearingaccountid);
	public List<AccountType> findListByfdaccounttype(AccountType fothermoneytype);
	public List<OrderProcurement> findListByforderprocurement(OrderProcurement forderprocurement);
//	public List<Office> findListByfstore(Office fstore);
	public List<SysModelType> findListByfmodeltypeid(SysModelType fmodeltypeid);
	public List<SysModelType> findListBysysModelType(SysModelType sysModelType);
	public Integer countNum(Integer status);
	
}