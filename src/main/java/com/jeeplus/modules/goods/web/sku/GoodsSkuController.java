/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.sku;

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

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.service.sku.GoodsSkuService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 商品详情Controller
 * 
 * @author maxiao
 * @version 2017-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/sku/goodsSku")
public class GoodsSkuController extends BaseController {

	@Autowired
	private GoodsSkuService goodsSkuService;

	@ModelAttribute
	public GoodsSku get(@RequestParam(required = false) String id) {
		GoodsSku entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = goodsSkuService.get(id);
		}
		if (entity == null) {
			entity = new GoodsSku();
		}
		return entity;
	}

	/**
	 * 商品详情管理,保存颜色,尺码等列表页面
	 *//*
		 * @RequiresPermissions("goods:sku:goodsSku:list")
		 * 
		 * @RequestMapping(value = {"list", ""}) public String list(GoodsSku
		 * goodsSku, @RequestParam(required=false) String spuid,
		 * HttpServletRequest request, HttpServletResponse response, Model
		 * model) { fspuid=spuid; goodsSku.setFspuid(fspuid);
		 * model.addAttribute("fspuid", fspuid); //List<GoodsSku>
		 * list=goodsSkuService.findSkuBySpuid(fspuid);
		 * //model.addAttribute("list", list);
		 * 
		 * Page<GoodsSku> page = goodsSkuService.findPage(new
		 * Page<GoodsSku>(request, response), goodsSku);
		 * model.addAttribute("page", page); return
		 * "modules/goods/sku/goodsSkuList"; }
		 */
	/**
	 * 查看，增加，编辑商品详情管理,保存颜色,尺码等表单页面
	 */
	@RequiresPermissions(value = { "goods:sku:goodsSku:view", "goods:sku:goodsSku:add",
			"goods:sku:goodsSku:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(GoodsSku goodsSku, Model model) {
		model.addAttribute("goodsSku", goodsSku);
		return "modules/goods/sku/goodsSkuForm";
	}

	/**
	 * 保存商品详情管理,保存颜色,尺码等
	 */
	@RequiresPermissions(value = { "goods:sku:goodsSku:add", "goods:sku:goodsSku:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(GoodsSku goodsSku, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, goodsSku)) {
			return form(goodsSku, model);
		}
		if (!goodsSku.getIsNewRecord()) {// 编辑表单保存
			GoodsSku t = goodsSkuService.get(goodsSku.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(goodsSku, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			goodsSkuService.save(t);// 保存
		} else {// 新增表单保存
			goodsSkuService.save(goodsSku);// 保存
		}
		addMessage(redirectAttributes, "保存商品详情管理,保存颜色,尺码等成功");
		return "redirect:" + Global.getAdminPath() + "/goods/sku/goodsSku/?repage";
	}

	/**
	 * 逻辑删除商品详情管理,保存颜色,尺码等
	 */
	@RequiresPermissions("goods:sku:goodsSku:del")
	@RequestMapping(value = "delete")
	public String delete(GoodsSku goodsSku, RedirectAttributes redirectAttributes) {
		goodsSkuService.deleteByLogic(goodsSku);
		addMessage(redirectAttributes, "删除商品详情管理,保存颜色,尺码等成功");
		return "redirect:" + Global.getAdminPath() + "/goods/sku/goodsSku/?repage";
	}

	/**
	 * 批量逻辑删除商品详情管理,保存颜色,尺码等
	 */
	@RequiresPermissions("goods:sku:goodsSku:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			goodsSkuService.deleteByLogic(goodsSkuService.get(id));
		}
		addMessage(redirectAttributes, "删除商品详情管理,保存颜色,尺码等成功");
		return "redirect:" + Global.getAdminPath() + "/goods/sku/goodsSku/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goods:sku:goodsSku:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(GoodsSku goodsSku, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "商品详情管理,保存颜色,尺码等" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<GoodsSku> page = goodsSkuService.findPage(new Page<GoodsSku>(request, response, -1), goodsSku);
			new ExportExcel("商品详情管理,保存颜色,尺码等", GoodsSku.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出商品详情管理,保存颜色,尺码等记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/sku/goodsSku/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("goods:sku:goodsSku:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GoodsSku> list = ei.getDataList(GoodsSku.class);
			for (GoodsSku goodsSku : list) {
				try {
					goodsSkuService.save(goodsSku);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条商品详情管理,保存颜色,尺码等记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条商品详情管理,保存颜色,尺码等记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入商品详情管理,保存颜色,尺码等失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/sku/goodsSku/?repage";
	}

	/**
	 * 下载导入商品详情管理,保存颜色,尺码等数据模板
	 */
	@RequiresPermissions("goods:sku:goodsSku:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "商品详情管理,保存颜色,尺码等数据导入模板.xlsx";
			List<GoodsSku> list = Lists.newArrayList();
			new ExportExcel("商品详情管理,保存颜色,尺码等数据", GoodsSku.class, 1).setDataList(list).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/sku/goodsSku/?repage";
	}

	/**
	 * 选择颜色
	 */
	@RequestMapping(value = "selectcolors")
	public String selectcolors(Colors colors, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!"admin".equals(user.getLoginName())) {
			Office company = user.getCompany();
			colors.setFsponsorid(company);
		}

		Page<Colors> page = goodsSkuService.findPageBycolors(new Page<Colors>(request, response), colors);
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
		model.addAttribute("obj", colors);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择尺码
	 */
	@RequestMapping(value = "selectsize")
	public String selectsize(Size size, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {

		User user = UserUtils.getUser();
		if (!"admin".equals(user.getLoginName())){
			Office company = user.getCompany();
			size.setFsponsorid(company);	
		}
		Page<Size> page = goodsSkuService.findPageBysize(new Page<Size>(request, response), size);
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
		model.addAttribute("obj", size);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

}