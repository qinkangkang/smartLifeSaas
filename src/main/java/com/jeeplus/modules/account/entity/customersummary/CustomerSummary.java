/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity.customersummary;

import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.sys.entity.Office;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Email;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 客户基本信息Entity
 * @author diqiang
 * @version 2017-06-07
 */
public class CustomerSummary extends DataEntity<CustomerSummary> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 客户名称
	private CustomerCate customerCate;		// 客户分类
	private String flinkman;		// 联系人
	private String fcellphone;		// 手机
	private String fdebt;		// 客户欠款
	private Integer fsex;		// 客户性别（1：男，2：女）
	private Date fbirthday;		// 客户生日
	private String fphone;		// 电话
	private String femail;		// 邮箱
	private String ffax;		// 传真
	private String fbankaccount;		// 银行账户
	private String fbankaccountnum;		// 银行账号
	private String fbank;		// 开户行
	private String fbankaccountname;		// 开户人姓名
	private String fbankaccountpersonid;		// 开户人身份证
	private String fbankphone;		// 开户预留手机号
	private Integer fsort;		// 排序
	private String faddress;		// 客户地址
	private Integer fshipmenttype;		// 发货方式（0：自提，1：车送，2：物流）
	private Integer fisrelate;		// 是否有关联账号（0：无，1：有）
	private String fstatus;		// 状态（1：启用，2：失效）
	private String fcustomerinfo;		// 关联账号信息ID
	private Integer fpaytype;		// 支付方式（0：支付宝，1：微信，2：pos机）
	private String fdiscount;		// 折扣
	private String fcatename;		// 分类名称
	private Office office;			//门店
	public CustomerSummary() {
		super();
	}

	public CustomerSummary(String id){
		super(id);
	}

	@ExcelField(title="客户名称", align=2, sort=2)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="客户分类", align=2, sort=1,value="customerCate.fname")
	public CustomerCate getCustomerCate() {
		return customerCate;
	}

	public void setCustomerCate(CustomerCate customerCate) {
		this.customerCate = customerCate;
	}
	
	@ExcelField(title="联系人", align=2, sort=3)
	public String getFlinkman() {
		return flinkman;
	}

	public void setFlinkman(String flinkman) {
		this.flinkman = flinkman;
	}
	
	@Length(min=11, max=11, message="手机长度必须介于 11 和 11 之间")
	@ExcelField(title="手机", align=2, sort=4)
	public String getFcellphone() {
		return fcellphone;
	}

	public void setFcellphone(String fcellphone) {
		this.fcellphone = fcellphone;
	}
	
	@ExcelField(title="客户欠款", align=2, sort=6)
	public String getFdebt() {
		return fdebt;
	}

	public void setFdebt(String fdebt) {
		this.fdebt = fdebt;
	}
	//（1：男，2：女）
	@ExcelField(title="客户性别", dictType="sex", align=2, sort=5)
	public Integer getFsex() {
		return fsex;
	}

	public void setFsex(Integer fsex) {
		this.fsex = fsex;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@ExcelField(title="客户生日", align=2, sort=13)
	public Date getFbirthday() {
		return fbirthday;
	}

	public void setFbirthday(Date fbirthday) {
		this.fbirthday = fbirthday;
	}
	
//	@ExcelField(title="电话", align=2, sort=14)
	public String getFphone() {
		return fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}
	
	@Email(message="邮箱必须为合法邮箱")
//	@ExcelField(title="邮箱", align=2, sort=15)
	public String getFemail() {
		return femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}
	
//	@ExcelField(title="传真", align=2, sort=16)
	public String getFfax() {
		return ffax;
	}

	public void setFfax(String ffax) {
		this.ffax = ffax;
	}
	
//	@ExcelField(title="银行账户", align=2, sort=17)
	public String getFbankaccount() {
		return fbankaccount;
	}

	public void setFbankaccount(String fbankaccount) {
		this.fbankaccount = fbankaccount;
	}
	
//	@ExcelField(title="银行账号", align=2, sort=18)
	public String getFbankaccountnum() {
		return fbankaccountnum;
	}

	public void setFbankaccountnum(String fbankaccountnum) {
		this.fbankaccountnum = fbankaccountnum;
	}
	
//	@ExcelField(title="开户行", align=2, sort=19)
	public String getFbank() {
		return fbank;
	}

	public void setFbank(String fbank) {
		this.fbank = fbank;
	}
	
//	@ExcelField(title="开户人姓名", align=2, sort=20)
	public String getFbankaccountname() {
		return fbankaccountname;
	}

	public void setFbankaccountname(String fbankaccountname) {
		this.fbankaccountname = fbankaccountname;
	}
	
//	@ExcelField(title="开户人身份证", align=2, sort=21)
	public String getFbankaccountpersonid() {
		return fbankaccountpersonid;
	}

	public void setFbankaccountpersonid(String fbankaccountpersonid) {
		this.fbankaccountpersonid = fbankaccountpersonid;
	}
	
//	@ExcelField(title="开户预留手机号", align=2, sort=22)
	public String getFbankphone() {
		return fbankphone;
	}

	public void setFbankphone(String fbankphone) {
		this.fbankphone = fbankphone;
	}
	
//	@ExcelField(title="排序", align=2, sort=23)
	public Integer getFsort() {
		return fsort;
	}

	public void setFsort(Integer fsort) {
		this.fsort = fsort;
	}
	
	@ExcelField(title="客户地址", align=2, sort=6)
	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	//（0：自提，1：车送，2：物流）
//	@ExcelField(title="发货方式", dictType="shipment_type", align=2, sort=25)
	public Integer getFshipmenttype() {
		return fshipmenttype;
	}

	public void setFshipmenttype(Integer fshipmenttype) {
		this.fshipmenttype = fshipmenttype;
	}
	
//	@ExcelField(title="是否有关联账号（0：无，1：有）", dictType="fIsRelate", align=2, sort=26)
	public Integer getFisrelate() {
		return fisrelate;
	}

	public void setFisrelate(Integer fisrelate) {
		this.fisrelate = fisrelate;
	}
	//（1：启用，2：失效）
	@ExcelField(title="状态", dictType="sys_status", align=2, sort=9)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
//	@ExcelField(title="关联账号信息ID", align=2, sort=28)
	public String getFcustomerinfo() {
		return fcustomerinfo;
	}

	public void setFcustomerinfo(String fcustomerinfo) {
		this.fcustomerinfo = fcustomerinfo;
	}
	//（0：支付宝，1：微信，2：pos机）
//	@ExcelField(title="支付方式",dictType="pay_type", align=2, sort=29)
	public Integer getFpaytype() {
		return fpaytype;
	}

	public void setFpaytype(Integer fpaytype) {
		this.fpaytype = fpaytype;
	}
	
	@ExcelField(title="折扣", align=2, sort=7)
	public String getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(String fdiscount) {
		this.fdiscount = fdiscount;
	}
	@ExcelField(title="门店", align=2, sort=8)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	//	@ExcelField(title="分类名称", align=2, sort=1)
	public String getFcatename() {
		return fcatename;
	}

	public void setFcatename(String fcatename) {
		this.fcatename = fcatename;
	}
	
}