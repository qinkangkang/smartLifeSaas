/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.entity.info;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 线上客户Entity
 * @author diqiang
 * @version 2017-06-06
 */
public class CustomerInfo extends DataEntity<CustomerInfo> {
	
	private static final long serialVersionUID = 1L;
	private String fcustomerbasicid;		// 客户基本信息表ID
	private Integer fisrelate;		// 是否有关联账户（0：无，1：有）
	private String fusername;		// 登录名
	private String fpassword;		// 登录密码
	private Integer fregisterchannel;		// 注册渠道（0：微信，2：QQ，3：其他）
	private Date flogintime;		// 登录时间
	private Date flogouttime;		// 登出时间
	private String fphoto;		// 客户头像
	private String fweixinid;		// 微信openID
	private String fweixinunionid;		// 微信唯一ID
	private String fweixinname;		// 微信名
	private String fregion;		// 所在地区
	private Integer fsex;		// 性别（1：男，2：女）
	private String fticket;		// 客户认证票
	private String flinkman;		// 联系人
	private String fcellphone;		// 手机
	private String femail;		// 邮箱
	private String fphone;		// 电话
	private String ffax;		// 传真
	private String fbankaccount;		// 银行账户
	private String fbankaccountnum;		// 银行账号
	private String fbank;		// 开户行
	private String fbankaccountname;		// 开户人姓名
	private String fbankaccountpersonid;		// 开户人身份证
	private String fbankphone;		// 预留电话
	private String faddress;		// 地址
	private String fdiscount;		// 折扣
	private Integer fshipmenttype;		// 发货方式（0：自提，1：车送，2：物流）
	private Integer fpaytype;		// 支付方式（0：支付宝，1：微信，2：pos机）
	private String fdebt;		// 欠款
	private Integer fsort;		// 排序
	private String fcustomercateid;		// 客户分类ID
	private String fstatus;		// 客户状态（1：启用，2：未启用）
	private String fcatename;		// 分类名称
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Date beginFlogintime;		// 开始 登录时间
	private Date endFlogintime;		// 结束 登录时间
	private String beginFdebt;		// 开始 欠款
	private String endFdebt;		// 结束 欠款
	
	public CustomerInfo() {
		super();
	}

	public CustomerInfo(String id){
		super(id);
	}

	@ExcelField(title="客户基本信息表ID", align=2, sort=7)
	public String getFcustomerbasicid() {
		return fcustomerbasicid;
	}

	public void setFcustomerbasicid(String fcustomerbasicid) {
		this.fcustomerbasicid = fcustomerbasicid;
	}
	
	@ExcelField(title="是否有关联账户（0：无，1：有）", dictType="fIsRelate", align=2, sort=8)
	public Integer getFisrelate() {
		return fisrelate;
	}

	public void setFisrelate(Integer fisrelate) {
		this.fisrelate = fisrelate;
	}
	
	@ExcelField(title="登录名", align=2, sort=9)
	public String getFusername() {
		return fusername;
	}

	public void setFusername(String fusername) {
		this.fusername = fusername;
	}
	
