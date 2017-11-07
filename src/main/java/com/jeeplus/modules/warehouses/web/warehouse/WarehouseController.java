/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.web.warehouse;

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
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.service.WarehouseService;

/**
 * 仓库Controller
 * @author Hxting
 * @version 2017-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouses/warehouse")
public class WarehouseController extends BaseController {

	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	
	@ModelAttribute
	public Warehouse get(@RequestParam(required=false) String id) {
		Warehouse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warehouseService.get(id);
		}
		if (entity == null){
			entity = new Warehouse();
		}
		return entity;
	}
	
	/**
	 * 仓库列表页面
	 */
	@RequiresPermissions("warehouses:warehouse:list")
	@RequestMapping(value = {"list", ""})
	public String list(Warehouse warehouse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Warehouse> page = warehouseService.findPage(new Page<Warehouse>(request, response), warehouse); 
		List<Warehouse> list = page.getList();
		for (Warehouse wh : list) {
			if(wh.getCreateBy() != null && wh.getCreateBy().getId() != null ){
				wh.setCreateBy(systemService.getUser(wh.getCreateBy().getId()));
			}
			if(wh.getUpdateBy() != null && wh.getUpdateBy().getId() != null ){
				wh.setUpdateBy(systemService.getUser(wh.getUpdateBy().getId()));
			}
			if(wh.getOffice() != null && wh.getOffice().getId() != null   ){
				wh.setOffice(officeService.get(wh.getOffice().getId()));
			}
			if(  wh.getChargePerson() != null && wh.getChargePerson().getId()  != null ){
				wh.setChargePerson(systemService.getUser(wh.getChargePerson().getId()));
			}
		}
		model.addAttribute("page", page);
		return "modules/warehouses/warehouse/warehouseList";
	}

	/**
	 * 查看，增加，编辑仓库表单页面
	 */
	@RequiresPermissions(value={"warehouses:warehouse:view","warehouses:warehouse:add","warehouses:warehouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Warehouse warehouse, Model model) {
		model.addAttribute("warehouse", warehouse);
		return "modules/warehouses/warehouse/warehouseForm";
	}

	/**
	 * 保存仓库
	 */
	@RequiresPermissions(value={"warehouses:warehouse:add","warehouses:warehouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Warehouse warehouse, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		if (!beanValidator(model, warehouse)){
			return form(warehouse, model);
		}
		if(!warehouse.getIsNewRecord()){//编辑表单保存
			Warehouse t = warehouseService.get(warehouse.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(warehouse, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			warehouseService.save(t);//保存
		}else{//新增表单保存
			warehouseService.save(warehouse);//保存
		}
		addMessage(redirectAttributes, "保存仓库成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouse/?repage";
	}
	
	/**
	 * 逻辑删除仓库
	 */
	@RequiresPermissions("warehouses:warehouse:del")
	@RequestMapping(value = "delete")
	public String delete(Warehouse warehouse, RedirectAttributes redirectAttributes) {
		warehouseService.deleteByLogic(warehouse);
		addMessage(redirectAttributes, "删除仓库成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouse/?repage";
	}
	
	/**
	 * 批量逻辑删除仓库
	 */
	@RequiresPermissions("warehouses:warehouse:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warehouseService.deleteByLogic(warehouseService.get(id));
		}
		addMessage(redirectAttributes, "删除仓库成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouse/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("warehouses:warehouse:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Warehouse warehouse, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Warehouse> page = warehouseService.findPage(new Page<Warehouse>(request, response, -1), warehouse);
    		new ExportExcel("仓库", Warehouse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出仓库记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouse/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("warehouses:warehouse:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Warehouse> list = ei.getDataList(Warehouse.class);
			for (Warehouse warehouse : list){
				try{
					warehouseService.save(warehouse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条仓库记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条仓库记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouse/?repage";
    }
	
	/**
	 * 下载导入仓库数据模板
	 */
	@RequiresPermissions("warehouses:warehouse:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库数据导入模板.xlsx";
    		List<Warehouse> list = Lists.newArrayList(); 
    		new ExportExcel("仓库数据", Warehouse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouse/?repage";
    }
	
	
	

}