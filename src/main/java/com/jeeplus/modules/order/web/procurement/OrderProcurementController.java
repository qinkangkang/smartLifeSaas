/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.web.procurement;

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
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.account.entity.clearingaccount.ClearingAccount;
import com.jeeplus.modules.account.entity.supplieraccount.SupplierAccount;
import com.jeeplus.modules.account.service.capitalaccount.CapitalAccountService;
import com.jeeplus.modules.account.service.clearingaccount.ClearingAccountService;
import com.jeeplus.modules.account.service.supplieraccount.SupplierAccountService;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.service.color.ColorsService;
import com.jeeplus.modules.goods.service.size.SizeService;
import com.jeeplus.modules.goods.service.sku.GoodsSkuService;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.account.entity.accountmanagement.AccountType;
import com.jeeplus.modules.account.entity.capitalaccount.CapitalAccount;
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
import com.jeeplus.modules.order.entity.procurement.OrderProDetail;
import com.jeeplus.modules.order.entity.procurement.OrderProcurement;
import com.jeeplus.modules.order.service.procurement.OrderProDetailService;
import com.jeeplus.modules.order.service.procurement.OrderProcurementService;
import com.jeeplus.modules.store.entity.employee.Employee;
import com.jeeplus.modules.store.service.employee.EmployeeService;

