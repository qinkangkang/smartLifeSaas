/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.entity.category;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 客户分类Entity
 * @author diqiang
 * @version 2017-06-06
 */
public class CustomerCate extends DataEntity<CustomerCate> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 分类名称
	private String fdiscount;		// 折扣
	private String fstatus;		// 客户分类状态（0：启用；999：失效）
	private Office fsponsor;	//所属商户
	private Office fstore;  	//所属门店
	public CustomerCate() {
		super();
	}

	public CustomerCate(String id){
		super(id);
	}

	@ExcelField(title="分类名称", align=2, sort=7)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="折扣", align=2, sort=8)
	public String getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(String fdiscount) {
		this.fdiscount = fdiscount;
	}
	
	@ExcelField(title="客户分类状态（0：启用；999：失效）", dictType="select", align=2, sort=9)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
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
	
	
	
}