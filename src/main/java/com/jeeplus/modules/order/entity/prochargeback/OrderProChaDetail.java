/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity.prochargeback;

import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;

import java.math.BigDecimal;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购退单Entity
 * @author diqiang
 * @version 2017-06-14
 */
public class OrderProChaDetail extends DataEntity<OrderProChaDetail> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据状态（1.启用2.禁用）
	 */
	public static final Integer STATUS_AVAILABLE = 1;
	public static final Integer STATUS_DISABLE = 2;
	private GoodsSpu fspu;		// 商品ID
	private GoodsSku fsku;		// 商品详情ID
	private Integer fgoodsnum;		// 商品数量
//	private String fgoodsprice;		// 商品单价
	private BigDecimal fgoodsprice;		// 商品单价
	private String fdiscount;		// 折扣
//	private String fdiscountPrice;		// 折后单价
	private BigDecimal fdiscountPrice;		// 折后单价
//	private String fcountprice;		// 商品折扣前总额
	private BigDecimal fcountprice;		// 商品折扣前总额
//	private String fdiscountcountprice;		// 折扣后总价
	private BigDecimal fdiscountcountprice;		// 折扣后总价
	private Integer fstatus;		// 状态（1：启用，2禁用）
	private OrderProcurement fprocurement;		// 采购单id
	private OrderProChargeback fprocurementchargeback;		// 采购退单id 父类
	private WarehouseGoodsInfo fwarehouseGoodsInfo;
	
	public WarehouseGoodsInfo getFwarehouseGoodsInfo() {
		return fwarehouseGoodsInfo;
	}

	public void setFwarehouseGoodsInfo(WarehouseGoodsInfo fwarehouseGoodsInfo) {
		this.fwarehouseGoodsInfo = fwarehouseGoodsInfo;
	}

	public OrderProChaDetail() {
		super();
	}

	public OrderProChaDetail(String id){
		super(id);
	}

	public OrderProChaDetail(OrderProChargeback fprocurementchargeback){
		this.fprocurementchargeback = fprocurementchargeback;
	}

	@ExcelField(title="商品", align=2, sort=7,value="fspu.fgoodsname")
	public GoodsSpu getFspu() {
		return fspu;
	}

	public void setFspu(GoodsSpu fspu) {
		this.fspu = fspu;
	}
	
	@ExcelField(title="商品货号", align=2, sort=8,value="fsku.fgoodsnumber")
	public GoodsSku getFsku() {
		return fsku;
	}

	public void setFsku(GoodsSku fsku) {
		this.fsku = fsku;
	}
	
	@ExcelField(title="商品数量", align=2, sort=9)
	public Integer getFgoodsnum() {
		return fgoodsnum;
	}

	public void setFgoodsnum(Integer fgoodsnum) {
		this.fgoodsnum = fgoodsnum;
	}
	
	@ExcelField(title="商品单价", align=2, sort=10)
	public BigDecimal getFgoodsprice() {
		return fgoodsprice;
	}

	public void setFgoodsprice(BigDecimal fgoodsprice) {
		this.fgoodsprice = fgoodsprice;
	}
	
	@ExcelField(title="折扣", align=2, sort=11)
	public String getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(String fdiscount) {
		this.fdiscount = fdiscount;
	}
	
	@ExcelField(title="折后单价", align=2, sort=12)
	public BigDecimal getFdiscountPrice() {
		return fdiscountPrice;
	}

	public void setFdiscountPrice(BigDecimal fdiscountPrice) {
		this.fdiscountPrice = fdiscountPrice;
	}
	
	@ExcelField(title="商品折扣前总额", align=2, sort=13)
	public BigDecimal getFcountprice() {
		return fcountprice;
	}

	public void setFcountprice(BigDecimal fcountprice) {
		this.fcountprice = fcountprice;
	}
	
	@ExcelField(title="折扣后总额", align=2, sort=14)
	public BigDecimal getFdiscountcountprice() {
		return fdiscountcountprice;
	}

	public void setFdiscountcountprice(BigDecimal fdiscountcountprice) {
		this.fdiscountcountprice = fdiscountcountprice;
	}
	
	@ExcelField(title="状态",dictType="sys_status", align=2, sort=15)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
//	@ExcelField(title="采购单id", align=2, sort=16)
	public OrderProcurement getFprocurement() {
		return fprocurement;
	}

	public void setFprocurement(OrderProcurement fprocurement) {
		this.fprocurement = fprocurement;
	}
	@ExcelField(title="采购退单单号", align=2, sort=6,value="fprocurementchargeback.fordernum")
	public OrderProChargeback getFprocurementchargeback() {
		return fprocurementchargeback;
	}

	public void setFprocurementchargeback(OrderProChargeback fprocurementchargeback) {
		this.fprocurementchargeback = fprocurementchargeback;
	}
	
}