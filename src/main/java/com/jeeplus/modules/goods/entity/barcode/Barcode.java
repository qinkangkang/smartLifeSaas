/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.entity.barcode;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 生成条形码Entity
 * @author maxiao
 * @version 2017-06-12
 */
public class Barcode extends DataEntity<Barcode> {
	
	private static final long serialVersionUID = 1L;
	private String codeType;		// 条码类型
	private String fstrCode;		// 八位厂商代码
	private String fstatus;		// 条形码产生标识1:(1:有效;2:无效)
	private String fskuid;		// 商品详情ID
	private String  fproductCode;		// 产品代码
	
	public Barcode() {
		super();
	}

	public Barcode(String id){
		super(id);
	}

	@ExcelField(title="条码类型", dictType="", align=2, sort=7)
	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
	@ExcelField(title="八位厂商代码", align=2, sort=8)
	public String getFstrCode() {
		return fstrCode;
	}

	public void setFstrCode(String fstrCode) {
		this.fstrCode = fstrCode;
	}
	
	@ExcelField(title="条形码产生标识1:(1:有效;2:无效)", dictType="", align=2, sort=9)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
	@ExcelField(title="商品详情ID", align=2, sort=10)
	public String getFskuid() {
		return fskuid;
	}

	public void setFskuid(String fskuid) {
		this.fskuid = fskuid;
	}
	
	@ExcelField(title="产品代码", align=2, sort=11)
	public String getFproductCode() {
		return  fproductCode;
	}

	public void setFproductCode(String  fproductCode) {
		this. fproductCode =  fproductCode;
	}
	
}