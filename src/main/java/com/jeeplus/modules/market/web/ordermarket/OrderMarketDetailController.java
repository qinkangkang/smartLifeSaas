/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.web.ordermarket;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.service.basic.CustomerBasicService;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketCha;
import com.jeeplus.modules.market.entity.ordermarketchargeback.OrderMarketChaDetail;
import com.jeeplus.modules.market.service.ordermarket.OrderMarketDetailService;
import com.jeeplus.modules.market.service.ordermarket.OrderMarketService;
import com.jeeplus.modules.market.service.ordermarketchargeback.OrderMarketChaService;

/**
 * 销售单详细Controller辅助退单
 * 
 * @author diqiang
 * @version 2017-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/market/ordermarket/orderMarketDetail")
public class OrderMarketDetailController extends BaseController {

	@Autowired
	private OrderMarketDetailService orderMarketDetailService;
	@Autowired
	private OrderMarketService orderMarketService;
	@Autowired
	private OrderMarketChaService orderMarketChaService;
	
	
	@Autowired
	private CustomerBasicService customerBasicService;
	
	@ModelAttribute
	public OrderMarketDetail get(@RequestParam(required = false) String id) {
		OrderMarketDetail entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = orderMarketDetailService.get(id);
		}
		if (entity == null) {
			entity = new OrderMarketDetail();
		}
		return entity;
	}

	/**
	 * 销售单详细列表页面
	 */
	@RequiresPermissions("market:ordermarket:orderMarketDetail:list")
	@RequestMapping(value = { "list", "" })
	public String list(OrderMarketDetail orderMarketDetail, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<OrderMarketDetail> page = null;
		// 获取销售单。并设置属性
		OrderMarket orderMarket = orderMarketService.get(orderMarketDetail.getOrderMarket());
		if (orderMarket != null&&orderMarket.getFstatus()==3) {
			
//				orderMarketService.setOrderOrderMarketProperties(orderMarket);
				
				page = orderMarketDetailService.findPage(new Page<OrderMarketDetail>(request, response), orderMarketDetail);
				List<OrderMarketDetail> list = page.getList();
				if(list!=null&&list.size()>0){
					for (OrderMarketDetail omd : list) {
						orderMarketService.setOrderMarketDetailProperties(omd);
//						Integer finventory = omd.getFwarehousegoodsinfo().getFinventory();
//						System.out.println(finventory);

					}	
				}
		}
		model.addAttribute("page", page);
		return "modules/market/ordermarket/orderMarketDetailList";
	}

	/**
	 * 查看，增加，编辑销售单详细表单页面
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarketDetail:view",
			"market:ordermarket:orderMarketDetail:add",
			"market:ordermarket:orderMarketDetail:edit","market:ordermarketchargeback:orderMarketCha:add","market:ordermarketchargeback:orderMarketCha:del" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderMarketDetail orderMarketDetail, Model model) {

		orderMarketService.setOrderMarketDetailProperties(orderMarketDetail);
		model.addAttribute("orderMarketDetail", orderMarketDetail);
		return "modules/market/ordermarket/orderMarketDetailForm";
	}

	/**
	 * 保存销售单详细
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarketDetail:add",
			"market:ordermarket:orderMarketDetail:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(OrderMarketDetail orderMarketDetail, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		if (!beanValidator(model, orderMarketDetail)) {
			return form(orderMarketDetail, model);
		}
		if (!orderMarketDetail.getIsNewRecord()) {// 编辑表单保存
			OrderMarketDetail t = orderMarketDetailService.get(orderMarketDetail.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(orderMarketDetail, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			orderMarketDetailService.save(t);// 保存
		} else {// 新增表单保存
			orderMarketDetailService.save(orderMarketDetail);// 保存
		}
		addMessage(redirectAttributes, "保存销售单详细成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarketDetail/?repage";
	}

	/**
	 * 逻辑删除销售单详细
	 */
	@RequiresPermissions("market:ordermarket:orderMarketDetail:del")
	@RequestMapping(value = "delete")
	public String delete(OrderMarketDetail orderMarketDetail, RedirectAttributes redirectAttributes) {
		orderMarketDetailService.deleteByLogic(orderMarketDetail);
		addMessage(redirectAttributes, "删除销售单详细成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarketDetail/?repage";
	}

	/**
	 * 批量逻辑删除销售单详细
	 */
	@RequiresPermissions("market:ordermarket:orderMarketDetail:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			orderMarketDetailService.deleteByLogic(orderMarketDetailService.get(id));
		}
		addMessage(redirectAttributes, "删除销售单详细成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarketDetail/?repage";
	}

	/**
	 * 生成退单
	 * 
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "market:ordermarket:orderMarketDetail:del",
			"market:ordermarketchargeback:orderMarketCha:add",
			"market:ordermarketchargeback:orderMarketCha:del" }, logical = Logical.OR)
	@RequestMapping(value = "marketreturnAll")
	public String returnAll(String ids, RedirectAttributes redirectAttributes) {
		OrderMarket orderMarket = null;
		if (!StringUtils.isEmpty(ids)) {
			String[] arr = ids.split(";");
			List<OrderMarketChaDetail> list = new ArrayList<>();
			for (String s : arr) {
				String[] arr1 = s.split(":");
				String id = arr1[0];
				String returnnum = arr1[1];
				OrderMarketDetail orderMarketDetail = orderMarketDetailService.get(id);
				// 设置采购信息备注
				orderMarketDetail.setFreturnnum(Integer.parseInt(returnnum));
				Integer fgoodsnum = orderMarketDetail.getFgoodsnum();
				if (Integer.parseInt(returnnum) >= fgoodsnum) {
					orderMarketDetail.setFstatus(2);
				}
				
				//设置销售单详细退货数量
				if(orderMarketDetail!=null){
					orderMarketDetail.setFreturnnum(Integer.parseInt(returnnum));
					orderMarketDetailService.save(orderMarketDetail);
				}

				// 设置销售退单详细信息
				orderMarket = orderMarketDetail.getOrderMarket();
				OrderMarketChaDetail omdc = new OrderMarketChaDetail();
				// 赋值
				omdc.setFspu(orderMarketDetail.getGoodsSpu());
				omdc.setFsku(orderMarketDetail.getGoodsSku());
				omdc.setFwarehousegoodsinfo(orderMarketDetail.getFwarehousegoodsinfo());
				omdc.setFgoodsprice(orderMarketDetail.getFgoodsprice());
				omdc.setFcountmoney(orderMarketDetail.getFcountmoney());
				omdc.setFgoodsdiscount(orderMarketDetail.getFgoodsdiscount());
				omdc.setFdiscountcountmoney(orderMarketDetail.getFdiscountcountmoney());
				omdc.setFdiscountprice(orderMarketDetail.getFdiscountprice());
				omdc.setFstatus("1");
				omdc.setFgoodsnum(Integer.parseInt(returnnum));
				omdc.setFordermarket(orderMarket);
				;
				list.add(omdc);

				// System.out.println("成功了");
			}
			OrderMarketCha omc = new OrderMarketCha();
			omc.setOrderMarketChaDetailList(list);
			// 设置单号(已在service层设置)
		
			if (orderMarket != null) {
				orderMarket = orderMarketService.get(orderMarket.getId());
			}
			omc.setCustomerBasic(orderMarket.getCustomerBasic());
			omc.setClearingAccount(orderMarket.getClearingAccount());
			omc.setWarehouse(orderMarket.getWarehouse());
			omc.setFsponsor(orderMarket.getFsponsor());
			omc.setFordermodel(orderMarket.getFordermodel());
			omc.setFstore(orderMarket.getFstore());
			//设置整单折扣
			omc.setForderdiscount(orderMarket.getForderdiscount());
			// 设置退单状态
			omc.setFordertype(1);
			omc.setFstatus(1);
			// 总价

//			omc.setFcountprice(countPrice(list));
			omc.setFcountprice(new BigDecimal(countPrice(list)));
			
			//再次计算订单价钱
			orderMarketChaService.setOrderOrderMarketChaProperties(omc);

			//获取订单总价
			BigDecimal fordercountprice = omc.getFordercountprice();
			//判断是否有整单折扣设置整单折扣后价格
			String forderdiscount = omc.getForderdiscount();

			if(forderdiscount!=null&&!forderdiscount.trim().isEmpty()){
			
				BigDecimal dis=new BigDecimal(forderdiscount).divide(new BigDecimal(100));
				//订单折后总价
				omc.setFdiscountprice(fordercountprice.multiply(dis));
				//有整单折扣重新计算欠款
				orderMarketChaService.setfdebt(omc);
			}else{
				//无折扣订单折后总价
				omc.setFdiscountprice(fordercountprice);
			}
			
			//获取是否有客户对象是否有折扣
			CustomerBasic customerBasic = omc.getCustomerBasic();
			String fdiscount = "";
			if(customerBasic!=null&&StringUtils.isNoneBlank(customerBasic.getId())){
				CustomerBasic customerBasic2 = customerBasicService.get(customerBasic);
				fdiscount = customerBasic2.getFdiscount();
			}
			//如果客户有折扣则再折上折
			if(fdiscount!=null&&!fdiscount.trim().isEmpty()){
				BigDecimal fdiscountprice = omc.getFdiscountprice();
				BigDecimal customerdiscount = new BigDecimal(fdiscount).divide(new BigDecimal(100));
				omc.setFdiscountprice(fdiscountprice.multiply(customerdiscount));
			}
			

			orderMarketChaService.save(omc);
		}
		addMessage(redirectAttributes, "生成采购退单成功");
		return "redirect:" + Global.getAdminPath() + "/market/ordermarketchargeback/orderMarketCha/?repage";
	}

	/**
	 * 设置退单金额
	 * 
	 * @param list
	 * @return
	 */

	public String countPrice(List<OrderMarketChaDetail> list) {
		BigDecimal total;
		BigDecimal count=new BigDecimal(0);
		for (OrderMarketChaDetail omcd : list) {
			int num = omcd.getFgoodsnum();
//			double singlePrice = Double.parseDouble(omcd.getFgoodsprice());
			BigDecimal singlePrice = omcd.getFgoodsprice();
//			double discount = Double.parseDouble(omcd.getFgoodsdiscount().equals("") ? "1" : omcd.getFgoodsdiscount());
			double discount = Double.parseDouble(omcd.getFgoodsdiscount().equals("") ? "100" : omcd.getFgoodsdiscount());
			discount=(new BigDecimal(discount).divide(new BigDecimal(100))).doubleValue();
//			total = singlePrice * num * discount;
			total = (singlePrice .multiply(new BigDecimal(num))).multiply(new BigDecimal(discount)) ;
			count = count.add(total);
		}
		return String.valueOf(count.doubleValue());
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("market:ordermarket:orderMarketDetail:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(OrderMarketDetail orderMarketDetail, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售单详细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<OrderMarketDetail> page = orderMarketDetailService
					.findPage(new Page<OrderMarketDetail>(request, response, -1), orderMarketDetail);
			new ExportExcel("销售单详细", OrderMarketDetail.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出销售单详细记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarketDetail/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("market:ordermarket:orderMarketDetail:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderMarketDetail> list = ei.getDataList(OrderMarketDetail.class);
			for (OrderMarketDetail orderMarketDetail : list) {
				try {
					orderMarketDetailService.save(orderMarketDetail);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条销售单详细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条销售单详细记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售单详细失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarketDetail/?repage";
	}

	/**
	 * 下载导入销售单详细数据模板
	 */
	@RequiresPermissions("market:ordermarket:orderMarketDetail:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "销售单详细数据导入模板.xlsx";
			List<OrderMarketDetail> list = Lists.newArrayList();
			new ExportExcel("销售单详细数据", OrderMarketDetail.class, 1).setDataList(list).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/market/ordermarket/orderMarketDetail/?repage";
	}

	/**
	 * 选择销售单ID
	 */
	@RequestMapping(value = "selectorderMarket")
	public String selectorderMarket(OrderMarket orderMarket, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		orderMarket.setFstatus(3);
		Page<OrderMarket> page = orderMarketDetailService
				.findPageByorderMarket(new Page<OrderMarket>(request, response), orderMarket);
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
		model.addAttribute("obj", orderMarket);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择商品名称
	 */
	@RequestMapping(value = "selectgoodsSpu")
	public String selectgoodsSpu(GoodsSpu goodsSpu, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<GoodsSpu> page = orderMarketDetailService.findPageBygoodsSpu(new Page<GoodsSpu>(request, response),
				goodsSpu);
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
	 * 选择商品详情
	 */
	@RequestMapping(value = "selectgoodsSku")
	public String selectgoodsSku(GoodsSku goodsSku, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<GoodsSku> page = orderMarketDetailService.findPageBygoodsSku(new Page<GoodsSku>(request, response),
				goodsSku);
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

}