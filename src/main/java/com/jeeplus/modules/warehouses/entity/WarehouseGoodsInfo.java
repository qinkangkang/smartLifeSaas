/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.entity;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库商品Entity
 * @author hxting
 * @version 2017-07-11
 */
public class WarehouseGoodsInfo extends DataEntity<WarehouseGoodsInfo> {
	
	private static final long serialVersionUID = 1L;
	private Warehouse warehouse;		// 仓库
	private GoodsSpu goodsSpu;		// 商品SPU
	private GoodsSku goodsSku;		// 商品SKU
	private Brand goodsBrand;		// 商品品牌
	private Categorys goodsCategory;		// 商品分类
	private GoodsUnit goodsUnit;		// 单位
	private Integer finventory;		// 可用库存量
	private Integer flockinventory;		// 锁定库存量
	private Integer ftotalinventory;		// 库存总量
	private Office compan;		// 所属商户
	private Office office;		// 所属门店
	private Integer beginFtotalinventory;		// 开始 库存总量
	private Integer endFtotalinventory;		// 结束 库存总量
	private SupplierBasic supplierBasic;  //供应商
	private String queryConditions;  //查询条件
	private String inventoryFilter;  //库存过滤
	private String inventoryWarning; //库存预警
	
	public WarehouseGoodsInfo() {
		super();
	}

	public WarehouseGoodsInfo(String id){
		super(id);
	}

	@ExcelField(title="仓库名称", align=2, sort=7, value="warehouse.fname")
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	@ExcelField(title="商品名称", align=2, sort=8, value="goodsSpu.fgoodsname")
	public GoodsSpu getGoodsSpu() {
		return goodsSpu;
	}

	public void setGoodsSpu(GoodsSpu goodsSpu) {
		this.goodsSpu = goodsSpu;
	}
	
	@ExcelField(title="商品货号", align=2, sort=9, value="goodsSku.fgoodsnumber")
	public GoodsSku getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
	}
	
	@ExcelField(title="商品品牌", align=2, sort=10, value="goodsSpu.brand.fbrandname")
	public Brand getGoodsBrand() {
		return goodsBrand;
	}

	public void setGoodsBrand(Brand goodsBrand) {
		this.goodsBrand = goodsBrand;
	}
	
	@ExcelField(title="商品分类", align=2, sort=11, value="goodsSpu.categorys.name")
	public Categorys getGoodsCategory() {
		return goodsCategory;
	}

	public void setGoodsCategory(Categorys goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	
	@ExcelField(title="单位", align=2, sort=12, value="goodsSpu.goodsUnit.funitname")
	public GoodsUnit getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(GoodsUnit goodsUnit) {
		this.goodsUnit = goodsUnit;
	}
	
	@ExcelField(title="可用库存量", align=2, sort=13)
	public Integer getFinventory() {
		return finventory;
	}
	
	public void setFinventory(Integer finventory) {
		this.finventory = finventory;
	}
	
	@ExcelField(title="锁定库存量", align=2, sort=14)
	public Integer getFlockinventory() {
		return flockinventory;
	}

	public void setFlockinventory(Integer flockinventory) {
		this.flockinventory = flockinventory;
	}
	
	@ExcelField(title="库存总量", align=2, sort=15)
	public Integer getFtotalinventory() {
		return ftotalinventory;
	}

	public void setFtotalinventory(Integer ftotalinventory) {
		this.ftotalinventory = ftotalinventory;
	}
	
	public Office getCompan() {
		return compan;
	}

	public void setCompan(Office compan) {
		this.compan = compan;
	}
	
	@ExcelField(title="所属门店", align=2, sort=17)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public Integer getBeginFtotalinventory() {
		return beginFtotalinventory;
	}

	public void setBeginFtotalinventory(Integer beginFtotalinventory) {
		this.beginFtotalinventory = beginFtotalinventory;
	}
	
	public Integer getEndFtotalinventory() {
		return endFtotalinventory;
	}

	public void setEndFtotalinventory(Integer endFtotalinventory) {
		this.endFtotalinventory = endFtotalinventory;
	}
	
	public SupplierBasic getSupplierBasic() {
		return supplierBasic;
	}

	public void setSupplierBasic(SupplierBasic supplierBasic) {
		this.supplierBasic = supplierBasic;
	}

	public String getQueryConditions() {
		return queryConditions;
	}

	public void setQueryConditions(String queryConditions) {
		this.queryConditions = queryConditions;
	}

	public String getInventoryFilter() {
		return inventoryFilter;
	}

	public void setInventoryFilter(String inventoryFilter) {
		this.inventoryFilter = inventoryFilter;
	}

	public String getInventoryWarning() {
		return inventoryWarning;
	}

	public void setInventoryWarning(String inventoryWarning) {
		this.inventoryWarning = inventoryWarning;
	}
	
	
		
}