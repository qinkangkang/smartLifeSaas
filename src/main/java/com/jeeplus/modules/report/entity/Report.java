/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 物流信息Entity
 * @author qkk
 * @version 2017-06-01
 */
public class Report extends DataEntity<Report> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 物流名称
	private String fwebsite;		// 网址
	private String fexpresscompany;		// 物流公司
	private String frangedesc;		// 运费描述
	private String fsponsorid;		// 商户
	private String status;		// 是否启用
	
	public Report() {
		super();
	}

	public Report(String id){
		super(id);
	}

	@ExcelField(title="物流名称", align=2, sort=6)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="网址", align=2, sort=7)
	public String getFwebsite() {
		return fwebsite;
	}

	public void setFwebsite(String fwebsite) {
		this.fwebsite = fwebsite;
	}
	
	@ExcelField(title="物流公司", dictType="express_company", align=2, sort=8)
	public String getFexpresscompany() {
		return fexpresscompany;
	}

	public void setFexpresscompany(String fexpresscompany) {
		this.fexpresscompany = fexpresscompany;
	}
	
	@ExcelField(title="运费描述", align=2, sort=9)
	public String getFrangedesc() {
		return frangedesc;
	}

	public void setFrangedesc(String frangedesc) {
		this.frangedesc = frangedesc;
	}
	
	@ExcelField(title="商户", align=2, sort=10)
	public String getFsponsorid() {
		return fsponsorid;
	}

	public void setFsponsorid(String fsponsorid) {
		this.fsponsorid = fsponsorid;
	}
	
	@ExcelField(title="是否启用", dictType="sys_status", align=2, sort=11)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}