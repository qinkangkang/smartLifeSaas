/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.entity.employee;

import com.jeeplus.modules.store.entity.storemanage.Store;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 员工账号管理Entity
 * @author 金圣智
 * @version 2017-06-23
 */
public class Employee extends DataEntity<Employee> {
	
	private static final long serialVersionUID = 1L;
	private String faccountNumber;		// 员工账号
	private String fname;		// 员工姓名
	private String fpassword;		// 员工密码
	private Store store;		// 所属门店
	private String fidentification;		// 身份证
	private String fmailbox;		// 邮箱
	private Date ftimeEnter;		// 入职时间
	private String faddress;		// 住址
	private String froleID;		// 员工角色
	private Integer fstatus;		// 员工状态
	private Date flastTime;		// 最后登录时间
	private String flastIP;		// 最后登录IP
	
	private User fuser;			//登陆用户
	
	
	

	public Employee() {
		super();
	}

	public Employee(String id){
		super(id);
	}

	@ExcelField(title="员工账号", align=2, sort=1)
	public String getFaccountNumber() {
		return faccountNumber;
	}

	public void setFaccountNumber(String faccountNumber) {
		this.faccountNumber = faccountNumber;
	}
	
	@ExcelField(title="员工姓名", align=2, sort=2)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="员工密码", align=2, sort=3)
	public String getFpassword() {
		return fpassword;
	}

	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}
	
	@ExcelField(title="所属门店", align=2, sort=4)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	@ExcelField(title="身份证", align=2, sort=5)
	public String getFidentification() {
		return fidentification;
	}

	public void setFidentification(String fidentification) {
		this.fidentification = fidentification;
	}
	
	@ExcelField(title="邮箱", align=2, sort=6)
	public String getFmailbox() {
		return fmailbox;
	}

	public void setFmailbox(String fmailbox) {
		this.fmailbox = fmailbox;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="入职时间", align=2, sort=7)
	public Date getFtimeEnter() {
		return ftimeEnter;
	}

	public void setFtimeEnter(Date ftimeEnter) {
		this.ftimeEnter = ftimeEnter;
	}
	
	@ExcelField(title="住址", align=2, sort=8)
	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	
	@ExcelField(title="员工角色", align=2, sort=9)
	public String getFroleID() {
		return froleID;
	}

	public void setFroleID(String froleID) {
		this.froleID = froleID;
	}
	
	@ExcelField(title="员工状态", dictType="sys_status", align=2, sort=10)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录时间", align=2, sort=11)
	public Date getFlastTime() {
		return flastTime;
	}

	public void setFlastTime(Date flastTime) {
		this.flastTime = flastTime;
	}
	
	@ExcelField(title="最后登录IP", align=2, sort=12)
	public String getFlastIP() {
		return flastIP;
	}

	public void setFlastIP(String flastIP) {
		this.flastIP = flastIP;
	}
	

	public User getFuser() {
		return fuser;
	}

	public void setFuser(User fuser) {
		this.fuser = fuser;
	}
	
}