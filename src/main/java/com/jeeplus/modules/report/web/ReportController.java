/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.report.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketChaDetail;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.report.service.CheckReportService;
import com.jeeplus.modules.report.service.MarketBackReportService;
import com.jeeplus.modules.report.service.MarketReportService;
import com.jeeplus.modules.report.service.PurchaseReportService;
import com.jeeplus.modules.report.service.TransferReportService;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckGoods;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;

/**
 * 数据报表Controller
 * @author hxteng
 * @version 2017-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportData")
public class ReportController extends BaseController {

	@Autowired
	private PurchaseReportService purchaseReportService;
	
	@Autowired
	private MarketReportService marketReportService;
	
	@Autowired
	private MarketBackReportService marketBackReportService;
	
	@Autowired
	private CheckReportService checkReportService;
	
	@Autowired
	private TransferReportService transferReportService;
	
	
	
	
	
	/**
	 * 采购商品报表
	 */
	@RequestMapping(value = {"PurchasingReport"})
	public String PurchasingReport(OrderProDetail orderProDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderProDetail> page = purchaseReportService.findOrderProDetailList(new Page<OrderProDetail>(request, response), orderProDetail);
		Map<String, Object> purDetail = purchaseReportService.getPurDetail(orderProDetail);
		
		model.addAttribute("page", page);
		model.addAttribute("purDetail", purDetail);
		return "modules/report/purchase/purchasingReport";
	}
	/**
	 * 采购商品详情
	 * @param orderProDetail
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"purchaseForm"})
	public String purchaseForm(OrderProDetail orderProDetail, Model model, String id) {
		orderProDetail = purchaseReportService.getOrderProDetailById(id);
		model.addAttribute("orderProDetail", orderProDetail);
		return "modules/report/purchase/purchasingReportForm";
	}
	
	/**
	 * 采购退单商品报表
	 */
	@RequestMapping(value = {"purchasingBackOrderReport"})
	public String  purchasingBackOrderReport(OrderProChaDetail orderProChaDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderProChaDetail> page = purchaseReportService.findOrderProChaDetailList(new Page<OrderProChaDetail>(request, response), orderProChaDetail);
		Map<String, Object> purBackDetail = purchaseReportService.getPurBackDetail(orderProChaDetail);
		model.addAttribute("page", page);
		model.addAttribute("purBackDetail", purBackDetail);
		return "modules/report/purchase/purchasingBackReport";
	}
	
	
	@RequestMapping(value = {"purchaseBackOrderForm"})
	public String purchaseBackOrderForm(OrderProChaDetail orderProChaDetail, Model model, String id) {
		orderProChaDetail = purchaseReportService.getOrderProChaDetailById(id);
		model.addAttribute("orderProChaDetail", orderProChaDetail);
		return "modules/report/purchase/purchasingBackReportForm";
	}
	
	/**
	 * 销售商品列表
	 * @param orderMarketDetail
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"marketGoodsListReport"})
	public String  marketGoodsListReport(OrderMarketDetail orderMarketDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderMarketDetail> page = marketReportService.findMarketDetailList(new Page<OrderMarketDetail>(request, response), orderMarketDetail);
		Map<String, Object> marketDetail = marketReportService.getMarketDetail(orderMarketDetail);
		model.addAttribute("page", page);
		model.addAttribute("marketDetail", marketDetail);
		
		return "modules/report/market/marketGoodsListReport";
	}
	
	/**
	 * 销售商品详情
	 * @param orderMarketDetail
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"marketGoodsForm"})
	public String marketGoodsForm(OrderMarketDetail orderMarketDetail, Model model, String id) {
		orderMarketDetail = marketReportService.get(id);
		model.addAttribute("orderMarketDetail", orderMarketDetail);
		return "modules/report/market/marketGoodsForm";
	}
	
	
	/**
	 * 销售退单商品列表
	 * @param orderMarketChaDetail
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"marketBackGoodsListReport"})
	public String  marketBackGoodsListReport(OrderMarketChaDetail orderMarketChaDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderMarketChaDetail> page = marketBackReportService.findMarketBackDetailList(new Page<OrderMarketChaDetail>(request, response), orderMarketChaDetail);
		Map<String, Object> marketBackDetail = marketBackReportService.getMarketBackDetail(orderMarketChaDetail);
		model.addAttribute("page", page);
		model.addAttribute("marketBackDetail", marketBackDetail);
		
		return "modules/report/market/marketBackGoodsListReport";
	}
	
	/**
	 * 销售退货退货商品详情
	 * @param orderMarketChaDetail
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"marketBackGoodsForm"})
	public String marketBackGoodsForm(OrderMarketChaDetail orderMarketChaDetail, Model model, String id) {
		orderMarketChaDetail = marketBackReportService.get(id);
		model.addAttribute("orderMarketChaDetail", orderMarketChaDetail);
		return "modules/report/market/marketBackGoodsForm";
	}
	
	/**
	 * 盘点商品列表
	 * @param warehouseCheckGoods
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"checkGoodsListReport"})
	public String  checkGoodsListReport(WarehouseCheckGoods warehouseCheckGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseCheckGoods> page = checkReportService.findPage(new Page<WarehouseCheckGoods>(request, response), warehouseCheckGoods);
		Map<String, Object> checkGoodsDetail = checkReportService.getCheckGoodsDetail(warehouseCheckGoods);
		model.addAttribute("page", page);
		model.addAttribute("checkGoodsDetail", checkGoodsDetail);
		return "modules/report/check/checkGoodsListReport";
	}
	
	/**
	 * 盘点商品详情
	 * @param warehouseCheckGoods
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"checkGoodsForm"})
	public String checkGoodsForm(WarehouseCheckGoods warehouseCheckGoods, Model model, String id) {
		warehouseCheckGoods = checkReportService.get(id);
		model.addAttribute("warehouseCheckGoods", warehouseCheckGoods);
		return "modules/report/check/checkGoodsForm";
	}
	
	/**
	 * 调拨商品列表
	 * @param warehouseTransferGoods
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"transferGoodsListReport"})
	public String  transferGoodsListReport(WarehouseTransferGoods warehouseTransferGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarehouseTransferGoods> page = transferReportService.findPage(new Page<WarehouseTransferGoods>(request, response), warehouseTransferGoods);
		model.addAttribute("page", page);
		return "modules/report/transfer/transferGoodsListReport";
	}
	
	/**
	 * 调拨商品详情
	 * @param warehouseTransferGoods
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"transferGoodsForm"})
	public String transferGoodsForm(WarehouseTransferGoods warehouseTransferGoods, Model model, String id) {
		warehouseTransferGoods = transferReportService.get(id);
		model.addAttribute("warehouseTransferGoods", warehouseTransferGoods);
		return "modules/report/transfer/transferGoodsForm";
	}
	
	
}