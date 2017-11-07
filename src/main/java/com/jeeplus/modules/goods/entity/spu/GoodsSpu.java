/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.entity.spu;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 商品管理Entity
 * 
 * @author maxiao
 * @version 2017-06-13
 */
public class GoodsSpu extends DataEntity<GoodsSpu> {

	private static final long serialVersionUID = 1L;
	private String fgoodsname; // 商品名称
	private Office company; // 所属商户
	private Office office;//所属门店
	private String fbuyprice; // 采购价
	private GoodsUnit goodsUnit; // 商品单位
	private Brand brand; // 商品所属品牌
	private Categorys categorys; // 商品所属分类
	private String fimage1; // 商品主图
	private String fimage2; // 商品细节图
	private String fpictureId; // 商品图片
	private Date fonsalestime; // 上架时间
	private Date foffsalestime; // 下架时间
	private String fstatus; // 商品状态1:启用;2:未启用()
	private String fseason;//季节分类
	private List<GoodsSku> goodsSkuList = Lists.newArrayList(); // 子表列表

	public GoodsSpu() {
		super();
	}

	public GoodsSpu(String id) {
		super(id);
	}


	public String getFseason() {
		return fseason;
	}

	public void setFseason(String fseason) {
		this.fseason = fseason;
	}

	@ExcelField(title = "商品名称", align = 2, sort = 1)
	public String getFgoodsname() {
		return fgoodsname;
	}

	public void setFgoodsname(String fgoodsname) {
		this.fgoodsname = fgoodsname;
	}


	@ExcelField(title = "采购价", align = 2, sort = 8)
	public String getFbuyprice() {
		return fbuyprice;
	}
	
	@ExcelField(title = "所属商户", align = 2, sort = 4)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	//@ExcelField(title = "所属门店", align = 2, sort = 4)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public void setFbuyprice(String fbuyprice) {
		this.fbuyprice = fbuyprice;
	}

	@ExcelField(title = "商品单位", align = 2, sort = 9)
	public GoodsUnit getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(GoodsUnit goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	@NotNull(message = "商品所属品牌不能为空")
	@ExcelField(title = "商品所属品牌", align = 2, sort = 10)
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@NotNull(message = "商品所属分类不能为空")
	@ExcelField(title = "商品所属分类", align = 2, sort = 11)
	public Categorys getCategorys() {
		return categorys;
	}

	public void setCategorys(Categorys categorys) {
		this.categorys = categorys;
	}

	@ExcelField(title = "商品主图", align = 2, sort = 12)
	public String getFimage1() {
		return fimage1;
	}

	public void setFimage1(String fimage1) {
		this.fimage1 = fimage1;
	}

	@ExcelField(title = "商品细节图", align = 2, sort = 13)
	public String getFimage2() {
		return fimage2;
	}

	public void setFimage2(String fimage2) {
		this.fimage2 = fimage2;
	}

	@ExcelField(title = "商品图片", align = 2, sort = 14)
	public String getFpictureId() {
		return fpictureId;
	}

	public void setFpictureId(String fpictureId) {
		this.fpictureId = fpictureId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "上架时间", align = 2, sort = 15)
	public Date getFonsalestime() {
		return fonsalestime;
	}

	public void setFonsalestime(Date fonsalestime) {
		this.fonsalestime = fonsalestime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "下架时间", align = 2, sort = 16)
	public Date getFoffsalestime() {
		return foffsalestime;
	}

	public void setFoffsalestime(Date foffsalestime) {
		this.foffsalestime = foffsalestime;
	}

	@ExcelField(title = "商品状态1:启用;2:未启用()", dictType = "", align = 2, sort = 18)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}

	public List<GoodsSku> getGoodsSkuList() {
		return goodsSkuList;
	}

	public void setGoodsSkuList(List<GoodsSku> goodsSkuList) {
		this.goodsSkuList = goodsSkuList;
	}
}