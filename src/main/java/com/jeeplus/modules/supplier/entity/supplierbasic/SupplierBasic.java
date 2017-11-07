/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.supplier.entity.supplierbasic;

import org.hibernate.validator.constraints.Email;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 供应商管理Entity
 * @author 金圣智
 * @version 2017-06-06
 */
public class SupplierBasic extends DataEntity<SupplierBasic> {
	
	private static final long serialVersionUID = 1L;
	private String fname;		// 供应商名称
	private String ffullname;		// 全名
	private String fbrief;		// 简介
	private String fwebsite;		// 网址
	private String fbankid;		// 开户行ID
	private String fbank;		// 开户行
	private String fbankaccoun;		// 开户行账号
	private String fbankaccountname;		// 开户人姓名
	private String fbankaccountpersonId;		// 开户人身份证
	private String fbankphone;		// 银行预留手机
	private String faddress;		// 地址
	private String fbdid;		// 负责人
	private String freminder;		// 温馨提示
	private String femail;		// 邮箱
	private Integer fsort;		// 排序
	private String ffax;		// 传真
	private String fdiscount;		// 折扣
	private String fdebt;		// 欠款
	private String flinkman;		// 联系人
	private String fcellphone;		// 联系人手机
	private Integer fstatus;		// 供应商状态
	private Office fsponsor;		//所属商户
	private Office fstore;     		//门店
	public SupplierBasic() {
		super();
	}

	public SupplierBasic(String id){
		super(id);
	}

	@ExcelField(title="供应商名称", align=2, sort=1)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@ExcelField(title="全名", align=2, sort=2)
	public String getFfullname() {
		return ffullname;
	}

	public void setFfullname(String ffullname) {
		this.ffullname = ffullname;
	}
	
	@ExcelField(title="简介", align=2, sort=3)
	public String getFbrief() {
		return fbrief;
	}

	public void setFbrief(String fbrief) {
		this.fbrief = fbrief;
	}
	
	@ExcelField(title="网址", align=2, sort=4)
	public String getFwebsite() {
		return fwebsite;
	}

	public void setFwebsite(String fwebsite) {
		this.fwebsite = fwebsite;
	}
	
	@ExcelField(title="开户行ID", align=2, sort=5)
	public String getFbankid() {
		return fbankid;
	}

	public void setFbankid(String fbankid) {
		this.fbankid = fbankid;
	}
	
	@ExcelField(title="开户行", align=2, sort=6)
	public String getFbank() {
		return fbank;
	}

	public void setFbank(String fbank) {
		this.fbank = fbank;
	}
	
	@ExcelField(title="开户行账号", align=2, sort=7)
	public String getFbankaccoun() {
		return fbankaccoun;
	}

	public void setFbankaccoun(String fbankaccoun) {
		this.fbankaccoun = fbankaccoun;
	}
	
	@ExcelField(title="开户人姓名", align=2, sort=8)
	public String getFbankaccountname() {
		return fbankaccountname;
	}

	public void setFbankaccountname(String fbankaccountname) {
		this.fbankaccountname = fbankaccountname;
	}
	
	@ExcelField(title="开户人身份证", align=2, sort=9)
	public String getFbankaccountpersonId() {
		return fbankaccountpersonId;
	}

	public void setFbankaccountpersonId(String fbankaccountpersonId) {
		this.fbankaccountpersonId = fbankaccountpersonId;
	}
	
	@ExcelField(title="银行预留手机", align=2, sort=10)
	public String getFbankphone() {
		return fbankphone;
	}

	public void setFbankphone(String fbankphone) {
		this.fbankphone = fbankphone;
	}
	
	@ExcelField(title="地址", align=2, sort=11)
	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	
	@ExcelField(title="负责人", align=2, sort=12)
	public String getFbdid() {
		return fbdid;
	}

	public void setFbdid(String fbdid) {
		this.fbdid = fbdid;
	}
	
	@ExcelField(title="温馨提示", align=2, sort=13)
	public String getFreminder() {
		return freminder;
	}

	public void setFreminder(String freminder) {
		this.freminder = freminder;
	}
	
	@Email(message="邮箱必须为合法邮箱")
	@ExcelField(title="邮箱", align=2, sort=14)
	public String getFemail() {
		return femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}
	
	@ExcelField(title="排序", align=2, sort=15)
	public Integer getFsort() {
		return fsort;
	}

	public void setFsort(Integer fsort) {
		this.fsort = fsort;
	}
	
	@ExcelField(title="传真", align=2, sort=16)
	public String getFfax() {
		return ffax;
	}

	public void setFfax(String ffax) {
		this.ffax = ffax;
	}
	
	@ExcelField(title="折扣", align=2, sort=17)
	public String getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(String fdiscount) {
		this.fdiscount = fdiscount;
	}
	
	@ExcelField(title="欠款", align=2, sort=18)
	public String getFdebt() {
		return fdebt;
	}

	public void setFdebt(String fdebt) {
		this.fdebt = fdebt;
	}
	
	@ExcelField(title="联系人", align=2, sort=19)
	public String getFlinkman() {
		return flinkman;
	}

	public void setFlinkman(String flinkman) {
		this.flinkman = flinkman;
	}
	
	@ExcelField(title="联系人手机", align=2, sort=20)
	public String getFcellphone() {
		return fcellphone;
	}

	public void setFcellphone(String fcellphone) {
		this.fcellphone = fcellphone;
	}
	
	@ExcelField(title="供应商状态", dictType="sys_status", align=2, sort=27)
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
//	@ExcelField(title="商户",align=2, sort=28)
	public Office getFsponsor() {
		return fsponsor;
	}

	public void setFsponsor(Office fsponsor) {
		this.fsponsor = fsponsor;
	}
	@ExcelField(title="门店",align=2, sort=28)
	public Office getFstore() {
		return fstore;
	}

	public void setFstore(Office fstore) {
		this.fstore = fstore;
	}
	
}