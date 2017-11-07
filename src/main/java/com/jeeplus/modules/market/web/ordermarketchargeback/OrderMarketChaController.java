/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.web.ordermarketchargeback;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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

import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.service.basic.CustomerBasicService;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.service.WarehouseGoodsInfoService;
import com.jeeplus.modules.warehouses.service.WarehouseRecordService;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
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
import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketCha;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketChaDetail;
import com.jeeplus.modules.market.service.ordermarket.OrderMarketDetailService;
import com.jeeplus.modules.market.service.ordermarket.OrderMarketService;
import com.jeeplus.modules.market.service.ordermarketchargeback.OrderMarketChaService;
import com.jeeplus.modules.merchant.entity.management.Merchant;

/**
 * 销售退货单Controller
 * 
 * @author diqiang
 * @version 2017-06-18
 */
@Controller
@RequestMapping(value = "${adminPath}/market/ordermarketchargeback/orderMarketCha")
public class OrderMarketChaController extends BaseController {

	@Autowired
	private OrderMarketChaService orderMarketChaService;
	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;
	@Autowired
	private WarehouseRecordService warehouseRecordService;
	
	@Autowired
	private OrderMarketService orderMarketService;
	
	@Autowired
	private OrderMarketDetailService orderMarketDetailService;
	
	@Autowired
	private CustomerBasicService customerBasicService;
	
