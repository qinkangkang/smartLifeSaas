/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.web.transferorder;

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

import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.service.unit.GoodsUnitService;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferOrder;
import com.jeeplus.modules.warehouses.service.WarehouseGoodsInfoService;
import com.jeeplus.modules.warehouses.service.WarehouseTransferOrderService;

/**
 * 仓库调拨Controller
 * @author hxting
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouses/warehouseTransferOrder")
public class WarehouseTransferOrderController extends BaseController {

	@Autowired
	private WarehouseTransferOrderService warehouseTransferOrderService;
	
	@Autowired
	private GoodsUnitService goodsUnitService;
	
	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;
	
	@ModelAttribute
	public WarehouseTransferOrder get(@RequestParam(required=false) String id) {
		WarehouseTransferOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warehouseTransferOrderService.get(id);
		}
		if (entity == null){
			entity = new WarehouseTransferOrder();
		}
		return entity;
	}
	
	/**
	 * 仓库调拨列表页面
	 */
	@RequiresPermissions("warehouses:warehouseTransferOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(WarehouseTransferOrder warehouseTransferOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseTransferOrder> page = warehouseTransferOrderService.findPage(new Page<WarehouseTransferOrder>(request, response), warehouseTransferOrder); 
		model.addAttribute("page", page);
		return "modules/warehouses/transferorder/warehouseTransferOrderList";
	}

	/**
	 * 查看，增加，编辑仓库调拨表单页面
	 */
	@RequiresPermissions(value={"warehouses:warehouseTransferOrder:view","warehouses:warehouseTransferOrder:add","warehouses:warehouseTransferOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WarehouseTransferOrder warehouseTransferOrder, Model model) {
		//生成调拨单号
		String orderNo = OddNumbers.getOrderNo();
		orderNo ="DB"+orderNo;
		warehouseTransferOrder.setFtransferOrder(orderNo);
		List<WarehouseTransferGoods> list = warehouseTransferOrder.getWarehouseTransferGoodsList();
		if(list != null && list.size() > 0){
			for (WarehouseTransferGoods wtg : list) {
				if(wtg.getGoodsSpu() != null && wtg.getGoodsSku() != null){
					GoodsUnit goodsUnit = goodsUnitService.get(wtg.getGoodsSpu().getGoodsUnit().getId());
					wtg.getGoodsSpu().setGoodsUnit(goodsUnit);
				}
			}			
		}
		model.addAttribute("warehouseTransferOrder", warehouseTransferOrder);	
		return "modules/warehouses/transferorder/warehouseTransferOrderForm";
	}

	/**
	 * 保存仓库调拨
	 */
	@RequiresPermissions(value={"warehouses:warehouseTransferOrder:add","warehouses:warehouseTransferOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WarehouseTransferOrder warehouseTransferOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		
		List<WarehouseTransferGoods> list = warehouseTransferOrder.getWarehouseTransferGoodsList();
		if(list != null && list.size() > 0){
			for (WarehouseTransferGoods wtg : list) {
				if(wtg.getGoodsSpu() != null && wtg.getGoodsSku() != null){
					//调出仓库商品
					WarehouseGoodsInfo w = new WarehouseGoodsInfo();
					w.setGoodsSpu(wtg.getGoodsSpu());
					w.setGoodsSku(wtg.getGoodsSku());
					w.setWarehouse(warehouseTransferOrder.getWarehouseOut());
					List<WarehouseGoodsInfo> findList = warehouseGoodsInfoService.findList(w);
					
					//调入仓库商品
					WarehouseGoodsInfo w1 = new WarehouseGoodsInfo();
					w1.setGoodsSpu(wtg.getGoodsSpu());
					w1.setGoodsSku(wtg.getGoodsSku());
					w1.setWarehouse(warehouseTransferOrder.getFwarehouseIn());
					List<WarehouseGoodsInfo> findList2 = warehouseGoodsInfoService.findList(w1);
					
					if(findList != null && findList.size()>0 ){
						WarehouseGoodsInfo warehouseGoodsInfo = findList.get(0);	
						wtg.setFtransferOutBeforeNum(warehouseGoodsInfo.getFtotalinventory());
						wtg.setWarehouseOutGoods(warehouseGoodsInfo);
					}else{
						//调出仓库没有要调的商品则设置调出仓库前数量为 0；
						wtg.setFtransferOutBeforeNum(0);
					}
					if(findList2 != null && findList2.size() > 0){
						WarehouseGoodsInfo warehouseGoodsInfo = findList2.get(0);
						wtg.setFtransferInBeforeNum(warehouseGoodsInfo.getFtotalinventory());
						wtg.setWarehouseInGoods(warehouseGoodsInfo);
					}else {
						//调入仓库没有该商品则设置调入仓库前数量为 0；
						wtg.setFtransferInBeforeNum(0);
					}
				}
			}
		}
		if (!beanValidator(model, warehouseTransferOrder)){
			return form(warehouseTransferOrder, model);
		}
		if(!warehouseTransferOrder.getIsNewRecord()){//编辑表单保存
			WarehouseTransferOrder t = warehouseTransferOrderService.get(warehouseTransferOrder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(warehouseTransferOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			warehouseTransferOrderService.save(t);//保存
			
		}else{//新增表单保存
			warehouseTransferOrderService.save(warehouseTransferOrder);//保存
		}
		addMessage(redirectAttributes, "保存仓库调拨成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferOrder/?repage";
	}
	
	/**
	 * 逻辑删除仓库调拨
	 */
	@RequiresPermissions("warehouses:warehouseTransferOrder:del")
	@RequestMapping(value = "delete")
	public String delete(WarehouseTransferOrder warehouseTransferOrder, RedirectAttributes redirectAttributes) {
		warehouseTransferOrderService.deleteByLogic(warehouseTransferOrder);
		addMessage(redirectAttributes, "删除仓库调拨成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferOrder/?repage";
	}
	
	/**
	 * 撤销调拨单
	 */
	@RequiresPermissions("warehouses:warehouseTransferOrder:del")
	@RequestMapping(value = "revoke")
	public String revoke(WarehouseTransferOrder warehouseTransferOrder, RedirectAttributes redirectAttributes){
		warehouseTransferOrderService.revoke(warehouseTransferOrder);
		addMessage(redirectAttributes, "撤销该调拨单成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferOrder/?repage";
		
	}
	
	
	/**
	 * 批量逻辑删除仓库调拨
	 */
	@RequiresPermissions("warehouses:warehouseTransferOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warehouseTransferOrderService.deleteByLogic(warehouseTransferOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除仓库调拨成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferOrder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("warehouses:warehouseTransferOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WarehouseTransferOrder warehouseTransferOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库调拨"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WarehouseTransferOrder> page = warehouseTransferOrderService.findPage(new Page<WarehouseTransferOrder>(request, response, -1), warehouseTransferOrder);
    		new ExportExcel("仓库调拨", WarehouseTransferOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出仓库调拨记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferOrder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("warehouses:warehouseTransferOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WarehouseTransferOrder> list = ei.getDataList(WarehouseTransferOrder.class);
			for (WarehouseTransferOrder warehouseTransferOrder : list){
				try{
					warehouseTransferOrderService.save(warehouseTransferOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条仓库调拨记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条仓库调拨记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库调拨失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferOrder/?repage";
    }
	
	/**
	 * 下载导入仓库调拨数据模板
	 */
	@RequiresPermissions("warehouses:warehouseTransferOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库调拨数据导入模板.xlsx";
    		List<WarehouseTransferOrder> list = Lists.newArrayList(); 
    		new ExportExcel("仓库调拨数据", WarehouseTransferOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseTransferOrder/?repage";
    }
	
	
	/**
	 * 选择调出仓库
	 */
	@RequestMapping(value = "selectwarehouseOut")
	public String selectwarehouseOut(Warehouse warehouseOut, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Warehouse> page = warehouseTransferOrderService.findPageBywarehouseOut(new Page<Warehouse>(request, response),  warehouseOut);
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
		model.addAttribute("obj", warehouseOut);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择调入仓库
	 */
	@RequestMapping(value = "selectfwarehouseIn")
	public String selectfwarehouseIn(Warehouse fwarehouseIn, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Warehouse> page = warehouseTransferOrderService.findPageByfwarehouseIn(new Page<Warehouse>(request, response),  fwarehouseIn);
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
		model.addAttribute("obj", fwarehouseIn);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}