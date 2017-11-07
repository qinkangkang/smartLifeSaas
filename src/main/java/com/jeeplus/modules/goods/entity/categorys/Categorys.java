/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.entity.categorys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.TreeEntity;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 商品的分类Entity
 * @author maxiao
 * @version 2017-06-07
 */
public class Categorys extends TreeEntity<Categorys> {
	
	private static final long serialVersionUID = 1L;
	private Office fsponsorId;		// 所属商户
	private Office office;
	private String fstatus;		// 分类状态(1:启用;2:未启用)
	private Categorys parent;		// 父级分类
	private String parentIds;		// 所有父级编号
	private String name;		// 名称
	private Integer sort;		// 排序
	
	public Categorys() {
		super();
	}

	public Categorys(String id){
		super(id);
	}

	public Office getFsponsorId() {
		return fsponsorId;
	}

	public void setFsponsorId(Office company) {
		this.fsponsorId = company;
	}
	
	public String getFstatus() {
		return fstatus;
	}

	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
	@JsonBackReference
	@NotNull(message="父级分类不能为空")
	public Categorys getParent() {
		return parent;
	}

	public void setParent(Categorys parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}