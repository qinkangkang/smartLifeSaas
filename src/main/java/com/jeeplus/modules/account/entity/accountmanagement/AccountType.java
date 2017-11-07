/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity.accountmanagement;

import java.util.Date;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 账目类型管理Entity
 * @author 金圣智
 * @version 2017-06-05
 */
public class AccountType extends DataEntity<AccountType> {
	
	private static final long serialVersionUID = 1L;
	private String ftypename;		// 类型名称
	private Integer forder;		// 排序
	private Integer fstatus;		// 开启状态
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	private Office fsponsor;		//所属商户
	private Office fstore;			//所属门店
	
	public AccountType() {
		super();
	}

	public AccountType(String id){
		super(id);
	}

	@ExcelField(title="类型名称", align=2, sort=1)
	public String getFtypename() {
		return ftypename;
	}

	public void setFtypename(String ftypename) {
		this.ftypename = ftypename;
	}
	
	@ExcelField(title="排序", align=2, sort=2)
	public Integer getForder() {
		return forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}
	
	@ExcelField(title="开启状态", dictType="sys_status", align=2, sort=9)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	@ExcelField(title="所属商户", align=2, sort=10)
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