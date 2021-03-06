/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.web.record;

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

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.service.WarehouseRecordService;

/**
 * 仓库流水Controller
 * @author hxting
 * @version 2017-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouses/warehouseRecord")
public class WarehouseRecordController extends BaseController {

	@Autowired
	private WarehouseRecordService warehouseRecordService;
	
	@ModelAttribute
	public WarehouseRecord get(@RequestParam(required=false) String id) {
		WarehouseRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warehouseRecordService.get(id);
		}
		if (entity == null){
			entity = new WarehouseRecord();
		}
		return entity;
	}
	
	/**
	 * 仓库流水列表页面
	 */
	@RequiresPermissions("warehouses:warehouseRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(WarehouseRecord warehouseRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseRecord> page = warehouseRecordService.findPage(new Page<WarehouseRecord>(request, response), warehouseRecord); 
		model.addAttribute("page", page);
		return "modules/warehouses/record/warehouseRecordList";
	}

	/**
	 * 查看，增加，编辑仓库流水表单页面
	 */
	@RequiresPermissions(value={"warehouses:warehouseRecord:view","warehouses:warehouseRecord:add","warehouses:warehouseRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WarehouseRecord warehouseRecord, Model model) {
		model.addAttribute("warehouseRecord", warehouseRecord);
		return "modules/warehouses/record/warehouseRecordForm";
	}

	/**
	 * 保存仓库流水
	 */
	@RequiresPermissions(value={"warehouses:warehouseRecord:add","warehouses:warehouseRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WarehouseRecord warehouseRecord, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, warehouseRecord)){
			return form(warehouseRecord, model);
		}
		if(!warehouseRecord.getIsNewRecord()){//编辑表单保存
			WarehouseRecord t = warehouseRecordService.get(warehouseRecord.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(warehouseRecord, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			warehouseRecordService.save(t);//保存
		}else{//新增表单保存
			warehouseRecordService.save(warehouseRecord);//保存
		}
		addMessage(redirectAttributes, "保存仓库流水成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseRecord/?repage";
	}
	
	/**
	 * 逻辑删除仓库流水
	 */
	@RequiresPermissions("warehouses:warehouseRecord:del")
	@RequestMapping(value = "delete")
	public String delete(WarehouseRecord warehouseRecord, RedirectAttributes redirectAttributes) {
		warehouseRecordService.deleteByLogic(warehouseRecord);
		addMessage(redirectAttributes, "删除仓库流水成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseRecord/?repage";
	}
	
	/**
	 * 批量逻辑删除仓库流水
	 */
	@RequiresPermissions("warehouses:warehouseRecord:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warehouseRecordService.deleteByLogic(warehouseRecordService.get(id));
		}
		addMessage(redirectAttributes, "删除仓库流水成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseRecord/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("warehouses:warehouseRecord:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WarehouseRecord warehouseRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库流水"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WarehouseRecord> page = warehouseRecordService.findPage(new Page<WarehouseRecord>(request, response, -1), warehouseRecord);
    		new ExportExcel("仓库流水", WarehouseRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出仓库流水记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseRecord/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("warehouses:warehouseRecord:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WarehouseRecord> list = ei.getDataList(WarehouseRecord.class);
			for (WarehouseRecord warehouseRecord : list){
				try{
					warehouseRecordService.save(warehouseRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条仓库流水记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条仓库流水记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库流水失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseRecord/?repage";
    }
	
	/**
	 * 下载导入仓库流水数据模板
	 */
	@RequiresPermissions("warehouses:warehouseRecord:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库流水数据导入模板.xlsx";
    		List<WarehouseRecord> list = Lists.newArrayList(); 
    		new ExportExcel("仓库流水数据", WarehouseRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseRecord/?repage";
    }
	
	
	/**
	 * 选择仓库
	 */
	@RequestMapping(value = "selectwarehouse")
	public String selectwarehouse(Warehouse warehouse, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Warehouse> page = warehouseRecordService.findPageBywarehouse(new Page<Warehouse>(request, response),  warehouse);
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
		model.addAttribute("obj", warehouse);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择商品SPU
	 */
	@RequestMapping(value = "selectgoodsSpu")
	public String selectgoodsSpu(GoodsSpu goodsSpu, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsSpu> page = warehouseRecordService.findPageBygoodsSpu(new Page<GoodsSpu>(request, response),  goodsSpu);
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
		model.addAttribute("obj", goodsSpu);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择商品SKU
	 */
	@RequestMapping(value = "selectgoodsSku")
	public String selectgoodsSku(GoodsSku goodsSku, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsSku> page = warehouseRecordService.findPageBygoodsSku(new Page<GoodsSku>(request, response),  goodsSku);
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
		model.addAttribute("obj", goodsSku);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}