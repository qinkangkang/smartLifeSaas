/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.entity.brand;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 商品品牌Entity
 * @author maxiao
 * @version 2017-06-05
 */
public class Brand extends DataEntity<Brand> {
	
	private static final long serialVersionUID = 1L;
	private String fbrandname;		// 品牌名称
	private Integer fsort;		// 排序
	private Office fsponsorId;		// 所属商户
	private Office office;			//所属机构
	private String description;		// 描述信息
	private String fstatus;		// 商品状态(1:启动,2:未启用)
	
	public Brand() {
		super();
	}

	public Brand(String id){
		super(id);
	}

	@ExcelField(title="品牌名称", align=2, sort=1)
	public String getFbrandname() {
		return fbrandname;
	}

	public void setFbrandname(String fbrandname) {
		this.fbrandname = fbrandname;
	}
	
	@ExcelField(title="排序", align=2, sort=3)
	public Integer getFsort() {
		return fsort;
	}

	public void setFsort(Integer fsort) {
		this.fsort = fsort;
	}
	
	@ExcelField(title="所属商户", align=2, sort=5)
	public Office getFsponsorId() {
		return fsponsorId;
	}

	public void setFsponsorId(Office fsponsorId) {
		this.fsponsorId = fsponsorId;
	}
	
	
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@ExcelField(title="描述信息", align=2, sort=8)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ExcelField(title="商品状态(1:启动,2:未启用)", dictType="", align=2, sort=10)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
}