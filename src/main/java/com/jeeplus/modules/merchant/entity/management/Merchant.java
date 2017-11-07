/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.merchant.entity.management;

import com.jeeplus.modules.sys.entity.Area;
import java.util.Date;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商户管理Entity
 * @author diqiang
 * @version 2017-06-23
 */
public class Merchant extends DataEntity<Merchant> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 商户名称
	private Area area;		// 所属区域
	private String code;		// 区域编号
	private String fmaster;		// 负责人
	private String cellphone;		// 手机
	private String femail;		// 邮箱
	private Integer fsort;		// 排序
	private String fstatus;		// 是否启用（1：启用，2：未启用）
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Date beginUpdateDate;		// 开始 更新时间
	private Date endUpdateDate;		// 结束 更新时间
	
	public Merchant() {
		super();
	}

	public Merchant(String id){
		super(id);
	}

	@ExcelField(title="商户名称", align=2, sort=1)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="所属区域", align=2, sort=2)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@ExcelField(title="区域编号", align=2, sort=3)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="负责人", align=2, sort=4)
	public String getFmaster() {
		return fmaster;
	}

	public void setFmaster(String fmaster) {
		this.fmaster = fmaster;
	}
	
	@ExcelField(title="手机", align=2, sort=5)
	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	
	@ExcelField(title="邮箱", align=2, sort=6)
	public String getFemail() {
		return femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}
	
	@ExcelField(title="排序", align=2, sort=7)
	public Integer getFsort() {
		return fsort;
	}

	public void setFsort(Integer fsort) {
		this.fsort = fsort;
	}
	
	@ExcelField(title="是否启用（1：启用，2：未启用）", align=2, sort=8)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
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
		
	public Date getBeginUpdateDate() {
		return beginUpdateDate;
	}

	public void setBeginUpdateDate(Date beginUpdateDate) {
		this.beginUpdateDate = beginUpdateDate;
	}
	
	public Date getEndUpdateDate() {
		return endUpdateDate;
	}

	public void setEndUpdateDate(Date endUpdateDate) {
		this.endUpdateDate = endUpdateDate;
	}
		
}