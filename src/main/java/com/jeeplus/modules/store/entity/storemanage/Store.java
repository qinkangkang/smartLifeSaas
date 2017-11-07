/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.entity.storemanage;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 门店管理Entity
 * @author 金圣智
 * @version 2017-06-23
 */
public class Store extends DataEntity<Store> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 门店名称
	private String faddress;		// 门店地址
	private String fphone;		// 门店电话
	private Integer fstatus;		// 门店状态
	
	public Store() {
		super();
	}

	public Store(String id){
		super(id);
	}

	@ExcelField(title="门店名称", align=2, sort=1)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="门店地址", align=2, sort=2)
	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	
	@ExcelField(title="门店电话", align=2, sort=3)
	public String getFphone() {
		return fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}
	
	@ExcelField(title="门店状态", dictType="sys_status", align=2, sort=10)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
}