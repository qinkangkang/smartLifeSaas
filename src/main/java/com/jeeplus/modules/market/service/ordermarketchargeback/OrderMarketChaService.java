/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.service.ordermarketchargeback;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketCha;
import com.jeeplus.modules.market.dao.ordermarketchargeback.OrderMarketChaDao;
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
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.customeraccount.CustomerAccount;
import com.jeeplus.modules.account.service.capitalaccount.CapitalAccountService;
import com.jeeplus.modules.account.service.clearingaccount.ClearingAccountService;
import com.jeeplus.modules.account.service.customeraccount.CustomerAccountService;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketChaDetail;
import com.jeeplus.modules.market.dao.ordermarketchargeback.OrderMarketChaDetailDao;

/**
 * 销售退货单Service
 * @author diqiang
 * @version 2017-06-18
 */
@Service
@Transactional(readOnly = true)
public class OrderMarketChaService extends CrudService<OrderMarketChaDao, OrderMarketCha> {

	@Autowired
	private OrderMarketChaDetailDao orderMarketChaDetailDao;
	@Autowired
	private OrderMarketChaDao orderMarketChaDao;
	
	@Autowired
	private CustomerAccountService customerAccountService;//客户对账Dao
	
	@Autowired
	private CustomerCateDao customerCateDao;//客户分类
	
	@Autowired
	private CapitalAccountService capitalAccountService;//账户流水Dao
	
	@Autowired
	private ClearingAccountService clearingAccountService;//结算账户Dao
	
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
	
	public OrderMarketCha get(String id) {
		OrderMarketCha orderMarketCha = super.get(id);
		orderMarketCha.setOrderMarketChaDetailList(orderMarketChaDetailDao.findList(new OrderMarketChaDetail(orderMarketCha)));
		return orderMarketCha;
	}
	
