/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web.supplieraccount;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.account.entity.supplieraccount.SupplierAccount;
import com.jeeplus.modules.account.service.capitalaccount.CapitalAccountService;
import com.jeeplus.modules.account.service.clearingaccount.ClearingAccountService;
import com.jeeplus.modules.account.service.supplieraccount.SupplierAccountService;

/**
 * 供应商对账Controller
 * @author 金圣智
 * @version 2017-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/account/supplieraccount/supplierAccount")
public class SupplierAccountController extends BaseController {

	@Autowired
	private SupplierAccountService supplierAccountService;//供应商对账service
	
	@Autowired
	private ClearingAccountService clearingAccountService;//结算账户service
	
	@Autowired
	private CapitalAccountService capitalAccountService;//账户资金流水service
	
	@ModelAttribute
	public SupplierAccount get(@RequestParam(required=false) String id) {
		SupplierAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supplierAccountService.get(id);
		}
		if (entity == null){
			entity = new SupplierAccount();
		}
		return entity;
	}
	
	/**
	 * 供应商对账/付款列表页面
	 */
	@RequiresPermissions("account:supplieraccount:supplierAccount:list")
	@RequestMapping(value = {"list", ""})
	public String list(SupplierAccount supplierAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SupplierAccount> page = supplierAccountService.findPage(new Page<SupplierAccount>(request, response), supplierAccount); 
		model.addAttribute("page", page);
		return "modules/account/supplieraccount/supplierAccountList";
	}

	/**
	 * 查看，增加，编辑供应商对账/付款表单页面
	 */
	@RequiresPermissions(value={"account:supplieraccount:supplierAccount:view","account:supplieraccount:supplierAccount:add","account:supplieraccount:supplierAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SupplierAccount supplierAccount, Model model) {
		model.addAttribute("supplierAccount", supplierAccount);
		return "modules/account/supplieraccount/supplierAccountForm";
	}

	/**
	 * 保存供应商对账/付款
	 */
	@RequiresPermissions(value={"account:supplieraccount:supplierAccount:add","account:supplieraccount:supplierAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SupplierAccount supplierAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, supplierAccount)){
			return form(supplierAccount, model);
		}
		
		//获取当前用户
		User user = UserUtils.getUser();
		Office company = user.getCompany();//商户
		Office office = user.getOffice();//门店
		Date date = new Date();
		
		if(!supplierAccount.getIsNewRecord()){//编辑表单保存
			SupplierAccount t = supplierAccountService.get(supplierAccount.getId());//从数据库取出记录的值
			//获取未修改前的数据，并将之前扣除的计算账户金额补回
			ClearingAccount clearingAccount = clearingAccountService.get(t.getClearingAccount());
			
			
			String balance ="0";
			String balance2 = "0";
			String fpayAmount ="0";
			String fpayAmount2 ="0";
			BigDecimal balance3= new BigDecimal(0);
			if(clearingAccount!=null){
				balance = clearingAccount.getFbalance();
				fpayAmount = t.getFpayAmount();// 原实付金额
				fpayAmount2 = supplierAccount.getFpayAmount();// 现实付金额	
				balance3=new BigDecimal(balance).add(new BigDecimal(fpayAmount));
				balance2= balance3.subtract(new BigDecimal(fpayAmount2)).toString();
				// 如果先前实付款和修改实付款不同则重新设置结算账户的余额
				if (!fpayAmount.equals(fpayAmount2)) {
					try {
						clearingAccount.setFbalance(balance2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					clearingAccountService.save(clearingAccount);
				}
			}else{
				
				try {
					throw new RuntimeException("没获取到结算账户对象！！！");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					addMessage(redirectAttributes, "哎呀！~系统运转失败啦！！！正在努力解决，客官稍等！！！");
					return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
				}

			}
				
			MyBeanUtils.copyBeanNotNull2Bean(supplierAccount, t);//将编辑表单中的非NULL值覆盖数据库记录中的值

			//订单执行完毕添加资金流水
			CapitalAccount capitalAccount=new CapitalAccount();
			//设置资金流水属性
			capitalAccount.setFbusinessHours(date);//业务时间
			capitalAccount.setAccountType(t.getAccountType());//账目类型
			capitalAccount.setClearingAccount(t.getClearingAccount());//结算账户
			capitalAccount.setFtrader(user);//交易人
			capitalAccount.setFoddNumbers(t.getFoddNumbers());//单号
			capitalAccount.setFinitialamount(balance3.toString());//期初金额
			capitalAccount.setFaccountBalance(balance2);//账号余额
			capitalAccount.setFexpenditureflag(0);//收入/支出（0支出，1收入）
			capitalAccount.setFincome("0");//收入
			capitalAccount.setFexpenditure(fpayAmount2);//支出
			BigDecimal fprofit=new BigDecimal(0);
			try {
				fprofit = new BigDecimal(0).subtract(new BigDecimal(fpayAmount2==""?"0":fpayAmount2));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			capitalAccount.setFprofit(fprofit.toString());//盈利
			capitalAccount.setFsponsor(company);//商户
			capitalAccount.setFstore(office);//门店
			capitalAccount.setRemarks(t.getRemarks());//备注
			capitalAccountService.save(capitalAccount);//保存资金流水
			
			//重新计算应付余额
			try {
				setFsolehandlingamount(t);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			supplierAccountService.save(t);//保存
		}else{//新增表单保存
			//生成单号
			StringBuffer buffer = new StringBuffer("FK");
			String str = OddNumbers.getOrderNo();
			buffer.append(str);
			supplierAccount.setFoddNumbers(buffer.toString());//设置单号
			
			//获取结算账户
			ClearingAccount clearingaccount =null;
			try {
				clearingaccount = clearingAccountService.get(supplierAccount.getClearingAccount());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String fpayAmount="";
			String fbalance="";
			BigDecimal accountBalance =new BigDecimal(0);
			if(clearingaccount!=null){
				fpayAmount = supplierAccount.getFpayAmount();//实付款
				fbalance = clearingaccount.getFbalance();//结算账号前余额
				accountBalance = new BigDecimal(fbalance).subtract(new BigDecimal(fpayAmount));//计算新余额
				clearingaccount.setFbalance(accountBalance.toString());//设置账户当前余额
				clearingAccountService.save(clearingaccount);
			}else{
				try {
					throw new RuntimeException("没获取到结算账户对象！！！");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					addMessage(redirectAttributes, "哎呀！~系统运转失败啦！！！正在努力解决，客官稍等！！！");
					return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
				}
			}
			
			//订单执行完毕添加资金流水
			CapitalAccount capitalAccount=new CapitalAccount();
			//设置资金流水属性
			capitalAccount.setFbusinessHours(date);//业务时间
			capitalAccount.setAccountType(supplierAccount.getAccountType());//账目类型
			capitalAccount.setClearingAccount(clearingaccount);//结算账户
			capitalAccount.setFtrader(user);//交易人
			capitalAccount.setFoddNumbers(supplierAccount.getFoddNumbers());//单号
			capitalAccount.setFinitialamount(fbalance);//期初金额
			capitalAccount.setFaccountBalance(accountBalance.toString());//账号余额
			capitalAccount.setFexpenditureflag(0);//收入/支出（0支出，1收入）
			capitalAccount.setFincome("0");//收入
			capitalAccount.setFexpenditure(fpayAmount);//支出
			BigDecimal fprofit=new BigDecimal(0);
			try {
				fprofit = new BigDecimal(0).subtract(new BigDecimal(fpayAmount==""?"0":fpayAmount));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			capitalAccount.setFprofit(fprofit.toString());//盈利
			capitalAccount.setFsponsor(company);//商户
			capitalAccount.setFstore(office);//门店
			capitalAccount.setRemarks(supplierAccount.getRemarks());//备注
			capitalAccountService.save(capitalAccount);//保存资金流水
			
			//设置供应商对账/付款所属商户和门店
			supplierAccount.setFsponsor(company);
			supplierAccount.setFstore(office);
			supplierAccount.setFbusinessHours(date);
			//计算应付余额
			try {
				setFsolehandlingamount(supplierAccount);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			supplierAccountService.save(supplierAccount);//保存

		}
		addMessage(redirectAttributes, "保存供应商对账/付款成功");
		return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
	}
	
	
	/**
	 * 设置应付余额
	 * @param supplierAccount
	 */
	private void setFsolehandlingamount(SupplierAccount supplierAccount) throws Exception{
		String famountPay = supplierAccount.getFamountPay();//应付金额
		String fpayAmount = supplierAccount.getFpayAmount();//实付金额
		String fpreferentialAmount = supplierAccount.getFpreferentialAmount();//优惠金额
		//应付金额减去实付金额再减去优惠金额
		BigDecimal subtract = (new BigDecimal(famountPay).subtract(new BigDecimal(fpayAmount))).subtract(new BigDecimal(fpreferentialAmount));
		supplierAccount.setFsolehandlingAmount(subtract.toString());
	}
	
	
	/**
	 * 逻辑删除供应商对账/付款
	 */
	@RequiresPermissions("account:supplieraccount:supplierAccount:del")
	@RequestMapping(value = "delete")
	public String delete(SupplierAccount supplierAccount, RedirectAttributes redirectAttributes) {
		supplierAccountService.deleteByLogic(supplierAccount);
		addMessage(redirectAttributes, "删除供应商对账/付款成功");
		return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
	}
	
	/**
	 * 批量逻辑删除供应商对账/付款
	 */
	@RequiresPermissions("account:supplieraccount:supplierAccount:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			supplierAccountService.deleteByLogic(supplierAccountService.get(id));
		}
		addMessage(redirectAttributes, "删除供应商对账/付款成功");
		return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:supplieraccount:supplierAccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SupplierAccount supplierAccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商对账/付款"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SupplierAccount> page = supplierAccountService.findPage(new Page<SupplierAccount>(request, response, -1), supplierAccount);
    		new ExportExcel("供应商对账/付款", SupplierAccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出供应商对账/付款记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:supplieraccount:supplierAccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SupplierAccount> list = ei.getDataList(SupplierAccount.class);
			for (SupplierAccount supplierAccount : list){
				try{
					supplierAccountService.save(supplierAccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条供应商对账/付款记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条供应商对账/付款记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入供应商对账/付款失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
    }
	
	/**
	 * 下载导入供应商对账/付款数据模板
	 */
	@RequiresPermissions("account:supplieraccount:supplierAccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商对账/付款数据导入模板.xlsx";
    		List<SupplierAccount> list = Lists.newArrayList(); 
    		new ExportExcel("供应商对账/付款数据", SupplierAccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/supplieraccount/supplierAccount/?repage";
    }
	
	
	/**
	 * 选择供应商名称
	 */
	@RequestMapping(value = "selectsupplierBasic")
	public String selectsupplierBasic(SupplierBasic supplierBasic, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SupplierBasic> page = supplierAccountService.findPageBysupplierBasic(new Page<SupplierBasic>(request, response),  supplierBasic);
		try {
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", supplierBasic);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择账目类型
	 */
	@RequestMapping(value = "selectaccountType")
	public String selectaccountType(AccountType accountType, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AccountType> page = supplierAccountService.findPageByaccountType(new Page<AccountType>(request, response),  accountType);
		try {
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", accountType);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择结算账户
	 */
	@RequestMapping(value = "selectclearingAccount")
	public String selectclearingAccount(ClearingAccount clearingAccount, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ClearingAccount> page = supplierAccountService.findPageByclearingAccount(new Page<ClearingAccount>(request, response),  clearingAccount);
		try {
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", clearingAccount);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	
	
	/**
	 * 根据订单号查询单条数据用于其他页面查看订单信息
	 * 
	 * @param 此处numid的值为单号
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "account:supplieraccount:supplierAccount:view" }, logical = Logical.OR)
	@RequestMapping("formbyordernum")
	public String formbyordernum(String numid, Model model) {
		// 回显对象属性
		SupplierAccount sa = null;
		try {
			sa = supplierAccountService.findUniqueByProperty("foddnumbers", numid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sa != null) {
			sa = supplierAccountService.get(sa.getId());
			model.addAttribute("supplierAccount", sa);
		}

		return "modules/account/supplieraccount/supplierAccountForm";

	}
	
	
	
}