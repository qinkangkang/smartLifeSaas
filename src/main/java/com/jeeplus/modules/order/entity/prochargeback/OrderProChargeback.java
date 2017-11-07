/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity.prochargeback;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购退单Entity
 * @author diqiang
 * @version 2017-06-14
 */
public class OrderProChargeback extends DataEntity<OrderProChargeback> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单状态（1.待审核2.驳回3.执行中4.出库中5.退款中6.完毕）
	 */
	public static final Integer STATUS_AUDIT = 1;
	public static final Integer STATUS_REJECTED = 2;
	public static final Integer STATUS_PERFORM = 3;
	public static final Integer STATUS_OUTWAREHOUSE = 4;
	public static final Integer STATUS_REFUND = 5;
	public static final Integer STATUS_END = 6;
	private String fordernum;		// 采购退单单号
	private SupplierBasic fsupplier;		// 供应商id
	private Warehouse fwarehose;		// 仓库ID
	private User fseniorarchirist;		// 审批人（角色ID）
	private User fexecutor;		// 执行人（角色ID）
	private Integer fordertype;		// 采购退货单类型（1：草稿，2：已退货，3：已撤销） 
	private Integer fstatus;		// 采购退单状态 （1：审批中，2：驳回，3：任务指派中，4：出库中，5：待退款，6：执行完毕）
	private Date fendtime;		// 结束时间
	private ClearingAccount fdclearingaccount;		// 结算账户ID
//	private String fothermoney;		// 其他费用
//	private String fcountprice;		// 商品总额
	
	private BigDecimal fothermoney;		// 其他费用
	private BigDecimal fcountprice;		// 商品总额
	
	private AccountType fdaccounttype;		// 其他费用类型（ 1：采购支出，2：采购退货，3：销售收入，4：销售退货，5：零售）
//	private String fcutsmall;		// 抹零
//	private String fordercountprice;		// 订单总价
	
	private BigDecimal fcutsmall;		// 抹零
	private BigDecimal fordercountprice;		// 订单总价
	
	
	private OrderProcurement forderprocurement;		// 采购单ID
//	private Merchant fsponsor;		// 商户ID
	private Office fsponsor;		// 商户ID
	private Office fstore;			// 门店ID
	private Integer fprint;		// 是否打印 （0：打印，1：不打印）
	private SysModelType fmodeltype;		// 打印模板ID
//	private String factualpayment;		// 实付款
//	private String fdebt;		// 欠款
//	private String beginFdebt;		// 开始 欠款
//	private String endFdebt;		// 结束 欠款
	
	private BigDecimal factualpayment;		// 实收款
	private BigDecimal fdebt;		// 欠款
	
	private String forderdiscount;//订单折扣
	private BigDecimal fdiscountprice;//折后价
	
	
	private BigDecimal beginFdebt;		// 开始 欠款
	private BigDecimal endFdebt;		// 结束 欠款
	
	
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private List<OrderProChaDetail> orderProChaDetailList = Lists.newArrayList();		// 子表列表
	
	public OrderProChargeback() {
		super();
	}

	public OrderProChargeback(String id){
		super(id);
	}

	@ExcelField(title="采购退单单号", align=2, sort=1)
	public String getFordernum() {
		return fordernum;
	}

	public void setFordernum(String fordernum) {
		this.fordernum = fordernum;
	}
	
	@ExcelField(title="供应商", align=2, sort=2,value="fsupplier.fname")
	public SupplierBasic getFsupplier() {
		return fsupplier;
	}

	public void setFsupplier(SupplierBasic fsupplier) {
		this.fsupplier = fsupplier;
	}
	
	@ExcelField(title="仓库", align=2, sort=3,value="fwarehose.fname")
	public Warehouse getFwarehose() {
		return fwarehose;
	}

	public void setFwarehose(Warehouse fwarehose) {
		this.fwarehose = fwarehose;
	}
	
	@ExcelField(title="审批人", fieldType=User.class, value="fseniorarchirist.loginName", align=2, sort=4)
	public User getFseniorarchirist() {
		return fseniorarchirist;
	}

	public void setFseniorarchirist(User fseniorarchirist) {
		this.fseniorarchirist = fseniorarchirist;
	}
	
	@ExcelField(title="执行人", fieldType=User.class, value="fexecutor.loginName", align=2, sort=5)
	public User getFexecutor() {
		return fexecutor;
	}

	public void setFexecutor(User fexecutor) {
		this.fexecutor = fexecutor;
	}
	//（1：草稿，2：已退货，3：已撤销）
	@ExcelField(title="采购退货单类型 ", dictType="chargeback_type", align=2, sort=6)
	public Integer getFordertype() {
		return fordertype;
	}

	public void setFordertype(Integer fordertype) {
		this.fordertype = fordertype;
	}
	//（1：审批中，2：驳回，3：任务指派中，4：出库中，5：待退款，6：执行完毕）
	@ExcelField(title="采购退单状态 ", dictType="procurement_charge_status", align=2, sort=7)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=8)
	public Date getFendtime() {
		return fendtime;
	}

	public void setFendtime(Date fendtime) {
		this.fendtime = fendtime;
	}
	
	@ExcelField(title="结算账户", align=2, sort=9,value="fdclearingaccount.faccountname")
	public ClearingAccount getFdclearingaccount() {
		return fdclearingaccount;
	}

	public void setFdclearingaccount(ClearingAccount fdclearingaccount) {
		this.fdclearingaccount = fdclearingaccount;
	}
	
	@ExcelField(title="其他费用", align=2, sort=12)
	public BigDecimal getFothermoney() {
		return fothermoney;
	}

	public void setFothermoney(BigDecimal fothermoney) {
		this.fothermoney = fothermoney;
	}
	
	@ExcelField(title="商品总额", align=2, sort=11)
	public BigDecimal getFcountprice() {
		return fcountprice;
	}

	public void setFcountprice(BigDecimal fcountprice) {
		this.fcountprice = fcountprice;
	}
	//（ 1：采购支出，2：采购退货，3：销售收入，4：销售退货，5：零售）
	@ExcelField(title="其他费用类型", align=2, sort=10,value="fdaccounttype.ftypename")
	public AccountType getFdaccounttype() {
		return fdaccounttype;
	}

	public void setFdaccounttype(AccountType fdaccounttype) {
		this.fdaccounttype = fdaccounttype;
	}
	
	@ExcelField(title="抹零", align=2, sort=15)
	public BigDecimal getFcutsmall() {
		return fcutsmall;
	}

	public void setFcutsmall(BigDecimal fcutsmall) {
		this.fcutsmall = fcutsmall;
	}
	
	@ExcelField(title="订单总价", align=2, sort=13)
	public BigDecimal getFordercountprice() {
		return fordercountprice;
	}

	public void setFordercountprice(BigDecimal fordercountprice) {
		this.fordercountprice = fordercountprice;
	}
	
