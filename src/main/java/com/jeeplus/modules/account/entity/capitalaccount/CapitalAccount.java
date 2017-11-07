/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity.capitalaccount;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 资金流水账Entity
 * @author 金圣智
 * @version 2017-06-05
 */
public class CapitalAccount extends DataEntity<CapitalAccount> {
	
	private static final long serialVersionUID = 1L;
	private String foddNumbers;		// 单据编号
	private Date fbusinessHours;		// 业务时间
	private AccountType accountType;		// 账目类型
	private ClearingAccount clearingAccount;		// 结算账户
	private User ftrader;		// 交易人
	private String fincome;		// 收入
	private String fexpenditure;		// 支出
	private String fprofit;		// 盈利
	private String faccountBalance;		// 账号余额
	private String finitialamount;		// 期初金额
	private Integer fexpenditureflag;		// 收入/支出
	private Office fsponsor;          //所属商户
	private Office fstore;            //所属门店
	public CapitalAccount() {
		super();
	}

	public CapitalAccount(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=1)
	public String getFoddNumbers() {
		return foddNumbers;
	}

	public void setFoddNumbers(String foddNumbers) {
		this.foddNumbers = foddNumbers;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="业务时间", align=2, sort=2)
	public Date getFbusinessHours() {
		return fbusinessHours;
	}

	public void setFbusinessHours(Date fbusinessHours) {
		this.fbusinessHours = fbusinessHours;
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
	
	@ExcelField(title="交易人", align=2, sort=5,value="ftrader.loginName")
	public User getFtrader() {
		return ftrader;
	}

	public void setFtrader(User ftrader) {
		this.ftrader = ftrader;
	}
	
	@ExcelField(title="收入", align=2, sort=6)
	public String getFincome() {
		return fincome;
	}

	public void setFincome(String fincome) {
		this.fincome = fincome;
	}
	
	@ExcelField(title="支出", align=2, sort=7)
	public String getFexpenditure() {
		return fexpenditure;
	}

	public void setFexpenditure(String fexpenditure) {
		this.fexpenditure = fexpenditure;
	}
	
	@ExcelField(title="盈利", align=2, sort=8)
	public String getFprofit() {
		return fprofit;
	}

	public void setFprofit(String fprofit) {
		this.fprofit = fprofit;
	}
	
	@ExcelField(title="账号余额", align=2, sort=9)
	public String getFaccountBalance() {
		return faccountBalance;
	}

	public void setFaccountBalance(String faccountBalance) {
		this.faccountBalance = faccountBalance;
	}
	
	@ExcelField(title="期初金额", align=2, sort=10)
	public String getFinitialamount() {
		return finitialamount;
	}

	public void setFinitialamount(String finitialamount) {
		this.finitialamount = finitialamount;
	}
	
	@ExcelField(title="收入/支出", dictType="fund_status", align=2, sort=11)
	public Integer getFexpenditureflag() {
		return fexpenditureflag;
	}

	public void setFexpenditureflag(Integer fexpenditureflag) {
		this.fexpenditureflag = fexpenditureflag;
	}
//	@ExcelField(title="所属商户", align=2, sort=12)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
	@ExcelField(title="所属门店", align=2, sort=12)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
	}
	
	
}