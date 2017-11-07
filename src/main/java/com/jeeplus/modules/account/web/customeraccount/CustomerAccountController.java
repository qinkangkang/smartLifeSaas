/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web.customeraccount;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.account.entity.customeraccount.CustomerAccount;
import com.jeeplus.modules.account.service.customeraccount.CustomerAccountService;

/**
 * 客户对账单Controller
 * @author 金圣智
 * @version 2017-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/account/customeraccount/customerAccount")
public class CustomerAccountController extends BaseController {

	@Autowired
	private CustomerAccountService customerAccountService;
	
	@ModelAttribute
	public CustomerAccount get(@RequestParam(required=false) String id) {
		CustomerAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerAccountService.get(id);
		}
		if (entity == null){
			entity = new CustomerAccount();
		}
		return entity;
	}
	
	/**
	 * 客户对账列表页面
	 */
	@RequiresPermissions("account:customeraccount:customerAccount:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerAccount customerAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerAccount> page = customerAccountService.findPage(new Page<CustomerAccount>(request, response), customerAccount); 
		model.addAttribute("page", page);
		return "modules/account/customeraccount/customerAccountList";
	}

	/**
	 * 查看，增加，编辑客户对账表单页面
	 */
	@RequiresPermissions(value={"account:customeraccount:customerAccount:view","account:customeraccount:customerAccount:add","account:customeraccount:customerAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CustomerAccount customerAccount, Model model) {
		model.addAttribute("customerAccount", customerAccount);
		return "modules/account/customeraccount/customerAccountForm";
	}

	/**
	 * 保存客户对账
	 */
	@RequiresPermissions(value={"account:customeraccount:customerAccount:add","account:customeraccount:customerAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CustomerAccount customerAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, customerAccount)){
			return form(customerAccount, model);
		}
		if(!customerAccount.getIsNewRecord()){//编辑表单保存
			CustomerAccount t = customerAccountService.get(customerAccount.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(customerAccount, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			customerAccountService.save(t);//保存
		}else{//新增表单保存
			customerAccountService.save(customerAccount);//保存
		}
		addMessage(redirectAttributes, "保存客户对账成功");
		return "redirect:"+Global.getAdminPath()+"/account/customeraccount/customerAccount/?repage";
	}
	
	/**
	 * 逻辑删除客户对账
	 */
	@RequiresPermissions("account:customeraccount:customerAccount:del")
	@RequestMapping(value = "delete")
	public String delete(CustomerAccount customerAccount, RedirectAttributes redirectAttributes) {
		customerAccountService.deleteByLogic(customerAccount);
		addMessage(redirectAttributes, "删除客户对账成功");
		return "redirect:"+Global.getAdminPath()+"/account/customeraccount/customerAccount/?repage";
	}
	
	/**
	 * 批量逻辑删除客户对账
	 */
	@RequiresPermissions("account:customeraccount:customerAccount:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerAccountService.deleteByLogic(customerAccountService.get(id));
		}
		addMessage(redirectAttributes, "删除客户对账成功");
		return "redirect:"+Global.getAdminPath()+"/account/customeraccount/customerAccount/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:customeraccount:customerAccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(CustomerAccount customerAccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户对账"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerAccount> page = customerAccountService.findPage(new Page<CustomerAccount>(request, response, -1), customerAccount);
    		new ExportExcel("客户对账", CustomerAccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出客户对账记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/customeraccount/customerAccount/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:customeraccount:customerAccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerAccount> list = ei.getDataList(CustomerAccount.class);
			for (CustomerAccount customerAccount : list){
				try{
					customerAccountService.save(customerAccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户对账记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户对账记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户对账失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/customeraccount/customerAccount/?repage";
    }
	
	/**
	 * 下载导入客户对账数据模板
	 */
	@RequiresPermissions("account:customeraccount:customerAccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户对账数据导入模板.xlsx";
    		List<CustomerAccount> list = Lists.newArrayList(); 
    		new ExportExcel("客户对账数据", CustomerAccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/customeraccount/customerAccount/?repage";
    }
	
	
	/**
	 * 选择客户名称
	 */
	@RequestMapping(value = "selectcustomerBasic")
	public String selectcustomerBasic(CustomerBasic customerBasic, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerBasic> page = customerAccountService.findPageBycustomerBasic(new Page<CustomerBasic>(request, response),  customerBasic);
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
		model.addAttribute("obj", customerBasic);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择客户分类
	 */
	@RequestMapping(value = "selectcustomerCate")
	public String selectcustomerCate(CustomerCate customerCate, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerCate> page = customerAccountService.findPageBycustomerCate(new Page<CustomerCate>(request, response),  customerCate);
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
		model.addAttribute("obj", customerCate);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择账目类型
	 */
	@RequestMapping(value = "selectaccountType")
	public String selectaccountType(AccountType accountType, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AccountType> page = customerAccountService.findPageByaccountType(new Page<AccountType>(request, response),  accountType);
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
		Page<ClearingAccount> page = customerAccountService.findPageByclearingAccount(new Page<ClearingAccount>(request, response),  clearingAccount);
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
	

}