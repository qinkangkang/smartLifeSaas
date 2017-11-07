/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity.procurement;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.store.entity.employee.Employee;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购单Entity
 * @author diqiang
 * @version 2017-06-12
 */
public class OrderProcurement extends DataEntity<OrderProcurement> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单状态（1.待审核2.驳回3.执行中4.采购中5.入库中6.完毕）
	 */
	public static final Integer STATUS_AUDIT = 1;
	public static final Integer STATUS_REJECTED = 2;
	public static final Integer STATUS_PERFORM = 3;
	public static final Integer STATUS_PROCUREMENT = 4;
	public static final Integer STATUS_TOWAREHOUSE = 5;
	public static final Integer STATUS_END = 6;
	
	private String fordernum;		// 采购单号
	private SupplierBasic fsupplier;		// 供应商id
	private Warehouse fwarehose;		// 仓库id
	private User fseniorarchirist;		// 审批人（角色ID）
	private User fexecutor;		// 执行人（角色ID）
	
//	private Employee fseniorarchirist;		// 审批人（角色ID）
//	private Employee fexecutor;		// 执行人（角色ID）
	
	
	private Integer fordertype;		// 订单类型 （ 1：草稿，2：已采购，3：已撤销）
	private Date fendtime;		// 订单结束时间
	private ClearingAccount fdclearingaccountid;		// 结算账户
//	private String fothermoney;		// 其他费用
//	private String fcountprice;		// 商品价格总额
	
	private BigDecimal fothermoney;		// 其他费用
	private BigDecimal fcountprice;		// 商品价格总额
	
	private AccountType fdaccounttype;		//账目类型
//	private String fcutsmall;		// 抹零
//	private String fordercountprice;		// 订单总价
	
	private BigDecimal fcutsmall;		// 抹零
	private BigDecimal fordercountprice;		// 订单总价
	
//	private Merchant fsponsor;		// 商户ID
	private Office fsponsor;		// 商户ID
	private Office fstore;			// 门店ID
	private Integer fstatus;		// 订单状态（ 1：审批中，2：驳回，3：任务指派中，4：采购中，5：入库中，6：执行完毕）
	private Integer fprint;		// 是否打印 （0：打印，1：不打印）
	private SysModelType fmodeltype;		// 打印模板ID
//	private String factualpayment;		// 实付款
//	private String fdebt;		// 欠款
	
	private BigDecimal factualpayment;		// 实付款
	private BigDecimal fdebt;		// 欠款
	
	private BigDecimal fdiscountprice;  //整单折后价
	private String forderdiscount;  //整单折扣

	private List<OrderProDetail> orderProDetailList = Lists.newArrayList();		// 子表列表
	
	public OrderProcurement() {
		super();
	}

	public OrderProcurement(String id){
		super(id);
	}

	@ExcelField(title="采购单号", align=2, sort=2)
	public String getFordernum() {
		return fordernum;
	}

	public void setFordernum(String fordernum) {
		this.fordernum = fordernum;
	}
	
	@NotNull(message="供应商id不能为空")
	@ExcelField(title="供应商", align=2, sort=3,value="fsupplier.fname")
	public SupplierBasic getFsupplier() {
		return fsupplier;
	}

	public void setFsupplier(SupplierBasic fsupplier) {
		this.fsupplier = fsupplier;
	}
	
	@NotNull(message="仓库id不能为空")
	@ExcelField(title="仓库", align=2, sort=4,value="fwarehose.fname")
	public Warehouse getFwarehose() {
		return fwarehose;
	}

	public void setFwarehose(Warehouse fwarehose) {
		this.fwarehose = fwarehose;
	}
	
	@ExcelField(title="审批人", align=2, sort=5,value="fseniorarchirist.loginName")
	public User getFseniorarchirist() {
		return fseniorarchirist;
	}

	public void setFseniorarchirist(User fseniorarchirist) {
		this.fseniorarchirist = fseniorarchirist;
	}
	
	@ExcelField(title="执行人", align=2, sort=6,value="fexecutor.loginName")
	public User getFexecutor() {
		return fexecutor;
	}

	public void setFexecutor(User fexecutor) {
		this.fexecutor = fexecutor;
	}
	//（ 1：草稿，2：已采购，3：已撤销）
	@ExcelField(title="订单类型 ", dictType="procurement_type", align=2, sort=19)
	public Integer getFordertype() {
		return fordertype;
	}

	public void setFordertype(Integer fordertype) {
		this.fordertype = fordertype;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="订单结束时间", align=2, sort=18)
	public Date getFendtime() {
		return fendtime;
	}

	public void setFendtime(Date fendtime) {
		this.fendtime = fendtime;
	}
	
	@ExcelField(title="结算账户", align=2, sort=7,value="fdclearingaccountid.faccountname")
	public ClearingAccount getFdclearingaccountid() {
		return fdclearingaccountid;
	}

	public void setFdclearingaccountid(ClearingAccount fdclearingaccountid) {
		this.fdclearingaccountid = fdclearingaccountid;
	}
	
	@ExcelField(title="其他费用", align=2, sort=9)
	public BigDecimal getFothermoney() {
		return fothermoney;
	}

	public void setFothermoney(BigDecimal fothermoney) {
		this.fothermoney = fothermoney;
	}
	
	@ExcelField(title="商品价格总额", align=2, sort=10)
	public BigDecimal getFcountprice() {
		return fcountprice;
	}

	public void setFcountprice(BigDecimal fcountprice) {
		this.fcountprice = fcountprice;
	}
	
	@ExcelField(title="账目类型", align=2, sort=8,value="fdaccounttype.ftypename")
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
	
	@ExcelField(title="订单总价", align=2, sort=11)
	public BigDecimal getFordercountprice() {
		return fordercountprice;
	}

	public void setFordercountprice(BigDecimal fordercountprice) {
		this.fordercountprice = fordercountprice;
	}
	
//	@NotNull(message="商户不能为空")
//	@ExcelField(title="商户", align=2, sort=15)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
//	@NotNull(message="门店不能为空")
	@ExcelField(title="门店", align=2, sort=17)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
	}
	//（ 1：审批中，2：驳回，3：任务指派中，4：采购中，5：入库中，6：执行完毕）
	@ExcelField(title="订单状态", dictType="procurement_status", align=2, sort=20)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
//	@ExcelField(title="是否打印 （0：打印，1：不打印）", dictType="print_status", align=2, sort=17)
	public Integer getFprint() {
		return fprint;
	}

	public void setFprint(Integer fprint) {
		this.fprint = fprint;
	}
	
//	@ExcelField(title="打印模板", align=2, sort=18)
	public SysModelType getFmodeltype() {
		return fmodeltype;
	}

	public void setFmodeltype(SysModelType fmodeltype) {
		this.fmodeltype = fmodeltype;
	}
	
	@ExcelField(title="实付款", align=2, sort=13)
	public BigDecimal getFactualpayment() {
		return factualpayment;
	}

	public void setFactualpayment(BigDecimal factualpayment) {
		this.factualpayment = factualpayment;
	}
	
	@ExcelField(title="欠款", align=2, sort=14)
	public BigDecimal getFdebt() {
		return fdebt;
	}
	
	public void setFdebt(BigDecimal fdebt) {
		this.fdebt = fdebt;
	}
	
	@ExcelField(title="整单折扣", align=2, sort=12)
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
	
	
	
	public List<OrderProDetail> getOrderProDetailList() {
		return orderProDetailList;
	}

	public void setOrderProDetailList(List<OrderProDetail> orderProDetailList) {
		this.orderProDetailList = orderProDetailList;
	}
}