/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.web.transferorder;

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
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferOrder;
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
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;
import com.jeeplus.modules.warehouses.service.WarehouseTransferGoodsService;

/**
 * 调拨商品Controller
 * @author hxting
 * @version 2017-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouses/warehouseTransferGoods")
public class WarehouseTransferGoodsController extends BaseController {

	@Autowired
	private WarehouseTransferGoodsService warehouseTransferGoodsService;
	
	@ModelAttribute
	public WarehouseTransferGoods get(@RequestParam(required=false) String id) {
		WarehouseTransferGoods entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warehouseTransferGoodsService.get(id);
		}
		if (entity == null){
			entity = new WarehouseTransferGoods();
		}
		return entity;
	}
	
	/**
	 * 调拨商品列表页面
	 */
	@RequiresPermissions("warehouses:warehouseTransferGoods:list")
	@RequestMapping(value = {"list", ""})
	public String list(WarehouseTransferGoods warehouseTransferGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseTransferGoods> page = warehouseTransferGoodsService.findPage(new Page<WarehouseTransferGoods>(request, response), warehouseTransferGoods); 
		model.addAttribute("page", page);
		return "modules/warehouses/transferorder/warehouseTransferGoodsList";
	}

	/**
	 * 查看，增加，编辑调拨商品表单页面
	 */
	@RequiresPermissions(value={"warehouses:warehouseTransferGoods:view","warehouses:warehouseTransferGoods:add","warehouses:warehouseTransferGoods:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WarehouseTransferGoods warehouseTransferGoods, Model model) {
		model.addAttribute("warehouseTransferGoods", warehouseTransferGoods);
		return "modules/warehouses/transferorder/warehouseTransferGoodsForm";
	}

	/**
	 * 保存调拨商品
	 */
	@RequiresPermissions(value={"warehouses:warehouseTransferGoods:add","warehouses:warehouseTransferGoods:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WarehouseTransferGoods warehouseTransferGoods, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, warehouseTransferGoods)){
			return form(warehouseTransferGoods, model);
		}
		if(!warehouseTransferGoods.getIsNewRecord()){//编辑表单保存
			WarehouseTransferGoods t = warehouseTransferGoodsService.get(warehouseTransferGoods.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(warehouseTransferGoods, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			warehouseTransferGoodsService.save(t);//保存
		}else{//新增表单保存
			warehouseTransferGoodsService.save(warehouseTransferGoods);//保存
		}
		addMessage(redirectAttributes, "保存调拨商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferGoods/?repage";
	}
	
	/**
	 * 逻辑删除调拨商品
	 */
	@RequiresPermissions("warehouses:warehouseTransferGoods:del")
	@RequestMapping(value = "delete")
	public String delete(WarehouseTransferGoods warehouseTransferGoods, RedirectAttributes redirectAttributes) {
		warehouseTransferGoodsService.deleteByLogic(warehouseTransferGoods);
		addMessage(redirectAttributes, "删除调拨商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferGoods/?repage";
	}
	
	/**
	 * 批量逻辑删除调拨商品
	 */
	@RequiresPermissions("warehouses:warehouseTransferGoods:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warehouseTransferGoodsService.deleteByLogic(warehouseTransferGoodsService.get(id));
		}
		addMessage(redirectAttributes, "删除调拨商品成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferGoods/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("warehouses:warehouseTransferGoods:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WarehouseTransferGoods warehouseTransferGoods, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "调拨商品"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WarehouseTransferGoods> page = warehouseTransferGoodsService.findPage(new Page<WarehouseTransferGoods>(request, response, -1), warehouseTransferGoods);
    		new ExportExcel("调拨商品", WarehouseTransferGoods.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出调拨商品记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferGoods/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("warehouses:warehouseTransferGoods:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WarehouseTransferGoods> list = ei.getDataList(WarehouseTransferGoods.class);
			for (WarehouseTransferGoods warehouseTransferGoods : list){
				try{
					warehouseTransferGoodsService.save(warehouseTransferGoods);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条调拨商品记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条调拨商品记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入调拨商品失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferGoods/?repage";
    }
	
	/**
	 * 下载导入调拨商品数据模板
	 */
	@RequiresPermissions("warehouses:warehouseTransferGoods:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "调拨商品数据导入模板.xlsx";
    		List<WarehouseTransferGoods> list = Lists.newArrayList(); 
    		new ExportExcel("调拨商品数据", WarehouseTransferGoods.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferGoods/?repage";
    }
	
	
	/**
	 * 选择商品SPU
	 */
	@RequestMapping(value = "selectgoodsSpu")
	public String selectgoodsSpu(GoodsSpu goodsSpu, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsSpu> page = warehouseTransferGoodsService.findPageBygoodsSpu(new Page<GoodsSpu>(request, response),  goodsSpu);
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
		return "modules/warehouses/transferorder/gridselectspu";
	}
	/**
	 * 选择商品SKU
	 */
	@RequestMapping(value = "selectgoodsSku")
	public String selectgoodsSku(GoodsSku goodsSku, String spuid, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {
		
		String mac = MacUtils.getMac();
		String  attribute = (String) session.getAttribute(mac+"spuId");
		GoodsSpu goodsSpu = new GoodsSpu();
		goodsSpu.setId(attribute);
		goodsSku.setGoodsSpu(goodsSpu);
		Page<GoodsSku> page = warehouseTransferGoodsService.findPageBygoodsSku(new Page<GoodsSku>(request, response),  goodsSku);
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
		return "modules/warehouses/transferorder/gridselectsku";
	}
	/**
	 * 选择商品单位
	 */
	@RequestMapping(value = "selectgoodsUnit")
	public String selectgoodsUnit(GoodsUnit goodsUnit, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsUnit> page = warehouseTransferGoodsService.findPageBygoodsUnit(new Page<GoodsUnit>(request, response),  goodsUnit);
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
	/**
	 * 选择调拨单
	 */
	@RequestMapping(value = "selecttransferOrder")
	public String selecttransferOrder(WarehouseTransferOrder transferOrder, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseTransferOrder> page = warehouseTransferGoodsService.findPageBytransferOrder(new Page<WarehouseTransferOrder>(request, response),  transferOrder);
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
		model.addAttribute("obj", transferOrder);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
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
	  //  String windowsMACAddress=getMacAddress();
	    String mac = MacUtils.getMac();
	    
	    
	    
	    session.setAttribute(mac+"spuId", spuId);
	  }

}