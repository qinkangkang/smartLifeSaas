/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.entity;

import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckOrder;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库盘点Entity
 * @author hxting
 * @version 2017-06-19
 */
public class WarehouseCheckGoods extends DataEntity<WarehouseCheckGoods> {
	
	private static final long serialVersionUID = 1L;
	private GoodsSpu goodsSpu;		// 商品SPU
	private GoodsSku goodsSku;		// 商品SKU
	private String goodsBrand;		//商品品牌
	private String goodsCategory;		//商品品牌
	private String goodsUint;   //商品单位
	private WarehouseCheckOrder checkOrder;		// 盘点单 父类
	private Integer checkNum;		// 盘点数量
	private Integer checkBeforeNum;		// 盘点前数量
	private Integer profitLossNum;		// 盈亏数量
	private Double profitLossMoney;		// 盈亏金额
	private WarehouseGoodsInfo checkwarehouseGoods; //盘点仓库商品
	
	public WarehouseCheckGoods() {
		super();
	}

	public WarehouseCheckGoods(String id){
		super(id);
	}

	public WarehouseCheckGoods(WarehouseCheckOrder checkOrder){
		this.checkOrder = checkOrder;
	}

	@ExcelField(title="商品名称", align=2, sort=7, value="goodsSpu.fgoodsname")
	public GoodsSpu getGoodsSpu() {
		return goodsSpu;
	}

	public void setGoodsSpu(GoodsSpu goodsSpu) {
		this.goodsSpu = goodsSpu;
	}
	
	@ExcelField(title="商品货号", align=2, sort=8, value="goodsSku.fgoodsnumber")
	public GoodsSku getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
	}
	
	
	@ExcelField(title="商品品牌", align=2, sort=9, value="goodsSpu.brand.fbrandname")
	public String getGoodsBrand() {
		return goodsBrand;
	}
	
	@ExcelField(title="商品分类", align=2, sort=10, value="goodsSpu.categorys.name")
	public String getGoodsCategory() {
		return goodsCategory;
	}

	@ExcelField(title="商品单位", align=2, sort=11, value="goodsSpu.goodsUnit.funitname")
	public String getGoodsUint() {
		return goodsUint;
	}
	
	public WarehouseCheckOrder getCheckOrder() {
		return checkOrder;
	}

	public void setCheckOrder(WarehouseCheckOrder checkOrder) {
		this.checkOrder = checkOrder;
	}
	
	@ExcelField(title="盘点数量", align=2, sort=12)
	public Integer getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(Integer checkNum) {
		this.checkNum = checkNum;
	}
	
	@ExcelField(title="盘点前数量", align=2, sort=13)
	public Integer getCheckBeforeNum() {
		return checkBeforeNum;
	}

	public void setCheckBeforeNum(Integer checkBeforeNum) {
		this.checkBeforeNum = checkBeforeNum;
	}
	
	@ExcelField(title="盈亏数量", align=2, sort=14)
	public Integer getProfitLossNum() {
		return profitLossNum;
	}

	public void setProfitLossNum(Integer profitLossNum) {
		this.profitLossNum = profitLossNum;
	}
	
	public Double getProfitLossMoney() {
		return profitLossMoney;
	}

	public void setProfitLossMoney(Double profitLossMoney) {
		this.profitLossMoney = profitLossMoney;
	}

	public WarehouseGoodsInfo getCheckwarehouseGoods() {
		return checkwarehouseGoods;
	}

	public void setCheckwarehouseGoods(WarehouseGoodsInfo checkwarehouseGoods) {
		this.checkwarehouseGoods = checkwarehouseGoods;
	}


	
	
	
	
}