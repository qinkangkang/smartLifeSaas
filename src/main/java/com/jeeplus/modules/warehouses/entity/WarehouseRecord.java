/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.entity;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库流水Entity
 * @author hxting
 * @version 2017-07-11
 */
public class WarehouseRecord extends DataEntity<WarehouseRecord> {
	
	private static final long serialVersionUID = 1L;
	private Warehouse warehouse;		// 仓库
	private GoodsSpu goodsSpu;		// 商品SPU
	private GoodsSku goodsSku;		// 商品SKU
	private Date businessTime;		// 业务时间
	private Integer businessType;		// 业务类型
	private String businessorderNumber;		// 业务单号
	private Integer changeNum;		// 改变数量
	private Integer remainingNum;		// 剩余数量
	private Office company;		// 所属商户
	private Office office;		// 所属门店
	private Date beginBusinessTime;		// 开始 业务时间
	private Date endBusinessTime;		// 结束 业务时间
	
	public WarehouseRecord() {
		super();
	}

	public WarehouseRecord(String id){
		super(id);
	}

	@ExcelField(title="仓库", align=2, sort=7)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	@ExcelField(title="商品SPU", align=2, sort=8)
	public GoodsSpu getGoodsSpu() {
		return goodsSpu;
	}

	public void setGoodsSpu(GoodsSpu goodsSpu) {
		this.goodsSpu = goodsSpu;
	}
	
	@ExcelField(title="商品SKU", align=2, sort=9)
	public GoodsSku getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="业务时间", align=2, sort=10)
	public Date getBusinessTime() {
		return businessTime;
	}

	public void setBusinessTime(Date businessTime) {
		this.businessTime = businessTime;
	}
	
	@ExcelField(title="业务类型", align=2, sort=11)
	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	
	@ExcelField(title="业务单号", align=2, sort=12)
	public String getBusinessorderNumber() {
		return businessorderNumber;
	}

	public void setBusinessorderNumber(String businessorderNumber) {
		this.businessorderNumber = businessorderNumber;
	}
	
	@ExcelField(title="改变数量", align=2, sort=13)
	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}
	
	@ExcelField(title="剩余数量", align=2, sort=14)
	public Integer getRemainingNum() {
		return remainingNum;
	}

	public void setRemainingNum(Integer remainingNum) {
		this.remainingNum = remainingNum;
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
	
	public Date getBeginBusinessTime() {
		return beginBusinessTime;
	}

	public void setBeginBusinessTime(Date beginBusinessTime) {
		this.beginBusinessTime = beginBusinessTime;
	}
	
	public Date getEndBusinessTime() {
		return endBusinessTime;
	}

	public void setEndBusinessTime(Date endBusinessTime) {
		this.endBusinessTime = endBusinessTime;
	}
	
	/**
	 * 业务类型（0：采购单；1：销售单；2：采购退货单；3：销售退货单；4：调拨单；5：盘点单 ；）
	 */
	public static final Integer BUSINESS_TYPE_PROCUREMENT = 0;
	public static final Integer BUSINESS_TYPE_MARKET = 1;
	public static final Integer BUSINESS_TYPE_PROCUREMENTCHA = 2;
	public static final Integer BUSINESS_TYPE_MARKETCHA = 3;
	public static final Integer BUSINESS_TYPE_TRANSFER = 4;
	public static final Integer BUSINESS_TYPE_CHECK = 5;
		
}