/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;

import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferOrder;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库调拨Entity
 * 
 * @author hxting
 * @version 2017-06-12
 */
public class WarehouseTransferGoods extends DataEntity<WarehouseTransferGoods> {

	private static final long serialVersionUID = 1L;
	private Integer transferNum; // 调拨数量
	private GoodsSpu goodsSpu; // 商品SPU
	private GoodsSku goodsSku; // 商品SKU
	private WarehouseTransferOrder transferOrder; // 调拨单 父类
	private Integer ftransferOutBeforeNum; // 调出仓库当前数量
	private Integer ftransferInBeforeNum; // 调入仓库当前数量
	private WarehouseGoodsInfo warehouseOutGoods; // 调出仓库商品信息
	private WarehouseGoodsInfo warehouseInGoods; // 调入仓库商品信息

	public WarehouseTransferGoods() {
		super();
	}

	public WarehouseTransferGoods(String id) {
		super(id);
	}

	public WarehouseTransferGoods(WarehouseTransferOrder transferOrder) {
		this.transferOrder = transferOrder;
	}

	@NotNull(message = "调拨数量不能为空")
	@ExcelField(title = "调拨数量", align = 2, sort = 7)
	public Integer getTransferNum() {
		return transferNum;
	}

	public void setTransferNum(Integer transferNum) {
		this.transferNum = transferNum;
	}

	@NotNull(message = "商品SPU不能为空")
	@ExcelField(title = "商品SPU", align = 2, sort = 8)
	public GoodsSpu getGoodsSpu() {
		return goodsSpu;
	}

	public void setGoodsSpu(GoodsSpu goodsSpu) {
		this.goodsSpu = goodsSpu;
	}

	@NotNull(message = "商品SKU不能为空")
	@ExcelField(title = "商品SKU", align = 2, sort = 9)
	public GoodsSku getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
	}

	@NotNull(message = "调拨单不能为空")
	public WarehouseTransferOrder getTransferOrder() {
		return transferOrder;
	}

	public void setTransferOrder(WarehouseTransferOrder transferOrder) {
		this.transferOrder = transferOrder;
	}

	@ExcelField(title = "调出仓库当前数量", align = 2, sort = 11)
	public Integer getFtransferOutBeforeNum() {
		return ftransferOutBeforeNum;
	}

	public void setFtransferOutBeforeNum(Integer ftransferOutBeforeNum) {
		this.ftransferOutBeforeNum = ftransferOutBeforeNum;
	}

	@ExcelField(title = "调入仓库当前数量", align = 2, sort = 12)
	public Integer getFtransferInBeforeNum() {
		return ftransferInBeforeNum;
	}

	public void setFtransferInBeforeNum(Integer ftransferInBeforeNum) {
		this.ftransferInBeforeNum = ftransferInBeforeNum;
	}

	public WarehouseGoodsInfo getWarehouseOutGoods() {
		return warehouseOutGoods;
	}

	public void setWarehouseOutGoods(WarehouseGoodsInfo warehouseOutGoods) {
		this.warehouseOutGoods = warehouseOutGoods;
	}

	public WarehouseGoodsInfo getWarehouseInGoods() {
		return warehouseInGoods;
	}

	public void setWarehouseInGoods(WarehouseGoodsInfo warehouseInGoods) {
		this.warehouseInGoods = warehouseInGoods;
	}
	
}