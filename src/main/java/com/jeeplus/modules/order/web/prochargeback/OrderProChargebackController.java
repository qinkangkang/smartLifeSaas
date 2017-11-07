/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.web.prochargeback;

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

import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.supplier.service.supplierbasic.SupplierBasicService;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.service.WarehouseGoodsInfoService;
import com.jeeplus.modules.warehouses.service.WarehouseRecordService;
import com.jeeplus.modules.warehouses.service.WarehouseService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.supplieraccount.SupplierAccount;
import com.jeeplus.modules.account.service.capitalaccount.CapitalAccountService;
import com.jeeplus.modules.account.service.clearingaccount.ClearingAccountService;
import com.jeeplus.modules.account.service.supplieraccount.SupplierAccountService;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.service.sku.GoodsSkuService;
import com.jeeplus.modules.goods.service.spu.GoodsSpuService;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.sys.entity.modeltype.SysModelType;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.MacUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChargeback;
import com.jeeplus.modules.order.service.prochargeback.OrderProChargebackService;
import com.jeeplus.modules.order.service.procurement.OrderProDetailService;
import com.jeeplus.modules.order.service.procurement.OrderProcurementService;

/**
 * 采购退单Controller
 * 
 * @author diqiang
 * @version 2017-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/order/prochargeback/orderProChargeback")
public class OrderProChargebackController extends BaseController {

	@Autowired
	private OrderProChargebackService orderProChargebackService;
	// 仓库商品表service
	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;
	
	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private SupplierBasicService supplierBasicService;
	
	@Autowired
	private WarehouseRecordService warehouseRecordService;// 仓库service

	@Autowired
	private ClearingAccountService clearingAccountService;// 结算账户service

	@Autowired
	private CapitalAccountService capitalAccountService;// 资金流水service

	@Autowired
	private SupplierAccountService supplierAccountService;// 供应商对账/付款service
	
	@Autowired
	private GoodsSpuService goodsSpuService;
	
	@Autowired
	private GoodsSkuService goodsSkuService;
	
	@ModelAttribute
	public OrderProChargeback get(@RequestParam(required = false) String id) {
		OrderProChargeback entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = orderProChargebackService.get(id);
		}
		if (entity == null) {
			entity = new OrderProChargeback();
		}
		return entity;
	}

	/**
	 * 采购退单列表页面
	 */
	@RequiresPermissions("order:prochargeback:orderProChargeback:list")
	@RequestMapping(value = { "list", "" })
	public String list(OrderProChargeback orderProChargeback, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<OrderProChargeback> page = orderProChargebackService
				.findPage(new Page<OrderProChargeback>(request, response), orderProChargeback);

		model.addAttribute("page", page);
		return "modules/order/prochargeback/orderProChargebackList";
	}

	/**
	 * 查看，增加，编辑采购退单表单页面
	 */
	@RequiresPermissions(value = { "order:prochargeback:orderProChargeback:view",
			"order:prochargeback:orderProChargeback:add",
			"order:prochargeback:orderProChargeback:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderProChargeback orderProChargeback, Model model,HttpServletRequest request) {
		String ocid = orderProChargeback.getId();
		if(ocid!=null&&!ocid.trim().isEmpty()){
			String mac = MacUtils.getMac();
			HttpSession session = request.getSession();
			SupplierBasic fsupplier = orderProChargeback.getFsupplier();
			if(fsupplier!=null&&!fsupplier.getId().trim().isEmpty()){
				session.setAttribute(mac+"ctsid", fsupplier.getId());
			}
			Warehouse fwarehose = orderProChargeback.getFwarehose();
			if(fwarehose!=null&&!fwarehose.getId().trim().isEmpty()){
				session.setAttribute(mac+"ctwid", fwarehose.getId());
			}
		}
		
		List<OrderProChaDetail> orderProChaDetailList = orderProChargeback.getOrderProChaDetailList();
		for (OrderProChaDetail opcd : orderProChaDetailList) {
			orderProChargebackService.setOrderProDetailProperties(opcd);
		}

		model.addAttribute("orderProChargeback", orderProChargeback);
		return "modules/order/prochargeback/orderProChargebackForm";
	}

	/**
	 * 保存采购退单
	 */
	@RequiresPermissions(value = { "order:prochargeback:orderProChargeback:add",
			"order:prochargeback:orderProChargeback:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(OrderProChargeback orderProChargeback, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request)
			throws Exception {
		if (!beanValidator(model, orderProChargeback)) {
			return form(orderProChargeback, model,request);
		}
		if (!orderProChargeback.getIsNewRecord()) {// 编辑表单保存
			OrderProChargeback t = orderProChargebackService.get(orderProChargeback.getId());// 从数据库取出记录的值
			SupplierBasic fsupplier = t.getFsupplier();
			Warehouse fwarehose2 = t.getFwarehose();
			Warehouse fwarehose3 = orderProChargeback.getFwarehose();
			SupplierBasic fsupplier2 = orderProChargeback.getFsupplier();
			String mac = MacUtils.getMac();	
			if(fsupplier!=null&&!fsupplier.getId().trim().isEmpty()&&fsupplier2!=null&&!fsupplier2.getId().trim().isEmpty()){
				if(!fsupplier.getId().equals(fsupplier2.getId())){
					addMessage(redirectAttributes, "修改退单失败，供应商不可修改！！");
					return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
				}
			}
			if(fwarehose2!=null&&!fwarehose2.getId().trim().isEmpty()&&fwarehose3!=null&&!fwarehose3.getId().trim().isEmpty()){
				if(!fwarehose2.getId().equals(fwarehose3.getId())){
					addMessage(redirectAttributes, "修改退单失败，仓库不可修改！！");
					return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
				}
			}
			
			if (t.getFordertype() == 1) {
				MyBeanUtils.copyBeanNotNull2Bean(orderProChargeback, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
				User fexecutor = orderProChargeback.getFexecutor();
				if (fexecutor != null) {
					t.setFseniorarchirist(UserUtils.getUser());
				}
				// 如果采购单已执行完毕就更改仓库记录
				if (t.getFstatus() == 6) {
					t.setFordertype(2);
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					Warehouse fwarehose = t.getFwarehose();
					if (fwarehose != null) {
						wgInfo.setWarehouse(fwarehose);
					}
					List<OrderProChaDetail> list = new ArrayList<OrderProChaDetail>();
					List<OrderProChaDetail> orderProChaDetailList = t.getOrderProChaDetailList();
					for (OrderProChaDetail opd : orderProChaDetailList) {
						// 设置商品属性
						orderProChargebackService.setgoods(opd);
						// 单条商品数量
						Integer fgoodsnum = opd.getFgoodsnum();
						// 商品ID
						GoodsSpu fspu = opd.getFspu();
						Brand brand = null;
						Categorys categorys = null;
						GoodsUnit goodsUnit = null;
						if (fspu != null) {
							brand = fspu.getBrand();
							categorys = fspu.getCategorys();
							goodsUnit = fspu.getGoodsUnit();
						}
						// skuID
						GoodsSku fsku = opd.getFsku();
						wgInfo.setGoodsSpu(fspu);
						wgInfo.setGoodsSku(fsku);
						List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
						if (wginfolist == null || wginfolist.size() == 0) {
							addMessage(redirectAttributes, "生成退单失败，仓库无该商品记录！！");
							return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
						} else {
							WarehouseGoodsInfo warehouseGoodsInfo = wginfolist.get(0);
							Integer finventory = warehouseGoodsInfo.getFinventory();
							Integer ftotalinventory = warehouseGoodsInfo.getFtotalinventory();
							BigDecimal add = new BigDecimal(finventory.toString())
									.subtract(new BigDecimal(fgoodsnum.toString()));
							BigDecimal add2 = new BigDecimal(ftotalinventory.toString())
									.subtract(new BigDecimal(fgoodsnum.toString()));
							warehouseGoodsInfo.setFinventory(add.intValue());
							warehouseGoodsInfo.setFtotalinventory(add2.intValue());
							warehouseGoodsInfoService.save(warehouseGoodsInfo);

							// 添加流水
							WarehouseRecord warehouseRecord = new WarehouseRecord();
							warehouseRecord.setBusinessType(warehouseRecord.BUSINESS_TYPE_PROCUREMENTCHA);
							warehouseRecord.setGoodsSku(fsku);
							warehouseRecord.setGoodsSpu(fspu);
							warehouseRecord.setWarehouse(t.getFwarehose());
							warehouseRecord.setBusinessTime(new Date());
							warehouseRecord.setBusinessorderNumber(t.getFordernum());
							warehouseRecord.setChangeNum(fgoodsnum);
							warehouseRecord.setRemainingNum(warehouseGoodsInfo.getFtotalinventory());
							warehouseRecordService.save(warehouseRecord);
						}
						list.add(opd);
					}
					t.setOrderProChaDetailList(list);
				}
				// 如果订单执行完毕设置结束时间
				if (t.getFstatus() == 6) {
					t.setFendtime(new Date());
				}

				// 设置订单金额
				BigDecimal countprice = new BigDecimal("0");
				List<OrderProChaDetail> orderProChaDetailList = t.getOrderProChaDetailList();
				if (orderProChaDetailList != null && orderProChaDetailList.size() > 0) {
					for (OrderProChaDetail opd : orderProChaDetailList) {
						orderProChargebackService.setOrderProDetailProperties(opd);
						countprice = countprice.add(opd.getFdiscountcountprice());

					}
					t.setFcountprice(countprice);
					// 重新计算价钱
					orderProChargebackService.setOrderProcurementProperties(t);
					// 获取订单总价
					BigDecimal fordercountprice = t.getFordercountprice();
					//获取抹零
					BigDecimal fcutsmall = t.getFcutsmall();
					fcutsmall = (fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall:new BigDecimal(0);
					// 判断是否有整单折扣设置整单折扣后价格
					String forderdiscount = orderProChargeback.getForderdiscount();

					if (forderdiscount != null && !forderdiscount.trim().isEmpty()) {

						BigDecimal dis = new BigDecimal(forderdiscount).divide(new BigDecimal(100));
						// 订单折后总价
						t.setFdiscountprice((fordercountprice.multiply(dis)).subtract(fcutsmall));
						// 重新金额
						orderProChargebackService.setfdebt(t);
					} else {
						// 无折扣订单折后总价
						t.setFdiscountprice(fordercountprice.subtract(fcutsmall));
					}
				} else {
					t.setFcountprice(new BigDecimal(0));
				}

				// 订单执行完毕后添加资金流水和供应商对账/付款对象信息
				if (t.getFstatus() == 6) {
					Date businessTime = new Date();
					Office fsponsor = t.getFsponsor();// 所属商户
					Office fstore = t.getFstore();// 所属门店
					// 获取订单实付款
					BigDecimal factualpayment = new BigDecimal(0);
					BigDecimal factualpayment2 = t.getFactualpayment();// 实收款
					factualpayment2 = (factualpayment2!=null&&factualpayment2.doubleValue()>0)?factualpayment2:new BigDecimal(0);
					if (factualpayment2 != null && factualpayment2.doubleValue() > 0) {
						factualpayment = factualpayment2;
					}
					// 订单执行完毕添加资金流水
					CapitalAccount capitalAccount = new CapitalAccount();
					// 获取结算账户
					ClearingAccount fdclearingaccountid = clearingAccountService.get(t.getFdclearingaccount());

					String fbalance = "";
					BigDecimal faccountBalance = new BigDecimal(0);
					if (fdclearingaccountid != null) {
						fbalance = fdclearingaccountid.getFbalance();// 获取结算账户余额
						fbalance=(fbalance!=null&&StringUtils.isNoneBlank(fbalance))?fbalance:"0";
						faccountBalance = new BigDecimal(fbalance).add(factualpayment);// 账户余额加上实收款
						// 修改结算账户余额
						if (faccountBalance != null && faccountBalance.doubleValue() > 0) {
							fdclearingaccountid.setFbalance(faccountBalance.toString());
							// 保存修改后的结算账户对象
							clearingAccountService.save(fdclearingaccountid);
						}
					}else{
						try {
							throw new Exception("获取结算账户时出错");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							addMessage(redirectAttributes, "修改采购退单失败，系统运转时出错");
							return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
						}
					}
					capitalAccount.setFoddNumbers(t.getFordernum());// 单据号
					capitalAccount.setAccountType(t.getFdaccounttype());// 账目类型
					capitalAccount.setClearingAccount(fdclearingaccountid);// 结算账户
					capitalAccount.setFinitialamount(fbalance);// 期初金额
					capitalAccount.setFaccountBalance(faccountBalance.toString());// 账户余额
					capitalAccount.setFtrader(t.getFexecutor());// 交易人
					capitalAccount.setFincome(factualpayment.toString());// 收入
					
					BigDecimal fothermoney = t.getFothermoney();//获取其他费用
					String fothermoneystr = (fothermoney==null)?"0":fothermoney.toString();
					capitalAccount.setFexpenditure(fothermoneystr);// 支出
					// 计算盈利。
					String profit="0";
					try {
						profit = factualpayment.subtract(new BigDecimal(fothermoneystr)).toString();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					capitalAccount.setFprofit(profit);// 盈利
					capitalAccount.setFexpenditureflag(1); // 收入/支出（0支出，1收入）
					capitalAccount.setFsponsor(fsponsor);// 商户
					capitalAccount.setFstore(fstore);// 门店
					capitalAccount.setFbusinessHours(businessTime);// 业务时间
					capitalAccount.setRemarks(t.getRemarks());
					// 保存资金流水对象
					capitalAccountService.save(capitalAccount);

					// 添加供应商对账/付款信息
					SupplierAccount supplierAccount = new SupplierAccount();
					supplierAccount.setFsponsor(fsponsor);// 所属商户
					supplierAccount.setFstore(fstore);// 所属门店
					supplierAccount.setSupplierBasic(t.getFsupplier());// 供应商
					supplierAccount.setAccountType(t.getFdaccounttype());// 账目类型
					supplierAccount.setClearingAccount(fdclearingaccountid);// 结算账户
					supplierAccount.setFoddNumbers(t.getFordernum());// 单据编号
					supplierAccount.setFbusinessHours(businessTime);// 业务时间				
					BigDecimal fdiscountprice = t.getFdiscountprice();//获取订单折后价
					fdiscountprice = (fdiscountprice!=null&&fdiscountprice.doubleValue()>0)?fdiscountprice:new BigDecimal(0);
					BigDecimal fcutsmall = t.getFcutsmall();//获取抹零
					BigDecimal amountPay=fdiscountprice.add(fcutsmall);//因为订单最后价钱已经减去了抹零所以先加回来
					fcutsmall=(fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall:new BigDecimal(0);
					supplierAccount.setFamountPay(new BigDecimal(0).subtract(amountPay).toString());// 设置应付金额
					supplierAccount.setFpayAmount(new BigDecimal(0).subtract(factualpayment2).toString());// 设置实付金额
					supplierAccount.setFpreferentialAmount(new BigDecimal(0).subtract(fcutsmall).toString());// 设置优惠金额
					BigDecimal solehandlingAmount = amountPay.subtract(factualpayment2).subtract(fcutsmall);// 本单应付金额
					supplierAccount.setFsolehandlingAmount(new BigDecimal(0).subtract(solehandlingAmount).toString());// 本单应付金额
					supplierAccount.setRemarks(t.getRemarks());// 备注
					// 保存供应商对账/付款信息
					supplierAccountService.save(supplierAccount);
				}
				HttpSession session = request.getSession();
				session.removeAttribute(mac+"ctsid");
				session.removeAttribute(mac+"ctwid");
				orderProChargebackService.save(t);// 保存退单
			} else if (t.getFordertype() == 2) {
				addMessage(redirectAttributes, "修改采购退单失败，已采购的订单不可修改");
				return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
			} else {
				addMessage(redirectAttributes, "修改采购退单失败，已撤销的订单不可修改");
				return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
			}
		} else {// 新增表单保存
				// 设置单号
			StringBuffer buffer = new StringBuffer("CT");
			String str = OddNumbers.getOrderNo();
			buffer.append(str);
			orderProChargeback.setFordernum(buffer.toString());

			// 计算各种金额
			orderProChargebackService.setOrderProcurementProperties(orderProChargeback);

			BigDecimal countprice = new BigDecimal("0");
			List<OrderProChaDetail> orderProChaDetailList = orderProChargeback.getOrderProChaDetailList();
			if (orderProChaDetailList != null && orderProChaDetailList.size() > 0) {
				for (OrderProChaDetail opd : orderProChaDetailList) {
					orderProChargebackService.setOrderProDetailProperties(opd);
					countprice = countprice.add(opd.getFdiscountcountprice());
				}
				orderProChargeback.setFcountprice(countprice);
				// 获取订单总价
				BigDecimal fordercountprice = orderProChargeback.getFordercountprice();
				//获取抹零
				BigDecimal fcutsmall = orderProChargeback.getFcutsmall();
				fcutsmall = (fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall:new BigDecimal(0);
				// 判断是否有整单折扣设置整单折扣后价格
				String forderdiscount = orderProChargeback.getForderdiscount();
				if (forderdiscount != null && !forderdiscount.trim().isEmpty()) {
					BigDecimal dis = new BigDecimal(forderdiscount).divide(new BigDecimal(100));
					// 订单折后总价
					orderProChargeback.setFdiscountprice((fordercountprice.multiply(dis)).subtract(fcutsmall));
					// 重新计算欠款
					orderProChargebackService.setfdebt(orderProChargeback);
				} else {
					// 无折扣订单折后总价
					orderProChargeback.setFdiscountprice(fordercountprice.subtract(fcutsmall));
				}
			} else {
				orderProChargeback.setFcountprice(new BigDecimal(0));
			}
			// 设置订单状态
			orderProChargeback.setFstatus(1);

			orderProChargebackService.save(orderProChargeback);// 保存
		}
		addMessage(redirectAttributes, "保存采购退单成功");
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}

	/**
	 * 逻辑删除采购退单
	 */
	@RequiresPermissions("order:prochargeback:orderProChargeback:del")
	@RequestMapping(value = "delete")
	public String delete(OrderProChargeback orderProChargeback, RedirectAttributes redirectAttributes) {
		orderProChargebackService.deleteByLogic(orderProChargeback);
		addMessage(redirectAttributes, "删除采购退单成功");
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}

	/**
	 * 批量逻辑删除采购退单
	 */
	@RequiresPermissions("order:prochargeback:orderProChargeback:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			orderProChargebackService.deleteByLogic(orderProChargebackService.get(id));
		}
		addMessage(redirectAttributes, "删除采购退单成功");
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("order:prochargeback:orderProChargeback:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(OrderProChargeback orderProChargeback, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "采购退单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<OrderProChargeback> page = orderProChargebackService
					.findPage(new Page<OrderProChargeback>(request, response, -1), orderProChargeback);
			new ExportExcel("采购退单", OrderProChargeback.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出采购退单记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}
	
	/**
	 * 导出采购退单详细excel文件
	 */
	@RequiresPermissions("order:prochargeback:orderProChargeback:export")
	@RequestMapping(value = "exportproChaDetail", method = RequestMethod.GET)
	public String exportDetail(OrderProChargeback orderProChargeback, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "采购退单详细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			OrderProChaDetail detail = new OrderProChaDetail();
			detail.setFprocurementchargeback(orderProChargeback);
//			detail.setFstatus(1);
			Page<OrderProChaDetail> page = orderProChargebackService
					.findPageDetail(new Page<OrderProChaDetail>(request, response, -1), detail);
			new ExportExcel("采购退单详细", OrderProChaDetail.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出采购退单详细失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}
	
	
	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("order:prochargeback:orderProChargeback:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderProChargeback> list = ei.getDataList(OrderProChargeback.class);
			for (OrderProChargeback orderProChargeback : list) {
				try {
					orderProChargebackService.save(orderProChargeback);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条采购退单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条采购退单记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购退单失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}

	/**
	 * 下载导入采购退单数据模板
	 */
	@RequiresPermissions("order:prochargeback:orderProChargeback:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "采购退单数据导入模板.xlsx";
			List<OrderProChargeback> list = Lists.newArrayList();
			new ExportExcel("采购退单数据", OrderProChargeback.class, 1).setDataList(list).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}

	/**
	 * 选择供应商id
	 */
	@RequestMapping(value = "selectfsupplier")
	public String selectfsupplier(SupplierBasic fsupplier, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SupplierBasic> page = orderProChargebackService
				.findPageByfsupplier(new Page<SupplierBasic>(request, response), fsupplier);
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
		model.addAttribute("obj", fsupplier);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择仓库ID
	 */
	@RequestMapping(value = "selectfwarehose")
	public String selectfwarehose(Warehouse fwarehose, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Warehouse> page = orderProChargebackService.findPageByfwarehose(new Page<Warehouse>(request, response),
				fwarehose);
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
		model.addAttribute("obj", fwarehose);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	// /**
	// * 选择审批人（角色ID）
	// */
	// @RequestMapping(value = "selectfseniorarchirist")
	// public String selectfseniorarchirist(User fseniorarchirist, String url,
	// String fieldLabels, String fieldKeys, String searchLabel, String
	// searchKey, HttpServletRequest request, HttpServletResponse response,
	// Model model) {
	// Page<User> page =
	// orderProChargebackService.findPageByfseniorarchirist(new
	// Page<User>(request, response), fseniorarchirist);
	// try {
	// fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
	// fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
	// searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
	// searchKey = URLDecoder.decode(searchKey, "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// model.addAttribute("labelNames", fieldLabels.split("\\|"));
	// model.addAttribute("labelValues", fieldKeys.split("\\|"));
	// model.addAttribute("fieldLabels", fieldLabels);
	// model.addAttribute("fieldKeys", fieldKeys);
	// model.addAttribute("url", url);
	// model.addAttribute("searchLabel", searchLabel);
	// model.addAttribute("searchKey", searchKey);
	// model.addAttribute("obj", fseniorarchirist);
	// model.addAttribute("page", page);
	// return "modules/sys/gridselect";
	// }
	// /**
	// * 选择执行人（角色ID）
	// */
	// @RequestMapping(value = "selectfexecutor")
	// public String selectfexecutor(User fexecutor, String url, String
	// fieldLabels, String fieldKeys, String searchLabel, String searchKey,
	// HttpServletRequest request, HttpServletResponse response, Model model) {
	// Page<User> page = orderProChargebackService.findPageByfexecutor(new
	// Page<User>(request, response), fexecutor);
	// try {
	// fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
	// fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
	// searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
	// searchKey = URLDecoder.decode(searchKey, "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// model.addAttribute("labelNames", fieldLabels.split("\\|"));
	// model.addAttribute("labelValues", fieldKeys.split("\\|"));
	// model.addAttribute("fieldLabels", fieldLabels);
	// model.addAttribute("fieldKeys", fieldKeys);
	// model.addAttribute("url", url);
	// model.addAttribute("searchLabel", searchLabel);
	// model.addAttribute("searchKey", searchKey);
	// model.addAttribute("obj", fexecutor);
	// model.addAttribute("page", page);
	// return "modules/sys/gridselect";
	// }
	/**
	 * 选择结算账户ID
	 */
	@RequestMapping(value = "selectfdclearingaccount")
	public String selectfdclearingaccountid(ClearingAccount fdclearingaccount, String url, String fieldLabels,
			String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<ClearingAccount> page = orderProChargebackService
				.findPageByfdclearingaccountid(new Page<ClearingAccount>(request, response), fdclearingaccount);
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
		model.addAttribute("obj", fdclearingaccount);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择其他费用类型（ 1：采购支出，2：采购退货，3：销售收入，4：销售退货，5：零售）
	 */
	@RequestMapping(value = "selectfdaccounttype")
	public String selectfothermoneytype(AccountType fothermoneytype, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<AccountType> page = orderProChargebackService
				.findPageByfothermoneytype(new Page<AccountType>(request, response), fothermoneytype);
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
		model.addAttribute("obj", fothermoneytype);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择采购单ID
	 */
	@RequestMapping(value = "selectforderprocurement")
	public String selectforderprocurement(OrderProcurement forderprocurement, String url, String fieldLabels,
			String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<OrderProcurement> page = orderProChargebackService
				.findPageByforderprocurement(new Page<OrderProcurement>(request, response), forderprocurement);
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
		model.addAttribute("obj", forderprocurement);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	// /**
	// * 选择门店ID
	// */
	// @RequestMapping(value = "selectfstore")
	// public String selectfsponsor(Office fstore, String url, String
	// fieldLabels, String fieldKeys, String searchLabel, String searchKey,
	// HttpServletRequest request, HttpServletResponse response, Model model) {
	// Page<Office> page = orderProChargebackService.findPageByfstore(new
	// Page<Office>(request, response), fstore);
	// try {
	// fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
	// fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
	// searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
	// searchKey = URLDecoder.decode(searchKey, "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// model.addAttribute("labelNames", fieldLabels.split("\\|"));
	// model.addAttribute("labelValues", fieldKeys.split("\\|"));
	// model.addAttribute("fieldLabels", fieldLabels);
	// model.addAttribute("fieldKeys", fieldKeys);
	// model.addAttribute("url", url);
	// model.addAttribute("searchLabel", searchLabel);
	// model.addAttribute("searchKey", searchKey);
	// model.addAttribute("obj", fstore);
	// model.addAttribute("page", page);
	// return "modules/sys/gridselect";
	// }
	/**
	 * 选择打印模板ID
	 */
	@RequestMapping(value = "selectfmodeltype")
	public String selectfmodeltype(SysModelType fmodeltype, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SysModelType> page = orderProChargebackService
				.findPageByfmodeltypeid(new Page<SysModelType>(request, response), fmodeltype);
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
		
		String mac = MacUtils.getMac();
		HttpSession session = request.getSession();
		String ctsid=(String) session.getAttribute(mac+"ctsid");
		String ctwid=(String) session.getAttribute(mac+"ctwid");
		
		WarehouseGoodsInfo wgi = new WarehouseGoodsInfo();
		if(ctsid!=null&&!ctsid.trim().isEmpty()){
			wgi.setSupplierBasic(supplierBasicService.get(ctsid));
			
		}else{
			SupplierBasic sbs=new SupplierBasic();
			wgi.setSupplierBasic(sbs);
		}
		if(ctwid!=null&&!ctwid.trim().isEmpty()){
			wgi.setWarehouse(warehouseService.get(ctwid));

		}else{
			Warehouse wh=new Warehouse();
			wgi.setWarehouse(wh);
		}
		
		
		
		List<WarehouseGoodsInfo> findList = warehouseGoodsInfoService.findList(wgi);
		List<GoodsSpu> list=new ArrayList<>();
		for(WarehouseGoodsInfo info:findList){
			list.add(info.getGoodsSpu());
		}
		Page<GoodsSpu> page = new Page<>();
		fspu.setPage(page);
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
		model.addAttribute("obj", fspu);
		model.addAttribute("page", page);
		return "modules/sys/order/ctspuselect";
	}
	
	/**
	 * 获取到选中的spuid
	 */
	@RequestMapping(value = "ctspuid")
	public void getspuid(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String ctspuid = "";
		ctspuid = (String) request.getParameter("ctspuid");
		// System.out.println(abc);
		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();

		session.setAttribute(windowsMACAddress + "ctspuid", ctspuid);
	}
	
	/**
	 * 选择商品详情ID
	 */
	@RequestMapping(value = "selectfsku")
	public String selectfsku(GoodsSku fsku, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		
		String mac = MacUtils.getMac();
		GoodsSpu goodsSpu =null;
		String ctspuid=(String) session.getAttribute(mac+"ctspuid");
		if(ctspuid!=null&&!ctspuid.trim().isEmpty()){
			goodsSpu = goodsSpuService.get(ctspuid);
		}
		Page<GoodsSku> page =null;
		if(goodsSpu!=null&&!goodsSpu.getId().trim().isEmpty()){
			fsku.setGoodsSpu(goodsSpu);
			page = goodsSkuService.findPage(new Page<GoodsSku>(request, response), fsku);
			session.removeAttribute(mac+"ctspuid");
		}else{
			page = new Page<>();
			fsku.setPage(page);
			page.setList(new ArrayList<GoodsSku>());
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
		model.addAttribute("obj", fsku);
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
		Page<SysModelType> page = orderProChargebackService
				.findPageBysysModelType(new Page<SysModelType>(request, response), sysModelType);
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
	 * 撤销订单
	 * 
	 * @param orderProChargeback
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value = { "order:prochargeback:orderProChargeback:edit" }, logical = Logical.OR)
	@RequestMapping(value = "repeal")
	public String repeal(OrderProChargeback orderProChargeback, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request)
			throws Exception {
		if (!beanValidator(model, orderProChargeback)) {
			return form(orderProChargeback, model,request);
		}
		if (!orderProChargeback.getIsNewRecord()) {// 编辑表单保存
			OrderProChargeback t = orderProChargebackService.get(orderProChargeback.getId());// 从数据库取出记录的值
			if (t.getFordertype() == 1) {
				MyBeanUtils.copyBeanNotNull2Bean(orderProChargeback, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
				t.setFordertype(3);
				orderProChargebackService.save(t);// 保存
			} else {
				addMessage(redirectAttributes, "采购退单撤销失败，该订单属于不可操作状态");
				return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
			}

		}

		addMessage(redirectAttributes, "采购退单撤销成功");
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";

	}
	
	
	
	@RequiresPermissions(value = { "order:prochargeback:orderProChargeback:view" }, logical = Logical.OR)
	@RequestMapping("formbyordernum")
	public String formbyordernum(String numid,Model model){
		
		OrderProChargeback opc = null;
		try {
			opc = orderProChargebackService.findUniqueByProperty("fordernum", numid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(opc != null){
			OrderProChargeback orderProChargeback = orderProChargebackService.get(opc.getId());
			
			List<OrderProChaDetail> orderProChaDetailList = orderProChargeback.getOrderProChaDetailList();
			if(orderProChaDetailList!=null&&orderProChaDetailList.size()>0){
				for(OrderProChaDetail opcd:orderProChaDetailList){
					orderProChargebackService.setOrderProDetailProperties(opcd);
				}
			}
			
			model.addAttribute("orderProChargeback", orderProChargeback);
		}
		return "modules/order/prochargeback/orderProChargebackForm";
	}
	
	
	
	

}