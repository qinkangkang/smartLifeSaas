/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web.modeltype;

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
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.service.modeltype.SysModelTypeService;

/**
 * 打印模板Controller
 * @author diqiang
 * @version 2017-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/modeltype/sysModelType")
public class SysModelTypeController extends BaseController {

	@Autowired
	private SysModelTypeService sysModelTypeService;
	
	@ModelAttribute
	public SysModelType get(@RequestParam(required=false) String id) {
		SysModelType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysModelTypeService.get(id);
		}
		if (entity == null){
			entity = new SysModelType();
		}
		return entity;
	}
	
	/**
	 * 打印模板列表页面
	 */
	@RequiresPermissions("sys:modeltype:sysModelType:list")
	@RequestMapping(value = {"list", ""})
	public String list(SysModelType sysModelType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysModelType> page = sysModelTypeService.findPage(new Page<SysModelType>(request, response), sysModelType); 
		model.addAttribute("page", page);
		return "modules/sys/modeltype/sysModelTypeList";
	}

	/**
	 * 查看，增加，编辑打印模板表单页面
	 */
	@RequiresPermissions(value={"sys:modeltype:sysModelType:view","sys:modeltype:sysModelType:add","sys:modeltype:sysModelType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysModelType sysModelType, Model model) {
		model.addAttribute("sysModelType", sysModelType);
		return "modules/sys/modeltype/sysModelTypeForm";
	}

	/**
	 * 保存打印模板
	 */
	@RequiresPermissions(value={"sys:modeltype:sysModelType:add","sys:modeltype:sysModelType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SysModelType sysModelType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sysModelType)){
			return form(sysModelType, model);
		}
		if(!sysModelType.getIsNewRecord()){//编辑表单保存
			SysModelType t = sysModelTypeService.get(sysModelType.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysModelType, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysModelTypeService.save(t);//保存
		}else{//新增表单保存
			sysModelTypeService.save(sysModelType);//保存
		}
		addMessage(redirectAttributes, "保存打印模板成功");
		return "redirect:"+Global.getAdminPath()+"/sys/modeltype/sysModelType/?repage";
	}
	
	/**
	 * 逻辑删除打印模板
	 */
	@RequiresPermissions("sys:modeltype:sysModelType:del")
	@RequestMapping(value = "delete")
	public String delete(SysModelType sysModelType, RedirectAttributes redirectAttributes) {
		sysModelTypeService.deleteByLogic(sysModelType);
		addMessage(redirectAttributes, "删除打印模板成功");
		return "redirect:"+Global.getAdminPath()+"/sys/modeltype/sysModelType/?repage";
	}
	
	/**
	 * 批量逻辑删除打印模板
	 */
	@RequiresPermissions("sys:modeltype:sysModelType:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysModelTypeService.deleteByLogic(sysModelTypeService.get(id));
		}
		addMessage(redirectAttributes, "删除打印模板成功");
		return "redirect:"+Global.getAdminPath()+"/sys/modeltype/sysModelType/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:modeltype:sysModelType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SysModelType sysModelType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "打印模板"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysModelType> page = sysModelTypeService.findPage(new Page<SysModelType>(request, response, -1), sysModelType);
    		new ExportExcel("打印模板", SysModelType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出打印模板记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/modeltype/sysModelType/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:modeltype:sysModelType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysModelType> list = ei.getDataList(SysModelType.class);
			for (SysModelType sysModelType : list){
				try{
					sysModelTypeService.save(sysModelType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条打印模板记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条打印模板记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入打印模板失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/modeltype/sysModelType/?repage";
    }
	
	/**
	 * 下载导入打印模板数据模板
	 */
	@RequiresPermissions("sys:modeltype:sysModelType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "打印模板数据导入模板.xlsx";
    		List<SysModelType> list = Lists.newArrayList(); 
    		new ExportExcel("打印模板数据", SysModelType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/modeltype/sysModelType/?repage";
    }
	
	
	

}