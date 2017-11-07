/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 物流信息Entity
 * @author qkk
 * @version 2017-06-01
 */
public class CustomerSalse extends DataEntity<CustomerSalse> {
	
	private static final long serialVersionUID = 1L;
	private String customerName;		// 用户名称
	private String customerPhone;		// 用户手机号
	private String orderCount;		// 下单数量
	private String orderTotal;		// 下单金额
	private String customerTime;		// 用户注册时间
	
	public CustomerSalse() {
		super();
	}

	public CustomerSalse(String id){
		super(id);
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getCustomerTime() {
		return customerTime;
	}

	public void setCustomerTime(String customerTime) {
		this.customerTime = customerTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}