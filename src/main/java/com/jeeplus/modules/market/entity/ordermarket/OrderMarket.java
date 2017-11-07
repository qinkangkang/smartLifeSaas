/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.entity.ordermarket;

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.store.entity.storemanage.Store;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售订单管理Entity
 * 
 * @author diqiang
 * @version 2017-06-18
 */
public class OrderMarket extends DataEntity<OrderMarket> {

	private static final long serialVersionUID = 1L;
	/**
	 * 订单状态（0.待支付1.待发货2.待收货3.完毕）
	 */
	public static final String STATUS_UNPAID = "0";
	public static final String STATUS_SHIPMENTS = "1";
	public static final String STATUS_COLLECT = "2";
	public static final String STATUS_END = "3";
	private String fordernum; // 销售单号
	private CustomerBasic customerBasic; // 客户名称
	private Warehouse warehouse; // 仓库名称
	private AccountType accountType; // 账目类型
	private ClearingAccount clearingAccount; // 结算账户
	private Integer fshipmenttype; // 发货方式
	private Integer fordertype; // 订单类型
	private Integer fstatus; // 订单状态
	private Date fendtime; // 销售单结束时间
	// private String fothermoney; // 其他费用
	// private String fcountprice; // 商品总价
	// private String fcutsmall; // 抹零
	// private String fordercountprice; // 订单实付总额

	private BigDecimal fothermoney; // 其他费用
	private BigDecimal fcountprice; // 商品总价
	private BigDecimal fcutsmall; // 抹零
	private BigDecimal fordercountprice;
	private BigDecimal fproceeds; // 实收款
	private BigDecimal fdebt; // 欠款

	private Integer fordermodel; // 订单形式
	private Office fsponsor; // 商户id

	private Office fstore; // 所属门店

	private String forderdiscount; // 订单折扣
	private BigDecimal fdiscountprice; // 折后价

	private Integer fprint; // 是否打印
	private SysModelType sysModelType; // 打印模板
	// private String fproceeds; // 实收款
	// private String fdebt; // 欠款
	private List<OrderMarketDetail> orderMarketDetailList = Lists.newArrayList(); // 子表列表

	public OrderMarket() {
		super();
	}

	public OrderMarket(String id) {
		super(id);
	}

	@ExcelField(title = "销售单号", align = 2, sort = 1)
	public String getFordernum() {
		return fordernum;
	}

	public void setFordernum(String fordernum) {
		this.fordernum = fordernum;
	}

	@ExcelField(title = "客户名称", align = 2, sort = 2,value="customerBasic.fname")
	public CustomerBasic getCustomerBasic() {
		return customerBasic;
	}

	public void setCustomerBasic(CustomerBasic customerBasic) {
		this.customerBasic = customerBasic;
	}

	@ExcelField(title = "仓库", align = 2, sort = 3,value="warehouse.fname")
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@ExcelField(title = "账目类型", align = 2, sort = 5,value="accountType.ftypename")
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	@ExcelField(title = "结算账户", align = 2, sort = 4,value="clearingAccount.faccountname")
	public ClearingAccount getClearingAccount() {
		return clearingAccount;
	}

	public void setClearingAccount(ClearingAccount clearingAccount) {
		this.clearingAccount = clearingAccount;
	}

	@ExcelField(title = "发货方式", dictType = "shipment_type", align = 2, sort = 6)
	public Integer getFshipmenttype() {
		return fshipmenttype;
	}

	public void setFshipmenttype(Integer fshipmenttype) {
		this.fshipmenttype = fshipmenttype;
	}

	@ExcelField(title = "订单类型", dictType = "sdelas_type", align = 2, sort = 7)
	public Integer getFordertype() {
		return fordertype;
	}

	public void setFordertype(Integer fordertype) {
		this.fordertype = fordertype;
	}

	@ExcelField(title = "订单状态 ", dictType = "sdeals_status", align = 2, sort = 8)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "销售单结束时间", align = 2, sort = 9)
	public Date getFendtime() {
		return fendtime;
	}

	public void setFendtime(Date fendtime) {
		this.fendtime = fendtime;
	}

	@ExcelField(title = "其他费用", align = 2, sort = 11)
	public BigDecimal getFothermoney() {
		return fothermoney;
	}

	public void setFothermoney(BigDecimal fothermoney) {
		this.fothermoney = fothermoney;
	}

	@ExcelField(title = "商品总价", align = 2, sort = 10)
	public BigDecimal getFcountprice() {
		return fcountprice;
	}

	public void setFcountprice(BigDecimal fcountprice) {
		this.fcountprice = fcountprice;
	}

	@ExcelField(title = "抹零", align = 2, sort = 14)
	public BigDecimal getFcutsmall() {
		return fcutsmall;
	}

	public void setFcutsmall(BigDecimal fcutsmall) {
		this.fcutsmall = fcutsmall;
	}

	@ExcelField(title = "订单总额", align = 2, sort = 12)
	public BigDecimal getFordercountprice() {
		return fordercountprice;
	}

	public void setFordercountprice(BigDecimal fordercountprice) {
		this.fordercountprice = fordercountprice;
	}

	@ExcelField(title = "订单形式", dictType = "order_model", align = 2, sort = 18)
	public Integer getFordermodel() {
		return fordermodel;
	}

	public void setFordermodel(Integer fordermodel) {
		this.fordermodel = fordermodel;
	}

//	@ExcelField(title = "商户", align = 2, sort = 20)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
	@ExcelField(title = "门店",align = 2, sort = 19)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
	}
	
//	@ExcelField(title = "是否打印", dictType = "print_status", align = 2, sort = 21)
	public Integer getFprint() {
		return fprint;
	}

	public void setFprint(Integer fprint) {
		this.fprint = fprint;
	}

//	@ExcelField(title = "打印模板", align = 2, sort = 22)
	public SysModelType getSysModelType() {
		return sysModelType;
	}

	public void setSysModelType(SysModelType sysModelType) {
		this.sysModelType = sysModelType;
	}

	@ExcelField(title = "实收款", align = 2, sort = 16)
	public BigDecimal getFproceeds() {
		return fproceeds;
	}

	public void setFproceeds(BigDecimal fproceeds) {
		this.fproceeds = fproceeds;
	}

	@ExcelField(title = "欠款", align = 2, sort = 17)
	public BigDecimal getFdebt() {
		return fdebt;
	}

	public void setFdebt(BigDecimal fdebt) {
		this.fdebt = fdebt;
	}

	@ExcelField(title = "整单折扣", align = 2, sort = 13)
	public String getForderdiscount() {
		return forderdiscount;
	}

	public void setForderdiscount(String forderdiscount) {
		this.forderdiscount = forderdiscount;
	}

	@ExcelField(title = "最终金额", align = 2, sort = 15)
	public BigDecimal getFdiscountprice() {
		return fdiscountprice;
	}

	public void setFdiscountprice(BigDecimal fdiscountprice) {
		this.fdiscountprice = fdiscountprice;
	}

	public List<OrderMarketDetail> getOrderMarketDetailList() {
		return orderMarketDetailList;
	}

	public void setOrderMarketDetailList(List<OrderMarketDetail> orderMarketDetailList) {
		this.orderMarketDetailList = orderMarketDetailList;
	}
}