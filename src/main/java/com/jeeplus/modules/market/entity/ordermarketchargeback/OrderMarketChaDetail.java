/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.entity.ordermarketchargeback;

import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketCha;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;

import java.math.BigDecimal;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售退货单Entity
 * 
 * @author diqiang
 * @version 2017-06-18
 */
public class OrderMarketChaDetail extends DataEntity<OrderMarketChaDetail> {

	private static final long serialVersionUID = 1L;
	/**
	 * 数据状态（1.启用2.禁用）
	 */
	public static final String STATUS_AVAILABLE = "1";
	public static final String STATUS_DISABLE = "2";
	private OrderMarket fordermarket; // 销售单ID
	private GoodsSpu fspu; // 商品ID
	private Integer fgoodsnum; // 商品数量
	// private String fgoodsprice; // 商品单价
	private String fgoodsdiscount; // 商品折扣
	// private String fdiscountprice; // 折后单价
	// private String fcountmoney; // 折扣前金额
	// private String fdiscountcountmoney; // 折后总额
	private GoodsSku fsku; // 商品详情ID
	private String fstatus; // 状态（1：启用，2：禁用）

	private BigDecimal fgoodsprice; // 商品单价
	private BigDecimal fdiscountprice; // 折后单价
	private BigDecimal fcountmoney; // 折扣前金额
	private BigDecimal fdiscountcountmoney; // 折后总额

	private OrderMarketCha fordermarketchargeback; // 销售退单id 父类

	private WarehouseGoodsInfo fwarehousegoodsinfo;// 库存

	private BigDecimal purchasingCost; // 采购成本
	private BigDecimal grossProfit; // 毛利润
	private BigDecimal grossProfitMargin; // 毛利率

	public WarehouseGoodsInfo getFwarehousegoodsinfo() {
		return fwarehousegoodsinfo;
	}

	public void setFwarehousegoodsinfo(WarehouseGoodsInfo fwarehousegoodsinfo) {
		this.fwarehousegoodsinfo = fwarehousegoodsinfo;
	}

	public OrderMarketChaDetail() {
		super();
	}

	public OrderMarketChaDetail(String id) {
		super(id);
	}

	public OrderMarketChaDetail(OrderMarketCha fordermarketchargeback) {
		this.fordermarketchargeback = fordermarketchargeback;
	}

	@ExcelField(title = "所属销售单号", align = 2, sort = 7,value="fordermarket.fordernum")
	public OrderMarket getFordermarket() {
		return fordermarket;
	}

	public void setFordermarket(OrderMarket fordermarket) {
		this.fordermarket = fordermarket;
	}

	@ExcelField(title = "商品", align = 2, sort = 8, value = "fspu.fgoodsname")
	public GoodsSpu getFspu() {
		return fspu;
	}

	public void setFspu(GoodsSpu fspu) {
		this.fspu = fspu;
	}

	@ExcelField(title = "商品数量", align = 2, sort = 10)
	public Integer getFgoodsnum() {
		return fgoodsnum;
	}

	public void setFgoodsnum(Integer fgoodsnum) {
		this.fgoodsnum = fgoodsnum;
	}

	@ExcelField(title = "商品单价", align = 2, sort = 11)
	public BigDecimal getFgoodsprice() {
		return fgoodsprice;
	}

	public void setFgoodsprice(BigDecimal fgoodsprice) {
		this.fgoodsprice = fgoodsprice;
	}

	@ExcelField(title = "商品折扣", align = 2, sort = 12)
	public String getFgoodsdiscount() {
		return fgoodsdiscount;
	}

	public void setFgoodsdiscount(String fgoodsdiscount) {
		this.fgoodsdiscount = fgoodsdiscount;
	}

	@ExcelField(title = "折后单价", align = 2, sort = 13)
	public BigDecimal getFdiscountprice() {
		return fdiscountprice;
	}

	public void setFdiscountprice(BigDecimal fdiscountprice) {
		this.fdiscountprice = fdiscountprice;
	}

	@ExcelField(title = "折扣前金额", align = 2, sort = 14)
	public BigDecimal getFcountmoney() {
		return fcountmoney;
	}

	public void setFcountmoney(BigDecimal fcountmoney) {
		this.fcountmoney = fcountmoney;
	}

	@ExcelField(title = "折后总额", align = 2, sort = 15)
	public BigDecimal getFdiscountcountmoney() {
		return fdiscountcountmoney;
	}

	public void setFdiscountcountmoney(BigDecimal fdiscountcountmoney) {
		this.fdiscountcountmoney = fdiscountcountmoney;
	}

	@ExcelField(title = "商品货号", align = 2, sort = 9, value = "fsku.fgoodsnumber")
	public GoodsSku getFsku() {
		return fsku;
	}

	public void setFsku(GoodsSku fsku) {
		this.fsku = fsku;
	}

	@ExcelField(title = "状态", dictType = "sys_status", align = 2, sort = 16)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	@ExcelField(title = "销售退单号", align = 2, sort = 6,value="fordermarketchargeback.fordernum")
	public OrderMarketCha getFordermarketchargeback() {
		return fordermarketchargeback;
	}

	public void setFordermarketchargeback(OrderMarketCha fordermarketchargeback) {
		this.fordermarketchargeback = fordermarketchargeback;
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