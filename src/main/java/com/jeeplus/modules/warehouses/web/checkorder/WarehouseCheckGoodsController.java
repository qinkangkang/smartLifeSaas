/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.web.checkorder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MacUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckGoods;
import com.jeeplus.modules.warehouses.service.WarehouseCheckGoodsService;

/**
 * 盘点商品Controller
 * @author hxting
 * @version 2017-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouses/warehouseCheckGoods")
public class WarehouseCheckGoodsController extends BaseController {

	@Autowired
	private WarehouseCheckGoodsService warehouseCheckGoodsService;
	
	@ModelAttribute
	public WarehouseCheckGoods get(@RequestParam(required=false) String id) {
		WarehouseCheckGoods entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warehouseCheckGoodsService.get(id);
		}
		if (entity == null){
			entity = new WarehouseCheckGoods();
		}
		return entity;
	}
	
	/**
	 * 盘点商品列表页面
	 */
	@RequiresPermissions("warehouses:warehouseCheckGoods:list")
	@RequestMapping(value = {"list", ""})
	public String list(WarehouseCheckGoods warehouseCheckGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseCheckGoods> page = warehouseCheckGoodsService.findPage(new Page<WarehouseCheckGoods>(request, response), warehouseCheckGoods); 
		model.addAttribute("page", page);
		return "modules/warehouses/checkorder/warehouseCheckGoodsList";
	}

	/**
	 * 查看，增加，编辑盘点商品表单页面
	 */
	@RequiresPermissions(value={"warehouses:warehouseCheckGoods:view","warehouses:warehouseCheckGoods:add","warehouses:warehouseCheckGoods:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WarehouseCheckGoods warehouseCheckGoods, Model model) {
		model.addAttribute("warehouseCheckGoods", warehouseCheckGoods);
		return "modules/warehouses/checkorder/warehouseCheckGoodsForm";
	}

	/**
	 * 保存盘点商品
	 */
	@RequiresPermissions(value={"warehouses:warehouseCheckGoods:add","warehouses:warehouseCheckGoods:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WarehouseCheckGoods warehouseCheckGoods, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, warehouseCheckGoods)){
			return form(warehouseCheckGoods, model);
		}
		if(!warehouseCheckGoods.getIsNewRecord()){//编辑表单保存
			WarehouseCheckGoods t = warehouseCheckGoodsService.get(warehouseCheckGoods.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(warehouseCheckGoods, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			warehouseCheckGoodsService.save(t);//保存
		}else{//新增表单保存
			warehouseCheckGoodsService.save(warehouseCheckGoods);//保存
		}
		addMessage(redirectAttributes, "保存盘点商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseCheckGoods/?repage";
	}
	
	/**
	 * 逻辑删除盘点商品
	 */
	@RequiresPermissions("warehouses:warehouseCheckGoods:del")
	@RequestMapping(value = "delete")
	public String delete(WarehouseCheckGoods warehouseCheckGoods, RedirectAttributes redirectAttributes) {
		warehouseCheckGoodsService.deleteByLogic(warehouseCheckGoods);
		addMessage(redirectAttributes, "删除盘点商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseCheckGoods/?repage";
	}
	
	/**
	 * 批量逻辑删除盘点商品
	 */
	@RequiresPermissions("warehouses:warehouseCheckGoods:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warehouseCheckGoodsService.deleteByLogic(warehouseCheckGoodsService.get(id));
		}
		addMessage(redirectAttributes, "删除盘点商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseCheckGoods/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
    @RequestMapping(value = "export", method=RequestMethod.GET)
    public String exportFile(WarehouseCheckGoods warehouseCheckGoods, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "盘点商品"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WarehouseCheckGoods> page = warehouseCheckGoodsService.findPage(new Page<WarehouseCheckGoods>(request, response, -1), warehouseCheckGoods);
    		new ExportExcel("盘点商品", WarehouseCheckGoods.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出盘点商品记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseCheckGoods/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("warehouses:warehouseCheckGoods:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WarehouseCheckGoods> list = ei.getDataList(WarehouseCheckGoods.class);
			for (WarehouseCheckGoods warehouseCheckGoods : list){
				try{
					warehouseCheckGoodsService.save(warehouseCheckGoods);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条盘点商品记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条盘点商品记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入盘点商品失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseCheckGoods/?repage";
    }
	
	/**
	 * 下载导入盘点商品数据模板
	 */
	@RequiresPermissions("warehouses:warehouseCheckGoods:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "盘点商品数据导入模板.xlsx";
    		List<WarehouseCheckGoods> list = Lists.newArrayList(); 
    		new ExportExcel("盘点商品数据", WarehouseCheckGoods.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseCheckGoods/?repage";
    }
	
	
	/**
	 * 选择商品SPU
	 */
	@RequestMapping(value = "selectgoodsSpu")
	public String selectgoodsSpu(GoodsSpu goodsSpu, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsSpu> page = warehouseCheckGoodsService.findPageBygoodsSpu(new Page<GoodsSpu>(request, response),  goodsSpu);
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
		return "modules/warehouses/checkorder/gridselectspu";
	}
	/**
	 * 选择商品SKU
	 */
	@RequestMapping(value = "selectgoodsSku")
	public String selectgoodsSku(GoodsSku goodsSku, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {
		String mac = MacUtils.getMac();
		String  attribute = (String) session.getAttribute(mac+"spuId");
		GoodsSpu goodsSpu = new GoodsSpu();
		goodsSpu.setId(attribute);
		goodsSku.setGoodsSpu(goodsSpu);
		Page<GoodsSku> page = warehouseCheckGoodsService.findPageBygoodsSku(new Page<GoodsSku>(request, response),  goodsSku);
		session.removeAttribute(mac+"spuId");
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
		return "modules/warehouses/checkorder/gridselectsku";
	}
	
	
	/**
	   * 获取到选中的sizeid
	   */
	  @RequestMapping(value="setSpuId")
	  public void setSpuId(HttpServletRequest request, HttpServletResponse response, HttpSession session){
	    String spuId="";
	    spuId=(String) request.getParameter("spuId");
//	    System.out.println(abc);
	    //获取操作系统及mac地址
	   // String windowsMACAddress=getMacAddress();
	    String mac = MacUtils.getMac();
	    
	    
	    session.setAttribute(mac+"spuId", spuId);
	  }
	

}