/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity.supplieraccount;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 供应商对账Entity
 * @author 金圣智
 * @version 2017-06-06
 */
public class SupplierAccount extends DataEntity<SupplierAccount> {
	
	private static final long serialVersionUID = 1L;
	private SupplierBasic supplierBasic;		// 供应商名称
	private String foddNumbers;		// 单据编号
	private AccountType accountType;		// 账目类型
	private ClearingAccount clearingAccount;		// 结算账户
	private Date fbusinessHours;		// 业务时间
	private String famountPay;		// 应付金额
	private String fpayAmount;		// 实付金额
	private String fsolehandlingAmount;		// 本单应付余额
	private String fpreferentialAmount;		// 优惠金额
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Office fsponsor;       //所属商户
	private Office fstore;			//所属门店
	public SupplierAccount() {
		super();
	}

	public SupplierAccount(String id){
		super(id);
	}

	@ExcelField(title="供应商名称", align=2, sort=1,value="supplierBasic.fname")
	public SupplierBasic getSupplierBasic() {
		return supplierBasic;
	}

	public void setSupplierBasic(SupplierBasic supplierBasic) {
		this.supplierBasic = supplierBasic;
	}
	
	@ExcelField(title="单据编号", align=2, sort=2)
	public String getFoddNumbers() {
		return foddNumbers;
	}

	public void setFoddNumbers(String foddNumbers) {
		this.foddNumbers = foddNumbers;
	}
	
	@ExcelField(title="账目类型", align=2, sort=3,value="accountType.ftypename")
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	@ExcelField(title="结算账户", align=2, sort=4,value="clearingAccount.faccountname")
	public ClearingAccount getClearingAccount() {
		return clearingAccount;
	}

	public void setClearingAccount(ClearingAccount clearingAccount) {
		this.clearingAccount = clearingAccount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="业务时间", align=2, sort=5)
	public Date getFbusinessHours() {
		return fbusinessHours;
	}

	public void setFbusinessHours(Date fbusinessHours) {
		this.fbusinessHours = fbusinessHours;
	}
	
	@ExcelField(title="应付金额", align=2, sort=6)
	public String getFamountPay() {
		return famountPay;
	}

	public void setFamountPay(String famountPay) {
		this.famountPay = famountPay;
	}
	
	@ExcelField(title="实付金额", align=2, sort=7)
	public String getFpayAmount() {
		return fpayAmount;
	}

	public void setFpayAmount(String fpayAmount) {
		this.fpayAmount = fpayAmount;
	}
	
	@ExcelField(title="本单应付余额", align=2, sort=8)
	public String getFsolehandlingAmount() {
		return fsolehandlingAmount;
	}

	public void setFsolehandlingAmount(String fsolehandlingAmount) {
		this.fsolehandlingAmount = fsolehandlingAmount;
	}
	
	@ExcelField(title="优惠金额", align=2, sort=9)
	public String getFpreferentialAmount() {
		return fpreferentialAmount;
	}

	public void setFpreferentialAmount(String fpreferentialAmount) {
		this.fpreferentialAmount = fpreferentialAmount;
	}
//	@ExcelField(title="所属商户", align=2, sort=10)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
	@ExcelField(title="所属门店", align=2, sort=10)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
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