	@ModelAttribute
	public OrderMarketCha get(@RequestParam(required = false) String id) {
		OrderMarketCha entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = orderMarketChaService.get(id);
		}
		if (entity == null) {
			entity = new OrderMarketCha();
		}
		return entity;
	}

	/**
	 * 销售退货单列表页面
	 */
	@RequiresPermissions("market:ordermarketchargeback:orderMarketCha:list")
	@RequestMapping(value = { "list", "" })
	public String list(OrderMarketCha orderMarketCha, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<OrderMarketCha> page = orderMarketChaService.findPage(new Page<OrderMarketCha>(request, response),
				orderMarketCha);
		List<OrderMarketCha> list = page.getList();
		for (OrderMarketCha omc : list) {
			orderMarketChaService.setOrderOrderMarketChaProperties(omc);
			List<OrderMarketChaDetail> orderMarketChaDetailList = omc.getOrderMarketChaDetailList();
			for (OrderMarketChaDetail omdc : orderMarketChaDetailList) {
				orderMarketChaService.setOrderMarketChaDetailProperties(omdc);
			}
		}
		model.addAttribute("page", page);
		return "modules/market/ordermarketchargeback/orderMarketChaList";
	}

	/**
	 * 编辑销售退货单表单页面
	 */
	@RequiresPermissions(value = { 
			"market:ordermarketchargeback:orderMarketCha:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderMarketCha orderMarketCha, Model model,HttpServletRequest request) {
	
		orderMarketChaService.setOrderOrderMarketChaProperties(orderMarketCha);
		List<OrderMarketChaDetail> orderMarketChaDetailList = orderMarketCha.getOrderMarketChaDetailList();
		for (OrderMarketChaDetail omdc : orderMarketChaDetailList) {
			orderMarketChaService.setOrderMarketChaDetailProperties(omdc);
		}

		model.addAttribute("orderMarketCha", orderMarketCha);
		return "modules/market/ordermarketchargeback/orderMarketChaForm";
	}
	
	
	/**
	 * 查看销售退货单表单页面
	 */
	@RequiresPermissions(value = { "market:ordermarketchargeback:orderMarketCha:view" }, logical = Logical.OR)
	@RequestMapping(value = "view")
	public String view(OrderMarketCha orderMarketCha, Model model,HttpServletRequest request) {
		
		orderMarketChaService.setOrderOrderMarketChaProperties(orderMarketCha);
		List<OrderMarketChaDetail> orderMarketChaDetailList = orderMarketCha.getOrderMarketChaDetailList();
		for (OrderMarketChaDetail omdc : orderMarketChaDetailList) {
			orderMarketChaService.setOrderMarketChaDetailProperties(omdc);
		}

		model.addAttribute("orderMarketCha", orderMarketCha);
		return "modules/market/ordermarketchargeback/orderMarketChaView";
	}

	/**
	 * 保存销售退货单
	 */
	@RequiresPermissions(value = { "market:ordermarketchargeback:orderMarketCha:add",
			"market:ordermarketchargeback:orderMarketCha:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(OrderMarketCha orderMarketCha, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request)
			throws Exception {
		if (!beanValidator(model, orderMarketCha)) {
			return form(orderMarketCha, model,request);
		}
		if (!orderMarketCha.getIsNewRecord()) {// 编辑表单保存
			
			//先计算一下传输对象的价钱
//			orderMarketChaService.setOrderOrderMarketChaProperties(orderMarketCha);
			
			OrderMarketCha t = orderMarketChaService.get(orderMarketCha.getId());// 从数据库取出记录的值
			Integer fordertype = t.getFordertype();
			MyBeanUtils.copyBeanNotNull2Bean(orderMarketCha, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			if ( fordertype== 1) {

				// 如果采购单已执行完毕就更改仓库记录
				if (t.getFstatus() == 7) {
					t.setFordertype(2);
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					Warehouse fwarehose = t.getWarehouse();
					if (fwarehose != null) {
						wgInfo.setWarehouse(fwarehose);
					}
					List<OrderMarketChaDetail> list = new ArrayList<>();
					List<OrderMarketChaDetail> orderMarketChaDetailList = t.getOrderMarketChaDetailList();
					for (OrderMarketChaDetail omdc : orderMarketChaDetailList) {
						
						orderMarketChaService.setspu(omdc);
						// 设置商品属性
						orderMarketChaService.setgoods(omdc);
						// 单条商品数量
						Integer fgoodsnum = omdc.getFgoodsnum();
						// 商品ID
						GoodsSpu fspu = omdc.getFspu();
						Brand brand = null;
						Categorys categorys = null;
						GoodsUnit goodsUnit = null;
						if (fspu != null) {
							brand = fspu.getBrand();
							categorys = fspu.getCategorys();
							goodsUnit = fspu.getGoodsUnit();
						}
						// skuID
						GoodsSku fsku = omdc.getFsku();
						wgInfo.setGoodsSku(fsku);
						wgInfo.setGoodsSpu(fspu);
						List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
						if (wginfolist == null || wginfolist.size() == 0) {
							addMessage(redirectAttributes, "销售退货失败，无该商品记录");
							return "redirect:" + Global.getAdminPath()
									+ "/market/ordermarketchargeback/orderMarketCha/?repage";
						} else {
							WarehouseGoodsInfo warehouseGoodsInfo = wginfolist.get(0);
							Integer finventory = warehouseGoodsInfo.getFinventory();
							Integer ftotalinventory = warehouseGoodsInfo.getFtotalinventory();
							if(finventory!=null&&finventory>0){
								BigDecimal add = new BigDecimal(finventory.toString())
										.add(new BigDecimal(fgoodsnum.toString()));
								warehouseGoodsInfo.setFinventory(add.intValue());
							}
							if(ftotalinventory!=null&&ftotalinventory>0){
								BigDecimal add2 = new BigDecimal(ftotalinventory.toString())
										.add(new BigDecimal(fgoodsnum.toString()));
								
								warehouseGoodsInfo.setFtotalinventory(add2.intValue());
							}
							
							warehouseGoodsInfoService.save(warehouseGoodsInfo);

							// 添加流水
							WarehouseRecord warehouseRecord = new WarehouseRecord();
							warehouseRecord.setBusinessType(warehouseRecord.BUSINESS_TYPE_MARKETCHA);
							warehouseRecord.setGoodsSku(fsku);
							warehouseRecord.setGoodsSpu(fspu);
							warehouseRecord.setWarehouse(t.getWarehouse());
							warehouseRecord.setBusinessTime(new Date());
							warehouseRecord.setBusinessorderNumber(t.getFordernum());
							warehouseRecord.setChangeNum(fgoodsnum);
							warehouseRecord.setRemainingNum(warehouseGoodsInfo.getFtotalinventory());
							warehouseRecordService.save(warehouseRecord);

							omdc.setFwarehousegoodsinfo(warehouseGoodsInfo);
							list.add(omdc);

						}
					}

					// 如果订单执行完毕设置结束时间
					if (t.getFstatus() == 7) {
						t.setFendtime(new Date());
						t.setFordertype(2);
					}
					t.setOrderMarketChaDetailList(list);
				}

			}else{
				addMessage(redirectAttributes, "修改销售退单失败，该订单属于不可操作状态");
				return "redirect:" + Global.getAdminPath()
						+ "/market/ordermarketchargeback/orderMarketCha/?repage";
			}

			// 设置订单金额
			BigDecimal countprice = new BigDecimal("0");
			List<OrderMarketChaDetail> list = new ArrayList<>();
			List<OrderMarketChaDetail> orderMarketChaDetailList = t.getOrderMarketChaDetailList();
			if (orderMarketChaDetailList != null && orderMarketChaDetailList.size() > 0) {
				for (OrderMarketChaDetail omdc : orderMarketChaDetailList) {
					orderMarketChaService.setspu(omdc);
					orderMarketChaService.setOrderMarketChaDetailProperties(omdc);
					countprice = countprice.add(omdc.getFdiscountcountmoney());
					// 设置从库存
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					// 设置商品属性
					orderMarketChaService.setgoods(omdc);
					// 商品ID
					GoodsSpu fspu = omdc.getFspu();
					// 单条商品数量
					Integer fgoodsnum = omdc.getFgoodsnum();

					Brand brand = null;
					Categorys categorys = null;
					GoodsUnit goodsUnit = null;
					if (fspu != null) {
						brand = fspu.getBrand();
						categorys = fspu.getCategorys();
						goodsUnit = fspu.getGoodsUnit();
					}
					// skuID
					GoodsSku fsku = omdc.getFsku();
					wgInfo.setGoodsSku(fsku);
					wgInfo.setGoodsSpu(fspu);
					List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
					if (wginfolist != null && wginfolist.size() > 0) {
						omdc.setFwarehousegoodsinfo(wginfolist.get(0));
					} else {
						addMessage(redirectAttributes, "修改销售退单失败，库存无该商品");
						return "redirect:" + Global.getAdminPath()
								+ "/market/ordermarketchargeback/orderMarketCha/?repage";
					}
					list.add(omdc);

				}
				t.setOrderMarketChaDetailList(list);
				t.setFcountprice(countprice);
				orderMarketChaService.setOrderOrderMarketChaProperties(t);
				//获取订单总价
				BigDecimal fordercountprice = t.getFordercountprice();
				//判断是否有整单折扣设置整单折扣后价格
				String forderdiscount = t.getForderdiscount();

				if(forderdiscount!=null&&!forderdiscount.trim().isEmpty()){
				
					BigDecimal dis=new BigDecimal(forderdiscount).divide(new BigDecimal(100));
					//订单折后总价
					t.setFdiscountprice(fordercountprice.multiply(dis));
					//有整单折扣重新计算欠款
					orderMarketChaService.setfdebt(t);
				}else{
					//无折扣订单折后总价
					t.setFdiscountprice(fordercountprice);
				}
				
				//获取是否有客户对象是否有折扣
				CustomerBasic customerBasic = t.getCustomerBasic();
				String fdiscount = "";
				if(customerBasic!=null&&StringUtils.isNoneBlank(customerBasic.getId())){
					CustomerBasic customerBasic2 = customerBasicService.get(customerBasic);
					fdiscount = customerBasic2.getFdiscount();
				}
				//如果客户有折扣则再折上折
				if(fdiscount!=null&&!fdiscount.trim().isEmpty()){
					BigDecimal fdiscountprice = t.getFdiscountprice();
					BigDecimal customerdiscount = new BigDecimal(fdiscount).divide(new BigDecimal(100));
					t.setFdiscountprice(fdiscountprice.multiply(customerdiscount));
				}

			} else {
				// t.setFcountprice("0");
				t.setFcountprice(new BigDecimal("0"));
				t.setFdiscountprice(new BigDecimal("0"));
			}
			//判断是否有抹零
			BigDecimal fcutsmall = t.getFcutsmall();
			if(fcutsmall!=null){
				t.setFdiscountprice(t.getFdiscountprice().subtract(fcutsmall));
			}
			orderMarketChaService.save(t);// 保存
		} else {// 新增表单保存
				// 设置订单金额
			orderMarketChaService.setOrderOrderMarketChaProperties(orderMarketCha);
			BigDecimal countprice = new BigDecimal("0");
			List<OrderMarketChaDetail> orderMarketChaDetailList = orderMarketCha.getOrderMarketChaDetailList();
			if (orderMarketChaDetailList != null && orderMarketChaDetailList.size() > 0) {
				for (OrderMarketChaDetail omdc : orderMarketChaDetailList) {
					orderMarketChaService.setspu(omdc);
					orderMarketChaService.setOrderMarketChaDetailProperties(omdc);
					countprice = countprice.add(omdc.getFdiscountcountmoney());
					// 设置从库存
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					// 设置商品属性
					orderMarketChaService.setgoods(omdc);
					// 商品ID
					GoodsSpu fspu = omdc.getFspu();
					// 单条商品数量
					Integer fgoodsnum = omdc.getFgoodsnum();

					Brand brand = null;
					Categorys categorys = null;
					GoodsUnit goodsUnit = null;
					if (fspu != null) {
						brand = fspu.getBrand();
						categorys = fspu.getCategorys();
						goodsUnit = fspu.getGoodsUnit();
					}
					// skuID
					GoodsSku fsku = omdc.getFsku();
					wgInfo.setGoodsSku(fsku);
					wgInfo.setGoodsSpu(fspu);
					List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
					if (wginfolist != null && wginfolist.size() > 0) {
						omdc.setFwarehousegoodsinfo(wginfolist.get(0));
					} else {
						addMessage(redirectAttributes, "生成销售退单失败，库存无该商品");
						return "redirect:" + Global.getAdminPath()
								+ "/market/ordermarketchargeback/orderMarketCha/?repage";
					}
				}
				orderMarketCha.setFcountprice(countprice);
				
				//再次计算订单价钱
				orderMarketChaService.setOrderOrderMarketChaProperties(orderMarketCha);

				//获取订单总价
				BigDecimal fordercountprice = countprice;
				//判断是否有整单折扣设置整单折扣后价格
				String forderdiscount = orderMarketCha.getForderdiscount();

				if(forderdiscount!=null&&!forderdiscount.trim().isEmpty()){
				
					BigDecimal dis=new BigDecimal(forderdiscount).divide(new BigDecimal(100));
					//订单折后总价
					orderMarketCha.setFdiscountprice(fordercountprice.multiply(dis));
					//有整单折扣重新计算欠款
					orderMarketChaService.setfdebt(orderMarketCha);
				}else{
					//无折扣订单折后总价
					orderMarketCha.setFdiscountprice(fordercountprice);
				}
				
				//获取是否有客户对象是否有折扣
				CustomerBasic customerBasic = orderMarketCha.getCustomerBasic();
				String fdiscount = "";
				if(customerBasic!=null&&StringUtils.isNoneBlank(customerBasic.getId())){
					CustomerBasic customerBasic2 = customerBasicService.get(customerBasic);
					fdiscount = customerBasic2.getFdiscount();
				}
				//如果客户有折扣则再折上折
				if(fdiscount!=null&&!fdiscount.trim().isEmpty()){
					BigDecimal fdiscountprice = orderMarketCha.getFdiscountprice();
					BigDecimal customerdiscount = new BigDecimal(fdiscount).divide(new BigDecimal(100));
					orderMarketCha.setFdiscountprice(fdiscountprice.multiply(customerdiscount));
				}
				
			} else {
				orderMarketCha.setFcountprice(new BigDecimal(0));
				orderMarketCha.setFdiscountprice(new BigDecimal(0));
			}
			
			//判断是否有抹零
			BigDecimal fcutsmall = orderMarketCha.getFcutsmall();
			if(fcutsmall!=null){
				orderMarketCha.setFdiscountprice(orderMarketCha.getFdiscountprice().subtract(fcutsmall));
			}
			
			orderMarketCha.setFstatus(1);
			orderMarketChaService.save(orderMarketCha);// 保存
		}
		addMessage(redirectAttributes, "保存销售退货单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}

	/**
	 * 逻辑删除销售退货单
	 */
	@RequiresPermissions("market:ordermarketchargeback:orderMarketCha:del")
	@RequestMapping(value = "delete")
	public String delete(OrderMarketCha orderMarketCha, RedirectAttributes redirectAttributes) {
		orderMarketChaService.deleteByLogic(orderMarketCha);
		addMessage(redirectAttributes, "删除销售退货单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}

	/**
	 * 批量逻辑删除销售退货单
	 */
	@RequiresPermissions("market:ordermarketchargeback:orderMarketCha:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			orderMarketChaService.deleteByLogic(orderMarketChaService.get(id));
		}
		addMessage(redirectAttributes, "删除销售退货单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("market:ordermarketchargeback:orderMarketCha:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(OrderMarketCha orderMarketCha, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售退货单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<OrderMarketCha> page = orderMarketChaService.findPage(new Page<OrderMarketCha>(request, response, -1),
					orderMarketCha);
			new ExportExcel("销售退货单", OrderMarketCha.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出销售退货单记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}

	/**
	 * 导出采购退单详细excel文件
	 */
	@RequiresPermissions("market:ordermarketchargeback:orderMarketCha:export")
	@RequestMapping(value = "exportmarketChaDetail", method = RequestMethod.GET)
	public String exportMarketChaDetail(OrderMarketCha orderMarketCha, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售退单详细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			OrderMarketChaDetail detail = new OrderMarketChaDetail();
			detail.setFordermarketchargeback(orderMarketCha);
//			detail.setFstatus(1);
			Page<OrderMarketChaDetail> page = orderMarketChaService
					.findPageDetil(new Page<OrderMarketChaDetail>(request, response, -1), detail);
			new ExportExcel("销售退单详细", OrderMarketChaDetail.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出销售退单详细失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}
	
	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("market:ordermarketchargeback:orderMarketCha:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderMarketCha> list = ei.getDataList(OrderMarketCha.class);
			for (OrderMarketCha orderMarketCha : list) {
				try {
					orderMarketChaService.save(orderMarketCha);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条销售退货单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条销售退货单记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售退货单失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}

	/**
	 * 下载导入销售退货单数据模板
	 */
	@RequiresPermissions("market:ordermarketchargeback:orderMarketCha:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售退货单数据导入模板.xlsx";
			List<OrderMarketCha> list = Lists.newArrayList();
			new ExportExcel("销售退货单数据", OrderMarketCha.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}

	/**
	 * 选择客户名称
	 */
	@RequestMapping(value = "selectcustomerBasic")
	public String selectcustomerBasic(CustomerBasic customerBasic, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<CustomerBasic> page = orderMarketChaService
				.findPageBycustomerBasic(new Page<CustomerBasic>(request, response), customerBasic);
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
		model.addAttribute("obj", customerBasic);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择仓库名称
	 */
	@RequestMapping(value = "selectwarehouse")
	public String selectwarehouse(Warehouse warehouse, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Warehouse> page = orderMarketChaService.findPageBywarehouse(new Page<Warehouse>(request, response),
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

	/**
	 * 选择结算账户
	 */
	@RequestMapping(value = "selectclearingAccount")
	public String selectclearingAccount(ClearingAccount clearingAccount, String url, String fieldLabels,
			String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<ClearingAccount> page = orderMarketChaService
				.findPageByclearingAccount(new Page<ClearingAccount>(request, response), clearingAccount);
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
		model.addAttribute("obj", clearingAccount);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择账目类型
	 */
	@RequestMapping(value = "selectaccountType")
	public String selectaccountType(AccountType accountType, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<AccountType> page = orderMarketChaService.findPageByaccountType(new Page<AccountType>(request, response),
				accountType);
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
		model.addAttribute("obj", accountType);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

//	/**
//	 * 选择商户ID
//	 */
//	@RequestMapping(value = "selectfsponsor")
//	public String selectfsponsor(Merchant fsponsor, String url, String fieldLabels, String fieldKeys,
//			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
//			Model model) {
//		Page<Merchant> page = orderMarketChaService.findPageByfsponsor(new Page<Merchant>(request, response), fsponsor);
//		try {
//			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
//			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
//			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
//			searchKey = URLDecoder.decode(searchKey, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		model.addAttribute("labelNames", fieldLabels.split("\\|"));
//		model.addAttribute("labelValues", fieldKeys.split("\\|"));
//		model.addAttribute("fieldLabels", fieldLabels);
//		model.addAttribute("fieldKeys", fieldKeys);
//		model.addAttribute("url", url);
//		model.addAttribute("searchLabel", searchLabel);
//		model.addAttribute("searchKey", searchKey);
//		model.addAttribute("obj", fsponsor);
//		model.addAttribute("page", page);
//		return "modules/sys/gridselect";
//	}

	/**
	 * 选择打印模板
	 */
	@RequestMapping(value = "selectfmodeltype")
	public String selectfmodeltype(SysModelType fmodeltype, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SysModelType> page = orderMarketChaService.findPageByfmodeltype(new Page<SysModelType>(request, response),
				fmodeltype);
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
		model.addAttribute("obj", fmodeltype);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择商品ID
	 */
	@RequestMapping(value = "selectfspu")
	public String selectfspu(GoodsSpu fspu, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {		
		Page<GoodsSpu> page = orderMarketChaService.findPageByfspu(new Page<GoodsSpu>(request, response), fspu);
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
		model.addAttribute("obj", fspu);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择商品详情ID
	 */
	@RequestMapping(value = "selectfsku")
	public String selectfsku(GoodsSku fsku, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {

		
		List<GoodsSku> list=new ArrayList<GoodsSku>();
		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();
		HttpSession session = request.getSession();
		String ordernumid = (String) session.getAttribute(windowsMACAddress + "ordernumid");
		OrderMarket om=null;
		try {
			om=orderMarketService.findUniqueByProperty("id", ordernumid);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(om!=null){			
			OrderMarketDetail orderMarketDetail = new OrderMarketDetail();
			orderMarketDetail.setOrderMarket(om);
			 session.removeAttribute(windowsMACAddress + "ordernumid");//删除session中的参数
			List<OrderMarketDetail> findList = orderMarketDetailService.findList(orderMarketDetail);
			
			if(findList!=null&&findList.size()>0){
				for(OrderMarketDetail od:findList){	
					list.add(od.getGoodsSku());		
					}			
				}		
			}
		Page<GoodsSku> page = new Page<>();
		fsku.setPage(page);
		page.setList(list);
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
		model.addAttribute("obj", fsku);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择销售单号
	 */
	@RequestMapping(value = "selectfordermarket")
	public String selectfordermarket(OrderMarket market, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Page<OrderMarket> page = orderMarketChaService.findListByfordermarket(new Page<OrderMarket>(request, response),
				market);
		
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
		model.addAttribute("obj", market);
		model.addAttribute("page", page);
		return "modules/sys/market/marketordernum";
	}
	
	/**
	 * 获取销售单号
	 */
	@RequestMapping(value = "ordernumid")
	public void getwarehouseid(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String ordernumid = "";
		ordernumid = (String) request.getParameter("ordernumid");
		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();

		session.setAttribute(windowsMACAddress + "ordernumid", ordernumid);
	}
	
	
	/**
	 * 撤销销售退货单
	 */
	@RequiresPermissions(value = { "market:ordermarketchargeback:orderMarketCha:edit" }, logical = Logical.OR)
	@RequestMapping(value = "revoke")
	public String revoke(OrderMarketCha orderMarketCha, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request)
			throws Exception {
		if (!beanValidator(model, orderMarketCha)) {
			return form(orderMarketCha, model,request);
		}
		if (!orderMarketCha.getIsNewRecord()) {// 编辑表单保存
			OrderMarketCha t = orderMarketChaService.get(orderMarketCha.getId());// 从数据库取出记录的值
			if (t.getFordertype() == 1) {
				MyBeanUtils.copyBeanNotNull2Bean(orderMarketCha, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
				t.setFordertype(3);
				orderMarketChaService.save(t);// 保存
			} else {// 新增表单保存
				addMessage(redirectAttributes, "撤销销售退货单失败，订单属于不可操作状态");
				return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
			}

		}
		addMessage(redirectAttributes, "撤销销售退货单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}
	
	
	
	/**
	 * 根据订单号查询单条数据用于其他页面查看订单信息
	 * 
	 * @param 此处numid的值为订单号
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "market:ordermarketchargeback:orderMarketCha:view" }, logical = Logical.OR)
	@RequestMapping("formbyordernum")
	public String formbyordernum(String numid, Model model) {
		// 回显对象属性
		OrderMarketCha omc = null;
		try {
			omc = orderMarketChaService.findUniqueByProperty("fordernum", numid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (omc != null) {
			omc = orderMarketChaService.get(omc.getId());
			List<OrderMarketChaDetail> orderMarketChaDetailList = omc.getOrderMarketChaDetailList();
			for (OrderMarketChaDetail omcd : orderMarketChaDetailList) {
				orderMarketChaService.setOrderMarketChaDetailProperties(omcd);
			}
			model.addAttribute("orderMarketCha", omc);
		}

		return "modules/market/ordermarketchargeback/orderMarketChaView";

	}
	
}