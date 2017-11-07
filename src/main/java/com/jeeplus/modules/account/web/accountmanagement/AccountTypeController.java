/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web.accountmanagement;

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
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.service.accountmanagement.AccountTypeService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 账目类型管理Controller
 * @author 金圣智
 * @version 2017-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/account/accountmanagement/accountType")
public class AccountTypeController extends BaseController {

	@Autowired
	private AccountTypeService accountTypeService;
	
	@ModelAttribute
	public AccountType get(@RequestParam(required=false) String id) {
		AccountType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = accountTypeService.get(id);
		}
		if (entity == null){
			entity = new AccountType();
		}
		return entity;
	}
	
	/**
	 * 账目类型列表页面
	 */
	@RequiresPermissions("account:accountmanagement:accountType:list")
	@RequestMapping(value = {"list", ""})
	public String list(AccountType accountType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AccountType> page = accountTypeService.findPage(new Page<AccountType>(request, response), accountType); 
		model.addAttribute("page", page);
		return "modules/account/accountmanagement/accountTypeList";
	}

	/**
	 * 查看，增加，编辑账目类型表单页面
	 */
	@RequiresPermissions(value={"account:accountmanagement:accountType:view","account:accountmanagement:accountType:add","account:accountmanagement:accountType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AccountType accountType, Model model) {
		model.addAttribute("accountType", accountType);
		return "modules/account/accountmanagement/accountTypeForm";
	}

	/**
	 * 保存账目类型
	 */
	@RequiresPermissions(value={"account:accountmanagement:accountType:add","account:accountmanagement:accountType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AccountType accountType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, accountType)){
			return form(accountType, model);
		}
		if(!accountType.getIsNewRecord()){//编辑表单保存
			AccountType t = accountTypeService.get(accountType.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(accountType, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			accountTypeService.save(t);//保存
		}else{//新增表单保存
			
			//获取当前登录用户所属商户
			User user = UserUtils.getUser();
			Office company = user.getCompany();
			//将商户设置给对象
			accountType.setFsponsor(company);

			accountTypeService.save(accountType);//保存
		}
		addMessage(redirectAttributes, "保存账目类型成功");
		return "redirect:"+Global.getAdminPath()+"/account/accountmanagement/accountType/?repage";
	}
	
	/**
	 * 逻辑删除账目类型
	 */
	@RequiresPermissions("account:accountmanagement:accountType:del")
	@RequestMapping(value = "delete")
	public String delete(AccountType accountType, RedirectAttributes redirectAttributes) {
		accountTypeService.deleteByLogic(accountType);
		addMessage(redirectAttributes, "删除账目类型成功");
		return "redirect:"+Global.getAdminPath()+"/account/accountmanagement/accountType/?repage";
	}
	
	/**
	 * 批量逻辑删除账目类型
	 */
	@RequiresPermissions("account:accountmanagement:accountType:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			accountTypeService.deleteByLogic(accountTypeService.get(id));
		}
		addMessage(redirectAttributes, "删除账目类型成功");
		return "redirect:"+Global.getAdminPath()+"/account/accountmanagement/accountType/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:accountmanagement:accountType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(AccountType accountType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "账目类型"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AccountType> page = accountTypeService.findPage(new Page<AccountType>(request, response, -1), accountType);
    		new ExportExcel("账目类型", AccountType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出账目类型记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/accountmanagement/accountType/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:accountmanagement:accountType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AccountType> list = ei.getDataList(AccountType.class);
			for (AccountType accountType : list){
				try{
					accountTypeService.save(accountType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条账目类型记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条账目类型记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入账目类型失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/accountmanagement/accountType/?repage";
    }
	
	/**
	 * 下载导入账目类型数据模板
	 */
	@RequiresPermissions("account:accountmanagement:accountType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "账目类型数据导入模板.xlsx";
    		List<AccountType> list = Lists.newArrayList(); 
    		new ExportExcel("账目类型数据", AccountType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/accountmanagement/accountType/?repage";
    }
	
	
	

}