/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.service.prochargeback;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChargeback;
import com.jeeplus.modules.order.dao.prochargeback.OrderProChargebackDao;
import com.jeeplus.modules.order.dao.procurement.OrderProcurementDao;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.goods.dao.sku.GoodsSkuDao;
import com.jeeplus.modules.goods.dao.spu.GoodsSpuDao;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;
import com.jeeplus.modules.order.dao.prochargeback.OrderProChaDetailDao;

/**
 * 采购退单Service
 * @author diqiang
 * @version 2017-06-13
 */
@Service
@Transactional(readOnly = true)
public class OrderProChargebackService extends CrudService<OrderProChargebackDao, OrderProChargeback> {

	@Autowired
	private OrderProChaDetailDao orderProChaDetailDao;
	
	@Autowired
	private OrderProChargebackDao orderProChargebackDao;
	
	@Autowired
	private OrderProcurementDao oderProcurementDao;
	
	//商品dao
	@Autowired
	private GoodsSpuDao goodsSpuDao;
	//商品详情sku
	@Autowired
	private GoodsSkuDao goodsSkuDao;
	public OrderProChargeback get(String id) {
		OrderProChargeback orderProChargeback = super.get(id);
		orderProChargeback.setOrderProChaDetailList(orderProChaDetailDao.findList(new OrderProChaDetail(orderProChargeback)));
		return orderProChargeback;
	}
	
	public List<OrderProChargeback> findList(OrderProChargeback orderProChargeback) {
		//权限过滤
		customDataScopeFilter(orderProChargeback, "dsf", "id=a.office_id", "id=a.create_by");
//		orderProChargeback.getSqlMap().put("dsf", dataScopeFilter(orderProChargeback.getCurrentUser(), "fsponsor", "createby"));
		return super.findList(orderProChargeback);
	}
	
