/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.service.ordermarket;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.market.dao.ordermarket.OrderMarketDao;
import com.jeeplus.modules.customer.dao.basic.CustomerBasicDao;
import com.jeeplus.modules.customer.dao.category.CustomerCateDao;
import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.goods.dao.sku.GoodsSkuDao;
import com.jeeplus.modules.goods.dao.spu.GoodsSpuDao;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.warehouses.dao.WarehouseDao;
import com.jeeplus.modules.warehouses.dao.WarehouseGoodsInfoDao;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.service.WarehouseRecordService;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.customeraccount.CustomerAccount;
import com.jeeplus.modules.account.service.capitalaccount.CapitalAccountService;
import com.jeeplus.modules.account.service.clearingaccount.ClearingAccountService;
import com.jeeplus.modules.account.service.customeraccount.CustomerAccountService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.market.dao.ordermarket.OrderMarketDetailDao;

/**
 * 销售订单管理Service
 * @author diqiang
 * @version 2017-06-18
 */
@Service
@Transactional(readOnly = true)
public class OrderMarketService extends CrudService<OrderMarketDao, OrderMarket> {

	@Autowired
	private OrderMarketDetailDao orderMarketDetailDao;
	@Autowired
	private OrderMarketDao orderMarketDao;
	
	@Autowired
	private CustomerAccountService customerAccountService;//客户对账Dao
	
	@Autowired
	private CapitalAccountService capitalAccountService;//账户流水Dao
	
	@Autowired
	private ClearingAccountService clearingAccountService;//结算账户Dao
	
	@Autowired
	private WarehouseRecordService  warehouseRecordService;
	
	@Autowired
	private CustomerCateDao customerCateDao;
	// 仓库dao
		@Autowired
		private WarehouseDao warehouseDao;
		// 商品dao
		@Autowired
		private GoodsSpuDao goodsSpuDao;
		// 商品详情sku
		@Autowired
		private GoodsSkuDao goodsSkuDao;
		@Autowired
		private WarehouseGoodsInfoDao warehouseGoodsInfoDao;
		@Autowired
		private CustomerBasicDao customerBasicDao;
	
	public OrderMarket get(String id) {
		OrderMarket orderMarket = super.get(id);
		orderMarket.setOrderMarketDetailList(orderMarketDetailDao.findList(new OrderMarketDetail(orderMarket)));
		return orderMarket;
	}
	
	public List<OrderMarket> findList(OrderMarket orderMarket) {
		//权限过滤
		customDataScopeFilter(orderMarket,"dsf", "id=a.office_id", "id=a.create_by");
//		orderMarket.getSqlMap().put("dsf", dataScopeFilter(orderMarket.getCurrentUser(), "fsponsor", "createby"));
		return super.findList(orderMarket);
	}
	
