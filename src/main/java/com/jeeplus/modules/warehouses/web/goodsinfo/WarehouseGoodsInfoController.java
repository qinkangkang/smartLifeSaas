/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.web.goodsinfo;

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
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.service.WarehouseGoodsInfoService;

/**
 * 仓库商品Controller
 * @author hxting
 * @version 2017-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouses/warehouseGoodsInfo")
public class WarehouseGoodsInfoController extends BaseController {

	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;
	
	@ModelAttribute
	public WarehouseGoodsInfo get(@RequestParam(required=false) String id) {
		WarehouseGoodsInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warehouseGoodsInfoService.get(id);
		}
		if (entity == null){
			entity = new WarehouseGoodsInfo();
		}
		return entity;
	}
	
	/**
	 * 仓库商品列表页面
	 */
	@RequiresPermissions("warehouses:warehouseGoodsInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list(WarehouseGoodsInfo warehouseGoodsInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseGoodsInfo> page = warehouseGoodsInfoService.findPage(new Page<WarehouseGoodsInfo>(request, response), warehouseGoodsInfo); 
		model.addAttribute("page", page);
		return "modules/warehouses/goodsinfo/warehouseGoodsInfoList";
	}

	/**
	 * 查看，增加，编辑仓库商品表单页面
	 */
	@RequiresPermissions(value={"warehouses:warehouseGoodsInfo:view","warehouses:warehouseGoodsInfo:add","warehouses:warehouseGoodsInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WarehouseGoodsInfo warehouseGoodsInfo, Model model) {
		model.addAttribute("warehouseGoodsInfo", warehouseGoodsInfo);
		return "modules/warehouses/goodsinfo/warehouseGoodsInfoForm";
	}

	/**
	 * 保存仓库商品
	 */
	@RequiresPermissions(value={"warehouses:warehouseGoodsInfo:add","warehouses:warehouseGoodsInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WarehouseGoodsInfo warehouseGoodsInfo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, warehouseGoodsInfo)){
			return form(warehouseGoodsInfo, model);
		}
		if(!warehouseGoodsInfo.getIsNewRecord()){//编辑表单保存
			WarehouseGoodsInfo t = warehouseGoodsInfoService.get(warehouseGoodsInfo.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(warehouseGoodsInfo, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			warehouseGoodsInfoService.save(t);//保存
		}else{//新增表单保存
			warehouseGoodsInfoService.save(warehouseGoodsInfo);//保存
		}
		addMessage(redirectAttributes, "保存仓库商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseGoodsInfo/?repage";
	}
	
	/**
	 * 逻辑删除仓库商品
	 */
	@RequiresPermissions("warehouses:warehouseGoodsInfo:del")
	@RequestMapping(value = "delete")
	public String delete(WarehouseGoodsInfo warehouseGoodsInfo, RedirectAttributes redirectAttributes) {
		warehouseGoodsInfoService.deleteByLogic(warehouseGoodsInfo);
		addMessage(redirectAttributes, "删除仓库商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseGoodsInfo/?repage";
	}
	
	/**
	 * 批量逻辑删除仓库商品
	 */
	@RequiresPermissions("warehouses:warehouseGoodsInfo:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warehouseGoodsInfoService.deleteByLogic(warehouseGoodsInfoService.get(id));
		}
		addMessage(redirectAttributes, "删除仓库商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseGoodsInfo/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("warehouses:warehouseGoodsInfo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WarehouseGoodsInfo warehouseGoodsInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库商品"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WarehouseGoodsInfo> page = warehouseGoodsInfoService.findPage(new Page<WarehouseGoodsInfo>(request, response, -1), warehouseGoodsInfo);
    		new ExportExcel("仓库商品", WarehouseGoodsInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出仓库商品记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseGoodsInfo/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("warehouses:warehouseGoodsInfo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WarehouseGoodsInfo> list = ei.getDataList(WarehouseGoodsInfo.class);
			for (WarehouseGoodsInfo warehouseGoodsInfo : list){
				try{
					warehouseGoodsInfoService.save(warehouseGoodsInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条仓库商品记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条仓库商品记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库商品失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseGoodsInfo/?repage";
    }
	
	/**
	 * 下载导入仓库商品数据模板
	 */
	@RequiresPermissions("warehouses:warehouseGoodsInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库商品数据导入模板.xlsx";
    		List<WarehouseGoodsInfo> list = Lists.newArrayList(); 
    		new ExportExcel("仓库商品数据", WarehouseGoodsInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseGoodsInfo/?repage";
    }
	
	
	/**
	 * 选择仓库
	 */
	@RequestMapping(value = "selectwarehouse")
	public String selectwarehouse(Warehouse warehouse, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Warehouse> page = warehouseGoodsInfoService.findPageBywarehouse(new Page<Warehouse>(request, response),  warehouse);
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
		Page<GoodsSpu> page = warehouseGoodsInfoService.findPageBygoodsSpu(new Page<GoodsSpu>(request, response),  goodsSpu);
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
		Page<GoodsSku> page = warehouseGoodsInfoService.findPageBygoodsSku(new Page<GoodsSku>(request, response),  goodsSku);
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
	/**
	 * 选择商品品牌
	 */
	@RequestMapping(value = "selectgoodsBrand")
	public String selectgoodsBrand(Brand goodsBrand, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Brand> page = warehouseGoodsInfoService.findPageBygoodsBrand(new Page<Brand>(request, response),  goodsBrand);
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
		model.addAttribute("obj", goodsBrand);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择商品分类
	 */
	@RequestMapping(value = "selectgoodsCategory")
	public String selectgoodsCategory(Categorys goodsCategory, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Categorys> page = warehouseGoodsInfoService.findPageBygoodsCategory(new Page<Categorys>(request, response),  goodsCategory);
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
		model.addAttribute("obj", goodsCategory);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择单位
	 */
	@RequestMapping(value = "selectgoodsUnit")
	public String selectgoodsUnit(GoodsUnit goodsUnit, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsUnit> page = warehouseGoodsInfoService.findPageBygoodsUnit(new Page<GoodsUnit>(request, response),  goodsUnit);
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
		model.addAttribute("obj", goodsUnit);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	
	
	
//	/**
//	 * 采购退单辅助方法
//	 */
//	@RequiresPermissions("warehouses:warehouseGoodsInfo:list")
//	@RequestMapping(value = {"ct", ""})
//	public String ct(WarehouseGoodsInfo warehouseGoodsInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<WarehouseGoodsInfo> page = warehouseGoodsInfoService.findPage(new Page<WarehouseGoodsInfo>(request, response), warehouseGoodsInfo); 
//		model.addAttribute("page", page);
//		return "modules/warehouses/goodsinfo/ct";
//	}

}