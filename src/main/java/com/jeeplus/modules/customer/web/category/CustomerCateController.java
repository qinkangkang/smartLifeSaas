/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.web.category;

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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.customer.service.category.CustomerCateService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 客户分类Controller
 * @author diqiang
 * @version 2017-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/customer/category/customerCate")
public class CustomerCateController extends BaseController {

	@Autowired
	private CustomerCateService customerCateService;
	@Autowired
	private SystemService systemService;
	@ModelAttribute
	public CustomerCate get(@RequestParam(required=false) String id) {
		CustomerCate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerCateService.get(id);
		}
		if (entity == null){
			entity = new CustomerCate();
		}
		return entity;
	}
	
	/**
	 * 客户分类列表页面
	 */
	@RequiresPermissions("customer:customerCate:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerCate customerCate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerCate> page = customerCateService.findPage(new Page<CustomerCate>(request, response), customerCate); 
		List<CustomerCate> list = page.getList();
		for(CustomerCate cate:list){
			cate.setCreateBy(systemService.getUser(cate.getCreateBy().getId()));
			cate.setUpdateBy(systemService.getUser(cate.getUpdateBy().getId()));
			
			String id = cate.getCreateBy().getId();
			String id2 = cate.getUpdateBy().getId();
			System.out.println(id+id2);
		}
		model.addAttribute("page", page);
		return "modules/customer/category/customerCateList";
	}

	/**
	 * 查看，增加，编辑客户分类表单页面
	 */
	@RequiresPermissions(value={"customer:customerCate:view","customer:customerCate:add","customer:customerCate:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CustomerCate customerCate, Model model) {
		model.addAttribute("customerCate", customerCate);
		return "modules/customer/category/customerCateForm";
	}

	/**
	 * 保存客户分类
	 */
	@RequiresPermissions(value = { "customer:customerCate:add", "customer:customerCate:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(CustomerCate customerCate, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, customerCate)) {
			return form(customerCate, model);
		}
		
//		String fdiscount = customerCate.getFdiscount();
//		boolean b = fdiscount.matches("^[0-9]\\.[0-9]$");
//		if(!b){
//			addMessage(redirectAttributes, "添加客户分类失败，折扣格式错误，例‘9.8’");
//			return "redirect:" + Global.getAdminPath() + "/customer/category/customerCate/?repage";
//		}
		
		if (!customerCate.getIsNewRecord()) {// 编辑表单保存
			CustomerCate t = customerCateService.get(customerCate.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(customerCate, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			customerCateService.save(t);// 保存
		} else {// 新增表单保存
			String fname = customerCate.getFname();
			CustomerCate customecategory=new CustomerCate();
			customecategory.setFname(fname);
			List<CustomerCate> list = customerCateService.findList(customecategory);
			if (list != null && list.size() > 0) {
				for (CustomerCate cate : list) {
					if (cate.getFname().equals(fname)) {
						addMessage(redirectAttributes, "添加客户分类失败，分类名称已存在");
						return "redirect:" + Global.getAdminPath() + "/customer/category/customerCate/?repage";
					}
				}
			}
			//获取当前登录用户
			//并获取当前用户所属商户，设置给客户分类
			User user = UserUtils.getUser();
			Office company = user.getCompany();
			Office office = user.getOffice();
			customerCate.setFsponsor(company);
			customerCate.setFstore(office);
			
			customerCateService.save(customerCate);// 保存

		}
		addMessage(redirectAttributes, "保存客户分类成功");
		return "redirect:" + Global.getAdminPath() + "/customer/category/customerCate/?repage";
	}
	
	/**
	 * 删除客户分类
	 */
	@RequiresPermissions("customer:customerCate:del")
	@RequestMapping(value = "delete")
	public String delete(CustomerCate customerCate, RedirectAttributes redirectAttributes) {
		customerCateService.delete(customerCate);
		addMessage(redirectAttributes, "删除客户分类成功");
		return "redirect:"+Global.getAdminPath()+"/customer/category/customerCate/?repage";
	}
	
	/**
	 * 批量删除客户分类
	 */
	@RequiresPermissions("customer:customerCate:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerCateService.delete(customerCateService.get(id));
		}
		addMessage(redirectAttributes, "删除客户分类成功");
		return "redirect:"+Global.getAdminPath()+"/customer/category/customerCate/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("customer:customerCate:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(CustomerCate customerCate, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户分类"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerCate> page = customerCateService.findPage(new Page<CustomerCate>(request, response, -1), customerCate);
    		new ExportExcel("客户分类", CustomerCate.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出客户分类记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/customer/category/customerCate/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("customer:customerCate:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerCate> list = ei.getDataList(CustomerCate.class);
			for (CustomerCate customerCate : list){
				try{
					customerCateService.save(customerCate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户分类记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户分类记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户分类失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/customer/category/customerCate/?repage";
    }
	
	/**
	 * 下载导入客户分类数据模板
	 */
	@RequiresPermissions("customer:customerCate:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户分类数据导入模板.xlsx";
    		List<CustomerCate> list = Lists.newArrayList(); 
    		new ExportExcel("客户分类数据", CustomerCate.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/customer/category/customerCate/?repage";
    }
	
	
	

}