	public Page<OrderMarket> findPage(Page<OrderMarket> page, OrderMarket orderMarket) {
		//权限过滤
		customDataScopeFilter(orderMarket,"dsf", "id=a.office_id", "id=a.create_by");
//		orderMarket.getSqlMap().put("dsf", dataScopeFilter(orderMarket.getCurrentUser(), "fsponsor", "createby"));
		return super.findPage(page, orderMarket);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderMarket orderMarket) {
		String id = orderMarket.getId();
		/*
		 * 获取订单状态如果为完毕状态则添加账户流水及客户对账
		 */
		Integer fstatus = orderMarket.getFstatus();
		if(fstatus==3){
			Date businessTime = new Date();
			BigDecimal fproceeds = orderMarket.getFproceeds();//实收款
			Office fsponsor = orderMarket.getFsponsor();// 所属商户
			Office fstore = orderMarket.getFstore();// 所属门店
			AccountType accountType = orderMarket.getAccountType();//账目类型
			String fordernum = orderMarket.getFordernum();//单据编号
//			Date fendtime = orderMarket.getFendtime();//业务时间
			String remarks = orderMarket.getRemarks();//备注
			BigDecimal fdebt2 = orderMarket.getFdebt();//欠款
			fdebt2=(fdebt2!=null&&fdebt2.doubleValue()>0)?fdebt2:new BigDecimal(0);
			//获取结算账户
			ClearingAccount clearingAccount = orderMarket.getClearingAccount();//结算账户
			ClearingAccount clearingAccount2 = null;
			String fbalance = "";//期初余额变量
			BigDecimal faccountBalance = new BigDecimal(0);//变动余额变量
			if(clearingAccount!=null&&!clearingAccount.getId().trim().isEmpty()){
				clearingAccount2=clearingAccountService.get(clearingAccount.getId());
				fbalance = clearingAccount2.getFbalance();// 获取结算账户余额
				fbalance=(fbalance!=null&&StringUtils.isNoneBlank(fbalance))?fbalance:"0";
				faccountBalance = new BigDecimal(fbalance).add(fproceeds);// 账户余额加上实收款
				if(!fbalance.equals(faccountBalance.toString())){
					clearingAccount2.setFbalance(faccountBalance.toString());
					clearingAccountService.save(clearingAccount2);//修改账户余额
				}	
			}
			/*
			 * 创建账户流水对象
			 */
			CapitalAccount ca = new CapitalAccount();
			ca.setFoddNumbers(fordernum);// 单据号
			ca.setAccountType(accountType);// 账目类型
			ca.setClearingAccount(clearingAccount2);// 结算账户
			ca.setFinitialamount(fbalance);// 期初金额
			ca.setFaccountBalance(faccountBalance.toString());// 账户余额
			ca.setFtrader(orderMarket.getUpdateBy());// 交易人
			ca.setFincome(fproceeds.toString());// 收入
			ca.setFexpenditure("0");// 支出
			ca.setFprofit(fproceeds.toString());// 盈利
			ca.setFexpenditureflag(1); // 收入/支出（0支出，1收入）
			ca.setFsponsor(fsponsor);// 商户
			ca.setFstore(fstore);// 门店
			ca.setFbusinessHours(businessTime);// 业务时间
			ca.setRemarks(remarks);
			// 保存资金流水对象
			capitalAccountService.save(ca);
			
			/*
			 * 添加客户对账对象和客户对账总汇对象
			 */
			
			CustomerBasic customerBasic = orderMarket.getCustomerBasic();
			if(customerBasic!=null&&!customerBasic.getId().trim().isEmpty()){
				CustomerBasic customerBasic2 = customerBasicDao.get(customerBasic.getId());
				String fcategoryId = customerBasic2.getFcategoryId();
				CustomerCate customerCate = customerCateDao.get(fcategoryId);
				//客户对账对象
				CustomerAccount customerAccount=new CustomerAccount();
				customerAccount.setCustomerBasic(customerBasic2);//客户
				customerAccount.setCustomerCate(customerCate);//客户分类
				customerAccount.setFoddNumbers(fordernum);//单据编号
				customerAccount.setAccountType(accountType);//账目类型
				customerAccount.setClearingAccount(clearingAccount2);//结算账户
				customerAccount.setFbusinessHours(businessTime);//业务时间
				customerAccount.setFamountReceivable(orderMarket.getFdiscountprice().toString());//应收金额
				customerAccount.setFpaidAmount(orderMarket.getFproceeds().toString());//实收款
				customerAccount.setFresidualAmount(fdebt2.toString());//应收余额
				BigDecimal fcutsmall = orderMarket.getFcutsmall();
				String fcutsmallstr = (fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall.toString():"0";
				customerAccount.setFpreferentialAmount(fcutsmallstr);//优惠金额
				customerAccount.setFsponsor(fsponsor);//商户
				customerAccount.setFstore(fstore);//门店	
				customerAccount.setRemarks(remarks);//备注
				customerAccountService.save(customerAccount);//保存客户对账对象
				String fdebt = customerBasic2.getFdebt();//获取客户欠款如果有欠款则加上本单欠款
				if(fdebt2.doubleValue()>0){
					if(fdebt!=null&&!fdebt.trim().isEmpty()){
						fdebt=new BigDecimal(fdebt).add(fdebt2).toString();
						customerBasic2.setFdebt(fdebt);
					}else{
						customerBasic2.setFdebt(fdebt2.toString());
					}
					customerBasicDao.update(customerBasic2);
				}
			}
		}

		super.save(orderMarket);
		for (OrderMarketDetail orderMarketDetail : orderMarket.getOrderMarketDetailList()){
			if (orderMarketDetail.getId() == null){
				continue;
			}
			
			if (OrderMarketDetail.DEL_FLAG_NORMAL.equals(orderMarketDetail.getDelFlag())){
				if (StringUtils.isBlank(orderMarketDetail.getId())){
					orderMarketDetail.setOrderMarket(orderMarket);
					orderMarketDetail.preInsert();
					orderMarketDetailDao.insert(orderMarketDetail);
				}else{
					orderMarketDetail.preUpdate();
					orderMarketDetailDao.update(orderMarketDetail);
				}
			}else{
				orderMarketDetailDao.deleteByLogic(orderMarketDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderMarket orderMarket) {
		super.deleteByLogic(orderMarket);
		orderMarketDetailDao.deleteByLogic(new OrderMarketDetail(orderMarket));
	}
	
	public Page<CustomerBasic> findPageBycustomerBasic(Page<CustomerBasic> page, CustomerBasic customerBasic) {
		//权限过滤
		customDataScopeFilter(customerBasic,"dsf", "id=a.office_id", "id=a.create_by");
//		customerBasic.getSqlMap().put("dsf", dataScopeFilter(customerBasic.getCurrentUser(), "fsponsor", "createby"));
		customerBasic.setPage(page);
		page.setList(dao.findListBycustomerBasic(customerBasic));
		return page;
	}
	public Page<Warehouse> findPageBywarehouse(Page<Warehouse> page, Warehouse warehouse) {
		//权限过滤
		customDataScopeFilter(warehouse,"dsf", "id=a.company_id", "id=a.create_by");
//		warehouse.getSqlMap().put("dsf", dataScopeFilter(warehouse.getCurrentUser(), "fsponsor", "createby"));
		warehouse.setPage(page);
		page.setList(dao.findListBywarehouse(warehouse));
		return page;
	}
	public Page<AccountType> findPageByaccountType(Page<AccountType> page, AccountType accountType) {
		//权限过滤
		customDataScopeFilter(accountType,"dsf", "id=a.office_id", "id=createby");
//		accountType.getSqlMap().put("dsf", dataScopeFilter(accountType.getCurrentUser(), "fsponsor", "createby"));
		accountType.setPage(page);
		page.setList(dao.findListByaccountType(accountType));
		return page;
	}
	public Page<ClearingAccount> findPageByclearingAccount(Page<ClearingAccount> page, ClearingAccount clearingAccount) {
		//权限过滤
		customDataScopeFilter(clearingAccount,"dsf", "id=a.office_id", "id=a.create_by");
//		clearingAccount.getSqlMap().put("dsf", dataScopeFilter(clearingAccount.getCurrentUser(), "fsponsor", "createby"));
		clearingAccount.setPage(page);
		page.setList(dao.findListByclearingAccount(clearingAccount));
		return page;
	}
//	public Page<Merchant> findPageByfsponsor(Page<Merchant> page, Merchant fsponsor) {
//		//权限过滤
//		customDataScopeFilter(customerBasic,"dsf", "id=a.office_id", "id=a.create_by");
//		fsponsor.setPage(page);
//		page.setList(dao.findListByfsponsor(fsponsor));
//		return page;
//	}
	public Page<SysModelType> findPageBysysModelType(Page<SysModelType> page, SysModelType sysModelType) {
		sysModelType.setPage(page);
		page.setList(dao.findListBysysModelType(sysModelType));
		return page;
	}

	public Page<GoodsSpu> findPageByfspu(Page<GoodsSpu> page, GoodsSpu fspu) {
		//权限过滤
//		customDataScopeFilter(fspu,"dsf", "id=a.office_id", "id=a.create_by");
//		fspu.getSqlMap().put("dsf", dataScopeFilter(fspu.getCurrentUser(), "fsponsor", "createby"));
		fspu.setPage(page);
		page.setList(orderMarketDetailDao.findListBygoodsSpu(fspu));
		return page;
	}
	public Page<GoodsSku> findPageByfsku(Page<GoodsSku> page, GoodsSku fsku) {
		//权限过滤
//		customDataScopeFilter(fsku,"dsf", "id=a.office_id", "id=a.create_by");
//		fsku.getSqlMap().put("dsf", dataScopeFilter(fsku.getCurrentUser(), "fsponsor", "createby"));
		fsku.setPage(page);
		page.setList(orderMarketDetailDao.findListBygoodsSku(fsku));
		return page;
	}
//	public Page<WarehouseGoodsInfo> findPageBywgi(Page<WarehouseGoodsInfo> page,
//			WarehouseGoodsInfo warehouseGoodsInfo) {
//		// TODO Auto-generated method stub
//		warehouseGoodsInfo.setPage(page);
//		
//		page.setList(orderMarketDetailDao.findPageBywarehouseGoodesInfo(warehouseGoodsInfo));
//		
//		return page;
//	}
	
	
//	/**
//	 * 添加实体所需对象属性
//	 * 
//	 * @param op
//	 * @return
//	 */
//	public void setOrderOrderMarketProperties(OrderMarket om) {
//		User createBy = om.getCreateBy();
//		if (createBy != null) {
//			om.setCreateBy(UserUtils.get(createBy.getId()));
//		}
//
//		User updateBy = om.getUpdateBy();
//		if (updateBy != null) {
//			om.setUpdateBy(UserUtils.get(updateBy.getId()));
//		}
//
//		OrderMarket orderMarket = orderMarketDao.get(om.getId());
//		// 如果为新对象则不查询详情表
//		if (orderMarket != null) {
//
//			OrderMarketDetail omd=new OrderMarketDetail();
//			omd.setOrderMarket(orderMarket);
//
//			List<OrderMarketDetail> omdList = orderMarketDetailDao.findList(omd);
//			// 给详情对象添加商品对象信息用于回显
//			for (OrderMarketDetail omd1 : omdList) {
//				GoodsSpu fspu = omd1.getGoodsSpu();
//				if (fspu != null) {
//					omd1.setGoodsSpu(goodsSpuDao.get(fspu.getId()));
//				}
//				GoodsSku fsku = omd1.getGoodsSku();
//				if (fsku != null) {
//					omd1.setGoodsSku(goodsSkuDao.get(fsku.getId()));
//				}
//			}
//			om.setOrderMarketDetailList(omdList);
//		}
//		// 计算折扣及价钱
//		// 其它费用
//		String fothermoney = om.getFothermoney();
//		
//		boolean ortherEmpty = StringUtils.isNoneEmpty(fothermoney);
//		// 商品价格总额
//		String fcountprice = om.getFcountprice();
//		boolean countEmpty = StringUtils.isNoneEmpty(fcountprice);
//		// 抹零
//		String fcutsmall = om.getFcutsmall();
//		boolean cutEmpty = StringUtils.isNoneEmpty(fcutsmall);
//		// 设置订单总额
//		if (ortherEmpty && countEmpty && cutEmpty) {
//			// 有其他费用有抹零
//			BigDecimal ordercountprice = new BigDecimal(fothermoney).add(new BigDecimal(fcountprice));
//			ordercountprice = ordercountprice.subtract(new BigDecimal(fcutsmall));
//			om.setFordercountprice(ordercountprice.toString());
//		} else if (!ortherEmpty && countEmpty && cutEmpty) {
//			// 无其他费用有抹零
//			BigDecimal ordercountprice = new BigDecimal(fcountprice).subtract(new BigDecimal(fcutsmall));
//			om.setFordercountprice(ordercountprice.toString());
//		} else if (ortherEmpty && countEmpty && !cutEmpty) {
//			// 有其他费用无抹零
//			BigDecimal ordercountprice = new BigDecimal(fothermoney).add(new BigDecimal(fcountprice));
//			om.setFordercountprice(ordercountprice.toString());
//		} else {
////			om.setFordercountprice(fcountprice);
//			om.setFordercountprice(fcountprice);
//		}
//
//		// 实付款
//		String factualpayment = om.getFproceeds();
//		boolean actualpaymentEmpty = StringUtils.isNoneEmpty(factualpayment);
//		// 订单总额
//		String fordercountprice = om.getFordercountprice();
//		boolean ordercountEmpty = StringUtils.isNoneEmpty(fordercountprice);
//
//		// 设置欠款
//		if (actualpaymentEmpty && ordercountEmpty) {
//			BigDecimal fdebt = new BigDecimal(fordercountprice).subtract(new BigDecimal(factualpayment));
//			om.setFdebt(fdebt.toString());
//		}
//
//	}
	
	/**
	 * 添加实体所需对象属性
	 * 
	 * @param op
	 * @return
	 */
	public void setOrderOrderMarketProperties(OrderMarket om) {
		User createBy = om.getCreateBy();
		if (createBy != null) {
			om.setCreateBy(UserUtils.get(createBy.getId()));
		}

		User updateBy = om.getUpdateBy();
		if (updateBy != null) {
			om.setUpdateBy(UserUtils.get(updateBy.getId()));
		}

//		OrderMarket orderMarket = orderMarketDao.get(om.getId());
//		// 如果为新对象则不查询详情表
//		if (orderMarket != null) {
//
//			OrderMarketDetail omd=new OrderMarketDetail();
//			omd.setOrderMarket(orderMarket);
//
//			List<OrderMarketDetail> omdList = orderMarketDetailDao.findList(omd);
//			// 给详情对象添加商品对象信息用于回显
//			for (OrderMarketDetail omd1 : omdList) {
//				GoodsSpu fspu = omd1.getGoodsSpu();
//				if (fspu != null) {
//					omd1.setGoodsSpu(goodsSpuDao.get(fspu.getId()));
//				}
//				GoodsSku fsku = omd1.getGoodsSku();
//				if (fsku != null) {
//					omd1.setGoodsSku(goodsSkuDao.get(fsku.getId()));
//				}
//			}
//			om.setOrderMarketDetailList(omdList);
//		}
		// 计算折扣及价钱
		// 其它费用
		BigDecimal fothermoney = om.getFothermoney();
		
		boolean ortherEmpty =(fothermoney!=null&&fothermoney.doubleValue()>0);
		// 商品价格总额
		BigDecimal fcountprice = om.getFcountprice();
		boolean countEmpty = (fcountprice!=null&&fcountprice.doubleValue()>0);
		// 设置订单总额
		if (ortherEmpty && countEmpty) {
			// 有其他费用
			BigDecimal ordercountprice = fothermoney.add(fcountprice);
			om.setFordercountprice(ordercountprice);
		}  else {
			om.setFordercountprice(fcountprice);
		}

		// 实付款
		BigDecimal factualpayment = om.getFproceeds();
		boolean actualpaymentEmpty = (factualpayment!=null&&factualpayment.intValue()>0);
		// 订单总额
		BigDecimal fordercountprice = om.getFordercountprice();
		boolean ordercountEmpty = (fordercountprice!=null&&fordercountprice.intValue()>0);

		// 设置欠款
		if (actualpaymentEmpty && ordercountEmpty) {
			BigDecimal fdebt = fordercountprice.subtract(factualpayment);
			om.setFdebt(fdebt);
		}

	}
	
	/**
	 * 设置整单折后欠款
	 */
	public void setfdebt(OrderMarket om) {
		// 实付款
		BigDecimal factualpayment = om.getFproceeds();
		boolean actualpaymentEmpty = (factualpayment != null);
		// 订单折后总额
		BigDecimal fordercountprice = om.getFdiscountprice();
		boolean ordercountEmpty = (fordercountprice != null);

		// 设置欠款
		if (actualpaymentEmpty && ordercountEmpty) {
			BigDecimal fdebt = fordercountprice.subtract(factualpayment);
			om.setFdebt(fdebt);
		}
	}
	


//	/**
//	 * 设置单条商品金额
//	 * 
//	 * @param opd
//	 */
//	public void setOrderMarketDetailProperties(OrderMarketDetail omd) {
//		
//		
//		User createBy = omd.getCreateBy();
//		if (createBy != null) {
//			omd.setCreateBy(UserUtils.get(createBy.getId()));
//		}
//
//		User updateBy = omd.getUpdateBy();
//		if (updateBy != null) {
//			omd.setUpdateBy(UserUtils.get(updateBy.getId()));
//		}
//		
//		
//		// 设置采购单对象
//		OrderMarket orderMarket = omd.getOrderMarket();
//
//		if (orderMarket != null) {
//			 OrderMarket market= orderMarketDao.get(orderMarket.getId());
//
//			Warehouse fwarehose = market.getWarehouse();
//			if (fwarehose != null) {
//				market.setWarehouse(warehouseDao.get(fwarehose.getId()));
//			}
//			CustomerBasic customerBasic = market.getCustomerBasic();
//			if (customerBasic != null) {
//				market.setCustomerBasic(customerBasicDao.get(customerBasic.getId()));
//			}
//
//			omd.setOrderMarket(market);;
//		}
//		// 设置库存对象
//		WarehouseGoodsInfo fwarehousegoodsinfo = omd.getFwarehousegoodsinfo();
//		if (fwarehousegoodsinfo != null) {
//			WarehouseGoodsInfo warehouseGoodsInfo = warehouseGoodsInfoDao.get(fwarehousegoodsinfo.getId());
//			omd.setFwarehousegoodsinfo(warehouseGoodsInfo);
//		}
//
//		setgoods(omd);
//		int fgoodsnum = 0;
//		// 商品数量
//		Integer goodsnum = omd.getFgoodsnum();
//		if (goodsnum != null) {
//			fgoodsnum = goodsnum;
//		}
//		// 商品单价
//		String fgoodsprice = omd.getFgoodsprice();
//		// 商品折扣
//		String fgoodsdiscount = omd.getFgoodsdiscount();
//		boolean goodsdiscountEmpty = StringUtils.isNoneEmpty(fgoodsdiscount);
//		// 折扣
//		BigDecimal discount = null;
//		// 单价
//		BigDecimal goodsprice = null;
//		// 数量
//		BigDecimal num = null;
//
//		if (fgoodsnum > 0 && goodsdiscountEmpty) {
//
//			discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal("10"));
//			goodsprice = new BigDecimal(fgoodsprice);
//			num = new BigDecimal(String.valueOf(fgoodsnum));
//
//			// 折后单价
//			BigDecimal discountprice = goodsprice.multiply(discount);
//			omd.setFdiscountprice(discountprice.toString());
//			// 折前总额
//			BigDecimal countmoney = goodsprice.multiply(num);
//			omd.setFcountmoney(countmoney.toString());
//			// 折后总额
//			BigDecimal discountcountmoney = discountprice.multiply(num);
//			omd.setFdiscountcountmoney(discountcountmoney.toString());
//		} else if (fgoodsnum > 0 && !goodsdiscountEmpty) {
//			goodsprice = new BigDecimal(fgoodsprice);
//			num = new BigDecimal(String.valueOf(fgoodsnum));
//			// 折前总额
//			BigDecimal countmoney = goodsprice.multiply(num);
//			omd.setFcountmoney(countmoney.toString());
//			// 折后总额
//			omd.setFdiscountcountmoney(countmoney.toString());
//			// 折后单价
//			omd.setFdiscountprice(omd.getFgoodsprice());
//		} else if (fgoodsnum == 0 && goodsdiscountEmpty) {
//
//			discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal("10"));
//			goodsprice = new BigDecimal(fgoodsprice);
//			// 折后单价
//			BigDecimal discountprice = goodsprice.multiply(discount);
//			omd.setFdiscountprice(discountprice.toString());
//			// 折前总额
//			omd.setFcountmoney(fgoodsprice);
//			// 折后总额
//			omd.setFdiscountcountmoney(discountprice.toString());
//		} else {
//			// 折后单价
//			omd.setFdiscountprice(fgoodsprice);
//			// 折前总额
//			omd.setFcountmoney(fgoodsprice);
//			// 折后总额
//			omd.setFdiscountcountmoney(fgoodsprice);
//		}
//	}
	
	/**
	 * 设置单条商品金额
	 * 
	 * @param opd
	 */
	public void setOrderMarketDetailProperties(OrderMarketDetail omd) {
		
		
		User createBy = omd.getCreateBy();
		if (createBy != null) {
			omd.setCreateBy(UserUtils.get(createBy.getId()));
		}

		User updateBy = omd.getUpdateBy();
		if (updateBy != null) {
			omd.setUpdateBy(UserUtils.get(updateBy.getId()));
		}
//		
//		
//		// 设置采购单对象
//		OrderMarket orderMarket = omd.getOrderMarket();
//
//		if (orderMarket != null) {
//			 OrderMarket market= orderMarketDao.get(orderMarket.getId());
//
//			Warehouse fwarehose = market.getWarehouse();
//			if (fwarehose != null) {
//				market.setWarehouse(warehouseDao.get(fwarehose.getId()));
//			}
//			CustomerBasic customerBasic = market.getCustomerBasic();
//			if (customerBasic != null) {
//				market.setCustomerBasic(customerBasicDao.get(customerBasic.getId()));
//			}
//
//			omd.setOrderMarket(market);;
//		}
		// 设置库存对象
		WarehouseGoodsInfo fwarehousegoodsinfo = omd.getFwarehousegoodsinfo();
		if (fwarehousegoodsinfo != null) {
			WarehouseGoodsInfo warehouseGoodsInfo = warehouseGoodsInfoDao.get(fwarehousegoodsinfo.getId());
			omd.setFwarehousegoodsinfo(warehouseGoodsInfo);
		}

//		setgoods(omd);
		int fgoodsnum = 0;
		// 商品数量
		Integer goodsnum = omd.getFgoodsnum();
		if (goodsnum != null) {
			fgoodsnum = goodsnum;
		}
		// 商品单价
		BigDecimal fgoodsprice = omd.getFgoodsprice();
		// 商品折扣
		String fgoodsdiscount = omd.getFgoodsdiscount();
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
			omd.setFdiscountprice(discountprice);
			// 折前总额
			BigDecimal countmoney = goodsprice.multiply(num);
			omd.setFcountmoney(countmoney);
			// 折后总额
			BigDecimal discountcountmoney = discountprice.multiply(num);
			omd.setFdiscountcountmoney(discountcountmoney);
		} else if (fgoodsnum > 0 && !goodsdiscountEmpty) {
			goodsprice = fgoodsprice;
			num = new BigDecimal(String.valueOf(fgoodsnum));
			// 折前总额
			BigDecimal countmoney = goodsprice.multiply(num);
			omd.setFcountmoney(countmoney);
			// 折后总额
			omd.setFdiscountcountmoney(countmoney);
			// 折后单价
			omd.setFdiscountprice(omd.getFgoodsprice());
		} else if (fgoodsnum == 0 && goodsdiscountEmpty) {

			discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal(100));
			goodsprice = fgoodsprice;
			// 折后单价
			BigDecimal discountprice = goodsprice.multiply(discount);
			omd.setFdiscountprice(discountprice);
			// 折前总额
			omd.setFcountmoney(fgoodsprice);
			// 折后总额
			omd.setFdiscountcountmoney(discountprice);
		} else {
			// 折后单价
			omd.setFdiscountprice(fgoodsprice);
			// 折前总额
			omd.setFcountmoney(fgoodsprice);
			// 折后总额
			omd.setFdiscountcountmoney(fgoodsprice);
		}
	}
	
	
	

	/**
	 * 设置商品属性
	 * 
	 * @param opd
	 */
	public void setgoods(OrderMarketDetail omd) {
		// 添加sku信息
		GoodsSku goodsSku = omd.getGoodsSku();
		if (goodsSku != null) {
			GoodsSku goodsSku2 = goodsSkuDao.get(goodsSku.getId());

			omd.setGoodsSku(goodsSku2);
		}
		// 添加spu信息
		GoodsSpu fspu = omd.getGoodsSpu();
		if (fspu != null) {
			GoodsSpu goodsSpu = goodsSpuDao.get(fspu.getId());

			omd.setGoodsSpu(goodsSpu);
		}

	}


	public List<Map<String, Object>> findMonthPricList() {
		// TODO Auto-generated method stub
		return orderMarketDao.findMonthPricList();
	}
	
	/**
	 * @author maxiao
	 * @return
	 * 查找未发货的订单数量
	 */
	public int findCountByStatus() {
		// TODO Auto-generated method stub
		return orderMarketDao.findCountByStatus();
	}
	
	
	public void setspu(OrderMarketDetail orderMarketDetail){
		GoodsSpu fspu = orderMarketDetail.getGoodsSpu();
		if(fspu!=null&&!fspu.getId().trim().isEmpty()){
			
		}else{
			GoodsSku sku = goodsSkuDao.get( orderMarketDetail.getGoodsSku());
			if(sku!=null){
				orderMarketDetail.setGoodsSpu(goodsSpuDao.get(sku.getGoodsSpu()));
			}	
		}
	}
	/**
	 * @author diqiang
	 * @param goodsnum 商品数量
	 * @param info 库存对象
	 * <pre>
	 * 传入商品数量为负数则为退货，库存增加
	 * 传入商品数量为正数则为出货，库存减少
	 * 此方法只设置了属性并未调用service或dao进行保存，请手动调用
	 * </pre>
	 */
	public void setWarehouseGoodsInfo(Integer goodsnum,WarehouseGoodsInfo info) {
		BigDecimal num = (goodsnum!=null&&goodsnum!=0)?new BigDecimal(goodsnum):new BigDecimal(0);
		Integer ftotalinventory = info.getFtotalinventory();//库存总量
		Integer finventory = info.getFinventory();//库存量
		BigDecimal total = (ftotalinventory!=null&&ftotalinventory!=0)?new BigDecimal(ftotalinventory):new BigDecimal(0);
		BigDecimal inventory = (finventory!=null&&finventory!=0)?new BigDecimal(finventory):new BigDecimal(0);
		info.setFinventory(inventory.subtract(num).intValue());
		info.setFtotalinventory(total.subtract(num).intValue());
	}
	/**
	 * @author diqiang
	 * @param spu 商品
	 * @param sku 商品号
	 * @param wh  仓库
	 * @param ordernum 单号
	 * @param changenum 改变数量
	 * @param remainingnum 之前数量
	 */
	@Transactional(readOnly = false)
	public void setWarehouseRecord(GoodsSpu spu,GoodsSku sku,Warehouse wh,String ordernum,Integer changenum,Integer remainingnum){
		// 添加流水
		WarehouseRecord warehouseRecord = new WarehouseRecord();
		warehouseRecord.setBusinessType(warehouseRecord.BUSINESS_TYPE_MARKET);
		warehouseRecord.setGoodsSku(sku);
		warehouseRecord.setGoodsSpu(spu);
		warehouseRecord.setWarehouse(wh);
		warehouseRecord.setBusinessTime(new Date());
		warehouseRecord.setBusinessorderNumber(ordernum);
		warehouseRecord.setChangeNum(changenum);
		warehouseRecord.setRemainingNum(remainingnum);
		warehouseRecordService.save(warehouseRecord);
	}
	
	
}