	@ExcelField(title="登录密码", align=2, sort=10)
	public String getFpassword() {
		return fpassword;
	}

	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}
	
	@ExcelField(title="注册渠道（0：微信，2：QQ，3：其他）", align=2, sort=11)
	public Integer getFregisterchannel() {
		return fregisterchannel;
	}

	public void setFregisterchannel(Integer fregisterchannel) {
		this.fregisterchannel = fregisterchannel;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="登录时间", align=2, sort=12)
	public Date getFlogintime() {
		return flogintime;
	}

	public void setFlogintime(Date flogintime) {
		this.flogintime = flogintime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="登出时间", align=2, sort=13)
	public Date getFlogouttime() {
		return flogouttime;
	}

	public void setFlogouttime(Date flogouttime) {
		this.flogouttime = flogouttime;
	}
	
	@ExcelField(title="客户头像", align=2, sort=14)
	public String getFphoto() {
		return fphoto;
	}

	public void setFphoto(String fphoto) {
		this.fphoto = fphoto;
	}
	
	@ExcelField(title="微信openID", align=2, sort=15)
	public String getFweixinid() {
		return fweixinid;
	}

	public void setFweixinid(String fweixinid) {
		this.fweixinid = fweixinid;
	}
	
	@ExcelField(title="微信唯一ID", align=2, sort=16)
	public String getFweixinunionid() {
		return fweixinunionid;
	}

	public void setFweixinunionid(String fweixinunionid) {
		this.fweixinunionid = fweixinunionid;
	}
	
	@ExcelField(title="微信名", align=2, sort=17)
	public String getFweixinname() {
		return fweixinname;
	}

	public void setFweixinname(String fweixinname) {
		this.fweixinname = fweixinname;
	}
	
	@ExcelField(title="所在地区", align=2, sort=18)
	public String getFregion() {
		return fregion;
	}

	public void setFregion(String fregion) {
		this.fregion = fregion;
	}
	
	@ExcelField(title="性别（1：男，2：女）", dictType="fSex", align=2, sort=19)
	public Integer getFsex() {
		return fsex;
	}

	public void setFsex(Integer fsex) {
		this.fsex = fsex;
	}
	
	@ExcelField(title="客户认证票", align=2, sort=20)
	public String getFticket() {
		return fticket;
	}

	public void setFticket(String fticket) {
		this.fticket = fticket;
	}
	
	@ExcelField(title="联系人", align=2, sort=21)
	public String getFlinkman() {
		return flinkman;
	}

	public void setFlinkman(String flinkman) {
		this.flinkman = flinkman;
	}
	
	@ExcelField(title="手机", align=2, sort=22)
	public String getFcellphone() {
		return fcellphone;
	}

	public void setFcellphone(String fcellphone) {
		this.fcellphone = fcellphone;
	}
	
	@ExcelField(title="邮箱", align=2, sort=23)
	public String getFemail() {
		return femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}
	
	@ExcelField(title="电话", align=2, sort=24)
	public String getFphone() {
		return fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}
	
	@ExcelField(title="传真", align=2, sort=25)
	public String getFfax() {
		return ffax;
	}

	public void setFfax(String ffax) {
		this.ffax = ffax;
	}
	
	@ExcelField(title="银行账户", align=2, sort=26)
	public String getFbankaccount() {
		return fbankaccount;
	}

	public void setFbankaccount(String fbankaccount) {
		this.fbankaccount = fbankaccount;
	}
	
	@ExcelField(title="银行账号", align=2, sort=27)
	public String getFbankaccountnum() {
		return fbankaccountnum;
	}

	public void setFbankaccountnum(String fbankaccountnum) {
		this.fbankaccountnum = fbankaccountnum;
	}
	
	@ExcelField(title="开户行", align=2, sort=28)
	public String getFbank() {
		return fbank;
	}

	public void setFbank(String fbank) {
		this.fbank = fbank;
	}
	
	@ExcelField(title="开户人姓名", align=2, sort=29)
	public String getFbankaccountname() {
		return fbankaccountname;
	}

	public void setFbankaccountname(String fbankaccountname) {
		this.fbankaccountname = fbankaccountname;
	}
	
	@ExcelField(title="开户人身份证", align=2, sort=30)
	public String getFbankaccountpersonid() {
		return fbankaccountpersonid;
	}

	public void setFbankaccountpersonid(String fbankaccountpersonid) {
		this.fbankaccountpersonid = fbankaccountpersonid;
	}
	
	@ExcelField(title="预留电话", align=2, sort=31)
	public String getFbankphone() {
		return fbankphone;
	}

	public void setFbankphone(String fbankphone) {
		this.fbankphone = fbankphone;
	}
	
	@ExcelField(title="地址", align=2, sort=32)
	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	
	@ExcelField(title="折扣", align=2, sort=33)
	public String getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(String fdiscount) {
		this.fdiscount = fdiscount;
	}
	
	@ExcelField(title="发货方式（0：自提，1：车送，2：物流）", dictType="", align=2, sort=34)
	public Integer getFshipmenttype() {
		return fshipmenttype;
	}

	public void setFshipmenttype(Integer fshipmenttype) {
		this.fshipmenttype = fshipmenttype;
	}
	
	@ExcelField(title="支付方式（0：支付宝，1：微信，2：pos机）", dictType="", align=2, sort=35)
	public Integer getFpaytype() {
		return fpaytype;
	}

	public void setFpaytype(Integer fpaytype) {
		this.fpaytype = fpaytype;
	}
	
	@ExcelField(title="欠款", align=2, sort=36)
	public String getFdebt() {
		return fdebt;
	}

	public void setFdebt(String fdebt) {
		this.fdebt = fdebt;
	}
	
	@ExcelField(title="排序", align=2, sort=37)
	public Integer getFsort() {
		return fsort;
	}

	public void setFsort(Integer fsort) {
		this.fsort = fsort;
	}
	
	@ExcelField(title="客户分类ID", dictType="", align=2, sort=38)
	public String getFcustomercateid() {
		return fcustomercateid;
	}

	public void setFcustomercateid(String fcustomercateid) {
		this.fcustomercateid = fcustomercateid;
	}
	
	@ExcelField(title="客户状态（1：启用，2：未启用）", dictType="fStatus", align=2, sort=39)
	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	
	@ExcelField(title="分类名称", align=2, sort=40)
	public String getFcatename() {
		return fcatename;
	}

	public void setFcatename(String fcatename) {
		this.fcatename = fcatename;
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
		
	public Date getBeginFlogintime() {
		return beginFlogintime;
	}

	public void setBeginFlogintime(Date beginFlogintime) {
		this.beginFlogintime = beginFlogintime;
	}
	
	public Date getEndFlogintime() {
		return endFlogintime;
	}

	public void setEndFlogintime(Date endFlogintime) {
		this.endFlogintime = endFlogintime;
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