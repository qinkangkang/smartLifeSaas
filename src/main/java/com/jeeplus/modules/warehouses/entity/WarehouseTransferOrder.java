/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.entity;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.Office;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库调拨Entity
 * @author hxting
 * @version 2017-07-11
 */
public class WarehouseTransferOrder extends DataEntity<WarehouseTransferOrder> {
	
	private static final long serialVersionUID = 1L;
	private String ftransferOrder;		// 调拨单号
	private Warehouse warehouseOut;		// 调出仓库
	private Warehouse fwarehouseIn;		// 调入仓库
	private Date ftransferDate;		// 调拨日期
	private String fstatus;		// 状态（0：草稿；1：已调拨；2：已撤销）
	private Office company;		// 所属商户
	private Office office;		// 所属门店
	private Date beginFtransferDate;		// 开始 调拨日期
	private Date endFtransferDate;		// 结束 调拨日期
	private List<WarehouseTransferGoods> warehouseTransferGoodsList = Lists.newArrayList();		// 子表列表
	
	public WarehouseTransferOrder() {
		super();
	}

	public WarehouseTransferOrder(String id){
		super(id);
	}

	@ExcelField(title="调拨单号", align=2, sort=1)
	public String getFtransferOrder() {
		return ftransferOrder;
	}

	public void setFtransferOrder(String ftransferOrder) {
		this.ftransferOrder = ftransferOrder;
	}
	
	@NotNull(message="调出仓库不能为空")
	@ExcelField(title="调出仓库", align=2, sort=2)
	public Warehouse getWarehouseOut() {
		return warehouseOut;
	}

	public void setWarehouseOut(Warehouse warehouseOut) {
		this.warehouseOut = warehouseOut;
	}
	
	@NotNull(message="调入仓库不能为空")
	@ExcelField(title="调入仓库", align=2, sort=3)
	public Warehouse getFwarehouseIn() {
		return fwarehouseIn;
	}

	public void setFwarehouseIn(Warehouse fwarehouseIn) {
		this.fwarehouseIn = fwarehouseIn;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="调拨日期不能为空")
	@ExcelField(title="调拨日期", align=2, sort=4)
	public Date getFtransferDate() {
		return ftransferDate;
	}

	public void setFtransferDate(Date ftransferDate) {
		this.ftransferDate = ftransferDate;
	}
	
	@ExcelField(title="状态（0：草稿；1：已调拨；2：已撤销）", dictType="transferorder_status", align=2, sort=5)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
	@ExcelField(title="所属商户", align=2, sort=12)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@ExcelField(title="所属门店", align=2, sort=13)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public Date getBeginFtransferDate() {
		return beginFtransferDate;
	}

	public void setBeginFtransferDate(Date beginFtransferDate) {
		this.beginFtransferDate = beginFtransferDate;
	}
	
	public Date getEndFtransferDate() {
		return endFtransferDate;
	}

	public void setEndFtransferDate(Date endFtransferDate) {
		this.endFtransferDate = endFtransferDate;
	}
		
	public List<WarehouseTransferGoods> getWarehouseTransferGoodsList() {
		return warehouseTransferGoodsList;
	}

	public void setWarehouseTransferGoodsList(List<WarehouseTransferGoods> warehouseTransferGoodsList) {
		this.warehouseTransferGoodsList = warehouseTransferGoodsList;
	}
	
	/**
	 * 调拨状态（0：草稿；1：调拨；2：撤销；）
	 */
	public static final String TRANSFER_STATUS_DRAFT = "0"; //草稿
	public static final String TRANSFER_STATUS_TRANSFER = "1"; //调拨
	public static final String TRANSFER_STATUS_BACKOUT = "2";  //撤销
}