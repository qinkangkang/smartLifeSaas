/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.web.checkorder;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.service.spu.GoodsSpuService;
import com.jeeplus.modules.goods.service.unit.GoodsUnitService;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckGoods;
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
import com.jeeplus.modules.warehouses.entity.WarehouseCheckOrder;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.service.WarehouseCheckOrderService;
import com.jeeplus.modules.warehouses.service.WarehouseGoodsInfoService;

/**
 * 仓库盘点Controller
 * 
 * @author hxting
 * @version 2017-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouses/warehouseCheckOrder")
public class WarehouseCheckOrderController extends BaseController {

	@Autowired
	private WarehouseCheckOrderService warehouseCheckOrderService;

	@Autowired
	private GoodsUnitService goodsUnitService;

	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;

	@ModelAttribute
	public WarehouseCheckOrder get(@RequestParam(required = false) String id) {
		WarehouseCheckOrder entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = warehouseCheckOrderService.get(id);
		}
		if (entity == null) {
			entity = new WarehouseCheckOrder();
		}
		return entity;
	}

	/**
	 * 仓库盘点列表页面
	 */
	@RequiresPermissions("warehouses:warehouseCheckOrder:list")
	@RequestMapping(value = { "list", "" })
	public String list(WarehouseCheckOrder warehouseCheckOrder, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<WarehouseCheckOrder> page = warehouseCheckOrderService
				.findPage(new Page<WarehouseCheckOrder>(request, response), warehouseCheckOrder);
		model.addAttribute("page", page);
		return "modules/warehouses/checkorder/warehouseCheckOrderList";
	}

	/**
	 * 查看，增加，编辑仓库盘点表单页面
	 */
	@RequiresPermissions(value = { "warehouses:warehouseCheckOrder:view", "warehouses:warehouseCheckOrder:add",
			"warehouses:warehouseCheckOrder:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(WarehouseCheckOrder warehouseCheckOrder, Model model) {
		List<WarehouseCheckGoods> list = warehouseCheckOrder.getWarehouseCheckGoodsList();
		if (list != null && list.size() > 0) {
			for (WarehouseCheckGoods wcg : list) {
				if (wcg.getGoodsSpu() != null && wcg.getGoodsSku() != null) {
					GoodsUnit goodsUnit = goodsUnitService.get(wcg.getGoodsSpu().getGoodsUnit().getId());
					wcg.getGoodsSpu().setGoodsUnit(goodsUnit);
				}
			}
		}

		model.addAttribute("warehouseCheckOrder", warehouseCheckOrder);
		return "modules/warehouses/checkorder/warehouseCheckOrderForm";
	}

	/**
	 * 保存仓库盘点
	 */
	@RequiresPermissions(value={"warehouses:warehouseCheckOrder:add","warehouses:warehouseCheckOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WarehouseCheckOrder warehouseCheckOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{					
		//盘点总数
		int checkTotalNum = 0;
		//盈亏总数
		int profitLossTotalNum = 0;
		
		if(warehouseCheckOrder.getFcheckOrder() == null || warehouseCheckOrder.getFcheckOrder().equals("")){
			String orderNo = OddNumbers.getOrderNo();
			orderNo ="PD"+orderNo;
			warehouseCheckOrder.setFcheckOrder(orderNo);
		}
		if(warehouseCheckOrder.getWarehouse() != null){
			WarehouseGoodsInfo warehouseGoodsInfo = new WarehouseGoodsInfo();
			warehouseGoodsInfo.setWarehouse(warehouseCheckOrder.getWarehouse());
			List<WarehouseCheckGoods> warehouseCheckGoodsList = warehouseCheckOrder.getWarehouseCheckGoodsList();
			List<WarehouseGoodsInfo> findList = null;
			if(warehouseCheckGoodsList == null || warehouseCheckGoodsList.size() == 0){
				findList = warehouseGoodsInfoService.findList(warehouseGoodsInfo);
			}
			if(findList != null && findList.size()>0){
				for (WarehouseGoodsInfo wgi : findList) {
					WarehouseCheckGoods wcg = new WarehouseCheckGoods();
					wcg.setGoodsSpu(wgi.getGoodsSpu());
					wcg.setGoodsSku(wgi.getGoodsSku());
					warehouseCheckGoodsList.add(wcg);
				}
			}
		}
		//盘点状态
		if(WarehouseCheckOrder.CHECK_STATUS_CHECK.equals(warehouseCheckOrder.getFstatus())){
			for (WarehouseCheckGoods wcg : warehouseCheckOrder.getWarehouseCheckGoodsList()) {
				WarehouseGoodsInfo w = new WarehouseGoodsInfo(); 
				w.setGoodsSpu(wcg.getGoodsSpu());
				w.setGoodsSku(wcg.getGoodsSku());
				w.setWarehouse(warehouseCheckOrder.getWarehouse());
				List<WarehouseGoodsInfo> list = warehouseGoodsInfoService.findList(w);
				if(list != null && list.size() >0){
					WarehouseGoodsInfo wgi = list.get(0);
					//盘点前数量
					Integer ftotalinventory = wgi.getFtotalinventory();
					//盈亏数量
					Integer ProfitLossNum = wcg.getCheckNum() - wgi.getFtotalinventory();
					//盈亏金额
					Double profitLossMoney = null;
					
					//获得商品的采购价格
					/*if(wgi.getGoodsSpu() != null){
						String fbuyprice = wgi.getGoodsSpu().getFbuyprice();
						if(fbuyprice != null){
							BigDecimal	buyprice = new BigDecimal(fbuyprice).setScale(2, BigDecimal.ROUND_HALF_UP);
							profitLossMoney = buyprice.doubleValue()*ProfitLossNum;
						}
					}*/
					wcg.setCheckBeforeNum(ftotalinventory);
					wcg.setProfitLossNum(ProfitLossNum);
					wcg.setProfitLossMoney(profitLossMoney);
					//盘点总数
					checkTotalNum = checkTotalNum + wcg.getCheckNum();
					profitLossTotalNum = profitLossTotalNum + ProfitLossNum;
					warehouseCheckOrder.setCheckDate(new Date());
					warehouseCheckOrder.setCheckTotalNum(checkTotalNum);
					warehouseCheckOrder.setProfitLossTotalNum(profitLossTotalNum);
				}
			}
		}
		
		if(!warehouseCheckOrder.getIsNewRecord()){//编辑表单保存
			WarehouseCheckOrder t = warehouseCheckOrderService.get(warehouseCheckOrder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(warehouseCheckOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			warehouseCheckOrderService.save(t);//保存
		}else{//新增表单保存
			warehouseCheckOrderService.save(warehouseCheckOrder);//保存
		}
		addMessage(redirectAttributes, "保存仓库盘点成功");
		return "redirect:"+Global.getAdminPath()+"/warehouses/warehouseCheckOrder/?repage";
	}

	/**
	 * 逻辑删除仓库盘点
	 */
	@RequiresPermissions("warehouses:warehouseCheckOrder:del")
	@RequestMapping(value = "delete")
	public String delete(WarehouseCheckOrder warehouseCheckOrder, RedirectAttributes redirectAttributes) {
		warehouseCheckOrderService.deleteByLogic(warehouseCheckOrder);
		addMessage(redirectAttributes, "删除仓库盘点成功");
		return "redirect:" + Global.getAdminPath() + "/warehouses/warehouseCheckOrder/?repage";
	}

	/**
	 * 批量逻辑删除仓库盘点
	 */
	@RequiresPermissions("warehouses:warehouseCheckOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			warehouseCheckOrderService.deleteByLogic(warehouseCheckOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除仓库盘点成功");
		return "redirect:" + Global.getAdminPath() + "/warehouses/warehouseCheckOrder/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("warehouses:warehouseCheckOrder:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WarehouseCheckOrder warehouseCheckOrder, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "仓库盘点" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<WarehouseCheckOrder> page = warehouseCheckOrderService
					.findPage(new Page<WarehouseCheckOrder>(request, response, -1), warehouseCheckOrder);
			new ExportExcel("仓库盘点", WarehouseCheckOrder.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出仓库盘点记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/warehouses/warehouseCheckOrder/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("warehouses:warehouseCheckOrder:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WarehouseCheckOrder> list = ei.getDataList(WarehouseCheckOrder.class);
			for (WarehouseCheckOrder warehouseCheckOrder : list) {
				try {
					warehouseCheckOrderService.save(warehouseCheckOrder);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条仓库盘点记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条仓库盘点记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库盘点失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/warehouses/warehouseCheckOrder/?repage";
	}

	/**
	 * 下载导入仓库盘点数据模板
	 */
	@RequiresPermissions("warehouses:warehouseCheckOrder:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "仓库盘点数据导入模板.xlsx";
			List<WarehouseCheckOrder> list = Lists.newArrayList();
			new ExportExcel("仓库盘点数据", WarehouseCheckOrder.class, 1).setDataList(list).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/warehouses/warehouseCheckOrder/?repage";
	}

	/**
	 * 选择仓库
	 */
	@RequestMapping(value = "selectwarehouse")
	public String selectwarehouse(Warehouse warehouse, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Warehouse> page = warehouseCheckOrderService.findPageBywarehouse(new Page<Warehouse>(request, response),
				warehouse);
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

}