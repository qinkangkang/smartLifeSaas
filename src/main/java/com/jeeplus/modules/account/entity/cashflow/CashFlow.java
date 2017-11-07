/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity.cashflow;

import javax.validation.constraints.NotNull;
import java.util.Date;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 收款流水(线上)Entity
 * @author 金圣智
 * @version 2017-06-02
 */
public class CashFlow extends DataEntity<CashFlow> {
	
	private static final long serialVersionUID = 1L;
	private String foddnumbers;		// 单据编号
	private String ftransactionnumbers;		// 交易单号
	private Integer ftransactiontype;		// 交易类型
	private String ftransactionaccount;		// 交易金额
	private Integer fpaytype;		// 支付类型
	private Integer fpaystatus;		// 交易状态
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Office fsponsor; 		//所属商户
	private Office fstore;      	//所属门店
	public CashFlow() {
		super();
	}

	public CashFlow(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=1)
	public String getFoddnumbers() {
		return foddnumbers;
	}

	public void setFoddnumbers(String foddnumbers) {
		this.foddnumbers = foddnumbers;
	}
	
	@ExcelField(title="交易单号", align=2, sort=2)
	public String getFtransactionnumbers() {
		return ftransactionnumbers;
	}

	public void setFtransactionnumbers(String ftransactionnumbers) {
		this.ftransactionnumbers = ftransactionnumbers;
	}
	
	@ExcelField(title="交易类型", dictType="transaction_type", align=2, sort=3)
	public Integer getFtransactiontype() {
		return ftransactiontype;
	}

	public void setFtransactiontype(Integer ftransactiontype) {
		this.ftransactiontype = ftransactiontype;
	}
	
	@ExcelField(title="交易金额", align=2, sort=4)
	public String getFtransactionaccount() {
		return ftransactionaccount;
	}

	public void setFtransactionaccount(String ftransactionaccount) {
		this.ftransactionaccount = ftransactionaccount;
	}
	
	@ExcelField(title="支付类型", dictType="pay_type", align=2, sort=5)
	public Integer getFpaytype() {
		return fpaytype;
	}

	public void setFpaytype(Integer fpaytype) {
		this.fpaytype = fpaytype;
	}
	
	@NotNull(message="交易状态不能为空")
	@ExcelField(title="交易状态", dictType="transaction_status", align=2, sort=6)
	public Integer getFpaystatus() {
		return fpaystatus;
	}

	public void setFpaystatus(Integer fpaystatus) {
		this.fpaystatus = fpaystatus;
	}
	
	
	@ExcelField(title="所属商户", align=2, sort=7)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
	@ExcelField(title="所属门店", align=2, sort=8)
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