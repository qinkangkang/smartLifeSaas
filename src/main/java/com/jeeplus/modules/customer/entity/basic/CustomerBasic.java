/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.entity.basic;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Email;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 客户基本信息Entity
 * @author diqiang
 * @version 2017-06-06
 */
public class CustomerBasic extends DataEntity<CustomerBasic> {
	
	private static final long serialVersionUID = 1L;
	private String fcategoryId;		// 客户分类
	private String fname;		// 客户名称
	private Integer fsex;		// 客户性别（1：男，2：女）
	private Date fbirthday;		// 客户生日
	private String flinkman;		// 联系人
	private String fphone;		// 电话
	private String fcellphone;		// 手机
	private String femail;		// 邮箱
	private String ffax;		// 传真
	private String fbankaccount;		// 银行账户
	private String fbankaccountnum;		// 银行账号
	private String fbank;		// 开户行
	private String fbankaccountname;		// 开户人姓名
	private String fbankaccountpersonid;		// 开户人身份证
	private String fbankphone;		// 开户预留手机号
	private String fdebt;		// 客户欠款
	private Integer fsort;		// 排序
	private String faddress;		// 客户地址
	private Integer fshipmenttype;		// 发货方式（0：自提，1：车送，2：物流）
	private Integer fisrelate;		// 是否有关联账号（0：无，1：有）
	private String fstatus;		// 状态（1：启用，2：失效）
	private String fcustomerinfo;		// 关联账号信息ID
	private Integer fpaytype;		// 支付方式（0：微信，1：支付宝，2：银行账户）
	private String fdiscount;		// 折扣
	private String fcatename;		// 分类名称
	
	private Office fsponsor;		//所属商户
	private Office fstore;			//门店
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private String beginFdebt;		// 开始 客户欠款
	private String endFdebt;		// 结束 客户欠款
	
	public CustomerBasic() {
		super();
	}

	public CustomerBasic(String id){
		super(id);
	}

//	@ExcelField(title="客户分类", align=2, sort=1)
	public String getFcategoryId() {
		return fcategoryId;
	}

	public void setFcategoryId(String fcategoryId) {
		this.fcategoryId = fcategoryId;
	}
	
	@ExcelField(title="客户名称", align=2, sort=2)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
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
	@ExcelField(title="客户生日", align=2, sort=6)
	public Date getFbirthday() {
		return fbirthday;
	}

	public void setFbirthday(Date fbirthday) {
		this.fbirthday = fbirthday;
	}
	
	@ExcelField(title="联系人", align=2, sort=3)
	public String getFlinkman() {
		return flinkman;
	}

	public void setFlinkman(String flinkman) {
		this.flinkman = flinkman;
	}
	
	@ExcelField(title="电话", align=2, sort=10)
	public String getFphone() {
		return fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}
	
	@Length(min=11, max=11, message="手机长度必须介于 11 和 11 之间")
	@ExcelField(title="手机", align=2, sort=4)
	public String getFcellphone() {
		return fcellphone;
	}

	public void setFcellphone(String fcellphone) {
		this.fcellphone = fcellphone;
	}
	
	@Email(message="邮箱必须为合法邮箱")
	@ExcelField(title="邮箱", align=2, sort=11)
	public String getFemail() {
		return femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}
	
	@ExcelField(title="传真", align=2, sort=12)
	public String getFfax() {
		return ffax;
	}

	public void setFfax(String ffax) {
		this.ffax = ffax;
	}
	
	@ExcelField(title="银行账户", align=2, sort=13)
	public String getFbankaccount() {
		return fbankaccount;
	}

	public void setFbankaccount(String fbankaccount) {
		this.fbankaccount = fbankaccount;
	}
	
	@ExcelField(title="银行账号", align=2, sort=14)
	public String getFbankaccountnum() {
		return fbankaccountnum;
	}

	public void setFbankaccountnum(String fbankaccountnum) {
		this.fbankaccountnum = fbankaccountnum;
	}
	
	@ExcelField(title="开户行", align=2, sort=15)
	public String getFbank() {
		return fbank;
	}

	public void setFbank(String fbank) {
		this.fbank = fbank;
	}
	
	@ExcelField(title="开户人姓名", align=2, sort=16)
	public String getFbankaccountname() {
		return fbankaccountname;
	}

	public void setFbankaccountname(String fbankaccountname) {
		this.fbankaccountname = fbankaccountname;
	}
	
	@ExcelField(title="开户人身份证", align=2, sort=17)
	public String getFbankaccountpersonid() {
		return fbankaccountpersonid;
	}

	public void setFbankaccountpersonid(String fbankaccountpersonid) {
		this.fbankaccountpersonid = fbankaccountpersonid;
	}
	
	@ExcelField(title="开户预留手机号", align=2, sort=18)
	public String getFbankphone() {
		return fbankphone;
	}

	public void setFbankphone(String fbankphone) {
		this.fbankphone = fbankphone;
	}
	
	@ExcelField(title="客户欠款", align=2, sort=9)
	public String getFdebt() {
		return fdebt;
	}

	public void setFdebt(String fdebt) {
		this.fdebt = fdebt;
	}
	
	@ExcelField(title="排序", align=2, sort=19)
	public Integer getFsort() {
		return fsort;
	}

	public void setFsort(Integer fsort) {
		this.fsort = fsort;
	}
	
	@ExcelField(title="客户地址", align=2, sort=20)
	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	//（0：自提，1：车送，2：物流）
	@ExcelField(title="发货方式", dictType="shipment_type", align=2, sort=21)
	public Integer getFshipmenttype() {
		return fshipmenttype;
	}

	public void setFshipmenttype(Integer fshipmenttype) {
		this.fshipmenttype = fshipmenttype;
	}
	//（0：无，1：有）
//	@ExcelField(title="是否有关联账号", dictType="fIsRelate", align=2, sort=20)
	public Integer getFisrelate() {
		return fisrelate;
	}

	public void setFisrelate(Integer fisrelate) {
		this.fisrelate = fisrelate;
	}
	//（1：启用，2：失效）
	@ExcelField(title="状态", dictType="sys_status", align=2, sort=22)
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
	//（0：微信，1：支付宝，2：银行账户）
//	@ExcelField(title="支付方式",dictType="pay_type", align=2, sort=22)
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
	
	@ExcelField(title="客户分类", align=2, sort=1)
	public String getFcatename() {
		return fcatename;
	}

	public void setFcatename(String fcatename) {
		this.fcatename = fcatename;
	}
	
	
//	@ExcelField(title="所属商户", align=2, sort=32)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}

	@ExcelField(title="所属门店", align=2, sort=8)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
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
		
	public String getBeginFdebt() {
		return beginFdebt;
	}

	public void setBeginFdebt(String beginFdebt) {
		this.beginFdebt = beginFdebt;
	}
	
	public String getEndFdebt() {
		return endFdebt;
	}

	public void setEndFdebt(String endFdebt) {
		this.endFdebt = endFdebt;
	}
		
}