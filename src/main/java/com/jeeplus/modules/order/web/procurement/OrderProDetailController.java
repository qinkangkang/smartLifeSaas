package com.jeeplus.modules.order.web.procurement;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.service.spu.GoodsSpuService;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChaDetail;
import com.jeeplus.modules.order.entity.prochargeback.OrderProChargeback;
import com.jeeplus.modules.order.service.prochargeback.OrderProChargebackService;
import com.jeeplus.modules.order.service.procurement.OrderProDetailService;
import com.jeeplus.modules.supplier.entity.supplierbasic.SupplierBasic;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.service.WarehouseGoodsInfoService;
/**
 * 采购单详情Controller辅助采购退单
 * 因需求已将订单详细对象换为库存商品对象
 * @author diqiang
 * @version 2017-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/order/procurement/orderProDetail")
public class OrderProDetailController extends BaseController {

	@Autowired
	private OrderProDetailService orderProDetailService;

	@Autowired
	private OrderProChargebackService orderProChargebackService;
	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;//库存service
	@Autowired
	private GoodsSpuService GoodsSpuService;
	@ModelAttribute
	public WarehouseGoodsInfo get(@RequestParam(required = false) String id) {
		WarehouseGoodsInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = warehouseGoodsInfoService.get(id);
		}
		if (entity == null) {
			entity = new WarehouseGoodsInfo();
		}
		return entity;
	}

	/**
	 * 采购单详情列表页面
	 */
	@RequiresPermissions(value={"order:procurement:orderProDetail:list", "order:prochargeback:orderProChargeback:add", "order:prochargeback:orderProChargeback:del"},logical = Logical.OR)
	@RequestMapping(value = { "list", "" })
	public String list(WarehouseGoodsInfo orderProDetail, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Warehouse warehouse = orderProDetail.getWarehouse();
		SupplierBasic supplierBasic = orderProDetail.getSupplierBasic();
		String  warehouseid = null;
		String  supplierBasicid = null;
		if(warehouse!=null){
			warehouseid = warehouse.getId();
		}
		if(supplierBasic!=null){
			supplierBasicid = supplierBasic.getId();
		}
		if(warehouseid!=null&&supplierBasicid!=null&&!warehouseid.trim().isEmpty()&&!supplierBasicid.trim().isEmpty()){
			Page<WarehouseGoodsInfo> page = warehouseGoodsInfoService.findPage(new Page<WarehouseGoodsInfo>(request, response), orderProDetail); 
			model.addAttribute("page", page);
			return "modules/order/procurement/orderProDetailList";
		}else{
			return "modules/order/procurement/orderProDetailList";
		}
		
		
	}
	
	/**
	 * 查看，增加，编辑采购单详情表单页面
	 */
	@RequiresPermissions(value = { "order:procurement:orderProDetail:view", "order:procurement:orderProDetail:add",
			"order:procurement:orderProDetail:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(WarehouseGoodsInfo orderProDetail, Model model) {

		model.addAttribute("orderProDetail", orderProDetail);
		return "modules/order/procurement/orderProDetailForm";
	}
	/**
	 * 生成退单
	 * 
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * 
	 */
	@RequiresPermissions(value = {"order:procurement:orderProDetail:del",
			"order:prochargeback:orderProChargeback:add", "order:prochargeback:orderProChargeback:del" }, logical = Logical.OR)
	@RequestMapping(value = "returnAll")
	public String returnAll(String ids, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();
		Office company = user.getCompany();
		Office office = user.getOffice();
		WarehouseGoodsInfo warehouseGoodsInfo = null;
		if (!StringUtils.isEmpty(ids)) {
			String[] arr = ids.split(";");
			List<OrderProChaDetail> list = new ArrayList<>();
			for (String s : arr) {
				String[] arr1 = s.split(":");
				String id = arr1[0];
				String returnnum = arr1[1];
				warehouseGoodsInfo = warehouseGoodsInfoService.get(id);
				OrderProChaDetail orderProChaDetail = new OrderProChaDetail();
				GoodsSpu goodsSpu = warehouseGoodsInfo.getGoodsSpu();
				GoodsSku goodsSku = warehouseGoodsInfo.getGoodsSku();
				goodsSpu=GoodsSpuService.get(goodsSpu);
				String fbuyprice = goodsSpu.getFbuyprice();
				BigDecimal goodsprice = (fbuyprice!=null&&!fbuyprice.trim().isEmpty())?new BigDecimal(fbuyprice):new BigDecimal(0);
				orderProChaDetail.setFspu(goodsSpu);
				orderProChaDetail.setFsku(goodsSku);
				orderProChaDetail.setFgoodsnum(Integer.parseInt(returnnum));
				orderProChaDetail.setFgoodsprice(goodsprice);
				orderProChaDetail.setFstatus(1);
				orderProChaDetail.setFdiscount("100");
				list.add(orderProChaDetail);

				// System.out.println("成功了");
			}
			OrderProChargeback orderProChargeback = new OrderProChargeback();
			orderProChargeback.setOrderProChaDetailList(list);
			// 设置单号
			StringBuffer buffer = new StringBuffer("CT");
			String str = OddNumbers.getOrderNo();
			buffer.append(str);
			orderProChargeback.setFordernum(buffer.toString());
			//设置对应信息
			if(warehouseGoodsInfo!=null&&!warehouseGoodsInfo.getId().trim().isEmpty()){
				orderProChargeback.setFsupplier(warehouseGoodsInfo.getSupplierBasic());
				orderProChargeback.setFwarehose(warehouseGoodsInfo.getWarehouse());
				orderProChargeback.setForderdiscount("100");
			}
			orderProChargeback.setFsponsor(company);
			orderProChargeback.setFstore(office);
			// 设置退单状态
			orderProChargeback.setFordertype(1);
			orderProChargeback.setFstatus(1);
			// 总价
			BigDecimal fordercountprice=new BigDecimal(countPrice(list));
			//商品总价
			orderProChargeback.setFcountprice(fordercountprice);
			//订单总价
			orderProChargeback.setFordercountprice(fordercountprice);
			//判断是否有整单折扣设置整单折扣后价格
			String forderdiscount = orderProChargeback.getForderdiscount();

			if(forderdiscount!=null&&!forderdiscount.trim().isEmpty()){
			
				BigDecimal dis=new BigDecimal(forderdiscount).divide(new BigDecimal(100));
				//订单折后总价
				orderProChargeback.setFdiscountprice(fordercountprice.multiply(dis));
				//重新计算欠款
				orderProChargebackService.setfdebt(orderProChargeback);
			}else{
				//无折扣订单折后总价
				orderProChargeback.setFdiscountprice(fordercountprice);
			}
			
			orderProChargebackService.save(orderProChargeback);
		}
		addMessage(redirectAttributes, "生成采购退单成功");
		return "redirect:" + Global.getAdminPath() + "/order/prochargeback/orderProChargeback/?repage";
	}

	/**
	 * 设置退单金额
	 * 
	 * @param list
	 * @return
	 */

	// public String countPrice(List<OrderProChaDetail> list) {
	public String countPrice(List<OrderProChaDetail> list) {
		BigDecimal total;
		BigDecimal count=new BigDecimal(0);
		for (OrderProChaDetail orderProChaDetail : list) {
			int num = orderProChaDetail.getFgoodsnum();
			BigDecimal singlePrice = orderProChaDetail.getFgoodsprice();
			String fdiscount = orderProChaDetail.getFdiscount();
			double discount = Double
					.parseDouble((fdiscount!=null&&!fdiscount.trim().isEmpty()) ? orderProChaDetail.getFdiscount():"100");
			discount=(new BigDecimal(discount).divide(new BigDecimal(100))).doubleValue();
			total = (singlePrice.multiply(new BigDecimal(num))).multiply(new BigDecimal(discount));
			count=count.add(total);
		}
		return String.valueOf(count.doubleValue());
	}



	/**
	 * 选择供应商ID
	 */
	@RequestMapping(value = "selectsupplier")
	public String selectsupplier(SupplierBasic supplierBasic, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Page<SupplierBasic> page = orderProDetailService.findPageBySupplier(new Page<SupplierBasic>(request, response),
				supplierBasic);

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
		model.addAttribute("obj", supplierBasic);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	
	/**
	 * 选择仓库ID
	 */
	@RequestMapping(value = "selectwarehouse")
	public String selectwarehouse(Warehouse warehouse, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Page<Warehouse> page = warehouseGoodsInfoService.findPageBywarehouse(new Page<Warehouse>(request, response),
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
		return "modules/sys/gridselect";
	}

	/**
	 * 选择商品详情ID
	 */
	@RequestMapping(value = "selectfsku")
	public String selectfsku(GoodsSku fsku, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
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
		return "modules/sys/gridselect";
	}

}