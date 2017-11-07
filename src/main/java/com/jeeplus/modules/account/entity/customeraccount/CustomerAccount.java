/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity.customeraccount;

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 客户对账单Entity
 * @author 金圣智
 * @version 2017-06-06
 */
public class CustomerAccount extends DataEntity<CustomerAccount> {
	
	private static final long serialVersionUID = 1L;
	private CustomerBasic customerBasic;		// 客户名称
	private CustomerCate customerCate;		// 客户分类
	private String foddNumbers;		// 单据编号
	private AccountType accountType;		// 账目类型
	private ClearingAccount clearingAccount;		// 结算账户
	private Date fbusinessHours;		// 业务时间
	private String famountReceivable;		// 应收金额
	private String fpaidAmount;		// 实收金额
	private String fresidualAmount;		// 本单应收余额
	private String fpreferentialAmount;		// 优惠金额
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Office fsponsor;		//所属商户
	private Office fstore;			//所属门店
	public CustomerAccount() {
		super();
	}

	public CustomerAccount(String id){
		super(id);
	}

	@ExcelField(title="客户名称", align=2, sort=1,value="customerBasic.fname")
	public CustomerBasic getCustomerBasic() {
		return customerBasic;
	}

	public void setCustomerBasic(CustomerBasic customerBasic) {
		this.customerBasic = customerBasic;
	}
	
	@ExcelField(title="客户分类", align=2, sort=2,value="customerCate.fname")
	public CustomerCate getCustomerCate() {
		return customerCate;
	}

	public void setCustomerCate(CustomerCate customerCate) {
		this.customerCate = customerCate;
	}
	
	@ExcelField(title="单据编号", align=2, sort=3)
	public String getFoddNumbers() {
		return foddNumbers;
	}

	public void setFoddNumbers(String foddNumbers) {
		this.foddNumbers = foddNumbers;
	}
	
	@ExcelField(title="账目类型", align=2, sort=4,value="accountType.ftypename")
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	@ExcelField(title="结算账户", align=2, sort=5,value="clearingAccount.faccountname")
	public ClearingAccount getClearingAccount() {
		return clearingAccount;
	}

	public void setClearingAccount(ClearingAccount clearingAccount) {
		this.clearingAccount = clearingAccount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="业务时间", align=2, sort=6)
	public Date getFbusinessHours() {
		return fbusinessHours;
	}

	public void setFbusinessHours(Date fbusinessHours) {
		this.fbusinessHours = fbusinessHours;
	}
	
	@ExcelField(title="应收金额", align=2, sort=7)
	public String getFamountReceivable() {
		return famountReceivable;
	}

	public void setFamountReceivable(String famountReceivable) {
		this.famountReceivable = famountReceivable;
	}
	
	@ExcelField(title="实收金额", align=2, sort=8)
	public String getFpaidAmount() {
		return fpaidAmount;
	}

	public void setFpaidAmount(String fpaidAmount) {
		this.fpaidAmount = fpaidAmount;
	}
	
	@ExcelField(title="本单应收余额", align=2, sort=9)
	public String getFresidualAmount() {
		return fresidualAmount;
	}

	public void setFresidualAmount(String fresidualAmount) {
		this.fresidualAmount = fresidualAmount;
	}
	
	@ExcelField(title="优惠金额", align=2, sort=10)
	public String getFpreferentialAmount() {
		return fpreferentialAmount;
	}
//	@ExcelField(title="所属商户", align=2, sort=11)
	public Office getFsponsor() {
		return fsponsor;
	}
	
	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
	@ExcelField(title="所属门店", align=2, sort=11)
	public Office getFstore() {
		return fstore;
	}
	
	public void setFstore(Office fstore) {
		this.fstore = fstore;
	}

	public void setFpreferentialAmount(String fpreferentialAmount) {
		this.fpreferentialAmount = fpreferentialAmount;
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
		
}