	public Page<OrderProChargeback> findPage(Page<OrderProChargeback> page, OrderProChargeback orderProChargeback) {
		//权限过滤
		customDataScopeFilter(orderProChargeback, "dsf", "id=a.office_id", "id=a.create_by");
//		orderProChargeback.getSqlMap().put("dsf", dataScopeFilter(orderProChargeback.getCurrentUser(), "fsponsor", "createby"));
		return super.findPage(page, orderProChargeback);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderProChargeback orderProChargeback) {
		super.save(orderProChargeback);
		for (OrderProChaDetail orderProChaDetail : orderProChargeback.getOrderProChaDetailList()){
//			if (orderProChaDetail.getId() == null){
//				continue;
//			}
			if (OrderProChaDetail.DEL_FLAG_NORMAL.equals(orderProChaDetail.getDelFlag())){
				if (StringUtils.isBlank(orderProChaDetail.getId())){
					orderProChaDetail.setFprocurementchargeback(orderProChargeback);
					orderProChaDetail.preInsert();
					orderProChaDetailDao.insert(orderProChaDetail);
				}else{
					orderProChaDetail.preUpdate();
					orderProChaDetailDao.update(orderProChaDetail);
				}
			}else{
				orderProChaDetailDao.deleteByLogic(orderProChaDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void deleteByLogic(OrderProChargeback orderProChargeback) {
		super.deleteByLogic(orderProChargeback);
		orderProChaDetailDao.deleteByLogic(new OrderProChaDetail(orderProChargeback));
	}
	
	public Page<SupplierBasic> findPageByfsupplier(Page<SupplierBasic> page, SupplierBasic fsupplier) {
		//权限过滤
		customDataScopeFilter(fsupplier, "dsf", "id=a.office_id", "id=a.create_by");
//		fsupplier.getSqlMap().put("dsf", dataScopeFilter(fsupplier.getCurrentUser(), "fsponsor", "createby"));
		fsupplier.setPage(page);
		page.setList(dao.findListByfsupplier(fsupplier));
		return page;
	}
	public Page<Warehouse> findPageByfwarehose(Page<Warehouse> page, Warehouse fwarehose) {
		//权限过滤
		customDataScopeFilter(fwarehose, "dsf", "id=a.office_id", "id=a.create_by");
//		fwarehose.getSqlMap().put("dsf", dataScopeFilter(fwarehose.getCurrentUser(), "fsponsor", "createby"));
		fwarehose.setPage(page);
		page.setList(dao.findListByfwarehose(fwarehose));
		return page;
	}

	public Page<ClearingAccount> findPageByfdclearingaccountid(Page<ClearingAccount> page, ClearingAccount fdclearingaccountid) {
		//权限过滤
		customDataScopeFilter(fdclearingaccountid, "dsf", "id=a.office_id", "id=a.create_by");
//		fdclearingaccountid.getSqlMap().put("dsf", dataScopeFilter(fdclearingaccountid.getCurrentUser(), "fsponsor", "createby"));
		fdclearingaccountid.setPage(page);
		page.setList(dao.findListByfdclearingaccount(fdclearingaccountid));
		return page;
	}
	public Page<AccountType> findPageByfothermoneytype(Page<AccountType> page, AccountType fothermoneytype) {
		//权限过滤
		customDataScopeFilter(fothermoneytype, "dsf", "id=a.office_id", "id=a.createby");
//		fothermoneytype.getSqlMap().put("dsf", dataScopeFilter(fothermoneytype.getCurrentUser(), "fsponsor", "createby"));
		fothermoneytype.setPage(page);
		page.setList(dao.findListByfdaccounttype(fothermoneytype));
		return page;
	}
	public Page<OrderProcurement> findPageByforderprocurement(Page<OrderProcurement> page, OrderProcurement forderprocurement) {
		//权限过滤
		customDataScopeFilter(forderprocurement, "dsf", "id=a.office_id", "id=a.create_by");
//		forderprocurement.getSqlMap().put("dsf", dataScopeFilter(forderprocurement.getCurrentUser(), "fsponsor", "createby"));
		forderprocurement.setPage(page);
		page.setList(dao.findListByforderprocurement(forderprocurement));
		return page;
	}
//	public Page<Office> findPageByfstore(Page<Office> page, Office fstore) {
//		//权限过滤
//		customDataScopeFilter(fstore, "dsf", "id=parent_id", "id=create_by");
//		fstore.setPage(page);
//		page.setList(dao.findListByfstore(fstore));
//		return page;
//	}
	public Page<SysModelType> findPageByfmodeltypeid(Page<SysModelType> page, SysModelType fmodeltypeid) {
		fmodeltypeid.setPage(page);
		page.setList(dao.findListByfmodeltypeid(fmodeltypeid));
		return page;
	}
	
	
	public Page<GoodsSpu> findPageByfspu(Page<GoodsSpu> page, GoodsSpu fspu) {
		//权限过滤
//		customDataScopeFilter(fspu, "dsf", "id=a.office_id", "id=a.create_by");
//		fspu.getSqlMap().put("dsf", dataScopeFilter(fspu.getCurrentUser(), "fsponsor", "createby"));
		fspu.setPage(page);
		page.setList(orderProChaDetailDao.findListByfspu(fspu));
		return page;
	}
	public Page<GoodsSku> findPageByfsku(Page<GoodsSku> page, GoodsSku fsku) {
		//权限过滤
//		customDataScopeFilter(fsku, "dsf", "id=a.office_id", "id=a.create_by");
//		fsku.getSqlMap().put("dsf", dataScopeFilter(fsku.getCurrentUser(), "fsponsor", "createby"));
		fsku.setPage(page);
		page.setList(orderProChaDetailDao.findListByfsku(fsku));
		return page;
	}
	
	
	
	
	
	
//	/**
//	 * 添加实体所需对象属性
//	 * @param op
//	 * @return
//	 */
//	public  void setOrderProcurementProperties(OrderProChargeback op){
//
//	
//		User createBy = op.getCreateBy();
//		if(createBy!=null){
//			op.setCreateBy(UserUtils.get(createBy.getId()));
//		}
//		
//		User updateBy = op.getUpdateBy();
//		if(updateBy!=null){
//			op.setUpdateBy(UserUtils.get(updateBy.getId()));
//		}
//		
//
//		OrderProChargeback orderProChargeback = orderProChargebackDao.get(op.getId());
//		//如果为新对象则不查询详情表
//		if(orderProChargeback!=null){
//			
//			OrderProChaDetail opd=new OrderProChaDetail();
//			opd.setFprocurementchargeback(orderProChargeback);
//
//			List<OrderProChaDetail> opdList = orderProChaDetailDao.findList(opd);
//			//给详情对象添加商品对象信息用于回显
//			for(OrderProChaDetail opd1:opdList){
//				GoodsSpu fspu = opd1.getFspu();
//				if(fspu!=null){
//					opd1.setFspu(goodsSpuDao.get(fspu.getId()));
//				}
//				GoodsSku fsku = opd1.getFsku();
//				if(fsku!=null){
//					opd1.setFsku(goodsSkuDao.get(fsku.getId()));
//				}
//			}
//			op.setOrderProChaDetailList(opdList);
//		}
//		//计算折扣及价钱
//		//其它费用
//		String fothermoney = op.getFothermoney();
//		boolean ortherEmpty = StringUtils.isNoneEmpty(fothermoney);
//		//商品价格总额
//		String fcountprice = op.getFcountprice();
//		boolean countEmpty = StringUtils.isNoneEmpty(fcountprice);
//		//抹零
//		String fcutsmall = op.getFcutsmall();
//		boolean cutEmpty = StringUtils.isNoneEmpty(fcutsmall);
//		//设置订单总额
//		if(ortherEmpty&&countEmpty&&cutEmpty){
//			//有其他费用有抹零
//			BigDecimal  ordercountprice= new BigDecimal(fothermoney).add(new BigDecimal(fcountprice));
//			ordercountprice=ordercountprice.subtract(new BigDecimal(fcutsmall));
//			op.setFordercountprice(ordercountprice.toString());
//		}else if (!ortherEmpty&&countEmpty&&cutEmpty) {
//			//无其他费用有抹零
//			BigDecimal  ordercountprice= new BigDecimal(fcountprice).subtract(new BigDecimal(fcutsmall));
//			op.setFordercountprice(ordercountprice.toString());
//		}else if (ortherEmpty&&countEmpty&&!cutEmpty) {
//			//有其他费用无抹零
//			BigDecimal  ordercountprice= new BigDecimal(fothermoney).add(new BigDecimal(fcountprice));
//			op.setFordercountprice(ordercountprice.toString());
//		}else{
//			op.setFordercountprice(fcountprice);
//		}
//		
//		//实付款
//		String factualpayment = op.getFactualpayment();
//		boolean actualpaymentEmpty = StringUtils.isNoneEmpty(factualpayment);
//		//订单总额
//		String fordercountprice = op.getFordercountprice();
//		boolean ordercountEmpty = StringUtils.isNoneEmpty(fordercountprice);
//		
//		//设置欠款
//		if(actualpaymentEmpty&&ordercountEmpty){
//			BigDecimal fdebt=new BigDecimal(fordercountprice).subtract(new BigDecimal(factualpayment));
//			op.setFdebt(fdebt.toString());
//		}
//		
//	}
	
	/**
	 * 添加实体所需对象属性
	 * @param op
	 * @return
	 */
	public  void setOrderProcurementProperties(OrderProChargeback op){

	
		User createBy = op.getCreateBy();
		if(createBy!=null){
			op.setCreateBy(UserUtils.get(createBy.getId()));
		}
		
		User updateBy = op.getUpdateBy();
		if(updateBy!=null){
			op.setUpdateBy(UserUtils.get(updateBy.getId()));
		}
		

//		OrderProChargeback orderProChargeback = orderProChargebackDao.get(op.getId());
//		//如果为新对象则不查询详情表
//		if(orderProChargeback!=null){
//			
//			OrderProChaDetail opd=new OrderProChaDetail();
//			opd.setFprocurementchargeback(orderProChargeback);
//
//			List<OrderProChaDetail> opdList = orderProChaDetailDao.findList(opd);
//			//给详情对象添加商品对象信息用于回显
//			for(OrderProChaDetail opd1:opdList){
//				GoodsSpu fspu = opd1.getFspu();
//				if(fspu!=null){
//					opd1.setFspu(goodsSpuDao.get(fspu.getId()));
//				}
//				GoodsSku fsku = opd1.getFsku();
//				if(fsku!=null){
//					opd1.setFsku(goodsSkuDao.get(fsku.getId()));
//				}
//			}
//			op.setOrderProChaDetailList(opdList);
//		}
		//其它费用
		BigDecimal fothermoney = op.getFothermoney();
		boolean ortherEmpty = (fothermoney!=null&&fothermoney.doubleValue()>0);
		//商品价格总额
		BigDecimal fcountprice = op.getFcountprice();
		boolean countEmpty = (fcountprice!=null&&fcountprice.doubleValue()>0);
		//设置订单总额
		if(ortherEmpty&&countEmpty){
			//有其他费用
			BigDecimal  ordercountprice= fothermoney.add(fcountprice);
			op.setFordercountprice(ordercountprice);
		}else{
			op.setFordercountprice(fcountprice);
		}
		
		//实付款
		BigDecimal factualpayment = op.getFactualpayment();
		boolean actualpaymentEmpty = (factualpayment!=null&&factualpayment.doubleValue()>0);
		//订单总额
		BigDecimal fordercountprice = op.getFordercountprice();
		boolean ordercountEmpty =(fordercountprice!=null&&fordercountprice.doubleValue()>0);
		
		//设置欠款
		if(actualpaymentEmpty&&ordercountEmpty){
			BigDecimal fdebt=fordercountprice.subtract(factualpayment);
			op.setFdebt(fdebt);
		}
		
	}
	
	//有整单折扣重新设置欠款
	public void setfdebt(OrderProChargeback op){
				//实付款
				BigDecimal factualpayment = op.getFactualpayment();
				boolean actualpaymentEmpty = (factualpayment!=null&&factualpayment.doubleValue()>0);
				//订单折后总额
				BigDecimal fordercountprice = op.getFdiscountprice();
				boolean ordercountEmpty =(fordercountprice!=null&&fordercountprice.doubleValue()>0);
				
				//设置欠款
				if(actualpaymentEmpty&&ordercountEmpty){
					BigDecimal fdebt=fordercountprice.subtract(factualpayment);
					op.setFdebt(fdebt);
				}
	}
	
	
		
//	/**
//	 * 设置单条商品金额
//	 * @param opd
//	 */
//	public void setOrderProDetailProperties(OrderProChaDetail opd){
//		
//		//查询商品是哪个采购单的
//		OrderProcurement fprocurement = opd.getFprocurement();
//		if(fprocurement!=null){
//			opd.setFprocurement(oderProcurementDao.get(fprocurement.getId()));
//		}
//		
//		setgoods(opd);
//		
//		//商品数量
//		int fgoodsnum = opd.getFgoodsnum();
//		//商品单价
//		String fgoodsprice = opd.getFgoodsprice();
//		//商品折扣
//		String fgoodsdiscount = opd.getFdiscount();
//		boolean goodsdiscountEmpty = StringUtils.isNoneEmpty(fgoodsdiscount);
//		//折扣
//		BigDecimal discount = null;
//		//单价
//		BigDecimal goodsprice=null;
//		//数量
//		BigDecimal num=null;
//		
//		if(fgoodsnum>0&&goodsdiscountEmpty){
//			
//			 discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal("10"));
//			 goodsprice=new BigDecimal(fgoodsprice);
//			 num=new BigDecimal(String.valueOf(fgoodsnum));
//			 
//			 //折后单价
//			 BigDecimal discountprice = goodsprice.multiply(discount);
//			 opd.setFdiscountPrice(discountprice.toString());
//			//折前总额
//			 BigDecimal countmoney = goodsprice.multiply(num);
//			 opd.setFcountprice(countmoney.toString());
//			 //折后总额
//			 BigDecimal discountcountmoney = discountprice.multiply(num);
//			 opd.setFdiscountcountprice(discountcountmoney.toString());
//		}else if(fgoodsnum>0&&!goodsdiscountEmpty){
//			 goodsprice=new BigDecimal(fgoodsprice);
//			 num=new BigDecimal(String.valueOf(fgoodsnum));
//			//折前总额
//			 BigDecimal countmoney = goodsprice.multiply(num);
//			 opd.setFcountprice(countmoney.toString());
//			//折后总额
//			 opd.setFdiscountcountprice(countmoney.toString());
//			 //折后单价
//			 opd.setFdiscountPrice(opd.getFgoodsprice());
//		}else if (fgoodsnum==0&&goodsdiscountEmpty) {
//			
//			 discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal("10"));
//			 goodsprice=new BigDecimal(fgoodsprice);
//			 //折后单价
//			 BigDecimal discountprice = goodsprice.multiply(discount);
//			 opd.setFdiscountPrice(discountprice.toString());
//			//折前总额
//			 opd.setFcountprice(fgoodsprice);
//			 //折后总额
//			 opd.setFdiscountcountprice(discountprice.toString());
//		}else {
//			 //折后单价
//			 opd.setFdiscountPrice(fgoodsprice);
//			 //折前总额
//			 opd.setFcountprice(fgoodsprice);
//			 //折后总额
//			 opd.setFdiscountcountprice(fgoodsprice);
//		}
//	}
	
	
	/**
	 * 设置单条商品金额
	 * @param opd
	 */
	public void setOrderProDetailProperties(OrderProChaDetail opd){
		
		//查询商品是哪个采购单的
		OrderProcurement fprocurement = opd.getFprocurement();
		if(fprocurement!=null){
			opd.setFprocurement(oderProcurementDao.get(fprocurement.getId()));
		}
		
		setgoods(opd);
		
		//商品数量
		int fgoodsnum = opd.getFgoodsnum();
		//商品单价
		BigDecimal fgoodsprice = opd.getFgoodsprice();
		//商品折扣
		String fgoodsdiscount = opd.getFdiscount();
		boolean goodsdiscountEmpty = StringUtils.isNoneEmpty(fgoodsdiscount);
		//折扣
		BigDecimal discount = null;
		//单价
		BigDecimal goodsprice=null;
		//数量
		BigDecimal num=null;
		
		if(fgoodsnum>0&&goodsdiscountEmpty){
			
			 discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal(100));
			 goodsprice=fgoodsprice;
			 num=new BigDecimal(String.valueOf(fgoodsnum));
			 
			 //折后单价
			 BigDecimal discountprice = goodsprice.multiply(discount);
			 opd.setFdiscountPrice(discountprice);
			//折前总额
			 BigDecimal countmoney = goodsprice.multiply(num);
			 opd.setFcountprice(countmoney);
			 //折后总额
			 BigDecimal discountcountmoney = discountprice.multiply(num);
			 opd.setFdiscountcountprice(discountcountmoney);
		}else if(fgoodsnum>0&&!goodsdiscountEmpty){
			 goodsprice=fgoodsprice;
			 num=new BigDecimal(String.valueOf(fgoodsnum));
			//折前总额
			 BigDecimal countmoney = goodsprice.multiply(num);
			 opd.setFcountprice(countmoney);
			//折后总额
			 opd.setFdiscountcountprice(countmoney);
			 //折后单价
			 opd.setFdiscountPrice(opd.getFgoodsprice());
		}else if (fgoodsnum==0&&goodsdiscountEmpty) {
			
			 discount = new BigDecimal(fgoodsdiscount).divide(new BigDecimal(100));
			 goodsprice=fgoodsprice;
			 //折后单价
			 BigDecimal discountprice = goodsprice.multiply(discount);
			 opd.setFdiscountPrice(discountprice);
			//折前总额
			 opd.setFcountprice(fgoodsprice);
			 //折后总额
			 opd.setFdiscountcountprice(discountprice);
		}else {
			 //折后单价
			 opd.setFdiscountPrice(fgoodsprice);
			 //折前总额
			 opd.setFcountprice(fgoodsprice);
			 //折后总额
			 opd.setFdiscountcountprice(fgoodsprice);
		}
	}
	
	
	
	/**
	 * set商品属性
	 * @param opd
	 */
	public void setgoods(OrderProChaDetail opd){
		//添加sku信息
		GoodsSku goodsSku = opd.getFsku();
		if(goodsSku!=null){
			GoodsSku goodsSku2 = goodsSkuDao.get(goodsSku.getId());
			opd.setFsku(goodsSku2);
		}
//添加spu信息
		GoodsSpu fspu = opd.getFspu();
		if(fspu!=null){
			GoodsSpu goodsSpu = goodsSpuDao.get(fspu.getId());
			opd.setFspu(goodsSpu);
		}
		
	}

	public Page<SysModelType> findPageBysysModelType(Page<SysModelType> page, SysModelType sysModelType) {
		sysModelType.setPage(page);
		page.setList(dao.findListBysysModelType(sysModelType));
		return page;
	}
	/**
	 * 统计数量
	 * @param status(1：审批中，2：驳回，3：任务指派中，4：出库中，5：待退款，6：执行完毕)
	 * @return Integer
	 */
	public Integer countChangeBack(Integer status){
		return dao.countNum(status);
		
	}
	/**
	 * 分页查询采购退单详细列表
	 * @param page
	 * @param detail
	 * @return
	 */
	public Page<OrderProChaDetail> findPageDetail(Page<OrderProChaDetail> page, OrderProChaDetail detail) {
		// TODO Auto-generated method stub
		List<OrderProChaDetail> findList = orderProChaDetailDao.findList(detail);
		detail.setPage(page);
		page.setList(findList);
		return page;
	}
	
	
}