	public List<OrderMarketCha> findList(OrderMarketCha orderMarketCha) {
		customDataScopeFilter(orderMarketCha, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findList(orderMarketCha);
	}
	
	public Page<OrderMarketCha> findPage(Page<OrderMarketCha> page, OrderMarketCha orderMarketCha) {
		customDataScopeFilter(orderMarketCha, "dsf", "id=a.office_id", "id=a.create_by");
		return super.findPage(page, orderMarketCha);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderMarketCha orderMarketCha) {
		String id = orderMarketCha.getId();
		if(StringUtils.isEmpty(id)){
			//设置单号
			StringBuffer buffer=new StringBuffer("XT");
			String str = OddNumbers.getOrderNo();
			buffer.append(str);
			orderMarketCha.setFordernum(buffer.toString());
		}
		
		/*
		 * 获取订单状态如果为完毕状态则添加账户流水及客户对账
		 */
		Integer fstatus = orderMarketCha.getFstatus();
		if(fstatus==7){
			Date businessTime = new Date();
			BigDecimal fproceeds = orderMarketCha.getFproceeds();//实付款
			Office fsponsor = orderMarketCha.getFsponsor();// 所属商户
			Office fstore = orderMarketCha.getFstore();// 所属门店
			AccountType accountType = orderMarketCha.getAccountType();//账目类型
			String fordernum = orderMarketCha.getFordernum();//单据编号
//			Date fendtime = orderMarketCha.getFendtime();//业务时间
			String remarks = orderMarketCha.getRemarks();//备注
			BigDecimal fdebt2 = orderMarketCha.getFdebt();//欠款
			fdebt2=(fdebt2!=null&&fdebt2.doubleValue()>0)?fdebt2:new BigDecimal(0);
			//获取结算账户
			ClearingAccount clearingAccount = orderMarketCha.getClearingAccount();//结算账户
			ClearingAccount clearingAccount2 = null;
			String fbalance = "";//期初余额变量
			BigDecimal faccountBalance = new BigDecimal(0);//变动余额变量
			if(clearingAccount!=null&&!clearingAccount.getId().trim().isEmpty()){
				clearingAccount2=clearingAccountService.get(clearingAccount.getId());
				fbalance = clearingAccount2.getFbalance();// 获取结算账户余额
				fbalance=(fbalance!=null&&StringUtils.isNoneBlank(fbalance))?fbalance:"0";
				faccountBalance = new BigDecimal(fbalance).subtract(fproceeds);// 账户余额减去实付款
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
			ca.setFtrader(orderMarketCha.getUpdateBy());// 交易人
			ca.setFincome("0");// 收入
			ca.setFexpenditure(fproceeds.toString());// 支出
			ca.setFprofit(new BigDecimal(0).subtract(fproceeds).toString());// 盈利
			ca.setFexpenditureflag(0); // 收入/支出（0支出，1收入）
			ca.setFsponsor(fsponsor);// 商户
			ca.setFstore(fstore);// 门店
			ca.setFbusinessHours(businessTime);// 业务时间
			ca.setRemarks(remarks);
			// 保存资金流水对象
			capitalAccountService.save(ca);
			
			/*
			 * 添加客户对账对象和客户对账总汇对象
			 */
			
			CustomerBasic customerBasic = orderMarketCha.getCustomerBasic();
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
				BigDecimal fdiscountprice = orderMarketCha.getFdiscountprice();
				BigDecimal fdiscountprice2= (fdiscountprice!=null&&fdiscountprice.doubleValue()>0)?fdiscountprice:new BigDecimal(0);
				customerAccount.setFamountReceivable(new BigDecimal(0).subtract(fdiscountprice2).toString());//应收金额
				BigDecimal fproceeds2 = (fproceeds!=null&&fproceeds.doubleValue()>0)?fproceeds:new BigDecimal(0);
				customerAccount.setFpaidAmount(new BigDecimal(0).subtract(fproceeds2).toString());//实收款
				fdebt2 = (fdebt2!=null&&fdebt2.doubleValue()>0)?fdebt2:new BigDecimal(0);
				customerAccount.setFresidualAmount(new BigDecimal(0).subtract(fdebt2).toString());//应收余额
				BigDecimal fcutsmall = orderMarketCha.getFcutsmall();
				String fcutsmallstr = (fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall.toString():"0";
				customerAccount.setFpreferentialAmount(fcutsmallstr);//优惠金额
				customerAccount.setFsponsor(fsponsor);//商户
				customerAccount.setFstore(fstore);//门店	
				customerAccount.setRemarks(remarks);//备注
				customerAccountService.save(customerAccount);//保存客户对账对象
				String fdebt = customerBasic2.getFdebt();//获取客户欠款如果有欠款则加上本单欠款
				if(fdebt2.doubleValue()>0){
					if(fdebt!=null&&!fdebt.trim().isEmpty()){
						fdebt=new BigDecimal(fdebt).subtract(fdebt2).toString();
						customerBasic2.setFdebt(fdebt);
					}else{
						customerBasic2.setFdebt(fdebt2.toString());
					}
					customerBasicDao.update(customerBasic2);
				}
			}
		}
		
		
		super.save(orderMarketCha);
		for (OrderMarketChaDetail orderMarketChaDetail : orderMarketCha.getOrderMarketChaDetailList()){
//			if (orderMarketChaDetail.getId() == null){
//				continue;
//			}
			
			
			if (OrderMarketChaDetail.DEL_FLAG_NORMAL.equals(orderMarketChaDetail.getDelFlag())){
				if (StringUtils.isBlank(orderMarketChaDetail.getId())){
					orderMarketChaDetail.setFordermarketchargeback(orderMarketCha);
					orderMarketChaDetail.preInsert();
					orderMarketChaDetailDao.insert(orderMarketChaDetail);
				}else{
					orderMarketChaDetail.preUpdate();
					orderMarketChaDetailDao.update(orderMarketChaDetail);
				}
			}else{
				orderMarketChaDetailDao.delete(orderMarketChaDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderMarketCha orderMarketCha) {
		super.deleteByLogic(orderMarketCha);
		orderMarketChaDetailDao.deleteByLogic(new OrderMarketChaDetail(orderMarketCha));
	}
	public Page<CustomerBasic> findPageBycustomerBasic(Page<CustomerBasic> page, CustomerBasic customerBasic) {
		customDataScopeFilter(customerBasic, "dsf", "id=a.office_id", "id=a.create_by");
		customerBasic.setPage(page);
		page.setList(dao.findListBycustomerBasic(customerBasic));
		return page;
	}
	public Page<Warehouse> findPageBywarehouse(Page<Warehouse> page, Warehouse warehouse) {
		customDataScopeFilter(warehouse, "dsf", "id=a.office_id", "id=a.create_by");
		warehouse.setPage(page);
		page.setList(dao.findListBywarehouse(warehouse));
		return page;
	}
	public Page<ClearingAccount> findPageByclearingAccount(Page<ClearingAccount> page, ClearingAccount clearingAccount) {
		customDataScopeFilter(clearingAccount, "dsf", "id=a.office_id", "id=a.create_by");
		clearingAccount.setPage(page);
		page.setList(dao.findListByclearingAccount(clearingAccount));
		return page;
	}
	public Page<AccountType> findPageByaccountType(Page<AccountType> page, AccountType accountType) {
		customDataScopeFilter(accountType, "dsf", "id=a.office_id", "id=a.createby");
		accountType.setPage(page);
		page.setList(dao.findListByaccountType(accountType));
		return page;
	}
	public Page<SysModelType> findPageByfmodeltype(Page<SysModelType> page, SysModelType fmodeltype) {
		fmodeltype.setPage(page);
		page.setList(dao.findListByfmodeltype(fmodeltype));
		return page;
	}

	public Page<GoodsSpu> findPageByfspu(Page<GoodsSpu> page, GoodsSpu fspu) {
		fspu.setPage(page);
		page.setList(orderMarketChaDetailDao.findListByfspu(fspu));
		return page;
	}
	public Page<GoodsSku> findPageByfsku(Page<GoodsSku> page, GoodsSku fsku) {
		fsku.setPage(page);
		page.setList(orderMarketChaDetailDao.findListByfsku(fsku));
		return page;
	}

	public Page<OrderMarket> findListByfordermarket(Page<OrderMarket> page, OrderMarket market) {
		customDataScopeFilter(market, "dsf", "id=a.office_id", "id=a.create_by");
		// TODO Auto-generated method stub
		market.setPage(page);
		page.setList(orderMarketChaDetailDao.findListByfordermarket(market));
		return page;
	}
	
	

//	/**
//	 * 添加实体所需对象属性
//	 * 
//	 * @param op
//	 * @return
//	 */
//	public void setOrderOrderMarketChaProperties(OrderMarketCha omc) {
//
//
//		User createBy = omc.getCreateBy();
//		if (createBy != null) {
//			omc.setCreateBy(UserUtils.get(createBy.getId()));
//		}
//
//		User updateBy = omc.getUpdateBy();
//		if (updateBy != null) {
//			omc.setUpdateBy(UserUtils.get(updateBy.getId()));
//		}
//
//
//		OrderMarketCha orderMarketCha = orderMarketChaDao.get(omc.getId());
//		// 如果为新对象则不查询详情表
//		if (orderMarketCha != null) {
//
//			OrderMarketChaDetail omdc=new OrderMarketChaDetail();
//			omdc.setFordermarketchargeback(orderMarketCha);
//
//			List<OrderMarketChaDetail> omdcList = orderMarketChaDetailDao.findList(omdc);
//			// 给详情对象添加商品对象信息用于回显
//			for (OrderMarketChaDetail omdc1 : omdcList) {
//				GoodsSpu fspu = omdc1.getFspu();
//				if (fspu != null) {
//					omdc1.setFspu(goodsSpuDao.get(fspu.getId()));
//				}
//				GoodsSku fsku = omdc1.getFsku();
//				if (fsku != null) {
//					omdc1.setFsku(goodsSkuDao.get(fsku.getId()));
//				}
//			}
//			omc.setOrderMarketChaDetailList(omdcList);
//		}
//		// 计算折扣及价钱
//		// 其它费用
//		String fothermoney = omc.getFothermoney();
//		boolean ortherEmpty = StringUtils.isNoneEmpty(fothermoney);
//		// 商品价格总额
//		String fcountprice = omc.getFcountprice();
//		boolean countEmpty = StringUtils.isNoneEmpty(fcountprice);
//		// 抹零
//		String fcutsmall = omc.getFcutsmall();
//		boolean cutEmpty = StringUtils.isNoneEmpty(fcutsmall);
//		// 设置订单总额
//		if (ortherEmpty && countEmpty && cutEmpty) {
//			// 有其他费用有抹零
//			BigDecimal ordercountprice = new BigDecimal(fothermoney).add(new BigDecimal(fcountprice));
//			ordercountprice = ordercountprice.subtract(new BigDecimal(fcutsmall));
//			omc.setFordercountprice(ordercountprice.toString());
//		} else if (!ortherEmpty && countEmpty && cutEmpty) {
//			// 无其他费用有抹零
//			BigDecimal ordercountprice = new BigDecimal(fcountprice).subtract(new BigDecimal(fcutsmall));
//			omc.setFordercountprice(ordercountprice.toString());
//		} else if (ortherEmpty && countEmpty && !cutEmpty) {
//			// 有其他费用无抹零
//			BigDecimal ordercountprice = new BigDecimal(fothermoney).add(new BigDecimal(fcountprice));
//			omc.setFordercountprice(ordercountprice.toString());
//		} else {
//			omc.setFordercountprice(fcountprice);
//		}
//
//		// 实付款
//		String factualpayment = omc.getFproceeds();
//		boolean actualpaymentEmpty = StringUtils.isNoneEmpty(factualpayment);
//		// 订单总额
//		String fordercountprice = omc.getFordercountprice();
//		boolean ordercountEmpty = StringUtils.isNoneEmpty(fordercountprice);
//
//		// 设置欠款
//		if (actualpaymentEmpty && ordercountEmpty) {
//			BigDecimal fdebt = new BigDecimal(fordercountprice).subtract(new BigDecimal(factualpayment));
//			omc.setFdebt(fdebt.toString());
//		}
//
//	}
	
	
	/**
	 * 添加实体所需对象属性
	 * 
	 * @param op
	 * @return
	 */
	public void setOrderOrderMarketChaProperties(OrderMarketCha omc) {


		User createBy = omc.getCreateBy();
		if (createBy != null) {
			omc.setCreateBy(UserUtils.get(createBy.getId()));
		}

		User updateBy = omc.getUpdateBy();
		if (updateBy != null) {
			omc.setUpdateBy(UserUtils.get(updateBy.getId()));
		}

		// 其它费用
		BigDecimal fothermoney = omc.getFothermoney();
		boolean ortherEmpty = (fothermoney!=null&&fothermoney.doubleValue()>0);
		// 商品价格总额
		BigDecimal fcountprice = omc.getFcountprice();
		boolean countEmpty = (fcountprice!=null&&fcountprice.doubleValue()>0);
		// 抹零
//		BigDecimal fcutsmall = omc.getFcutsmall();
//		boolean cutEmpty = (fcutsmall!=null&&fcutsmall.doubleValue()>0);
		// 设置订单总额
		if (ortherEmpty && countEmpty) {
			// 有其他费用
			BigDecimal ordercountprice = fothermoney.add(fcountprice);
//			ordercountprice = ordercountprice.subtract(fcutsmall);
			omc.setFordercountprice(ordercountprice);
//		} else if (!ortherEmpty && countEmpty && cutEmpty) {
			// 无其他费用有抹零
//			BigDecimal ordercountprice = fcountprice.subtract(fcutsmall);
//			omc.setFordercountprice(ordercountprice);
//		} else if (ortherEmpty && countEmpty && !cutEmpty) {
//			// 有其他费用无抹零
//			BigDecimal ordercountprice = fothermoney.add(fcountprice);
//			omc.setFordercountprice(ordercountprice);
		} else {
			omc.setFordercountprice(fcountprice);
		}

		// 实付款
		BigDecimal factualpayment = omc.getFproceeds();
		boolean actualpaymentEmpty =(factualpayment!=null&&factualpayment.doubleValue()>0);
		// 订单总额
		BigDecimal fordercountprice = omc.getFordercountprice();
		boolean ordercountEmpty = (fordercountprice!=null&&fordercountprice.doubleValue()>0);

		// 设置欠款
		if (actualpaymentEmpty && ordercountEmpty) {
			BigDecimal fdebt = fordercountprice.subtract(factualpayment);
			omc.setFdebt(fdebt);
		}

	}
	
	/**
	 * 设置整单折扣后欠款
	 */
	public void setfdebt(OrderMarketCha omc) {
		// 实付款
		BigDecimal factualpayment = omc.getFproceeds();
		boolean actualpaymentEmpty = (factualpayment != null && factualpayment.doubleValue() > 0);
		// 订单折后总额
		BigDecimal fordercountprice = omc.getFdiscountprice();
		boolean ordercountEmpty = (fordercountprice != null && fordercountprice.doubleValue() > 0);

		// 设置欠款
		if (actualpaymentEmpty && ordercountEmpty) {
			BigDecimal fdebt = fordercountprice.subtract(factualpayment);
			omc.setFdebt(fdebt);
		}
	}
	
	
	
//
//	/**
//	 * 设置单条商品金额
//	 * 
//	 * @param opd
//	 */
//	public void setOrderMarketChaDetailProperties(OrderMarketChaDetail omdc) {
//
//		// 设置销售退单对象
//		OrderMarketCha fordermarketchargeback = omdc.getFordermarketchargeback();
//
//		if (fordermarketchargeback != null) {
//			  orderMarketChaDao.get(fordermarketchargeback.getId());
//
//			Warehouse fwarehose = fordermarketchargeback.getWarehouse();
//			if (fwarehose != null) {
//				fordermarketchargeback.setWarehouse(warehouseDao.get(fwarehose.getId()));
//			}
//			CustomerBasic customerBasic = fordermarketchargeback.getCustomerBasic();
//			if (customerBasic != null) {
//				fordermarketchargeback.setCustomerBasic(customerBasicDao.get(customerBasic.getId()));
//			}
//
//			omdc.setFordermarketchargeback(fordermarketchargeback);;
//		}
//		// 设置库存对象
//		WarehouseGoodsInfo fwarehousegoodsinfo = omdc.getFwarehousegoodsinfo();
//		if (fwarehousegoodsinfo != null) {
//			WarehouseGoodsInfo warehouseGoodsInfo = warehouseGoodsInfoDao.get(fwarehousegoodsinfo.getId());
//			omdc.setFwarehousegoodsinfo(warehouseGoodsInfo);
//		}
//
//		setgoods(omdc);
//		int fgoodsnum = 0;
//		// 商品数量
//		Integer goodsnum = omdc.getFgoodsnum();
//		if (goodsnum != null) {
//			fgoodsnum = goodsnum;
//		}
//		// 商品单价
//		String fgoodsprice = omdc.getFgoodsprice();
//		// 商品折扣
//		String fgoodsdiscount = omdc.getFgoodsdiscount();
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
//			omdc.setFdiscountprice(discountprice.toString());
//			// 折前总额
//			BigDecimal countmoney = goodsprice.multiply(num);
//			omdc.setFcountmoney(countmoney.toString());
//			// 折后总额
//			BigDecimal discountcountmoney = discountprice.multiply(num);
//			omdc.setFdiscountcountmoney(discountcountmoney.toString());
//		} else if (fgoodsnum > 0 && !goodsdiscountEmpty) {
//			goodsprice = new BigDecimal(fgoodsprice);
//			num = new BigDecimal(String.valueOf(fgoodsnum));
//			// 折前总额
//			BigDecimal countmoney = goodsprice.multiply(num);
//			omdc.setFcountmoney(countmoney.toString());
//			// 折后总额
//			omdc.setFdiscountcountmoney(countmoney.toString());
//			// 折后单价
//			omdc.setFdiscountprice(omdc.getFgoodsprice());
//		} else if (fgoodsnum == 0 && goodsdiscountEmpty) {
//
//			discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal("10"));
//			goodsprice = new BigDecimal(fgoodsprice);
//			// 折后单价
//			BigDecimal discountprice = goodsprice.multiply(discount);
//			omdc.setFdiscountprice(discountprice.toString());
//			// 折前总额
//			omdc.setFcountmoney(fgoodsprice);
//			// 折后总额
//			omdc.setFdiscountcountmoney(discountprice.toString());
//		} else {
//			// 折后单价
//			omdc.setFdiscountprice(fgoodsprice);
//			// 折前总额
//			omdc.setFcountmoney(fgoodsprice);
//			// 折后总额
//			omdc.setFdiscountcountmoney(fgoodsprice);
//		}
//	}
	
	

	/**
	 * 设置单条商品金额
	 * 
	 * @param opd
	 */
	public void setOrderMarketChaDetailProperties(OrderMarketChaDetail omdc) {

		// 设置销售退单对象
		OrderMarketCha fordermarketchargeback = omdc.getFordermarketchargeback();

		if (fordermarketchargeback != null) {
			  orderMarketChaDao.get(fordermarketchargeback.getId());

			Warehouse fwarehose = fordermarketchargeback.getWarehouse();
			if (fwarehose != null) {
				fordermarketchargeback.setWarehouse(warehouseDao.get(fwarehose.getId()));
			}
			CustomerBasic customerBasic = fordermarketchargeback.getCustomerBasic();
			if (customerBasic != null) {
				fordermarketchargeback.setCustomerBasic(customerBasicDao.get(customerBasic.getId()));
			}

			omdc.setFordermarketchargeback(fordermarketchargeback);;
		}
		// 设置库存对象
		WarehouseGoodsInfo fwarehousegoodsinfo = omdc.getFwarehousegoodsinfo();
		if (fwarehousegoodsinfo != null) {
			WarehouseGoodsInfo warehouseGoodsInfo = warehouseGoodsInfoDao.get(fwarehousegoodsinfo.getId());
			omdc.setFwarehousegoodsinfo(warehouseGoodsInfo);
		}

		setgoods(omdc);
		int fgoodsnum = 0;
		// 商品数量
		Integer goodsnum = omdc.getFgoodsnum();
		if (goodsnum != null) {
			fgoodsnum = goodsnum;
		}
		// 商品单价
		BigDecimal fgoodsprice = omdc.getFgoodsprice();
		// 商品折扣
		String fgoodsdiscount = omdc.getFgoodsdiscount();
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
			omdc.setFdiscountprice(discountprice);
			// 折前总额
			BigDecimal countmoney = goodsprice.multiply(num);
			omdc.setFcountmoney(countmoney);
			// 折后总额
			BigDecimal discountcountmoney = discountprice.multiply(num);
			omdc.setFdiscountcountmoney(discountcountmoney);
		} else if (fgoodsnum > 0 && !goodsdiscountEmpty) {
			goodsprice = fgoodsprice;
			num = new BigDecimal(String.valueOf(fgoodsnum));
			// 折前总额
			BigDecimal countmoney = goodsprice.multiply(num);
			omdc.setFcountmoney(countmoney);
			// 折后总额
			omdc.setFdiscountcountmoney(countmoney);
			// 折后单价
			omdc.setFdiscountprice(omdc.getFgoodsprice());
		} else if (fgoodsnum == 0 && goodsdiscountEmpty) {

			discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal(100));
			goodsprice = fgoodsprice;
			// 折后单价
			BigDecimal discountprice = goodsprice.multiply(discount);
			omdc.setFdiscountprice(discountprice);
			// 折前总额
			omdc.setFcountmoney(fgoodsprice);
			// 折后总额
			omdc.setFdiscountcountmoney(discountprice);
		} else {
			// 折后单价
			omdc.setFdiscountprice(fgoodsprice);
			// 折前总额
			omdc.setFcountmoney(fgoodsprice);
			// 折后总额
			omdc.setFdiscountcountmoney(fgoodsprice);
		}
	}
	
	

	/**
	 * 设置商品属性
	 * 
	 * @param opd
	 */
	public void setgoods(OrderMarketChaDetail omdc) {
		// 添加sku信息
		GoodsSku goodsSku = omdc.getFsku();
		if (goodsSku != null) {
			GoodsSku goodsSku2 = goodsSkuDao.get(goodsSku.getId());
//			Colors colors = goodsSku2.getColors();
//			if (colors != null) {
//				Colors colors2 = colorsDao.get(colors.getId());
//				goodsSku2.setColors(colors2);
//			}
//
//			Size size = goodsSku2.getSize();
//			if (size != null) {
//				Size size2 = sizeDao.get(size.getId());
//				goodsSku2.setSize(size2);
//			}
			omdc.setFsku(goodsSku2);
		}
		// 添加spu信息
		GoodsSpu fspu = omdc.getFspu();
		if (fspu != null) {
			GoodsSpu goodsSpu = goodsSpuDao.get(fspu.getId());
//			Brand brand = goodsSpu.getBrand();
//			if (brand != null) {
//				goodsSpu.setBrand(brandDao.get(brand.getId()));
//			}
//
//			Categorys categorys = goodsSpu.getCategorys();
//			if (categorys != null) {
//				goodsSpu.setCategorys(categorysDao.get(categorys.getId()));
//			}
//
//			GoodsUnit goodsUnit = goodsSpu.getGoodsUnit();
//			if (goodsUnit != null) {
//				goodsSpu.setGoodsUnit(goodsUnitDao.get(goodsUnit.getId()));
//			}
			omdc.setFspu(goodsSpu);
		}

	}
	
	
	public void setspu(OrderMarketChaDetail orderMarketChaDetail){
		GoodsSpu fspu = orderMarketChaDetail.getFspu();
		if(fspu!=null&&!fspu.getId().trim().isEmpty()){
			
		}else{
			GoodsSku sku = goodsSkuDao.get( orderMarketChaDetail.getFsku());
			if(sku!=null){
				GoodsSpu goodsSpu = goodsSpuDao.get(sku.getGoodsSpu());
				orderMarketChaDetail.setFspu(goodsSpu);
			}	
		}
	}
	/**
	 * 分页查询销售退单详细
	 * @param page
	 * @param detail
	 * @return
	 */
	public Page<OrderMarketChaDetail> findPageDetil(Page<OrderMarketChaDetail> page, OrderMarketChaDetail detail) {
		// TODO Auto-generated method stub
		detail.setPage(page);
		page.setList(orderMarketChaDetailDao.findList(detail));
		return page;
	}
	
	
}