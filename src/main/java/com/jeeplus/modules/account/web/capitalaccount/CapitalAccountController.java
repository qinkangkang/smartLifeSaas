/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web.capitalaccount;

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
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;
import com.jeeplus.modules.account.service.capitalaccount.CapitalAccountService;

/**
 * 资金流水账Controller
 * @author 金圣智
 * @version 2017-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/account/capitalaccount/capitalAccount")
public class CapitalAccountController extends BaseController {

	@Autowired
	private CapitalAccountService capitalAccountService;
	
	@ModelAttribute
	public CapitalAccount get(@RequestParam(required=false) String id) {
		CapitalAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = capitalAccountService.get(id);
		}
		if (entity == null){
			entity = new CapitalAccount();
		}
		return entity;
	}
	
	/**
	 * 资金流水账列表页面
	 */
	@RequiresPermissions("account:capitalaccount:capitalAccount:list")
	@RequestMapping(value = {"list", ""})
	public String list(CapitalAccount capitalAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CapitalAccount> page = capitalAccountService.findPage(new Page<CapitalAccount>(request, response), capitalAccount); 
		model.addAttribute("page", page);
		return "modules/account/capitalaccount/capitalAccountList";
	}

	/**
	 * 查看，增加，编辑资金流水账表单页面
	 */
	@RequiresPermissions(value={"account:capitalaccount:capitalAccount:view","account:capitalaccount:capitalAccount:add","account:capitalaccount:capitalAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CapitalAccount capitalAccount, Model model) {
		model.addAttribute("capitalAccount", capitalAccount);
		return "modules/account/capitalaccount/capitalAccountForm";
	}

	/**
	 * 保存资金流水账
	 */
	@RequiresPermissions(value={"account:capitalaccount:capitalAccount:add","account:capitalaccount:capitalAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CapitalAccount capitalAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, capitalAccount)){
			return form(capitalAccount, model);
		}
		if(!capitalAccount.getIsNewRecord()){//编辑表单保存
			CapitalAccount t = capitalAccountService.get(capitalAccount.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(capitalAccount, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			capitalAccountService.save(t);//保存
		}else{//新增表单保存
			capitalAccountService.save(capitalAccount);//保存
		}
		addMessage(redirectAttributes, "保存资金流水账成功");
		return "redirect:"+Global.getAdminPath()+"/account/capitalaccount/capitalAccount/?repage";
	}
	
	/**
	 * 逻辑删除资金流水账
	 */
	@RequiresPermissions("account:capitalaccount:capitalAccount:del")
	@RequestMapping(value = "delete")
	public String delete(CapitalAccount capitalAccount, RedirectAttributes redirectAttributes) {
		capitalAccountService.deleteByLogic(capitalAccount);
		addMessage(redirectAttributes, "删除资金流水账成功");
		return "redirect:"+Global.getAdminPath()+"/account/capitalaccount/capitalAccount/?repage";
	}
	
	/**
	 * 批量逻辑删除资金流水账
	 */
	@RequiresPermissions("account:capitalaccount:capitalAccount:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			capitalAccountService.deleteByLogic(capitalAccountService.get(id));
		}
		addMessage(redirectAttributes, "删除资金流水账成功");
		return "redirect:"+Global.getAdminPath()+"/account/capitalaccount/capitalAccount/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:capitalaccount:capitalAccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(CapitalAccount capitalAccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "资金流水账"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CapitalAccount> page = capitalAccountService.findPage(new Page<CapitalAccount>(request, response, -1), capitalAccount);
    		new ExportExcel("资金流水账", CapitalAccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出资金流水账记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/capitalaccount/capitalAccount/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:capitalaccount:capitalAccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CapitalAccount> list = ei.getDataList(CapitalAccount.class);
			for (CapitalAccount capitalAccount : list){
				try{
					capitalAccountService.save(capitalAccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条资金流水账记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条资金流水账记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入资金流水账失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/capitalaccount/capitalAccount/?repage";
    }
	
	/**
	 * 下载导入资金流水账数据模板
	 */
	@RequiresPermissions("account:capitalaccount:capitalAccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "资金流水账数据导入模板.xlsx";
    		List<CapitalAccount> list = Lists.newArrayList(); 
    		new ExportExcel("资金流水账数据", CapitalAccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/capitalaccount/capitalAccount/?repage";
    }
	
	
	/**
	 * 选择账目类型
	 */
	@RequestMapping(value = "selectaccountType")
	public String selectaccountType(AccountType accountType, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AccountType> page = capitalAccountService.findPageByaccountType(new Page<AccountType>(request, response),  accountType);
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
		Page<ClearingAccount> page = capitalAccountService.findPageByclearingAccount(new Page<ClearingAccount>(request, response),  clearingAccount);
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