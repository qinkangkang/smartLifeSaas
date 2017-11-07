/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.web.ordermarket;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.service.sku.GoodsSkuService;
import com.jeeplus.modules.goods.service.spu.GoodsSpuService;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.service.WarehouseGoodsInfoService;
import com.jeeplus.modules.warehouses.service.WarehouseRecordService;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MacUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.market.service.ordermarket.OrderMarketDetailService;
import com.jeeplus.modules.market.service.ordermarket.OrderMarketService;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChargeback;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;

/**
 * 销售订单管理Controller
 * 
 * @author diqiang
 * @version 2017-06-18
 */
@Controller
@RequestMapping(value = "${adminPath}/market/ordermarket/orderMarket")
public class OrderMarketController extends BaseController {

	@Autowired
	private OrderMarketService orderMarketService;

	@Autowired
	private OrderMarketDetailService orderMarketDetailService;

	@Autowired
	private WarehouseRecordService warehouseRecordService;

	// 库存商品service
	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;

	@Autowired
	private GoodsSpuService goodsSpuService;

	@Autowired
	private GoodsSkuService goodsSkuService;
	
	@Autowired
	private CustomerBasicService customerBasicService;

	@ModelAttribute
	public OrderMarket get(@RequestParam(required = false) String id) {
		OrderMarket entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = orderMarketService.get(id);
		}
		if (entity == null) {
			entity = new OrderMarket();
		}
		return entity;
	}

	/**
	 * 销售订单列表页面
	 */
	@RequiresPermissions("market:ordermarket:orderMarket:list")
	@RequestMapping(value = { "list", "" })
	public String list(OrderMarket orderMarket, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderMarket> page = orderMarketService.findPage(new Page<OrderMarket>(request, response), orderMarket);

		List<OrderMarket> list = page.getList();

		for (OrderMarket om : list) {
			orderMarketService.setOrderOrderMarketProperties(om);
			List<OrderMarketDetail> orderMarketDetailList = om.getOrderMarketDetailList();
			for (OrderMarketDetail omd : orderMarketDetailList) {
				orderMarketService.setOrderMarketDetailProperties(omd);
			}
		}

		model.addAttribute("page", page);
		return "modules/market/ordermarket/orderMarketList";
	}

	/**
	 * 增加销售订单表单页面
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarket:add",
			 }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderMarket orderMarket, Model model) {
		orderMarketService.setOrderOrderMarketProperties(orderMarket);

		List<OrderMarketDetail> orderMarketDetailList = orderMarket.getOrderMarketDetailList();

		for (OrderMarketDetail omd : orderMarketDetailList) {

			orderMarketService.setOrderMarketDetailProperties(omd);

		}
		model.addAttribute("orderMarket", orderMarket);
		return "modules/market/ordermarket/orderMarketForm";
	}
	
	
	/**
	 * 查看，编辑销售订单表单页面
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarket:view","market:ordermarket:orderMarket:edit"}, logical = Logical.OR)
	@RequestMapping(value = "view")
	public String view(OrderMarket orderMarket, Model model,HttpServletRequest request) {
		String omid = orderMarket.getId();
		if(omid!=null&&!omid.trim().isEmpty()){
			String mac = MacUtils.getMac();
			HttpSession session = request.getSession();
			Warehouse fwarehose = orderMarket.getWarehouse();
			if(fwarehose!=null&&!fwarehose.getId().trim().isEmpty()){
				session.setAttribute(mac+"marketgetwarehouseid", fwarehose.getId());
			}
		}
		orderMarketService.setOrderOrderMarketProperties(orderMarket);

		List<OrderMarketDetail> orderMarketDetailList = orderMarket.getOrderMarketDetailList();

		for (OrderMarketDetail omd : orderMarketDetailList) {

			orderMarketService.setOrderMarketDetailProperties(omd);

		}
		model.addAttribute("orderMarket", orderMarket);
		return "modules/market/ordermarket/orderMarketView";
	}
	
	/**
	 *换货销售订单表单页面
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarket:edit"
			 }, logical = Logical.OR)
	@RequestMapping(value = "exchange")
	public String exchange(OrderMarket orderMarket, Model model,HttpServletRequest request) {
		String omid = orderMarket.getId();
		if(omid!=null&&StringUtils.isNoneBlank(omid)){
			String mac = MacUtils.getMac();
			HttpSession session = request.getSession();
			Warehouse fwarehose = orderMarket.getWarehouse();
			if(fwarehose!=null&&!fwarehose.getId().trim().isEmpty()){
				session.setAttribute(mac+"marketgetwarehouseid", fwarehose.getId());
			}
		}
		orderMarketService.setOrderOrderMarketProperties(orderMarket);

		List<OrderMarketDetail> orderMarketDetailList = orderMarket.getOrderMarketDetailList();

		for (OrderMarketDetail omd : orderMarketDetailList) {

			orderMarketService.setOrderMarketDetailProperties(omd);

		}
		model.addAttribute("orderMarket", orderMarket);
		return "modules/market/ordermarket/orderMarketExchange";
	}
	
	
	/**
	 * 保存销售订单
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarket:add"}, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(OrderMarket orderMarket, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) throws Exception {
		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();
		HttpSession session = request.getSession();
		
		if (!beanValidator(model, orderMarket)) {
			return form(orderMarket, model);
		}
		if (!orderMarket.getIsNewRecord()) {// 编辑表单保存
			// 给传递对象设置属性避免价钱报错
			orderMarketService.setOrderOrderMarketProperties(orderMarket);

			OrderMarket t = orderMarketService.get(orderMarket.getId());// 从数据库取出记录的值
			Integer fordertype = t.getFordertype();
			MyBeanUtils.copyBeanNotNull2Bean(orderMarket, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			if (fordertype == 0) {
				// 如果销售单已执行完毕就更改仓库记录
				if (t.getFstatus() == 3) {
					t.setFordertype(1);
					t.setFendtime(new Date());
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					Warehouse fwarehose = t.getWarehouse();
					if (fwarehose != null) {
						wgInfo.setWarehouse(fwarehose);
					}
					List<OrderMarketDetail> list = new ArrayList<>();
					List<OrderMarketDetail> orderMarketDetailList = t.getOrderMarketDetailList();
					for (OrderMarketDetail omd : orderMarketDetailList) {
						orderMarketService.setspu(omd);
						// 设置商品属性
						orderMarketService.setOrderMarketDetailProperties(omd);

						// 单条商品数量
						Integer fgoodsnum = omd.getFgoodsnum();
						// 商品ID
						GoodsSpu fspu = omd.getGoodsSpu();
						Brand brand = null;
						Categorys categorys = null;
						GoodsUnit goodsUnit = null;
						if (fspu != null) {
							brand = fspu.getBrand();
							categorys = fspu.getCategorys();
							goodsUnit = fspu.getGoodsUnit();
						}
						// skuID
						GoodsSku fsku = omd.getGoodsSku();
						wgInfo.setGoodsSku(fsku);
						wgInfo.setGoodsSpu(fspu);
						List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
						if (wginfolist == null || wginfolist.size() == 0) {
							addMessage(redirectAttributes, "开单失败，该商品已售空！！");
							return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
						} else {
							WarehouseGoodsInfo warehouseGoodsInfo = wginfolist.get(0);
							if (fgoodsnum > warehouseGoodsInfo.getFinventory()) {
								addMessage(redirectAttributes, "开单失败，所选仓库库存不足！！");
								return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
							}
							// 修改库存
							Integer finventory = warehouseGoodsInfo.getFinventory();
							Integer ftotalinventory = warehouseGoodsInfo.getFtotalinventory();
							if (finventory != null && finventory > 0) {
								BigDecimal add = new BigDecimal(finventory.toString())
										.subtract(new BigDecimal(fgoodsnum.toString()));
								warehouseGoodsInfo.setFinventory(add.intValue());
							}

							if (ftotalinventory != null && ftotalinventory > 0) {
								BigDecimal add2 = new BigDecimal(ftotalinventory.toString())
										.subtract(new BigDecimal(fgoodsnum.toString()));
								warehouseGoodsInfo.setFtotalinventory(add2.intValue());
							}

							warehouseGoodsInfoService.save(warehouseGoodsInfo);

							// 添加流水
							WarehouseRecord warehouseRecord = new WarehouseRecord();
							warehouseRecord.setBusinessType(warehouseRecord.BUSINESS_TYPE_MARKET);
							warehouseRecord.setGoodsSku(fsku);
							warehouseRecord.setGoodsSpu(fspu);
							warehouseRecord.setWarehouse(t.getWarehouse());
							warehouseRecord.setBusinessTime(new Date());
							warehouseRecord.setBusinessorderNumber(t.getFordernum());
							warehouseRecord.setChangeNum(fgoodsnum);
							warehouseRecord.setRemainingNum(warehouseGoodsInfo.getFtotalinventory());
							warehouseRecordService.save(warehouseRecord);

							omd.setFwarehousegoodsinfo(warehouseGoodsInfo);
							list.add(omd);
						}
					}

					// 如果订单执行完毕设置结束时间
					if (t.getFstatus() == 3) {
						t.setFendtime(new Date());
						//获取结算账户以防出错
						ClearingAccount clearingAccount = orderMarket.getClearingAccount();//结算账户
						if(clearingAccount!=null&&!clearingAccount.getId().trim().isEmpty()){
							
						}else{
							try {
								throw new Exception("获取结算账户时出错");
							} catch (Exception e) {
								e.printStackTrace();
								addMessage(redirectAttributes, "开单失败，系统运转时出错！~");
								return "redirect:" + Global.getAdminPath() + "market/ordermarket/orderMarket/?repage";
							}
						}
					}
					t.setOrderMarketDetailList(list);
				}
			} else {
				addMessage(redirectAttributes, "修改销售订单失败，已完成的订单不可修改！！！");
				return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
			}
			//当订单未结束时修改订单
			Warehouse warehouse = t.getWarehouse();
			// 设置订单金额
			BigDecimal countprice = new BigDecimal("0");
			List<OrderMarketDetail> list = new ArrayList<>();
			List<OrderMarketDetail> orderMarketDetailList = t.getOrderMarketDetailList();
			if (orderMarketDetailList != null && orderMarketDetailList.size() > 0) {
				for (OrderMarketDetail omd : orderMarketDetailList) {
					orderMarketService.setspu(omd);
					orderMarketService.setOrderMarketDetailProperties(omd);
					countprice = countprice.add(omd.getFdiscountcountmoney());
					// 设置从库存
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					// 设置商品属性
					orderMarketService.setgoods(omd);
					// 商品ID
					GoodsSpu fspu = omd.getGoodsSpu();
					// 单条商品数量
					Integer fgoodsnum = omd.getFgoodsnum();
					Brand brand = null;
					Categorys categorys = null;
					GoodsUnit goodsUnit = null;
					if (fspu != null) {
						brand = fspu.getBrand();
						categorys = fspu.getCategorys();
						goodsUnit = fspu.getGoodsUnit();
					}
					// skuID
					GoodsSku fsku = omd.getGoodsSku();
					wgInfo.setGoodsSku(fsku);
					wgInfo.setGoodsSpu(fspu);
					wgInfo.setWarehouse(warehouse);
					List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
					if (wginfolist != null && wginfolist.size() > 0) {
						omd.setFwarehousegoodsinfo(wginfolist.get(0));

					} else {
						addMessage(redirectAttributes, "修改销售订单失败，库存无该商品");
						return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
					}
					list.add(omd);
				}
				t.setOrderMarketDetailList(list);
				t.setFcountprice(countprice);
				orderMarketService.setOrderOrderMarketProperties(t);
				// 获取订单总价
				BigDecimal fordercountprice = t.getFordercountprice();
				// 判断是否有整单折扣设置整单折扣后价格
				String forderdiscount = t.getForderdiscount();

				if (forderdiscount != null && !forderdiscount.trim().isEmpty()) {

					BigDecimal dis = new BigDecimal(forderdiscount).divide(new BigDecimal(100));
					// 订单折后总价
					t.setFdiscountprice(fordercountprice.multiply(dis));
					
				} else {
					// 无折扣订单折后总价
					t.setFdiscountprice(fordercountprice);
				}

				// 获取是否有客户对象是否有折扣
				CustomerBasic customerBasic = t.getCustomerBasic();
				String fdiscount = "";
				if (customerBasic != null&&StringUtils.isNoneBlank(customerBasic.getId())) {
					CustomerBasic customerBasic2 = customerBasicService.get(customerBasic);
					fdiscount = customerBasic2.getFdiscount();
				}
				// 如果客户有折扣则再折上折
				if (fdiscount != null && !fdiscount.trim().isEmpty()) {
					BigDecimal fdiscountprice = t.getFdiscountprice();
					BigDecimal customerdiscount = new BigDecimal(fdiscount).divide(new BigDecimal(100));
					t.setFdiscountprice(fdiscountprice.multiply(customerdiscount));
				}

			} else {
				t.setFcountprice(new BigDecimal(0));
				t.setFdiscountprice(new BigDecimal(0));
			}
			//计算完折扣抹零
			BigDecimal fcutsmall = t.getFcutsmall();
			if(fcutsmall!=null&&fcutsmall.doubleValue()>0){
				BigDecimal fdiscountprice = t.getFdiscountprice();
				t.setFdiscountprice(fdiscountprice.subtract(fcutsmall));
			}
			// 有整单折扣重新计算欠款
			orderMarketService.setfdebt(t);
			session.removeAttribute(windowsMACAddress+"marketgetwarehouseid");
			
			orderMarketService.save(t);// 保存		
		} else {// 新增表单保存
			
			//设置单号
			StringBuffer buffer=new StringBuffer("XS");
			String str = OddNumbers.getOrderNo();
			buffer.append(str);
			orderMarket.setFordernum(buffer.toString());
			
			//获取订单状态
			Integer fstatus = orderMarket.getFstatus();
			// 获取当前用户
			User user = UserUtils.getUser();
			// 获取当前用户所属商户和门店
			Office company = user.getCompany();
			Office office = user.getOffice();
			orderMarket.setFsponsor(company);
			orderMarket.setFstore(office);
			//设置订单类型
			orderMarket.setFordertype(0);//（0.草稿1.已出售3.撤销）
			orderMarketService.setOrderOrderMarketProperties(orderMarket);
			Warehouse warehouse = orderMarket.getWarehouse();
			// 设置订单金额
			BigDecimal countprice = new BigDecimal("0");
			List<OrderMarketDetail> list = new ArrayList<>();
			List<OrderMarketDetail> orderMarketDetailList = orderMarket.getOrderMarketDetailList();
			if (orderMarketDetailList != null && orderMarketDetailList.size() > 0) {
				for (OrderMarketDetail omd : orderMarketDetailList) {
					orderMarketService.setspu(omd);
					orderMarketService.setOrderMarketDetailProperties(omd);
					countprice = countprice.add(omd.getFdiscountcountmoney());
					// 设置从库存
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					// 设置商品属性
					orderMarketService.setgoods(omd);
					// 商品ID
					GoodsSpu fspu = omd.getGoodsSpu();
					// 单条商品数量
					Integer fgoodsnum = omd.getFgoodsnum();
					Brand brand = null;
					Categorys categorys = null;
					GoodsUnit goodsUnit = null;
					if (fspu != null) {
						brand = fspu.getBrand();
						categorys = fspu.getCategorys();
						goodsUnit = fspu.getGoodsUnit();
					}
					// skuID
					GoodsSku fsku = omd.getGoodsSku();
					wgInfo.setGoodsSku(fsku);
					wgInfo.setGoodsSpu(fspu);
					wgInfo.setWarehouse(warehouse);
					List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
					if (wginfolist != null && wginfolist.size() > 0) {
						WarehouseGoodsInfo warehouseGoodsInfo = wginfolist.get(0);
						Integer finventory = warehouseGoodsInfo.getFinventory();
						// 判断商品数量是否超出库存
						if (fgoodsnum > finventory) {
							addMessage(redirectAttributes,
									"生成销售订单失败，库存不足，商品" + fspu.getFgoodsname() + "当前可用库存：" + finventory);
							return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
						}
						omd.setFwarehousegoodsinfo(warehouseGoodsInfo);
						//如果直接销售则添加流水
						if(fstatus==3){
							orderMarketService.setWarehouseGoodsInfo(fgoodsnum, warehouseGoodsInfo);
							warehouseGoodsInfoService.save(warehouseGoodsInfo);
							orderMarketService.setWarehouseRecord(fspu, fsku, warehouse, orderMarket.getFordernum(),fgoodsnum, warehouseGoodsInfo.getFtotalinventory());
						}
					} else {
						addMessage(redirectAttributes, "生成销售订单失败，库存无该商品");
						return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
					}
					omd.setFstatus(1);
					list.add(omd);
				}
				orderMarket.setOrderMarketDetailList(list);
				orderMarket.setFcountprice(countprice);
				orderMarketService.setOrderOrderMarketProperties(orderMarket);
				// 获取订单总价
				BigDecimal fordercountprice = orderMarket.getFordercountprice();
				// 判断是否有整单折扣设置整单折扣后价格
				String forderdiscount = orderMarket.getForderdiscount();	
				if (forderdiscount != null && !forderdiscount.trim().isEmpty()) {

					BigDecimal dis = new BigDecimal(forderdiscount).divide(new BigDecimal(100));
					// 订单折后总价
					orderMarket.setFdiscountprice(fordercountprice.multiply(dis));
				} else {
					// 无折扣订单折后总价
					orderMarket.setFdiscountprice(fordercountprice);
				}

				// 获取是否有客户对象是否有折扣
				CustomerBasic customerBasic = orderMarket.getCustomerBasic();
				String fdiscount = "";
				if (customerBasic != null&&!customerBasic.getId().trim().isEmpty()) {
					CustomerBasic customerBasic2 = customerBasicService.get(customerBasic);
					fdiscount = customerBasic2.getFdiscount();
				}
				// 如果客户有折扣则再折上折
				if (fdiscount != null && !fdiscount.trim().isEmpty()) {
					BigDecimal fdiscountprice = orderMarket.getFdiscountprice();
					BigDecimal customerdiscount = new BigDecimal(fdiscount).divide(new BigDecimal(100));
					orderMarket.setFdiscountprice(fdiscountprice.multiply(customerdiscount));
				}
			} else {
				orderMarket.setFcountprice(new BigDecimal(0));
				orderMarket.setFdiscountprice(new BigDecimal(0));
			}
			//计算完折扣后抹零
			BigDecimal fcutsmall = orderMarket.getFcutsmall();
			if(fcutsmall!=null&&fcutsmall.doubleValue()>0){
				BigDecimal fdiscountprice = orderMarket.getFdiscountprice();
				orderMarket.setFdiscountprice(fdiscountprice.subtract(fcutsmall));
			}
			// 有整单折扣重新计算欠款
			orderMarketService.setfdebt(orderMarket);
			
			if(fstatus==3){
				orderMarket.setUpdateBy(user);
				orderMarket.setFendtime(new Date());
				orderMarket.setFordertype(1);//（0.草稿1.已出售3.撤销）
				ClearingAccount clearingAccount = orderMarket.getClearingAccount();//结算账户
				if(clearingAccount!=null&&!clearingAccount.getId().trim().isEmpty()){
					
				}else{
					try {
						throw new Exception("获取结算账户时出错");
					} catch (Exception e) {
						e.printStackTrace();
						addMessage(redirectAttributes, "开单失败，系统运转时出错！~");
						return "redirect:" + Global.getAdminPath() + "market/ordermarket/orderMarket/?repage";
					}
				}
				
			}
			
			session.removeAttribute(windowsMACAddress + "marketgetwarehouseid");
			orderMarketService.save(orderMarket);// 保存
		}
		addMessage(redirectAttributes, "保存销售订单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";

	}

	/**
	 * 逻辑删除销售订单
	 */
	@RequiresPermissions("market:ordermarket:orderMarket:del")
	@RequestMapping(value = "delete")
	public String delete(OrderMarket orderMarket, RedirectAttributes redirectAttributes) {
		orderMarketService.deleteByLogic(orderMarket);
		addMessage(redirectAttributes, "删除销售订单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
	}

	/**
	 * 批量逻辑删除销售订单
	 */
	@RequiresPermissions("market:ordermarket:orderMarket:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			orderMarketService.deleteByLogic(orderMarketService.get(id));
		}
		addMessage(redirectAttributes, "删除销售订单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("market:ordermarket:orderMarket:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(OrderMarket orderMarket, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售订单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<OrderMarket> page = orderMarketService.findPage(new Page<OrderMarket>(request, response, -1),
					orderMarket);
			new ExportExcel("销售订单", OrderMarket.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出销售订单记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
	}
	/**
	 * 导出采购退单详细excel文件
	 */
	@RequiresPermissions("market:ordermarket:orderMarket:export")
	@RequestMapping(value = "exportmarketDetail", method = RequestMethod.GET)
	public String exportDetail(OrderMarket orderMarket, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售单详细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			OrderMarketDetail detail = new OrderMarketDetail();
			detail.setOrderMarket(orderMarket);
//			detail.setFstatus(1);
			Page<OrderMarketDetail> page = orderMarketDetailService
					.findPage(new Page<OrderMarketDetail>(request, response, -1), detail);
			new ExportExcel("销售单详细", OrderMarketDetail.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出销售单详细失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
	}
	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("market:ordermarket:orderMarket:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderMarket> list = ei.getDataList(OrderMarket.class);
			for (OrderMarket orderMarket : list) {
				try {
					orderMarketService.save(orderMarket);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条销售订单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条销售订单记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售订单失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
	}

	/**
	 * 下载导入销售订单数据模板
	 */
	@RequiresPermissions("market:ordermarket:orderMarket:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售订单数据导入模板.xlsx";
			List<OrderMarket> list = Lists.newArrayList();
			new ExportExcel("销售订单数据", OrderMarket.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
	}

	/**
	 * 选择客户名称
	 */
	@RequestMapping(value = "selectcustomerBasic")
	public String selectcustomerBasic(CustomerBasic customerBasic, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<CustomerBasic> page = orderMarketService
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
		Page<Warehouse> page = orderMarketService.findPageBywarehouse(new Page<Warehouse>(request, response),
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
		return "modules/sys/market/marketwarehouseselect";
	}

	/**
	 * 获取仓库id
	 */
	@RequestMapping(value = "marketgetwarehouseid")
	public void getwarehouseid(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String marketgetwarehouseid = "";
		marketgetwarehouseid = (String) request.getParameter("marketgetwarehouseid");
		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();

		session.setAttribute(windowsMACAddress + "marketgetwarehouseid", marketgetwarehouseid);
	}

	/**
	 * 选择商品ID
	 */
	@RequestMapping(value = "selectfspu")
	public String selectfspu(GoodsSpu fspu, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<GoodsSpu> page = new Page<>(request, response);
		fspu.setPage(page);

		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();
		HttpSession session = request.getSession();
		String marketgetwarehouseid =  null;
		String attribute = (String) session.getAttribute(windowsMACAddress + "marketgetwarehouseid");
//		String attribute2 = (String)session.getAttribute(windowsMACAddress + "xswidedit");
//		String attribute3 = (String)session.getAttribute(windowsMACAddress + "xswidchange");
		if(attribute!=null){
			marketgetwarehouseid = attribute;
		}
//			else if(attribute2!=null&&attribute==null){
//			marketgetwarehouseid = attribute2;
//		}else{
//			marketgetwarehouseid = attribute2;
//		}
//		if(attribute3!=null&&attribute2==null&&attribute==null){
//			marketgetwarehouseid = attribute3;
//		}
	
		if (marketgetwarehouseid != null && marketgetwarehouseid != "") {
			// 获取库存对象
			WarehouseGoodsInfo warehouseGoodsInfo = new WarehouseGoodsInfo();

			Warehouse warehouse = new Warehouse();
			warehouse.setId(marketgetwarehouseid);
			// 通过仓库id查询库存list
			warehouseGoodsInfo.setWarehouse(warehouse);

			List<WarehouseGoodsInfo> findList = warehouseGoodsInfoService.findList(warehouseGoodsInfo);

			// 设置商品list
			List<GoodsSpu> list = new ArrayList<>();
			if (findList != null && findList.size() > 0) {
				for (WarehouseGoodsInfo wgi : findList) {
					GoodsSpu goodsSpu = wgi.getGoodsSpu();
					GoodsSpu goodsSpu2 = goodsSpuService.get(goodsSpu);
					list.add(goodsSpu2);
				}
			}
			page.setList(list);
		} else {
			page.setList(null);
		}
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
		return "modules/sys/market/marketspuselect";
	}

	/**
	 * 获取到选中的spuid
	 */
	@RequestMapping(value = "marketgetspuid")
	public void getspuid(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String spuid = "";
		spuid = (String) request.getParameter("marketspuid");
		// System.out.println(abc);
		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();

		session.setAttribute(windowsMACAddress + "marketspuid", spuid);
	}

	/**
	 * 选择商品详情ID
	 */
	@RequestMapping(value = "selectfsku")
	public String selectfsku(GoodsSku fsku, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {

		HttpSession session = request.getSession();

		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();
		String spuid = (String) session.getAttribute(windowsMACAddress + "marketspuid");
		String attribute = (String) session.getAttribute(windowsMACAddress + "marketgetwarehouseid");
		WarehouseGoodsInfo wgi=new WarehouseGoodsInfo();
		// 设置查询条件
		if (spuid != null && spuid != "") {
			GoodsSpu spu = new GoodsSpu();
			spu.setId(spuid);
			wgi.setGoodsSpu(spu);
			session.removeAttribute(windowsMACAddress + "marketspuid");
		} else {
			setParmert(fsku, url, fieldLabels, fieldKeys, searchLabel, searchKey, request, response, model, null);
			return "modules/sys/market/marketskuselect";
		}
		if(attribute!=null&&StringUtils.isNoneBlank(attribute)){
			Warehouse wh=new Warehouse();
			wh.setId(attribute);
			wgi.setWarehouse(wh);
//			session.removeAttribute(windowsMACAddress+"marketgetwarehouseid");
		}else{
			setParmert(fsku, url, fieldLabels, fieldKeys, searchLabel, searchKey, request, response, model, null);
			return "modules/sys/market/marketskuselect";
		}
		List<WarehouseGoodsInfo> infoList = warehouseGoodsInfoService.findList(wgi);
		List<GoodsSku> skuList=new ArrayList<>();
		if(infoList!=null&&infoList.size()>0){
			for(WarehouseGoodsInfo info:infoList){
				skuList.add(goodsSkuService.get(info.getGoodsSku()));
			}	
		}
		
		Page<GoodsSku> page = new Page<>();
		fsku.setPage(page);
		page.setList(skuList);

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

		return "modules/sys/market/marketskuselect";
	}

	/**
	 * 选择账目类型
	 */
	@RequestMapping(value = "selectaccountType")
	public String selectaccountType(AccountType accountType, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<AccountType> page = orderMarketService.findPageByaccountType(new Page<AccountType>(request, response),
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

	/**
	 * 选择结算账户
	 */
	@RequestMapping(value = "selectclearingAccount")
	public String selectclearingAccount(ClearingAccount clearingAccount, String url, String fieldLabels,
			String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<ClearingAccount> page = orderMarketService
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
	 * 选择打印模板
	 */
	@RequestMapping(value = "selectsysModelType")
	public String selectsysModelType(SysModelType sysModelType, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SysModelType> page = orderMarketService.findPageBysysModelType(new Page<SysModelType>(request, response),
				sysModelType);
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
		model.addAttribute("obj", sysModelType);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 设置自定义页面所需参数
	 */
	private void setParmert(Object obj, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model,
			Page<Object> page) {

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
		model.addAttribute("obj", obj);
		model.addAttribute("page", page);

	}

	/**
	 * 撤销销售订单
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarket:edit" }, logical = Logical.OR)
	@RequestMapping(value = "revoke")
	public String revoke(OrderMarket orderMarket, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, orderMarket)) {
			return form(orderMarket, model);
		}
		if (!orderMarket.getIsNewRecord()) {// 编辑表单保存
			OrderMarket t = orderMarketService.get(orderMarket.getId());// 从数据库取出记录的值
			if (t.getFordertype() == 0) {
				MyBeanUtils.copyBeanNotNull2Bean(orderMarket, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
				t.setFordertype(2);
				orderMarketService.save(t);// 保存
			} else {
				addMessage(redirectAttributes, "撤销订单失败，该订单处于不个操作状态");
				return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
			}

		}
		addMessage(redirectAttributes, "撤销订单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";

	}
	
	
	
	/**
	 * 根据订单号查询单条数据用于其他页面查看订单信息
	 * 
	 * @param 此处numid的值为订单号
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarket:view" }, logical = Logical.OR)
	@RequestMapping("formbyordernum")
	public String formbyordernum(String numid, Model model) {
		// 回显对象属性
		OrderMarket om = null;
		try {
			om = orderMarketService.findUniqueByProperty("fordernum", numid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (om != null) {
			om = orderMarketService.get(om.getId());
			List<OrderMarketDetail> orderMarketDetailList = om.getOrderMarketDetailList();
			for (OrderMarketDetail omd : orderMarketDetailList) {
				orderMarketService.setOrderMarketDetailProperties(omd);
			}
			model.addAttribute("orderMarket", om);
		}

		return "modules/market/ordermarket/orderMarketView";

	}
	
	
	
	
	/**
	 * 销售换货
	 */
	@RequiresPermissions(value = {
			"market:ordermarket:orderMarket:edit" }, logical = Logical.OR)
	@RequestMapping(value = "goodsexchange")
	public String goodsexchange(OrderMarket orderMarket, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request)
			throws Exception {
		String mac = MacUtils.getMac();
		HttpSession session = request.getSession();
		if (!beanValidator(model, orderMarket)) {
			return form(orderMarket, model);
		}
		if (!orderMarket.getIsNewRecord()) {// 编辑表单保存
			Warehouse newwarehouse = orderMarket.getWarehouse();
			OrderMarket t = orderMarketService.get(orderMarket.getId());// 从数据库取出记录的值
			//换货之前订单价格
			BigDecimal oldprice = t.getFdiscountprice();
			//换货之前实收款
			BigDecimal oldproceeds = t.getFproceeds();
			Warehouse oldwarehouse = t.getWarehouse();
			if(newwarehouse!=null&&oldwarehouse!=null&&StringUtils.isNoneBlank(newwarehouse.getId())&&StringUtils.isNoneBlank(oldwarehouse.getId())){
				if(!newwarehouse.getId().equals(oldwarehouse.getId())){
					addMessage(redirectAttributes, "换货失败,仓库不可更改");
					return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
				}
			}
			//接受金额变量
			BigDecimal countprice = new BigDecimal(0);
			List<OrderMarketDetail> oldList = t.getOrderMarketDetailList();//获取老订单的详情列表
			Iterator<OrderMarketDetail> olditerator = oldList.iterator();
			Integer fstatus = t.getFstatus();
			if(fstatus==3){
			MyBeanUtils.copyBeanNotNull2Bean(orderMarket, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			List<OrderMarketDetail> newList = t.getOrderMarketDetailList();
			Iterator<OrderMarketDetail> newiterator = newList.iterator();
			while (newiterator.hasNext()) {
				OrderMarketDetail newomd = newiterator.next();
				//获取新对象的id和商品数量
				String newid = newomd.getId();
				Integer newnum = newomd.getFgoodsnum();
				BigDecimal newnum2=new BigDecimal(newnum);
				Integer omdfstatus = newomd.getFstatus();
				//获取新商品的库存
				WarehouseGoodsInfo newinfo = new WarehouseGoodsInfo();
				newinfo.setWarehouse(newwarehouse);
				//获取商品唯一标记sku
				GoodsSku goodsSku = newomd.getGoodsSku();
				if(goodsSku!=null&&StringUtils.isNoneBlank(goodsSku.getId())){
					newinfo.setGoodsSku(goodsSku);
				}else{
					addMessage(redirectAttributes, "换货失败，商品货号有误！");
					return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
				}
				try {
					//以仓库和sku为查询条件获取库存对象
					List<WarehouseGoodsInfo> findList = warehouseGoodsInfoService.findList(newinfo);
					if(findList!=null&&findList.size()>0){
						newinfo =findList.get(0);
					} 
					if(newinfo!=null&&StringUtils.isNoneBlank(newinfo.getId())){
						newomd.setFwarehousegoodsinfo(newinfo);
					}else{
						addMessage(redirectAttributes, "获取库存失败！");
						return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				while(olditerator.hasNext()){
					OrderMarketDetail oldomd = olditerator.next();
					//获取库存对象
					WarehouseGoodsInfo oldinfo = oldomd.getFwarehousegoodsinfo();
					if(oldinfo!=null&&StringUtils.isNoneBlank(oldinfo.getId())){
						oldinfo=warehouseGoodsInfoService.get(oldinfo);	
					}else{
						addMessage(redirectAttributes, "获取库存失败！");
						return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
					}
					//获取老对象的id和商品数量
					String oldid = oldomd.getId();
					Integer oldnum = oldomd.getFgoodsnum();
					BigDecimal oldnum2=new BigDecimal(oldnum);
					if(newid.equals(oldid)){
						BigDecimal subtract = newnum2.subtract(oldnum2);
						if(subtract.intValue()!=0){
							orderMarketService.setWarehouseGoodsInfo(subtract.intValue(), oldinfo);
							warehouseGoodsInfoService.save(oldinfo);
							//添加库存流水
							orderMarketService.setWarehouseRecord(oldomd.getGoodsSpu(), oldomd.getGoodsSku(), newwarehouse, t.getFordernum(), subtract.intValue(), oldinfo.getFtotalinventory());
						}	
					}else{
						orderMarketService.setWarehouseGoodsInfo(newnum, newinfo);
						warehouseGoodsInfoService.save(newinfo);
						//添加库存流水
						orderMarketService.setWarehouseRecord(newomd.getGoodsSpu(), goodsSku, newwarehouse, t.getFordernum(), newomd.getFgoodsnum(), newinfo.getFtotalinventory());
					}
					
				}
				if(omdfstatus==2){
					newomd.setDelFlag(newomd.DEL_FLAG_DELETE);
				}else {
					orderMarketService.setOrderMarketDetailProperties(newomd);
					countprice = countprice.add(newomd.getFdiscountcountmoney());
				}
			}
			
			
			t.setFcountprice(countprice);
			orderMarketService.setOrderOrderMarketProperties(t);
			if(countprice.doubleValue()!=0){
				// 获取订单总价
				BigDecimal fordercountprice = t.getFordercountprice();
				// 判断是否有整单折扣设置整单折扣后价格
				String forderdiscount = t.getForderdiscount();

				if (forderdiscount != null && !forderdiscount.trim().isEmpty()) {

					BigDecimal dis = new BigDecimal(forderdiscount).divide(new BigDecimal(100));
					// 订单折后总价
					t.setFdiscountprice(fordercountprice.multiply(dis));
					
				} else {
					// 无折扣订单折后总价
					t.setFdiscountprice(fordercountprice);
				}

				// 获取是否有客户对象是否有折扣
				CustomerBasic customerBasic = t.getCustomerBasic();
				String fdiscount = "";
				if (customerBasic != null&&StringUtils.isNoneBlank(customerBasic.getId())) {
					CustomerBasic customerBasic2 = customerBasicService.get(customerBasic);
					fdiscount = customerBasic2.getFdiscount();
				}
				// 如果客户有折扣则再折上折
				if (fdiscount != null && !fdiscount.trim().isEmpty()) {
					BigDecimal fdiscountprice = t.getFdiscountprice();
					BigDecimal customerdiscount = new BigDecimal(fdiscount).divide(new BigDecimal(100));
					t.setFdiscountprice(fdiscountprice.multiply(customerdiscount));
				}
			}else{
				t.setFdiscountprice(countprice);
				t.setFordercountprice(countprice);
			}
			
			//计算完折扣抹零
			BigDecimal fcutsmall = t.getFcutsmall();
			if(fcutsmall!=null&&fcutsmall.doubleValue()>0){
				BigDecimal fdiscountprice = t.getFdiscountprice();
				t.setFdiscountprice(fdiscountprice.subtract(fcutsmall));
			}
			
			t.setFproceeds(new BigDecimal(0));
			// 有整单折扣重新计算欠款
//			orderMarketService.setfdebt(t);
			t.setFdebt(new BigDecimal(0));
			//原订单价钱减去换货后订单价钱得到差价并设置为订单价
			BigDecimal fdiscountprice = t.getFdiscountprice();
			BigDecimal subtract = oldprice.subtract(fdiscountprice);
			t.setFdiscountprice(subtract);
			
			
//			System.err.println("1111");
			
			}else{
				addMessage(redirectAttributes, "换货失败,该订单为未销售状态");
				return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
			}
			
//			t.setFendtime(new Date());
			t.setFstatus(1);
			t.setFordertype(0);
			session.removeAttribute(mac+"marketgetwarehouseid");	
			orderMarketService.save(t);// 保存
		} else {// 新增表单保存
			addMessage(redirectAttributes, "修改销售单失败");
			return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
		}
		addMessage(redirectAttributes, "修改销售单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarket/?repage";
	}
	
}