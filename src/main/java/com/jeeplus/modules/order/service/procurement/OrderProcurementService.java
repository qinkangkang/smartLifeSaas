/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.service.procurement;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.store.entity.employee.Employee;
import com.jeeplus.modules.order.dao.procurement.OrderProcurementDao;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.warehouses.dao.WarehouseGoodsInfoDao;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.goods.dao.sku.GoodsSkuDao;
import com.jeeplus.modules.goods.dao.spu.GoodsSpuDao;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChargeback;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.order.dao.procurement.OrderProDetailDao;

/**
 * 采购单Service
 * 
 * @author diqiang
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class OrderProcurementService extends CrudService<OrderProcurementDao, OrderProcurement> {

	@Autowired
	private OrderProDetailDao orderProDetailDao;

	@Autowired
	private OrderProcurementDao orderProcurementDao;

	// 商品dao
	@Autowired
	private GoodsSpuDao goodsSpuDao;
	// 商品详情sku
	@Autowired
	private GoodsSkuDao goodsSkuDao;

	@Autowired
	private WarehouseGoodsInfoDao warehouseGoodsInfoDao;

	public OrderProcurement get(String id) {
		OrderProcurement orderProcurement = super.get(id);
		if(orderProcurement!=null){
			OrderProDetail orderProDetail = new OrderProDetail(orderProcurement);
//			customDataScopeFilter(orderProDetail, "dsf", "id=a.office_id", "id=a.create_by");
			List<OrderProDetail> findList = orderProDetailDao.findList(orderProDetail);
			orderProcurement.setOrderProDetailList(findList);
		}
		return orderProcurement;
	}
	
	public List<OrderProcurement> findList(OrderProcurement orderProcurement) {
		//权限过滤
		customDataScopeFilter(orderProcurement, "dsf", "id=a.office_id", "id=a.create_by");
//		orderProcurement.getSqlMap().put("dsf",customdataScopeFilter(orderProcurement.getCurrentUser(), "fsponsor", "create_by"));
		return super.findList(orderProcurement);
	}

	public Page<OrderProcurement> findPage(Page<OrderProcurement> page, OrderProcurement orderProcurement) {
		//权限过滤
		customDataScopeFilter(orderProcurement, "dsf", "id=a.office_id", "id=a.create_by");
//		orderProcurement.getSqlMap().put("dsf",customdataScopeFilter(orderProcurement.getCurrentUser(), "fsponsor", "create_by"));
		return super.findPage(page, orderProcurement);
	}

	@Transactional(readOnly = false)
	public void save(OrderProcurement orderProcurement) {
		super.save(orderProcurement);
		for (OrderProDetail orderProDetail : orderProcurement.getOrderProDetailList()) {
			if (orderProDetail.getId() == null) {
				continue;
			}
			if (OrderProDetail.DEL_FLAG_NORMAL.equals(orderProDetail.getDelFlag())) {
				if (StringUtils.isBlank(orderProDetail.getId())) {
					orderProDetail.setForderprocurement(orderProcurement);
					orderProDetail.preInsert();
					try {
						GoodsSku fsku = orderProDetail.getFsku();
						GoodsSpu fspu = orderProDetail.getFspu();
						Warehouse fwarehose = orderProcurement.getFwarehose();
						WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
						wgInfo.setGoodsSku(fsku);
						wgInfo.setGoodsSpu(fspu);
						wgInfo.setWarehouse(fwarehose);
						WarehouseGoodsInfo warehouseGoodsInfo = warehouseGoodsInfoDao.get(wgInfo);
						if (warehouseGoodsInfo != null) {
							orderProDetail.setFwarehouseGoodsInfo(warehouseGoodsInfo);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//设置默认状态为启用
					orderProDetail.setFstatus(1);
					orderProDetailDao.insert(orderProDetail);
				} else {
					orderProDetail.preUpdate();
					orderProDetailDao.update(orderProDetail);
				}
			} else {
				orderProDetailDao.deleteByLogic(orderProDetail);
			}
		}
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(OrderProcurement orderProcurement) {
		super.deleteByLogic(orderProcurement);
		orderProDetailDao.deleteByLogic(new OrderProDetail(orderProcurement));
	}

	public Page<SupplierBasic> findPageByfsupplier(Page<SupplierBasic> page, SupplierBasic fsupplier) {
		//权限过滤
		customDataScopeFilter(fsupplier, "dsf", "id=a.office_id", "id=a.create_by");
//		fsupplier.getSqlMap().put("dsf",customdataScopeFilter(fsupplier.getCurrentUser(), "fsponsor", "create_by"));
		fsupplier.setPage(page);
		page.setList(dao.findListByfsupplier(fsupplier));
		return page;
	}

	public Page<Warehouse> findPageByfwarehose(Page<Warehouse> page, Warehouse fwarehose) {
		//权限过滤
		customDataScopeFilter(fwarehose, "dsf", "id=a.office_id", "id=a.create_by");
//		fwarehose.getSqlMap().put("dsf",customdataScopeFilter(fwarehose.getCurrentUser(), "company_id", "create_by"));
		fwarehose.setPage(page);
		page.setList(dao.findListByfwarehose(fwarehose));
		return page;
	}

//	public Page<User> findPageByfseniorarchirist(Page<User> page, User fseniorarchirist) {
//		//权限过滤
//		customDataScopeFilter(fseniorarchirist, "dsf", "id=a.office_id", "id=a.create_by");
//		fseniorarchirist.setPage(page);
//		page.setList(dao.findListByfseniorarchirist(fseniorarchirist));
//		return page;
//	}
//
//	public Page<User> findPageByfexecutor(Page<User> page, User fexecutor) {
//		//权限过滤
//		customDataScopeFilter(fexecutor, "dsf", "id=a.office_id", "id=a.create_by");
//		fexecutor.setPage(page);
//		page.setList(dao.findListByfexecutor(fexecutor));
//		return page;
//	}

	public Page<ClearingAccount> findPageByfdclearingaccountid(Page<ClearingAccount> page,
			ClearingAccount fdclearingaccountid) {
		//权限过滤
		customDataScopeFilter(fdclearingaccountid, "dsf", "id=a.office_id", "id=a.create_by");
//		fdclearingaccountid.getSqlMap().put("dsf",customdataScopeFilter(fdclearingaccountid.getCurrentUser(), "fsponsor", "create_by"));
		fdclearingaccountid.setPage(page);
		page.setList(dao.findListByfdclearingaccountid(fdclearingaccountid));
		return page;
	}

	public Page<AccountType> findPageByfdaccounttype(Page<AccountType> page, AccountType fdaccounttype) {
		//权限过滤
		customDataScopeFilter(fdaccounttype, "dsf", "id=a.office_id", "id=a.createby");
//		fdaccounttype.getSqlMap().put("dsf",customdataScopeFilter(fdaccounttype.getCurrentUser(), "fsponsor", "createby"));
		fdaccounttype.setPage(page);
		page.setList(dao.findListByfdaccounttype(fdaccounttype));
		return page;
	}

//	public Page<Office> findPageByfsponsor(Page<Office> page, Office fsponsor) {
//		//权限过滤
//		customDataScopeFilter(fsponsor, "dsf", "id=a.parent_id", "id=a.create_by");
//		fsponsor.setPage(page);
//		page.setList(dao.findListByfsponsor(fsponsor));
//		return page;
//	}

	public Page<SysModelType> findPageByfmodeltype(Page<SysModelType> page, SysModelType fmodeltype) {
		fmodeltype.setPage(page);
		page.setList(dao.findListByfmodeltype(fmodeltype));
		return page;
	}

	// /**
	// * 添加实体所需对象属性
	// *
	// * @param op
	// * @return
	// */
	// public void setOrderProcurementProperties(OrderProcurement op) {
	//
	//
	// User createBy = op.getCreateBy();
	// if (createBy != null) {
	// op.setCreateBy(UserUtils.get(createBy.getId()));
	// }
	//
	// User updateBy = op.getUpdateBy();
	// if (updateBy != null) {
	// op.setUpdateBy(UserUtils.get(updateBy.getId()));
	// }
	//
	//
	// OrderProcurement orderProcurement = orderProcurementDao.get(op.getId());
	// // 如果为新对象则不查询详情表
	// if (orderProcurement != null) {
	//
	// OrderProDetail opd = new OrderProDetail();
	// opd.setForderprocurement(orderProcurement);
	//
	// List<OrderProDetail> opdList = orderProDetailDao.findList(opd);
	// // 给详情对象添加商品对象信息用于回显
	// for (OrderProDetail opd1 : opdList) {
	// GoodsSpu fspu = opd1.getFspu();
	// if (fspu != null) {
	// opd1.setFspu(goodsSpuDao.get(fspu.getId()));
	// }
	// GoodsSku fsku = opd1.getFsku();
	// if (fsku != null) {
	// opd1.setFsku(goodsSkuDao.get(fsku.getId()));
	// }
	// }
	// op.setOrderProDetailList(opdList);
	// }
	// // 计算折扣及价钱
	// // 其它费用
	// String fothermoney = op.getFothermoney();
	// boolean ortherEmpty = StringUtils.isNoneEmpty(fothermoney);
	// // 商品价格总额
	// String fcountprice = op.getFcountprice();
	// boolean countEmpty = StringUtils.isNoneEmpty(fcountprice);
	// // 抹零
	// String fcutsmall = op.getFcutsmall();
	// boolean cutEmpty = StringUtils.isNoneEmpty(fcutsmall);
	// // 设置订单总额
	// if (ortherEmpty && countEmpty && cutEmpty) {
	// // 有其他费用有抹零
	// BigDecimal ordercountprice = new BigDecimal(fothermoney).add(new
	// BigDecimal(fcountprice));
	// ordercountprice = ordercountprice.subtract(new BigDecimal(fcutsmall));
	// op.setFordercountprice(ordercountprice.toString());
	// } else if (!ortherEmpty && countEmpty && cutEmpty) {
	// // 无其他费用有抹零
	// BigDecimal ordercountprice = new BigDecimal(fcountprice).subtract(new
	// BigDecimal(fcutsmall));
	// op.setFordercountprice(ordercountprice.toString());
	// } else if (ortherEmpty && countEmpty && !cutEmpty) {
	// // 有其他费用无抹零
	// BigDecimal ordercountprice = new BigDecimal(fothermoney).add(new
	// BigDecimal(fcountprice));
	// op.setFordercountprice(ordercountprice.toString());
	// } else {
	// op.setFordercountprice(fcountprice);
	// }
	//
	// // 实付款
	// String factualpayment = op.getFactualpayment();
	// boolean actualpaymentEmpty = StringUtils.isNoneEmpty(factualpayment);
	// // 订单总额
	// String fordercountprice = op.getFordercountprice();
	// boolean ordercountEmpty = StringUtils.isNoneEmpty(fordercountprice);
	//
	// // 设置欠款
	// if (actualpaymentEmpty && ordercountEmpty) {
	// BigDecimal fdebt = new BigDecimal(fordercountprice).subtract(new
	// BigDecimal(factualpayment));
	// op.setFdebt(fdebt.toString());
	// }
	//
	// }

	/**
	 * 添加实体所需对象属性
	 * 
	 * @param op
	 * @return
	 */
	public void setOrderProcurementProperties(OrderProcurement op) {

		User createBy = op.getCreateBy();
		if (createBy != null) {
			op.setCreateBy(UserUtils.get(createBy.getId()));
		}

		User updateBy = op.getUpdateBy();
		if (updateBy != null) {
			op.setUpdateBy(UserUtils.get(updateBy.getId()));
		}

//		OrderProcurement orderProcurement = orderProcurementDao.get(op.getId());
//		// 如果为新对象则不查询详情表
//		if (orderProcurement != null) {
//
//			OrderProDetail opd = new OrderProDetail();
//			opd.setForderprocurement(orderProcurement);
//
//			List<OrderProDetail> opdList = orderProDetailDao.findList(opd);
//			// 给详情对象添加商品对象信息用于回显
//			for (OrderProDetail opd1 : opdList) {
//				GoodsSpu fspu = opd1.getFspu();
//				if (fspu != null) {
//					opd1.setFspu(goodsSpuDao.get(fspu.getId()));
//				}
//				GoodsSku fsku = opd1.getFsku();
//				if (fsku != null) {
//					opd1.setFsku(goodsSkuDao.get(fsku.getId()));
//				}
//			}
//			op.setOrderProDetailList(opdList);
//		}
		// 计算折扣及价钱
		// 其它费用
		BigDecimal fothermoney = op.getFothermoney();
		boolean ortherEmpty = (fothermoney != null && fothermoney.doubleValue() > 0);
		// 商品价格总额
		BigDecimal fcountprice = op.getFcountprice();
		boolean countEmpty = (fcountprice != null && fcountprice.doubleValue() > 0);
		// 设置订单总额
		if (ortherEmpty && countEmpty) {
			// 有其他费用
			BigDecimal ordercountprice = fothermoney.add(fcountprice);
			op.setFordercountprice(ordercountprice);
		}else {
			op.setFordercountprice(fcountprice);
		}

		// 实付款
		BigDecimal factualpayment = op.getFactualpayment();
		boolean actualpaymentEmpty = (factualpayment != null && factualpayment.doubleValue() > 0);
		// 订单总额
		BigDecimal fordercountprice = op.getFordercountprice();
		boolean ordercountEmpty = (fordercountprice != null && fordercountprice.doubleValue() > 0);

		// 设置欠款
		if (actualpaymentEmpty && ordercountEmpty) {
			BigDecimal fdebt = fordercountprice.subtract(factualpayment);
			op.setFdebt(fdebt);
		}

	}

	// 有整单折扣重新设置欠款
	public void setfdebt(OrderProcurement op) {
		// 实付款
		BigDecimal factualpayment = op.getFactualpayment();
		boolean actualpaymentEmpty = (factualpayment != null && factualpayment.doubleValue() > 0);
		// 订单折后总额
		BigDecimal fordercountprice = op.getFdiscountprice();
		boolean ordercountEmpty = (fordercountprice != null && fordercountprice.doubleValue() > 0);

		// 设置欠款
		if (actualpaymentEmpty && ordercountEmpty) {
			BigDecimal fdebt = fordercountprice.subtract(factualpayment);
			op.setFdebt(fdebt);
		}
	}

	// /**
	// * 设置单条商品金额
	// *
	// * @param opd
	// */
	// public void setOrderProDetailProperties(OrderProDetail opd) {
	//
	// // 设置采购单对象
	// OrderProcurement forderprocurement = opd.getForderprocurement();
	//
	//
	// if (forderprocurement != null) {
	// OrderProcurement orderProcurement =
	// orderProcurementDao.get(forderprocurement.getId());
	//
	//
	//
	// opd.setForderprocurement(orderProcurement);
	// }
	// // 设置库存对象
	// WarehouseGoodsInfo fwarehouseGoodsInfo =opd.getFwarehouseGoodsInfo();
	// if (fwarehouseGoodsInfo != null) {
	// WarehouseGoodsInfo warehouseGoodsInfo =
	// warehouseGoodsInfoDao.get(fwarehouseGoodsInfo.getId());
	// opd.setFwarehouseGoodsInfo(warehouseGoodsInfo);
	// }
	//
	// setgoods(opd);
	// int fgoodsnum = 0;
	// // 商品数量
	// Integer goodsnum = opd.getFgoodsnum();
	// if (goodsnum != null) {
	// fgoodsnum = goodsnum;
	// }
	// // 商品单价
	// String fgoodsprice = opd.getFgoodsprice();
	// // 商品折扣
	// String fgoodsdiscount = opd.getFgoodsdiscount();
	// boolean goodsdiscountEmpty = StringUtils.isNoneEmpty(fgoodsdiscount);
	// // 折扣
	// BigDecimal discount = null;
	// // 单价
	// BigDecimal goodsprice = null;
	// // 数量
	// BigDecimal num = null;
	//
	// if (fgoodsnum > 0 && goodsdiscountEmpty) {
	//
	// discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal("10"));
	// goodsprice = new BigDecimal(fgoodsprice);
	// num = new BigDecimal(String.valueOf(fgoodsnum));
	//
	// // 折后单价
	// BigDecimal discountprice = goodsprice.multiply(discount);
	// opd.setFdiscountprice(discountprice.toString());
	// // 折前总额
	// BigDecimal countmoney = goodsprice.multiply(num);
	// opd.setFcountmoney(countmoney.toString());
	// // 折后总额
	// BigDecimal discountcountmoney = discountprice.multiply(num);
	// opd.setFdiscountcountmoney(discountcountmoney.toString());
	// } else if (fgoodsnum > 0 && !goodsdiscountEmpty) {
	// goodsprice = new BigDecimal(fgoodsprice);
	// num = new BigDecimal(String.valueOf(fgoodsnum));
	// // 折前总额
	// BigDecimal countmoney = goodsprice.multiply(num);
	// opd.setFcountmoney(countmoney.toString());
	// // 折后总额
	// opd.setFdiscountcountmoney(countmoney.toString());
	// // 折后单价
	// opd.setFdiscountprice(opd.getFgoodsprice());
	// } else if (fgoodsnum == 0 && goodsdiscountEmpty) {
	//
	// discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal("10"));
	// goodsprice = new BigDecimal(fgoodsprice);
	// // 折后单价
	// BigDecimal discountprice = goodsprice.multiply(discount);
	// opd.setFdiscountprice(discountprice.toString());
	// // 折前总额
	// opd.setFcountmoney(fgoodsprice);
	// // 折后总额
	// opd.setFdiscountcountmoney(discountprice.toString());
	// } else {
	// // 折后单价
	// opd.setFdiscountprice(fgoodsprice);
	// // 折前总额
	// opd.setFcountmoney(fgoodsprice);
	// // 折后总额
	// opd.setFdiscountcountmoney(fgoodsprice);
	// }
	// }

	/**
	 * 设置单条商品金额
	 * 
	 * @param opd
	 */
	public void setOrderProDetailProperties(OrderProDetail opd) {

		// 设置采购单对象
		OrderProcurement forderprocurement = opd.getForderprocurement();

		if (forderprocurement != null) {
			OrderProcurement orderProcurement = orderProcurementDao.get(forderprocurement.getId());

			opd.setForderprocurement(orderProcurement);
		}
		// 设置库存对象
		WarehouseGoodsInfo fwarehouseGoodsInfo = opd.getFwarehouseGoodsInfo();
		if (fwarehouseGoodsInfo != null) {
			WarehouseGoodsInfo warehouseGoodsInfo = warehouseGoodsInfoDao.get(fwarehouseGoodsInfo.getId());
			opd.setFwarehouseGoodsInfo(warehouseGoodsInfo);
		}

		setgoods(opd);
		int fgoodsnum = 0;
		// 商品数量
		Integer goodsnum = opd.getFgoodsnum();
		if (goodsnum != null) {
			fgoodsnum = goodsnum;
		}
		// 商品单价
		BigDecimal fgoodsprice = opd.getFgoodsprice();
		// 商品折扣
		String fgoodsdiscount = opd.getFgoodsdiscount();
		boolean goodsdiscountEmpty = StringUtils.isNoneEmpty(fgoodsdiscount);
		// 折扣
		BigDecimal discount = null;
		// 单价
		BigDecimal goodsprice = null;
		// 数量
		BigDecimal num = null;

		if (fgoodsnum > 0 && goodsdiscountEmpty) {

			discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal(100));
			goodsprice = fgoodsprice;
			num = new BigDecimal(String.valueOf(fgoodsnum));

			// 折后单价
			BigDecimal discountprice = goodsprice.multiply(discount);
			opd.setFdiscountprice(discountprice);
			// 折前总额
			BigDecimal countmoney = goodsprice.multiply(num);
			opd.setFcountmoney(countmoney);
			// 折后总额
			BigDecimal discountcountmoney = discountprice.multiply(num);
			opd.setFdiscountcountmoney(discountcountmoney);
		} else if (fgoodsnum > 0 && !goodsdiscountEmpty) {
			goodsprice = fgoodsprice;
			num = new BigDecimal(String.valueOf(fgoodsnum));
			// 折前总额
			BigDecimal countmoney = goodsprice.multiply(num);
			opd.setFcountmoney(countmoney);
			// 折后总额
			opd.setFdiscountcountmoney(countmoney);
			// 折后单价
			opd.setFdiscountprice(opd.getFgoodsprice());
		} else if (fgoodsnum == 0 && goodsdiscountEmpty) {

			discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal(100));
			goodsprice = fgoodsprice;
			// 折后单价
			BigDecimal discountprice = goodsprice.multiply(discount);
			opd.setFdiscountprice(discountprice);
			// 折前总额
			opd.setFcountmoney(fgoodsprice);
			// 折后总额
			opd.setFdiscountcountmoney(discountprice);
		} else {
			// 折后单价
			opd.setFdiscountprice(fgoodsprice);
			// 折前总额
			opd.setFcountmoney(fgoodsprice);
			// 折后总额
			opd.setFdiscountcountmoney(fgoodsprice);
		}
	}

	/**
	 * 设置商品属性
	 * 
	 * @param opd
	 */
	public void setgoods(OrderProDetail opd) {
		// 添加sku信息
		GoodsSku goodsSku = opd.getFsku();
		if (goodsSku != null) {
			GoodsSku goodsSku2 = goodsSkuDao.get(goodsSku.getId());

			opd.setFsku(goodsSku2);
		}
		// 添加spu信息
		GoodsSpu fspu = opd.getFspu();
		if (fspu != null) {
			GoodsSpu goodsSpu = goodsSpuDao.get(fspu.getId());

			opd.setFspu(goodsSpu);
		}

	}

}