/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.entity;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import javax.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库盘点Entity
 * @author hxting
 * @version 2017-06-19
 */
public class WarehouseCheckOrder extends DataEntity<WarehouseCheckOrder> {
	
	private static final long serialVersionUID = 1L;
	private String fcheckOrder;		// 盘点单号
	private Warehouse warehouse;		// 仓库
	private Integer checkTotalNum;		// 盘点总数
	private Integer profitLossTotalNum;		// 盈亏总数
	private Double profitLossTotalMoney;		// 盈亏总金额
	private Integer scanningTimes;		// 扫描次数
	private Date checkDate;		// 盘点日期
	private String fstatus;		// 状态（0：草稿；1：已盘点；2：已撤销）
	private Office company;		// 所属商户
	private Office office;		// 所属门店
	private Date beginCheckDate;		// 开始 盘点日期
	private Date endCheckDate;		// 结束 盘点日期
	private List<WarehouseCheckGoods> warehouseCheckGoodsList = Lists.newArrayList();		// 子表列表
	
	public WarehouseCheckOrder() {
		super();
	}

	public WarehouseCheckOrder(String id){
		super(id);
	}

	@ExcelField(title="盘点单号", align=2, sort=7)
	public String getFcheckOrder() {
		return fcheckOrder;
	}

	public void setFcheckOrder(String fcheckOrder) {
		this.fcheckOrder = fcheckOrder;
	}
	
	@NotNull(message="仓库不能为空")
	@ExcelField(title="仓库", align=2, sort=8)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	@ExcelField(title="盘点总数", align=2, sort=9)
	public Integer getCheckTotalNum() {
		return checkTotalNum;
	}

	public void setCheckTotalNum(Integer checkTotalNum) {
		this.checkTotalNum = checkTotalNum;
	}
	
	@ExcelField(title="盈亏总数", align=2, sort=10)
	public Integer getProfitLossTotalNum() {
		return profitLossTotalNum;
	}

	public void setProfitLossTotalNum(Integer profitLossTotalNum) {
		this.profitLossTotalNum = profitLossTotalNum;
	}
	
	@ExcelField(title="盈亏总金额", align=2, sort=11)
	public Double getProfitLossTotalMoney() {
		return profitLossTotalMoney;
	}

	public void setProfitLossTotalMoney(Double profitLossTotalMoney) {
		this.profitLossTotalMoney = profitLossTotalMoney;
	}
	
	@ExcelField(title="扫描次数", align=2, sort=12)
	public Integer getScanningTimes() {
		return scanningTimes;
	}

	public void setScanningTimes(Integer scanningTimes) {
		this.scanningTimes = scanningTimes;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="盘点日期不能为空")
	@ExcelField(title="盘点日期", align=2, sort=13)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="状态（0：草稿；1：已盘点；2：已撤销）", dictType="transferorder_status", align=2, sort=14)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
	@ExcelField(title="所属商户", align=2, sort=15)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@ExcelField(title="所属门店", align=2, sort=16)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public Date getBeginCheckDate() {
		return beginCheckDate;
	}

	public void setBeginCheckDate(Date beginCheckDate) {
		this.beginCheckDate = beginCheckDate;
	}
	
	public Date getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(Date endCheckDate) {
		this.endCheckDate = endCheckDate;
	}
		
	public List<WarehouseCheckGoods> getWarehouseCheckGoodsList() {
		return warehouseCheckGoodsList;
	}

	public void setWarehouseCheckGoodsList(List<WarehouseCheckGoods> warehouseCheckGoodsList) {
		this.warehouseCheckGoodsList = warehouseCheckGoodsList;
	}
	
	/**
	 * 盘点标记（0：草稿；1：盘点；）
	 */
	public static final String CHECK_STATUS_DRAFT = "0";
	public static final String CHECK_STATUS_CHECK = "1";


	
	
	
}