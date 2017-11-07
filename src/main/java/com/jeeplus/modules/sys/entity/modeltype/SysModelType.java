/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity.modeltype;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 打印模板Entity
 * @author diqiang
 * @version 2017-06-08
 */
public class SysModelType extends DataEntity<SysModelType> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 模板名称
	private String furl;		// 模板路径
	private Integer fstatus;		// 状态（1：启用，2：禁用）
	
	public SysModelType() {
		super();
	}

	public SysModelType(String id){
		super(id);
	}

	@ExcelField(title="模板名称", align=2, sort=1)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="模板路径", align=2, sort=2)
	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}
	
	@ExcelField(title="状态（1：启用，2：禁用）", align=2, sort=3)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
}