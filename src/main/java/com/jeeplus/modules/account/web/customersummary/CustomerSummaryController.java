/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web.customersummary;

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

import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.account.entity.customersummary.CustomerSummary;
import com.jeeplus.modules.account.service.customersummary.CustomerSummaryService;

/**
 * 客户基本信息Controller
 * @author diqiang
 * @version 2017-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/account/customersummary/customerSummary")
public class CustomerSummaryController extends BaseController {

	@Autowired
	private CustomerSummaryService customerSummaryService;
	
	@ModelAttribute
	public CustomerSummary get(@RequestParam(required=false) String id) {
		CustomerSummary entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerSummaryService.get(id);
		}
		if (entity == null){
			entity = new CustomerSummary();
		}
		return entity;
	}
	
	/**
	 * 客户基本信息列表页面
	 */
	@RequiresPermissions("account:customersummary:customerSummary:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerSummary customerSummary, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerSummary> page = customerSummaryService.findPage(new Page<CustomerSummary>(request, response), customerSummary); 
		model.addAttribute("page", page);
		return "modules/account/customersummary/customerSummaryList";
	}

	/**
	 * 查看，增加，编辑客户基本信息表单页面
	 */
	@RequiresPermissions(value={"account:customersummary:customerSummary:view","account:customersummary:customerSummary:add","account:customersummary:customerSummary:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CustomerSummary customerSummary, Model model) {
		model.addAttribute("customerSummary", customerSummary);
		return "modules/account/customersummary/customerSummaryForm";
	}

	/**
	 * 保存客户基本信息
	 */
	@RequiresPermissions(value={"account:customersummary:customerSummary:add","account:customersummary:customerSummary:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CustomerSummary customerSummary, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, customerSummary)){
			return form(customerSummary, model);
		}
		if(!customerSummary.getIsNewRecord()){//编辑表单保存
			CustomerSummary t = customerSummaryService.get(customerSummary.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(customerSummary, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			customerSummaryService.save(t);//保存
		}else{//新增表单保存
			customerSummaryService.save(customerSummary);//保存
		}
		addMessage(redirectAttributes, "保存客户基本信息成功");
		return "redirect:"+Global.getAdminPath()+"/account/customersummary/customerSummary/?repage";
	}
	
	/**
	 * 逻辑删除客户基本信息
	 */
	@RequiresPermissions("account:customersummary:customerSummary:del")
	@RequestMapping(value = "delete")
	public String delete(CustomerSummary customerSummary, RedirectAttributes redirectAttributes) {
		customerSummaryService.deleteByLogic(customerSummary);
		addMessage(redirectAttributes, "删除客户基本信息成功");
		return "redirect:"+Global.getAdminPath()+"/account/customersummary/customerSummary/?repage";
	}
	
	/**
	 * 批量逻辑删除客户基本信息
	 */
	@RequiresPermissions("account:customersummary:customerSummary:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerSummaryService.deleteByLogic(customerSummaryService.get(id));
		}
		addMessage(redirectAttributes, "删除客户基本信息成功");
		return "redirect:"+Global.getAdminPath()+"/account/customersummary/customerSummary/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:customersummary:customerSummary:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(CustomerSummary customerSummary, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户对账总汇"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerSummary> page = customerSummaryService.findPage(new Page<CustomerSummary>(request, response, -1), customerSummary);
    		new ExportExcel("客户对账总汇", CustomerSummary.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出客户对账总汇记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/customersummary/customerSummary/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:customersummary:customerSummary:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerSummary> list = ei.getDataList(CustomerSummary.class);
			for (CustomerSummary customerSummary : list){
				try{
					customerSummaryService.save(customerSummary);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户基本信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户对账总汇记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户对账总汇失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/customersummary/customerSummary/?repage";
    }
	
	/**
	 * 下载导入客户基本信息数据模板
	 */
	@RequiresPermissions("account:customersummary:customerSummary:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户基本信息数据导入模板.xlsx";
    		List<CustomerSummary> list = Lists.newArrayList(); 
    		new ExportExcel("客户基本信息数据", CustomerSummary.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/customersummary/customerSummary/?repage";
    }
	
	
	/**
	 * 选择客户分类
	 */
	@RequestMapping(value = "selectcustomerCate")
	public String selectcustomerCate(CustomerCate customerCate, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerCate> page = customerSummaryService.findPageBycustomerCate(new Page<CustomerCate>(request, response),  customerCate);
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
	

}