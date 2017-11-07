/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.entity;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库Entity
 * @author Hxting
 * @version 2017-07-04
 */
public class Warehouse extends DataEntity<Warehouse> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 仓库名
	private String fstatus;		// 仓库状态（1：启用；2：未启用）
	private Office company;		// 所属商户
	private Office office;		// 所属门店
	private User chargePerson;		// 仓库负责人
	
	public Warehouse() {
		super();
	}

	public Warehouse(String id){
		super(id);
	}

	@ExcelField(title="仓库名", align=2, sort=1)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="仓库状态（1：启用；2：未启用）", dictType="sys_status", align=2, sort=8)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
	@ExcelField(title="所属商户", align=2, sort=9)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@ExcelField(title="所属门店", align=2, sort=10)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="仓库负责人", align=2, sort=11)
	public User getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(User chargePerson) {
		this.chargePerson = chargePerson;
	}
	
}