/**
 * 采购单Controller
 * 
 * @author diqiang
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/order/procurement/orderProcurement")
public class OrderProcurementController extends BaseController {

	@Autowired
	private OrderProcurementService orderProcurementService; // 采购单service

	@Autowired
	private OrderProDetailService orderProDetailService; // 采购单详细service

	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService; // 库存service

	@Autowired
	private WarehouseRecordService warehouseRecordService; // 仓库流水service

	@Autowired
	private CapitalAccountService capitalAccountService; // 资金流水service

	@Autowired
	private ClearingAccountService clearingAccountService; // 结算账户service

	@Autowired
	private SupplierAccountService supplierAccountService; // 供应商对账/付款service

	@ModelAttribute
	public OrderProcurement get(@RequestParam(required = false) String id) {
		OrderProcurement entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = orderProcurementService.get(id);
		}
		if (entity == null) {
			entity = new OrderProcurement();
		}
		return entity;
	}

	/**
	 * 采购单列表页面
	 */
	@RequiresPermissions("order:procurement:orderProcurement:list")
	@RequestMapping(value = { "list", "" })
	public String list(OrderProcurement orderProcurement, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Page<OrderProcurement> page = orderProcurementService.findPage(new Page<OrderProcurement>(request, response),
				orderProcurement);

		// setOrderProcurement所需要的对象
		List<OrderProcurement> list = page.getList();

		for (OrderProcurement op : list) {
			orderProcurementService.setOrderProcurementProperties(op);
			List<OrderProDetail> orderProDetailList = op.getOrderProDetailList();
			for (OrderProDetail opd : orderProDetailList) {
				orderProcurementService.setOrderProDetailProperties(opd);
			}
		}

		model.addAttribute("page", page);
		return "modules/order/procurement/orderProcurementList";
	}

	/**
	 * 查看，增加，编辑采购单表单页面
	 */
	@RequiresPermissions(value = { "order:procurement:orderProcurement:add",
			 }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderProcurement orderProcurement, Model model) {
		// 回显对象属性
		// orderProcurementService.setOrderProcurementProperties(orderProcurement);
		List<OrderProDetail> orderProDetailList = orderProcurement.getOrderProDetailList();
		for (OrderProDetail opd : orderProDetailList) {
			orderProcurementService.setOrderProDetailProperties(opd);
		}

		model.addAttribute("orderProcurement", orderProcurement);
		return "modules/order/procurement/orderProcurementForm";
	}
	
	/**
	 * 查看，增加，编辑采购单表单页面
	 */
	@RequiresPermissions(value = { "order:procurement:orderProcurement:view", 
			"order:procurement:orderProcurement:edit" }, logical = Logical.OR)
	@RequestMapping(value = "view")
	public String view(OrderProcurement orderProcurement, Model model) {
		// 回显对象属性
		// orderProcurementService.setOrderProcurementProperties(orderProcurement);
		List<OrderProDetail> orderProDetailList = orderProcurement.getOrderProDetailList();
		for (OrderProDetail opd : orderProDetailList) {
			orderProcurementService.setOrderProDetailProperties(opd);
		}

		model.addAttribute("orderProcurement", orderProcurement);
		return "modules/order/procurement/orderProcurementView";
	}
	
	/**
	 * 保存采购单
	 */
	@RequiresPermissions(value = { "order:procurement:orderProcurement:add",
			"order:procurement:orderProcurement:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(OrderProcurement orderProcurement, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		if (!beanValidator(model, orderProcurement)) {
			return form(orderProcurement, model);
		}
		if (!orderProcurement.getIsNewRecord()) {// 编辑表单保存
			OrderProcurement t = orderProcurementService.get(orderProcurement.getId());// 从数据库取出记录的值

			if (t.getFordertype() == 1) {
				MyBeanUtils.copyBeanNotNull2Bean(orderProcurement, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值

				// 设置审批人和操作人，如果能获取到执行人，说明审批人在操作
				User fexecutor = orderProcurement.getFexecutor();
				User fseniorarchirist = orderProcurement.getFseniorarchirist();
				// 当前登录用户
				User currentuser = UserUtils.getUser();
				// 修改时如果执行人不为空审批人为空说明当前用户为审批者，设置审批者属性
				if (fexecutor != null && fseniorarchirist == null) {
					// 设置审批人
					t.setFseniorarchirist(currentuser);
				}
				// 如果采购单已执行完毕就更改仓库记录
				if (t.getFstatus() == 6) {
					t.setFordertype(2);
					WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
					Warehouse fwarehose = t.getFwarehose();
					if (fwarehose != null) {
						wgInfo.setWarehouse(fwarehose);
					}
					List<OrderProDetail> orderProDetailList = t.getOrderProDetailList();
					SupplierBasic fsupplier = t.getFsupplier();
					for (OrderProDetail opd : orderProDetailList) {
						// 合并详细修改

						// 设置商品属性
						orderProcurementService.setgoods(opd);
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
						wgInfo.setGoodsSku(fsku);
						wgInfo.setGoodsSpu(fspu);
						List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
						if (wginfolist == null || wginfolist.size() == 0) {
							WarehouseGoodsInfo whGoodsInfo = new WarehouseGoodsInfo();
							whGoodsInfo.setGoodsBrand(brand);
							whGoodsInfo.setGoodsCategory(categorys);
							whGoodsInfo.setGoodsUnit(goodsUnit);
							whGoodsInfo.setGoodsSku(fsku);
							whGoodsInfo.setGoodsSpu(fspu);
							whGoodsInfo.setFinventory(fgoodsnum);
							whGoodsInfo.setFtotalinventory(fgoodsnum);
							whGoodsInfo.setWarehouse(fwarehose);
							whGoodsInfo.setFlockinventory(0);
							whGoodsInfo.setSupplierBasic(fsupplier);
							warehouseGoodsInfoService.save(whGoodsInfo);

							// 添加流水
							WarehouseRecord warehouseRecord = new WarehouseRecord();
							warehouseRecord.setBusinessType(warehouseRecord.BUSINESS_TYPE_PROCUREMENT);
							warehouseRecord.setGoodsSku(fsku);
							warehouseRecord.setGoodsSpu(fspu);
							warehouseRecord.setWarehouse(t.getFwarehose());
							warehouseRecord.setBusinessTime(new Date());
							warehouseRecord.setBusinessorderNumber(t.getFordernum());
							warehouseRecord.setChangeNum(fgoodsnum);
							warehouseRecord.setRemainingNum(whGoodsInfo.getFtotalinventory());
							warehouseRecordService.save(warehouseRecord);

							opd.setFwarehouseGoodsInfo(whGoodsInfo);

//							System.out.println(whGoodsInfo.getId());
						} else {
							WarehouseGoodsInfo warehouseGoodsInfo = wginfolist.get(0);
							Integer finventory = warehouseGoodsInfo.getFinventory();
							Integer ftotalinventory = warehouseGoodsInfo.getFtotalinventory();
							if (finventory != null && finventory > 0) {
								BigDecimal add = new BigDecimal(finventory.toString())
										.add(new BigDecimal(fgoodsnum.toString()));
								warehouseGoodsInfo.setFinventory(add.intValue());
							}
							if (ftotalinventory != null && ftotalinventory > 0) {
								BigDecimal add2 = new BigDecimal(ftotalinventory.toString())
										.add(new BigDecimal(fgoodsnum.toString()));

								warehouseGoodsInfo.setFtotalinventory(add2.intValue());
							}

							warehouseGoodsInfoService.save(warehouseGoodsInfo);

							// 添加流水
							WarehouseRecord warehouseRecord = new WarehouseRecord();
							warehouseRecord.setBusinessType(warehouseRecord.BUSINESS_TYPE_PROCUREMENT);
							warehouseRecord.setGoodsSku(fsku);
							warehouseRecord.setGoodsSpu(fspu);
							warehouseRecord.setWarehouse(t.getFwarehose());
							warehouseRecord.setBusinessTime(new Date());
							warehouseRecord.setBusinessorderNumber(t.getFordernum());
							warehouseRecord.setChangeNum(fgoodsnum);
							warehouseRecord.setRemainingNum(warehouseGoodsInfo.getFtotalinventory());
							warehouseRecordService.save(warehouseRecord);

							opd.setFwarehouseGoodsInfo(warehouseGoodsInfo);
							// System.out.println(warehouseGoodsInfo.getId());
						}
					}
					// 如果订单执行完毕设置结束时间
					t.setFendtime(new Date());

				}

				// 设置金额
				BigDecimal countprice = new BigDecimal("0");
				List<OrderProDetail> orderProDetailList = t.getOrderProDetailList();
				if (orderProDetailList != null && orderProDetailList.size() > 0) {
					for (OrderProDetail opd : orderProDetailList) {

						orderProcurementService.setOrderProDetailProperties(opd);
						countprice = countprice.add(opd.getFdiscountcountmoney());

						// 设置从库存
						WarehouseGoodsInfo wgInfo = new WarehouseGoodsInfo();
						// 设置商品属性
						orderProcurementService.setgoods(opd);
						// 商品ID
						GoodsSpu fspu = opd.getFspu();
						// 单条商品数量
						Integer fgoodsnum = opd.getFgoodsnum();

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
						wgInfo.setGoodsSku(fsku);
						wgInfo.setGoodsSpu(fspu);
						List<WarehouseGoodsInfo> wginfolist = warehouseGoodsInfoService.findList(wgInfo);
						if (wginfolist != null && wginfolist.size() > 0) {
							opd.setFwarehouseGoodsInfo(wginfolist.get(0));
						} else {
							opd.setFwarehouseGoodsInfo(null);
						}

					}
					t.setFcountprice(countprice);
					// 重新计算价格
					orderProcurementService.setOrderProcurementProperties(t);
					// 获取订单总价
					BigDecimal fordercountprice = t.getFordercountprice();
					//获取抹零
					BigDecimal fcutsmall = t.getFcutsmall();
					fcutsmall=(fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall:new BigDecimal(0);
					// 判断是否有整单折扣设置整单折扣后价格
					String forderdiscount = t.getForderdiscount();

					if (forderdiscount != null && !forderdiscount.trim().isEmpty()) {

						try {
							BigDecimal dis = new BigDecimal(forderdiscount).divide(new BigDecimal(100));
							// 订单折后总价
							t.setFdiscountprice((fordercountprice.multiply(dis)).subtract(fcutsmall));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 有整单折扣重新计算欠款
						orderProcurementService.setfdebt(t);
					} else {
						// 无折扣订单折后总价
						t.setFdiscountprice(fordercountprice.subtract(fcutsmall));
					}

				} else {
					// t.setFcountprice("0");
					t.setFcountprice(new BigDecimal(0));
					// 订单折后总价
					t.setFdiscountprice(new BigDecimal(0));
				}

				orderProcurementService.save(t);// 保存
				// 订单执行完毕后添加资金流水和供应商对账/付款对象信息
				if (t.getFstatus() == 6) {
					Date businessTime = new Date();
					Office fsponsor = t.getFsponsor();// 所属商户
					Office fstore = t.getFstore();// 所属门店
					// 获取订单实付款
					BigDecimal factualpayment = new BigDecimal(0);
					BigDecimal factualpayment2 = t.getFactualpayment();
					factualpayment2= (factualpayment2!= null&&factualpayment2.doubleValue()>0)? factualpayment2:new BigDecimal(0);// 实付金额
					if (factualpayment2 != null && factualpayment2.doubleValue() > 0) {
						factualpayment = factualpayment2;
					}
					// 订单执行完毕添加资金流水
					CapitalAccount capotalAccount = new CapitalAccount();
					// 获取结算账户
					ClearingAccount fdclearingaccountid = clearingAccountService.get(t.getFdclearingaccountid());

					String fbalance = "";
					BigDecimal faccountBalance = new BigDecimal(0);
					if (fdclearingaccountid != null&&!fdclearingaccountid.getId().trim().isEmpty()) {
						fbalance = fdclearingaccountid.getFbalance();// 获取结算账户余额
						fbalance=(fbalance!=null&&StringUtils.isNoneBlank(fbalance))?fbalance:"0";
						faccountBalance = new BigDecimal(fbalance).subtract(factualpayment);// 账户余额减去实付款
						// 修改结算账户余额
						if (faccountBalance != null && faccountBalance.doubleValue() > 0) {
							fdclearingaccountid.setFbalance(faccountBalance.toString());
							// 保存修改后的结算账户对象
							clearingAccountService.save(fdclearingaccountid);
						}
					}else {
						try {
							throw new Exception("获取结算账户时出错");
						} catch (Exception e) {
							e.printStackTrace();
							addMessage(redirectAttributes, "修改采购单失败，系统运转时出错！~");
							return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
						}
					}
					capotalAccount.setFoddNumbers(t.getFordernum());// 单据号
					capotalAccount.setAccountType(t.getFdaccounttype());// 账目类型
					capotalAccount.setClearingAccount(fdclearingaccountid);// 结算账户
					capotalAccount.setFinitialamount(fbalance);// 期初金额
					capotalAccount.setFaccountBalance(faccountBalance.toString());// 账户余额
					capotalAccount.setFtrader(t.getFexecutor());// 交易人
					capotalAccount.setFincome("0");// 收入
					capotalAccount.setFexpenditure(factualpayment.toString());// 支出
					// 计算盈利。应为是采购单所以不可能有收入
					capotalAccount.setFprofit((new BigDecimal(0).subtract(factualpayment)).toString());// 盈利
					capotalAccount.setFexpenditureflag(0); // 收入/支出（0支出，1收入）
					capotalAccount.setFsponsor(fsponsor);// 商户
					capotalAccount.setFstore(fstore);// 门店
					capotalAccount.setFbusinessHours(businessTime);// 业务时间
					capotalAccount.setRemarks(t.getRemarks());
					// 保存资金流水对象
					capitalAccountService.save(capotalAccount);

					// 添加供应商对账/付款信息
					SupplierAccount supplierAccount = new SupplierAccount();
					supplierAccount.setFsponsor(fsponsor);// 所属商户
					supplierAccount.setFstore(fstore);// 所属门店
					supplierAccount.setSupplierBasic(t.getFsupplier());// 供应商
					supplierAccount.setAccountType(t.getFdaccounttype());// 账目类型
					supplierAccount.setClearingAccount(fdclearingaccountid);// 结算账户
					supplierAccount.setFoddNumbers(t.getFordernum());// 单据编号
					supplierAccount.setFbusinessHours(businessTime);// 业务时间
					BigDecimal fdiscountprice = t.getFdiscountprice();//订单折后价（因为计算价钱时已经减过了去零所以先加回来）
					fdiscountprice=(fdiscountprice!=null&&fdiscountprice.doubleValue()>0)?fdiscountprice:new BigDecimal(0);
					BigDecimal fcutsmall = t.getFcutsmall();//去零
					fcutsmall=(fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall:new BigDecimal(0);
					String amountPay = fdiscountprice.add(fcutsmall).toString();// 应付金额
					supplierAccount.setFamountPay(amountPay);// 设置应付金额
					supplierAccount.setFpayAmount(factualpayment2.toString());// 设置实付金额
					supplierAccount.setFpreferentialAmount(fcutsmall.toString());// 设置优惠金额
					String solehandlingAmount = (fdiscountprice.subtract(factualpayment2)).toString();// 本单应付金额
					supplierAccount.setFsolehandlingAmount(solehandlingAmount);// 本单应付金额
					supplierAccount.setRemarks(t.getRemarks());// 备注
					// 保存供应商对账/付款信息
					supplierAccountService.save(supplierAccount);
				}

			} else if (t.getFordertype() == 2) {
				addMessage(redirectAttributes, "修改采购单失败，已采购的订单不可修改");
				return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
			} else {
				addMessage(redirectAttributes, "修改采购单失败，已撤销的订单不可修改");
				return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
			}

		} else {// 新增表单保存
				// 设置单号
			StringBuffer buffer = new StringBuffer("CG");
			String str = OddNumbers.getOrderNo();
			buffer.append(str);
			orderProcurement.setFordernum(buffer.toString());
			// 计算各种金额
			orderProcurementService.setOrderProcurementProperties(orderProcurement);
//			Warehouse fwarehose = orderProcurement.getFwarehose();
			BigDecimal countprice = new BigDecimal("0");
			List<OrderProDetail> orderProDetailList = orderProcurement.getOrderProDetailList();

			List<OrderProDetail> list = new ArrayList<>();
			if (orderProDetailList != null && orderProDetailList.size() > 0) {
				for (OrderProDetail opd : orderProDetailList) {
					opd.setFstatus(opd.STATUS_AVAILABLE);
					opd.setFsupplier(orderProcurement.getFsupplier());
					orderProcurementService.setOrderProDetailProperties(opd);
					countprice = countprice.add(opd.getFdiscountcountmoney());
					list.add(opd);
				}
				orderProcurement.setFcountprice(countprice);
				orderProcurementService.setOrderProcurementProperties(orderProcurement);
				// 获取订单总价
				BigDecimal fordercountprice = orderProcurement.getFordercountprice();
				//获取模式否有抹零
				BigDecimal fcutsmall = orderProcurement.getFcutsmall();
				fcutsmall=(fcutsmall!=null&&fcutsmall.doubleValue()>0)?fcutsmall:new BigDecimal(0);//抹零赋值以防空指针
				// 判断是否有整单折扣设置整单折扣后价格
				String forderdiscount = orderProcurement.getForderdiscount();
				if (forderdiscount != null && !forderdiscount.trim().isEmpty()) {
					BigDecimal dis = new BigDecimal(forderdiscount).divide(new BigDecimal(100));
					// 订单折后及抹零总价
					orderProcurement.setFdiscountprice((fordercountprice.multiply(dis)).subtract(fcutsmall));
					// 有整单折扣重新计算欠款
					orderProcurementService.setfdebt(orderProcurement);
				} else {
					// 无折扣订单折后总价
					orderProcurement.setFdiscountprice(fordercountprice.subtract(fcutsmall));
				}

			} else {
				orderProcurement.setFcountprice(new BigDecimal(0));
				// 订单折后总价
				orderProcurement.setFdiscountprice(new BigDecimal(0));

			}
			// 将商品详细设置完属性后设置会订单对象中
			orderProcurement.setOrderProDetailList(list);
			// 设置操作人和审批人如果有执行人，说明审批人在操作
			User fexecutor = orderProcurement.getFexecutor();
			// 获取当前用户
			User currentuser = UserUtils.getUser();
			// 获取当前用户的所属公司和部门
			// 部门
			Office office = currentuser.getOffice();
			// 公司
			Office company = currentuser.getCompany();
			// 门店为部门
			orderProcurement.setFstore(office);
			// 商户为公司
			orderProcurement.setFsponsor(company);

			if (fexecutor != null) {

				// 设置审批人
				orderProcurement.setFseniorarchirist(currentuser);
			}
			// 设置订单状态
			orderProcurement.setFstatus(1);
			orderProcurementService.save(orderProcurement);// 保存
		}
		addMessage(redirectAttributes, "保存采购单成功");
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
	}

	/**
	 * 逻辑删除采购单
	 */
	@RequiresPermissions("order:procurement:orderProcurement:del")
	@RequestMapping(value = "delete")
	public String delete(OrderProcurement orderProcurement, RedirectAttributes redirectAttributes) {
		orderProcurementService.deleteByLogic(orderProcurement);
		addMessage(redirectAttributes, "删除采购单成功");
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
	}

	/**
	 * 批量逻辑删除采购单
	 */
	@RequiresPermissions("order:procurement:orderProcurement:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			orderProcurementService.deleteByLogic(orderProcurementService.get(id));
		}
		addMessage(redirectAttributes, "删除采购单成功");
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("order:procurement:orderProcurement:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(OrderProcurement orderProcurement, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "采购单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<OrderProcurement> page = orderProcurementService
					.findPage(new Page<OrderProcurement>(request, response, -1), orderProcurement);
			new ExportExcel("采购单", OrderProcurement.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出采购单记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("order:procurement:orderProcurement:export")
	@RequestMapping(value = "exportDetail", method = RequestMethod.GET)
	public String exportDetail(OrderProcurement orderProcurement, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "采购单详细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			OrderProDetail detail = new OrderProDetail();
			detail.setForderprocurement(orderProcurement);
//			detail.setFstatus(1);
			Page<OrderProDetail> page = orderProDetailService
					.findPage(new Page<OrderProDetail>(request, response, -1), detail);
			new ExportExcel("采购单详细", OrderProDetail.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出采购单详细失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("order:procurement:orderProcurement:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderProcurement> list = ei.getDataList(OrderProcurement.class);
			for (OrderProcurement orderProcurement : list) {
				try {
					orderProcurementService.save(orderProcurement);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条采购单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条采购单记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购单失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
	}

	/**
	 * 下载导入采购单数据模板
	 */
	@RequiresPermissions("order:procurement:orderProcurement:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "采购单数据导入模板.xlsx";
			List<OrderProcurement> list = Lists.newArrayList();
			new ExportExcel("采购单数据", OrderProcurement.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
	}

	/**
	 * 选择供应商id
	 */
	@RequestMapping(value = "selectfsupplier")
	public String selectfsupplier(SupplierBasic fsupplier, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		// 添加查询条件，获取登陆者所属商户
		// User user = UserUtils.getUser();
		// Office office = user.getOffice();
		// fsupplier.setFsponsor(office);
		Page<SupplierBasic> page = orderProcurementService
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
	 * 选择仓库id
	 */
	@RequestMapping(value = "selectfwarehose")
	public String selectfwarehose(Warehouse fwarehose, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Warehouse> page = orderProcurementService.findPageByfwarehose(new Page<Warehouse>(request, response),
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
	/**
	 * 选择结算账户
	 */
	@RequestMapping(value = "selectfdclearingaccountid")
	public String selectfdclearingaccountid(ClearingAccount fdclearingaccountid, String url, String fieldLabels,
			String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<ClearingAccount> page = orderProcurementService
				.findPageByfdclearingaccountid(new Page<ClearingAccount>(request, response), fdclearingaccountid);
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
		model.addAttribute("obj", fdclearingaccountid);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择其他费用类型
	 */
	@RequestMapping(value = "selectfdaccounttype")
	public String selectfdaccounttype(AccountType fdaccounttype, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<AccountType> page = orderProcurementService
				.findPageByfdaccounttype(new Page<AccountType>(request, response), fdaccounttype);
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
		model.addAttribute("obj", fdaccounttype);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择打印模板ID
	 */
	@RequestMapping(value = "selectfmodeltype")
	public String selectfmodeltype(SysModelType fmodeltype, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SysModelType> page = orderProcurementService
				.findPageByfmodeltype(new Page<SysModelType>(request, response), fmodeltype);
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

		Page<GoodsSpu> page = orderProDetailService.findPageByfspu(new Page<GoodsSpu>(request, response), fspu);
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

		return "modules/sys/order/orderspuselect";
	}

	/**
	 * 获取到选中的spuid
	 */
	@RequestMapping(value = "ordergetspuid")
	public void getspuid(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String spuid = "";
		spuid = (String) request.getParameter("prospuid");
		// System.out.println(abc);
		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();

		session.setAttribute(windowsMACAddress + "prospuid", spuid);
	}

//	/**
//	 * 获取到选中的colorsid
//	 */
//	@RequestMapping(value = "ordergetcolorsid")
//	public void getcolorsid(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		String colorsid = "";
//		colorsid = (String) request.getParameter("procolorsid");
//		// System.out.println(abc);
//		// 获取操作系统及mac地址
//		String windowsMACAddress = MacUtils.getMac();
//
//		session.setAttribute(windowsMACAddress + "procolorsid", colorsid);
//	}
//
//	/**
//	 * 获取到选中的sizeid
//	 */
//	@RequestMapping(value = "ordergetsizeid")
//	public void getsizeid(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		String sizeid = "";
//		sizeid = (String) request.getParameter("prosizeid");
//		// System.out.println(abc);
//		// 获取操作系统及mac地址
//		String windowsMACAddress = MacUtils.getMac();
//
//		session.setAttribute(windowsMACAddress + "prosizeid", sizeid);
//	}

	/**
	 * 选择商品详情ID
	 */
	@RequestMapping(value = "selectfsku")
	public String selectfsku(GoodsSku fsku, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {

		HttpSession session = request.getSession();

		// 获取操作系统及mac地址
		String windowsMACAddress = MacUtils.getMac();
		String spuid = (String) session.getAttribute(windowsMACAddress + "prospuid");
		// 设置查询条件
		if (spuid != null && spuid != "") {
			GoodsSpu spu = new GoodsSpu();
			spu.setId(spuid);
			fsku.setGoodsSpu(spu);
			session.removeAttribute(windowsMACAddress + "prospuid");
		} else {
			setParmert(fsku, url, fieldLabels, fieldKeys, searchLabel, searchKey, request, response, model, null);
			return "modules/sys/order/orderskuselect";
		}
		Page<GoodsSku> page = orderProDetailService.findPageByfsku(new Page<GoodsSku>(request, response), fsku);

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

		return "modules/sys/order/orderskuselect";
	}
//
//	/**
//	 * 查询颜色
//	 */
//	@RequestMapping(value = "selectcolors")
//	public String selectcolors(Colors colors, String url, String fieldLabels, String fieldKeys, String searchLabel,
//			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<Colors> page = orderProDetailService.findPageBycolors(new Page<Colors>(request, response), colors);
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
//		model.addAttribute("obj", colors);
//		model.addAttribute("page", page);
//		return "modules/sys/order/ordercolorsselect";
//	}
//
//	/**
//	 * 查询尺寸
//	 */
//	@RequestMapping(value = "selectsize")
//	public String selectsize(Size size, String url, String fieldLabels, String fieldKeys, String searchLabel,
//			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<Size> page = orderProDetailService.findPageBySize(new Page<Size>(request, response), size);
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
//		model.addAttribute("obj", size);
//		model.addAttribute("page", page);
//		return "modules/sys/order/ordersizeselect";
//	}

	/**
	 * 撤销订单
	 */
	@RequiresPermissions(value = { "order:procurement:orderProcurement:edit" }, logical = Logical.OR)
	@RequestMapping(value = "repeal")
	public String repeal(OrderProcurement orderProcurement, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		if (!beanValidator(model, orderProcurement)) {
			return form(orderProcurement, model);
		}
		if (!orderProcurement.getIsNewRecord()) {// 编辑表单保存
			OrderProcurement t = orderProcurementService.get(orderProcurement.getId());// 从数据库取出记录的值
			if (t.getFordertype() == 1) {
				MyBeanUtils.copyBeanNotNull2Bean(orderProcurement, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
				t.setFordertype(3);

			} else {
				addMessage(redirectAttributes, "撤销采购单失败，订单属于不可操作状态");
				return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";
			}

		}
		addMessage(redirectAttributes, "撤销采购单成功");
		return "redirect:" + Global.getAdminPath() + "/order/procurement/orderProcurement/?repage";

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
	 * 根据订单号查询单条数据用于其他页面查看订单信息
	 * 
	 * @param 此处numid的值为订单号
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "order:procurement:orderProcurement:view" }, logical = Logical.OR)
	@RequestMapping("formbyordernum")
	public String formbyordernum(String numid, Model model) {
		// 回显对象属性
		OrderProcurement orderProcurement = null;
		try {
			orderProcurement = orderProcurementService.findUniqueByProperty("fordernum", numid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (orderProcurement != null) {
			orderProcurement = orderProcurementService.get(orderProcurement.getId());
			List<OrderProDetail> orderProDetailList = orderProcurement.getOrderProDetailList();
			for (OrderProDetail opd : orderProDetailList) {
				orderProcurementService.setOrderProDetailProperties(opd);
			}
			model.addAttribute("orderProcurement", orderProcurement);
		}

		return "modules/order/procurement/orderProcurementForm";

	}

}