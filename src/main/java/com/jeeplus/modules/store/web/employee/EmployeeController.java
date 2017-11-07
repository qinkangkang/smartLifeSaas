/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.web.employee;

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

import com.jeeplus.modules.store.entity.storemanage.Store;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.store.entity.employee.Employee;
import com.jeeplus.modules.store.service.employee.EmployeeService;

/**
 * 员工账号管理Controller
 * @author 金圣智
 * @version 2017-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/store/employee/employee")
public class EmployeeController extends BaseController {

	@Autowired
	private EmployeeService employeeService;
	
	@ModelAttribute
	public Employee get(@RequestParam(required=false) String id) {
		Employee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = employeeService.get(id);
		}
		if (entity == null){
			entity = new Employee();
		}
		return entity;
	}
	
	/**
	 * 员工账号信息列表页面
	 */
	@RequiresPermissions("store:employee:employee:list")
	@RequestMapping(value = {"list", ""})
	public String list(Employee employee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Employee> page = employeeService.findPage(new Page<Employee>(request, response), employee); 
		model.addAttribute("page", page);
		return "modules/store/employee/employeeList";
	}

	/**
	 * 查看，增加，编辑员工账号信息表单页面
	 */
	@RequiresPermissions(value={"store:employee:employee:view","store:employee:employee:add","store:employee:employee:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Employee employee, Model model) {
		model.addAttribute("employee", employee);
		return "modules/store/employee/employeeForm";
	}

	/**
	 * 保存员工账号信息
	 */
	@RequiresPermissions(value={"store:employee:employee:add","store:employee:employee:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Employee employee, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, employee)){
			return form(employee, model);
		}
		if(!employee.getIsNewRecord()){//编辑表单保存
			Employee t = employeeService.get(employee.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(employee, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			employeeService.save(t);//保存
		}else{//新增表单保存
			employeeService.save(employee);//保存
		}
		addMessage(redirectAttributes, "保存员工账号信息成功");
		return "redirect:"+Global.getAdminPath()+"/store/employee/employee/?repage";
	}
	
	/**
	 * 逻辑删除员工账号信息
	 */
	@RequiresPermissions("store:employee:employee:del")
	@RequestMapping(value = "delete")
	public String delete(Employee employee, RedirectAttributes redirectAttributes) {
		employeeService.deleteByLogic(employee);
		addMessage(redirectAttributes, "删除员工账号信息成功");
		return "redirect:"+Global.getAdminPath()+"/store/employee/employee/?repage";
	}
	
	/**
	 * 批量逻辑删除员工账号信息
	 */
	@RequiresPermissions("store:employee:employee:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			employeeService.deleteByLogic(employeeService.get(id));
		}
		addMessage(redirectAttributes, "删除员工账号信息成功");
		return "redirect:"+Global.getAdminPath()+"/store/employee/employee/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("store:employee:employee:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Employee employee, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "员工账号信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Employee> page = employeeService.findPage(new Page<Employee>(request, response, -1), employee);
    		new ExportExcel("员工账号信息", Employee.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出员工账号信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/employee/employee/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("store:employee:employee:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Employee> list = ei.getDataList(Employee.class);
			for (Employee employee : list){
				try{
					employeeService.save(employee);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条员工账号信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条员工账号信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入员工账号信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/employee/employee/?repage";
    }
	
	/**
	 * 下载导入员工账号信息数据模板
	 */
	@RequiresPermissions("store:employee:employee:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "员工账号信息数据导入模板.xlsx";
    		List<Employee> list = Lists.newArrayList(); 
    		new ExportExcel("员工账号信息数据", Employee.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/employee/employee/?repage";
    }
	
	
	/**
	 * 选择所属门店
	 */
	@RequestMapping(value = "selectstore")
	public String selectstore(Store store, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Store> page = employeeService.findPageBystore(new Page<Store>(request, response),  store);
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
		model.addAttribute("obj", store);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}