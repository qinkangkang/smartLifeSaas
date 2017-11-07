/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web.clearingaccount;

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

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.service.clearingaccount.ClearingAccountService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 结算账户Controller
 * @author 金圣智
 * @version 2017-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/account/clearingaccount/clearingAccount")
public class ClearingAccountController extends BaseController {

	@Autowired
	private ClearingAccountService clearingAccountService;
	
	@ModelAttribute
	public ClearingAccount get(@RequestParam(required=false) String id) {
		ClearingAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = clearingAccountService.get(id);
		}
		if (entity == null){
			entity = new ClearingAccount();
		}
		return entity;
	}
	
	/**
	 * 结算账户列表页面
	 */
	@RequiresPermissions("account:clearingaccount:clearingAccount:list")
	@RequestMapping(value = {"list", ""})
	public String list(ClearingAccount clearingAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ClearingAccount> page = clearingAccountService.findPage(new Page<ClearingAccount>(request, response), clearingAccount); 
		model.addAttribute("page", page);
		return "modules/account/clearingaccount/clearingAccountList";
	}

	/**
	 * 查看，增加，编辑结算账户表单页面
	 */
	@RequiresPermissions(value={"account:clearingaccount:clearingAccount:view","account:clearingaccount:clearingAccount:add","account:clearingaccount:clearingAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ClearingAccount clearingAccount, Model model) {
		model.addAttribute("clearingAccount", clearingAccount);
		return "modules/account/clearingaccount/clearingAccountForm";
	}

	/**
	 * 保存结算账户
	 */
	@RequiresPermissions(value={"account:clearingaccount:clearingAccount:add","account:clearingaccount:clearingAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ClearingAccount clearingAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, clearingAccount)){
			return form(clearingAccount, model);
		}
		if(!clearingAccount.getIsNewRecord()){//编辑表单保存
			ClearingAccount t = clearingAccountService.get(clearingAccount.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(clearingAccount, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			clearingAccountService.save(t);//保存
		}else{//新增表单保存
			
			//获取当前登录用户所属商户
			User user = UserUtils.getUser();
			Office company = user.getCompany();
			//将商户设置给对象
			clearingAccount.setFsponsor(company);
			clearingAccountService.save(clearingAccount);//保存
		}
		addMessage(redirectAttributes, "保存结算账户成功");
		return "redirect:"+Global.getAdminPath()+"/account/clearingaccount/clearingAccount/?repage";
	}
	
	/**
	 * 删除结算账户
	 */
	@RequiresPermissions("account:clearingaccount:clearingAccount:del")
	@RequestMapping(value = "delete")
	public String delete(ClearingAccount clearingAccount, RedirectAttributes redirectAttributes) {
		clearingAccountService.delete(clearingAccount);
		addMessage(redirectAttributes, "删除结算账户成功");
		return "redirect:"+Global.getAdminPath()+"/account/clearingaccount/clearingAccount/?repage";
	}
	
	/**
	 * 批量删除结算账户
	 */
	@RequiresPermissions("account:clearingaccount:clearingAccount:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			clearingAccountService.delete(clearingAccountService.get(id));
		}
		addMessage(redirectAttributes, "删除结算账户成功");
		return "redirect:"+Global.getAdminPath()+"/account/clearingaccount/clearingAccount/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:clearingaccount:clearingAccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ClearingAccount clearingAccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "结算账户"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ClearingAccount> page = clearingAccountService.findPage(new Page<ClearingAccount>(request, response, -1), clearingAccount);
    		new ExportExcel("结算账户", ClearingAccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出结算账户记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/clearingaccount/clearingAccount/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:clearingaccount:clearingAccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ClearingAccount> list = ei.getDataList(ClearingAccount.class);
			for (ClearingAccount clearingAccount : list){
				try{
					clearingAccountService.save(clearingAccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条结算账户记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条结算账户记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入结算账户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/clearingaccount/clearingAccount/?repage";
    }
	
	/**
	 * 下载导入结算账户数据模板
	 */
	@RequiresPermissions("account:clearingaccount:clearingAccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "结算账户数据导入模板.xlsx";
    		List<ClearingAccount> list = Lists.newArrayList(); 
    		new ExportExcel("结算账户数据", ClearingAccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/clearingaccount/clearingAccount/?repage";
    }
	
	
	

}