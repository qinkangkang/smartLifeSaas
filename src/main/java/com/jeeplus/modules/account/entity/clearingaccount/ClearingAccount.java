/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity.clearingaccount;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 结算账户Entity
 * @author 金圣智
 * @version 2017-06-01
 */
public class ClearingAccount extends DataEntity<ClearingAccount> {
	
	private static final long serialVersionUID = 1L;
	private String faccountname;		// 账户名称
	private String faccountnumber;		// 账号
	private String faccountholder;		// 开户人
	private Integer faccounttype;		// 账户类型
	private String fbalance;			// 当前余额
	private Office fsponsor;			//所属商户
	private Office fstore;		// 所属门店
	private Integer fstatus;		// 启用状态
	
	public ClearingAccount() {
		super();
	}

	public ClearingAccount(String id){
		super(id);
	}

	@ExcelField(title="账户名称", align=2, sort=1)
	public String getFaccountname() {
		return faccountname;
	}

	public void setFaccountname(String faccountname) {
		this.faccountname = faccountname;
	}
	
	@ExcelField(title="账号", align=2, sort=2)
	public String getFaccountnumber() {
		return faccountnumber;
	}

	public void setFaccountnumber(String faccountnumber) {
		this.faccountnumber = faccountnumber;
	}
	
	@ExcelField(title="开户人", align=2, sort=3)
	public String getFaccountholder() {
		return faccountholder;
	}

	public void setFaccountholder(String faccountholder) {
		this.faccountholder = faccountholder;
	}
	
	@NotNull(message="账户类型不能为空")
	@ExcelField(title="账户类型", dictType="faccountt_ype", align=2, sort=4)
	public Integer getFaccounttype() {
		return faccounttype;
	}

	public void setFaccounttype(Integer faccounttype) {
		this.faccounttype = faccounttype;
	}
	
	@ExcelField(title="当前余额", align=2, sort=5)
	public String getFbalance() {
		return fbalance;
	}
	
	public void setFbalance(String fbalance) {
		this.fbalance = fbalance;
	}
	@ExcelField(title="所属商户", align=2, sort=6)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}

	@ExcelField(title="所属门店", align=2, sort=7)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
	}
	@ExcelField(title="启用状态", dictType="sys_status", align=2, sort=13)
	public Integer getFstatus() {
		return fstatus;
	}
	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
}