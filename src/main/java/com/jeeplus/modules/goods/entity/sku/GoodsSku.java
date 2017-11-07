/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.entity.sku;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;

/**
 * 商品管理Entity
 * 
 * @author maxiao
 * @version 2017-06-13
 */
public class GoodsSku extends DataEntity<GoodsSku> {

	private static final long serialVersionUID = 1L;
	private String fgoodsnumber; // 商品货号
	private Colors colors; // 颜色
	private Size size; // 尺码
	private String fprice; // 原价
	private String fonsalesprice; // 销售价
	private String fbarcode; // 商品条形码
	private String fsort; // 排序
	private String fstatus; // 商品状态(1:启用;2:未启用)
	private GoodsSpu goodsSpu; // 外键 父类
	private String fstorelowerno; // 库存下限
	private String fstoreupperno; // 库存上限
	private String beginFonsalesprice; // 开始 销售价
	private String endFonsalesprice; // 结束 销售价

	public GoodsSku() {
		super();
	}

	public GoodsSku(String id) {
		super(id);
	}

	public GoodsSku(GoodsSpu goodsSpu) {
		this.goodsSpu = goodsSpu;
	}

	@ExcelField(title = "商品货号", align = 2, sort = 1)
	public String getFgoodsnumber() {
		return fgoodsnumber;
	}

	public void setFgoodsnumber(String fgoodsnumber) {
		this.fgoodsnumber = fgoodsnumber;
	}

	@ExcelField(title = "颜色", align = 2, sort = 2)
	public Colors getColors() {
		return colors;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	@ExcelField(title = "尺码", align = 2, sort = 3)
	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	@ExcelField(title = "原价", align = 2, sort = 4)
	public String getFprice() {
		return fprice;
	}

	public void setFprice(String fprice) {
		this.fprice = fprice;
	}

	@ExcelField(title = "销售价", align = 2, sort = 5)
	public String getFonsalesprice() {
		return fonsalesprice;
	}

	public void setFonsalesprice(String fonsalesprice) {
		this.fonsalesprice = fonsalesprice;
	}

	@ExcelField(title = "商品条形码", align = 2, sort = 6)
	public String getFbarcode() {
		return fbarcode;
	}

	public void setFbarcode(String fbarcode) {
		this.fbarcode = fbarcode;
	}

	@ExcelField(title = "排序", align = 2, sort = 13)
	public String getFsort() {
		return fsort;
	}

	public void setFsort(String fsort) {
		this.fsort = fsort;
	}

	@ExcelField(title = "商品状态(1:启用;2:未启用)", dictType = "", align = 2, sort = 14)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}

	public GoodsSpu getGoodsSpu() {
		return goodsSpu;
	}

	public void setGoodsSpu(GoodsSpu goodsSpu) {
		this.goodsSpu = goodsSpu;
	}

	@ExcelField(title = "库存下限", align = 2, sort = 16)
	public String getFstorelowerno() {
		return fstorelowerno;
	}

	public void setFstorelowerno(String fstorelowerno) {
		this.fstorelowerno = fstorelowerno;
	}

	@ExcelField(title = "库存上限", align = 2, sort = 17)
	public String getFstoreupperno() {
		return fstoreupperno;
	}

	public void setFstoreupperno(String fstoreupperno) {
		this.fstoreupperno = fstoreupperno;
	}

	public String getBeginFonsalesprice() {
		return beginFonsalesprice;
	}

	public void setBeginFonsalesprice(String beginFonsalesprice) {
		this.beginFonsalesprice = beginFonsalesprice;
	}

	public String getEndFonsalesprice() {
		return endFonsalesprice;
	}

	public void setEndFonsalesprice(String endFonsalesprice) {
		this.endFonsalesprice = endFonsalesprice;
	}

}