/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.entity.size;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 尺寸设置Entity
 * @author maxiao
 * @version 2017-06-05
 */
public class Size extends DataEntity<Size> {
	
	private static final long serialVersionUID = 1L;
	private String fsizename;		// 尺寸名称
	private Office fsponsorid;		// 所属商户
	private  Office  office;
	private String fsort;		// 排序
	private String fstatus;		// 尺码状态(1:启用;2:未启用)
	
	public Size() {
		super();
	}

	public Size(String id){
		super(id);
	}

	@ExcelField(title="尺寸名称", align=2, sort=1)
	public String getFsizename() {
		return fsizename;
	}

	public void setFsizename(String fsizename) {
		this.fsizename = fsizename;
	}
	
	@ExcelField(title="所属商户", align=2, sort=6)
	public Office getFsponsorid() {
		return fsponsorid;
	}

	public void setFsponsorid(Office fsponsorid) {
		this.fsponsorid = fsponsorid;
	}
	
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@ExcelField(title="排序", align=2, sort=9)
	public String getFsort() {
		return fsort;
	}

	public void setFsort(String fsort) {
		this.fsort = fsort;
	}
	
	@ExcelField(title="尺码状态(1:启用;2:未启用)", dictType="", align=2, sort=10)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
}