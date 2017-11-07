/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.supplier.web.supplierbasic;

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
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.supplier.service.supplierbasic.SupplierBasicService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 供应商管理Controller
 * @author 金圣智
 * @version 2017-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/supplier/supplierbasic/supplierBasic")
public class SupplierBasicController extends BaseController {

	@Autowired
	private SupplierBasicService supplierBasicService;
	
	@ModelAttribute
	public SupplierBasic get(@RequestParam(required=false) String id) {
		SupplierBasic entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supplierBasicService.get(id);
		}
		if (entity == null){
			entity = new SupplierBasic();
		}
		return entity;
	}
	
	/**
	 * 供应商信息列表页面
	 */
	@RequiresPermissions("supplier:supplierbasic:supplierBasic:list")
	@RequestMapping(value = {"list", ""})
	public String list(SupplierBasic supplierBasic, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SupplierBasic> page = supplierBasicService.findPage(new Page<SupplierBasic>(request, response), supplierBasic); 
		model.addAttribute("page", page);
		return "modules/supplier/supplierbasic/supplierBasicList";
	}

	/**
	 * 查看，增加，编辑供应商信息表单页面
	 */
	@RequiresPermissions(value={"supplier:supplierbasic:supplierBasic:view","supplier:supplierbasic:supplierBasic:add","supplier:supplierbasic:supplierBasic:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SupplierBasic supplierBasic, Model model) {
		model.addAttribute("supplierBasic", supplierBasic);
		return "modules/supplier/supplierbasic/supplierBasicForm";
	}

	/**
	 * 保存供应商信息
	 */
	@RequiresPermissions(value={"supplier:supplierbasic:supplierBasic:add","supplier:supplierbasic:supplierBasic:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SupplierBasic supplierBasic, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, supplierBasic)){
			return form(supplierBasic, model);
		}
		if(!supplierBasic.getIsNewRecord()){//编辑表单保存
			SupplierBasic t = supplierBasicService.get(supplierBasic.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(supplierBasic, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			supplierBasicService.save(t);//保存
		}else{//新增表单保存
			
			String ffullname = supplierBasic.getFfullname();
			try {
				SupplierBasic findUniqueByProperty = null;
				if (StringUtils.isNoneBlank(ffullname)) {
					//通过供应商全名判断是否已经存在
					findUniqueByProperty = supplierBasicService.findUniqueByProperty("ffullname", ffullname);
				}
				if (findUniqueByProperty != null && StringUtils.isNoneBlank(findUniqueByProperty.getId())) {
					addMessage(redirectAttributes, "保存供应商信息失败，供应商同名");
					return "redirect:" + Global.getAdminPath() + "/supplier/supplierbasic/supplierBasic/?repage";
				} else {
					// 获取当前用户
					User user = UserUtils.getUser();
					Office company = user.getCompany();
					Office office = user.getOffice();
					supplierBasic.setFsponsor(company);
					supplierBasic.setFstore(office);
					supplierBasicService.save(supplierBasic);// 保存
					addMessage(redirectAttributes, "保存供应商信息成功");
					return "redirect:" + Global.getAdminPath() + "/supplier/supplierbasic/supplierBasic/?repage";			
				}
			} catch (Exception e) {
				addMessage(redirectAttributes, "保存供应商信息失败，数据重复");
				return "redirect:" + Global.getAdminPath() + "/supplier/supplierbasic/supplierBasic/?repage";
			}

		}
		addMessage(redirectAttributes, "保存供应商信息成功");
		return "redirect:" + Global.getAdminPath() + "/supplier/supplierbasic/supplierBasic/?repage";
	}
	
	/**
	 * 逻辑删除供应商信息
	 */
	@RequiresPermissions("supplier:supplierbasic:supplierBasic:del")
	@RequestMapping(value = "delete")
	public String delete(SupplierBasic supplierBasic, RedirectAttributes redirectAttributes) {
		supplierBasicService.deleteByLogic(supplierBasic);
		addMessage(redirectAttributes, "删除供应商信息成功");
		return "redirect:"+Global.getAdminPath()+"/supplier/supplierbasic/supplierBasic/?repage";
	}
	
	/**
	 * 批量逻辑删除供应商信息
	 */
	@RequiresPermissions("supplier:supplierbasic:supplierBasic:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			supplierBasicService.deleteByLogic(supplierBasicService.get(id));
		}
		addMessage(redirectAttributes, "删除供应商信息成功");
		return "redirect:"+Global.getAdminPath()+"/supplier/supplierbasic/supplierBasic/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("supplier:supplierbasic:supplierBasic:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SupplierBasic supplierBasic, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SupplierBasic> page = supplierBasicService.findPage(new Page<SupplierBasic>(request, response, -1), supplierBasic);
    		new ExportExcel("供应商信息", SupplierBasic.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出供应商信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/supplier/supplierbasic/supplierBasic/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("supplier:supplierbasic:supplierBasic:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SupplierBasic> list = ei.getDataList(SupplierBasic.class);
			for (SupplierBasic supplierBasic : list){
				try{
					supplierBasicService.save(supplierBasic);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条供应商信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条供应商信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入供应商信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/supplier/supplierbasic/supplierBasic/?repage";
    }
	
	/**
	 * 下载导入供应商信息数据模板
	 */
	@RequiresPermissions("supplier:supplierbasic:supplierBasic:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商信息数据导入模板.xlsx";
    		List<SupplierBasic> list = Lists.newArrayList(); 
    		new ExportExcel("供应商信息数据", SupplierBasic.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/supplier/supplierbasic/supplierBasic/?repage";
    }
	
	
	

}