//	@ExcelField(title="采购单", align=2, sort=15)
	public OrderProcurement getForderprocurement() {
		return forderprocurement;
	}

	public void setForderprocurement(OrderProcurement forderprocurement) {
		this.forderprocurement = forderprocurement;
	}
	
//	@ExcelField(title="商户", align=2, sort=16)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
	
	
	@ExcelField(title="门店", align=2, sort=19)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
	}

//	@ExcelField(title="是否打印 （0：打印，1：不打印）", dictType="print_status", align=2, sort=17)
	public Integer getFprint() {
		return fprint;
	}

	public void setFprint(Integer fprint) {
		this.fprint = fprint;
	}
	
//	@ExcelField(title="打印模板ID", align=2, sort=18)
	public SysModelType getFmodeltype() {
		return fmodeltype;
	}

	public void setFmodeltype(SysModelType fmodeltype) {
		this.fmodeltype = fmodeltype;
	}
	
	@ExcelField(title="实收款", align=2, sort=17)
	public BigDecimal getFactualpayment() {
		return factualpayment;
	}

	public void setFactualpayment(BigDecimal factualpayment) {
		this.factualpayment = factualpayment;
	}
	
	@ExcelField(title="欠款", align=2, sort=18)
	public BigDecimal getFdebt() {
		return fdebt;
	}

	public void setFdebt(BigDecimal fdebt) {
		this.fdebt = fdebt;
	}
	
	@ExcelField(title="整单折扣", align=2, sort=14)
	public String getForderdiscount() {
		return forderdiscount;
	}

	public void setForderdiscount(String forderdiscount) {
		this.forderdiscount = forderdiscount;
	}
	@ExcelField(title="最终金额", align=2, sort=16)
	public BigDecimal getFdiscountprice() {
		return fdiscountprice;
	}

	public void setFdiscountprice(BigDecimal fdiscountprice) {
		this.fdiscountprice = fdiscountprice;
	}

	public BigDecimal getBeginFdebt() {
		return beginFdebt;
	}

	public void setBeginFdebt(BigDecimal beginFdebt) {
		this.beginFdebt = beginFdebt;
	}
	
	public BigDecimal getEndFdebt() {
		return endFdebt;
	}

	public void setEndFdebt(BigDecimal endFdebt) {
		this.endFdebt = endFdebt;
	}
		
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
	public List<OrderProChaDetail> getOrderProChaDetailList() {
		return orderProChaDetailList;
	}

	public void setOrderProChaDetailList(List<OrderProChaDetail> orderProChaDetailList) {
		this.orderProChaDetailList = orderProChaDetailList;
	}
}