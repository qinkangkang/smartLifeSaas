/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.entity.ordermarket;

import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;

import java.math.BigDecimal;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售订单管理Entity
 * 
 * @author diqiang
 * @version 2017-06-18
 */
public class OrderMarketDetail extends DataEntity<OrderMarketDetail> {

	private static final long serialVersionUID = 1L;

	/**
	 * 数据状态（1.启用2.禁用）
	 */
	public static final int STATUS_AVAILABLE = 1;
	public static final int STATUS_DISABLE = 2;

	private OrderMarket orderMarket; // 销售单ID 父类
	// private WarehouseGoodsInfo wgi;
	private GoodsSpu goodsSpu;// 商品名称
	private Integer fgoodsnum; // 商品数量
	// private String fgoodsprice; // 商品单价
	private String fgoodsdiscount; // 商品折扣
	// private String fdiscountprice; // 折后单价
	// private String fcountmoney; // 折扣前金额
	// private String fdiscountcountmoney; // 折后总额

	private BigDecimal fdiscountprice; // 折后单价
	private BigDecimal fcountmoney; // 折扣前金额
	private BigDecimal fdiscountcountmoney;
	private BigDecimal fgoodsprice; // 商品单价

	private GoodsSku goodsSku; // 商品详情
	private Integer fstatus; // 数据状态
	private Integer freturnnum; // 退货数量

	private WarehouseGoodsInfo fwarehousegoodsinfo; // 库存

	private BigDecimal purchasingCost; //采购成本
	private BigDecimal grossProfit; //毛利润
	private BigDecimal grossProfitMargin; //毛利率

	public WarehouseGoodsInfo getFwarehousegoodsinfo() {
		return fwarehousegoodsinfo;
	}

	public void setFwarehousegoodsinfo(WarehouseGoodsInfo fwarehousegoodsinfo) {
		this.fwarehousegoodsinfo = fwarehousegoodsinfo;
	}

	public OrderMarketDetail() {
		super();
	}

	public OrderMarketDetail(String id) {
		super(id);
	}

	public OrderMarketDetail(OrderMarket orderMarket) {
		this.orderMarket = orderMarket;
	}
	@ExcelField(title = "销售单号", align = 2, sort = 6,value="orderMarket.fordernum")
	public OrderMarket getOrderMarket() {
		return orderMarket;
	}

	public void setOrderMarket(OrderMarket orderMarket) {
		this.orderMarket = orderMarket;
	}

	@ExcelField(title = "商品名称", align = 2, sort = 7,value="goodsSpu.fgoodsname")
	public GoodsSpu getGoodsSpu() {
		return goodsSpu;
	}

	public void setGoodsSpu(GoodsSpu goodsSpu) {
		this.goodsSpu = goodsSpu;
	}
	// public WarehouseGoodsInfo getWgi() {
	// return wgi;
	// }
	//
	// public void setWgi(WarehouseGoodsInfo wgi) {
	// this.wgi = wgi;
	// }

	@ExcelField(title = "商品数量", align = 2, sort = 9)
	public Integer getFgoodsnum() {
		return fgoodsnum;
	}

	public void setFgoodsnum(Integer fgoodsnum) {
		this.fgoodsnum = fgoodsnum;
	}

	@ExcelField(title = "商品单价", align = 2, sort = 10)
	public BigDecimal getFgoodsprice() {
		return fgoodsprice;
	}

	public void setFgoodsprice(BigDecimal fgoodsprice) {
		this.fgoodsprice = fgoodsprice;
	}

	@ExcelField(title = "商品折扣", align = 2, sort = 11)
	public String getFgoodsdiscount() {
		return fgoodsdiscount;
	}

	public void setFgoodsdiscount(String fgoodsdiscount) {
		this.fgoodsdiscount = fgoodsdiscount;
	}

	@ExcelField(title = "折后单价", align = 2, sort = 12)
	public BigDecimal getFdiscountprice() {
		return fdiscountprice;
	}

	public void setFdiscountprice(BigDecimal fdiscountprice) {
		this.fdiscountprice = fdiscountprice;
	}

	@ExcelField(title = "折扣前金额", align = 2, sort = 13)
	public BigDecimal getFcountmoney() {
		return fcountmoney;
	}

	public void setFcountmoney(BigDecimal fcountmoney) {
		this.fcountmoney = fcountmoney;
	}

	@ExcelField(title = "折后总额", align = 2, sort = 14)
	public BigDecimal getFdiscountcountmoney() {
		return fdiscountcountmoney;
	}

	public void setFdiscountcountmoney(BigDecimal fdiscountcountmoney) {
		this.fdiscountcountmoney = fdiscountcountmoney;
	}

	@ExcelField(title = "商品详情", align = 2, sort = 8,value="goodsSku.fgoodsnumber")
	public GoodsSku getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
	}

	@ExcelField(title = "数据状态", dictType="sys_status",align = 2, sort = 15)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

//	@ExcelField(title = "退货数量", align = 2, sort = 17)
	public Integer getFreturnnum() {
		return freturnnum;
	}

	public void setFreturnnum(Integer freturnnum) {
		this.freturnnum = freturnnum;
	}

	public BigDecimal getPurchasingCost() {
		return purchasingCost;
	}

	public void setPurchasingCost(BigDecimal purchasingCost) {
		this.purchasingCost = purchasingCost;
	}

	public BigDecimal getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}

	public BigDecimal getGrossProfitMargin() {
		return grossProfitMargin;
	}

	public void setGrossProfitMargin(BigDecimal grossProfitMargin) {
		this.grossProfitMargin = grossProfitMargin;
	}

}