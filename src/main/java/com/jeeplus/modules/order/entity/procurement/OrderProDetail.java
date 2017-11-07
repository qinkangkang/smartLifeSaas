/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity.procurement;

import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;

import java.math.BigDecimal;
import java.util.Date;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购单Entity
 * @author diqiang
 * @version 2017-06-12
 */
public class OrderProDetail extends DataEntity<OrderProDetail> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 数据状态（1.启用2.禁用）
	 */
	public static final Integer STATUS_AVAILABLE = 1;
	public static final Integer STATUS_DISABLE = 2;
	private OrderProcurement forderprocurement;		// 采购单ID 父类
	private GoodsSpu fspu;		// 商品ID
	private GoodsSku fsku;		// 商品详情ID
	private Integer fgoodsnum;		// 商品数量
//	private String fgoodsprice;		// 商品单价
	private BigDecimal fgoodsprice;		// 商品单价
	private String fgoodsdiscount;		// 商品折扣
//	private String fdiscountprice;		// 折后单价
	private BigDecimal fdiscountprice;		// 折后单价
//	private String fcountmoney;		// 折扣前总额
	private BigDecimal fcountmoney;		// 折扣前总额
//	private String fdiscountcountmoney;		// 折后总额
	private BigDecimal fdiscountcountmoney;		// 折后总额
	private Integer fstatus;		// 状态（1：启用，2：禁用）
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private WarehouseGoodsInfo fwarehouseGoodsInfo;//商品库存
	private Integer freturnnum; //退货数量
	
	private SupplierBasic fsupplier; //所属供应商
	

	public SupplierBasic getFsupplier() {
		return fsupplier;
	}

	public void setFsupplier(SupplierBasic fsupplier) {
		this.fsupplier = fsupplier;
	}

	public Integer getFreturnnum() {
		return freturnnum;
	}

	public void setFreturnnum(Integer freturnnum) {
		this.freturnnum = freturnnum;
	}

	public WarehouseGoodsInfo getFwarehouseGoodsInfo() {
		return fwarehouseGoodsInfo;
	}

	public void setFwarehouseGoodsInfo(WarehouseGoodsInfo fwarehouseGoodsInfo) {
		this.fwarehouseGoodsInfo = fwarehouseGoodsInfo;
	}

	public OrderProDetail() {
		super();
	}

	public OrderProDetail(String id){
		super(id);
	}

	public OrderProDetail(OrderProcurement forderprocurement){
		this.forderprocurement = forderprocurement;
	}
	@ExcelField(title="采购单号", align=2, sort=1,value="forderprocurement.fordernum")
	public OrderProcurement getForderprocurement() {
		return forderprocurement;
	}

	public void setForderprocurement(OrderProcurement forderprocurement) {
		this.forderprocurement = forderprocurement;
	}
	
	@ExcelField(title="商品spu", align=2, sort=2,value="fspu.fgoodsname")
	public GoodsSpu getFspu() {
		return fspu;
	}

	public void setFspu(GoodsSpu fspu) {
		this.fspu = fspu;
	}
	
	@ExcelField(title="商品sku", align=2, sort=3,value="fsku.fgoodsnumber")
	public GoodsSku getFsku() {
		return fsku;
	}

	public void setFsku(GoodsSku fsku) {
		this.fsku = fsku;
	}
	
	@ExcelField(title="商品数量", align=2, sort=4)
	public Integer getFgoodsnum() {
		return fgoodsnum;
	}

	public void setFgoodsnum(Integer fgoodsnum) {
		this.fgoodsnum = fgoodsnum;
	}
	
	@ExcelField(title="商品单价", align=2, sort=5)
	public BigDecimal getFgoodsprice() {
		return fgoodsprice;
	}

	public void setFgoodsprice(BigDecimal fgoodsprice) {
		this.fgoodsprice = fgoodsprice;
	}
	
	@ExcelField(title="商品折扣", align=2, sort=6)
	public String getFgoodsdiscount() {
		return fgoodsdiscount;
	}

	public void setFgoodsdiscount(String fgoodsdiscount) {
		this.fgoodsdiscount = fgoodsdiscount;
	}
	
	@ExcelField(title="折后单价", align=2, sort=7)
	public BigDecimal getFdiscountprice() {
		return fdiscountprice;
	}

	public void setFdiscountprice(BigDecimal fdiscountprice) {
		this.fdiscountprice = fdiscountprice;
	}
	
	@ExcelField(title="折扣前总额", align=2, sort=8)
	public BigDecimal getFcountmoney() {
		return fcountmoney;
	}

	public void setFcountmoney(BigDecimal fcountmoney) {
		this.fcountmoney = fcountmoney;
	}
	
	@ExcelField(title="折后总额", align=2, sort=9)
	public BigDecimal getFdiscountcountmoney() {
		return fdiscountcountmoney;
	}

	public void setFdiscountcountmoney(BigDecimal fdiscountcountmoney) {
		this.fdiscountcountmoney = fdiscountcountmoney;
	}
	
	@ExcelField(title="状态", dictType="sys_status", align=2, sort=10)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
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
		
}