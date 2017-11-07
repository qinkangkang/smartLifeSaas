/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.spu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.annotation.Resource;
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
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.service.spu.GoodsSpuService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 商品管理Controller
 * 
 * @author maxiao
 * @version 2017-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/spu/goodsSpu")
public class GoodsSpuController extends BaseController {

	@Autowired
	private GoodsSpuService goodsSpuService;

	@Resource
	private SystemService systemService;

	@ModelAttribute
	public GoodsSpu get(@RequestParam(required = false) String id) {
		GoodsSpu entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = goodsSpuService.get(id);
		}
		if (entity == null) {
			entity = new GoodsSpu();
		}
		return entity;
	}

	/**
	 * 商品管理列表页面
	 */
	@RequiresPermissions("goods:spu:goodsSpu:list")
	@RequestMapping(value = { "list", "" })
	public String list(GoodsSpu goodsSpu, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsSpu> page = goodsSpuService.findPage(new Page<GoodsSpu>(request, response), goodsSpu);
		List<GoodsSpu> list = page.getList();
		for (GoodsSpu goodsSpu2 : list) {
			goodsSpu2.setCreateBy(systemService.getUser(goodsSpu2.getCreateBy().getId()));
			goodsSpu2.setUpdateBy(systemService.getUser(goodsSpu2.getUpdateBy().getId()));
		}
		model.addAttribute("page", page);
		return "modules/goods/spu/goodsSpuList";
	}

	/**
	 * 查看，增加，编辑商品管理表单页面
	 */
	@RequiresPermissions(value = { "goods:spu:goodsSpu:view", "goods:spu:goodsSpu:add",
			"goods:spu:goodsSpu:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(GoodsSpu goodsSpu, Model model) {
		model.addAttribute("goodsSpu", goodsSpu);
		return "modules/goods/spu/goodsSpuForm";
	}

	/**
	 * 保存商品管理
	 */
	@RequiresPermissions(value = { "goods:spu:goodsSpu:add", "goods:spu:goodsSpu:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(GoodsSpu goodsSpu, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, goodsSpu)) {
			return form(goodsSpu, model);
		}
		if (!goodsSpu.getIsNewRecord()) {// 编辑表单保存
			GoodsSpu t = goodsSpuService.get(goodsSpu.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(goodsSpu, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			System.out.println(goodsSpu.getFimage2());
			goodsSpuService.save(t);// 保存
		} else {// 新增表单保存
			System.out.println(goodsSpu.getFimage1());
			// 当前登录用户
			User currentuser = UserUtils.getUser();
			if ("adimin" != currentuser.getLoginName()) {
				// 部门
				Office office = currentuser.getOffice();
				goodsSpu.setOffice(office);
				// 公司:公司的员工可以看到该公司的产品
				Office company = currentuser.getCompany();
				goodsSpu.setCompany(company);
			}

			goodsSpuService.save(goodsSpu);// 保存
		}
		addMessage(redirectAttributes, "保存商品管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/spu/goodsSpu/?repage";
	}

	/**
	 * 逻辑删除商品管理
	 */
	@RequiresPermissions("goods:spu:goodsSpu:del")
	@RequestMapping(value = "delete")
	public String delete(GoodsSpu goodsSpu, RedirectAttributes redirectAttributes) {
		goodsSpuService.deleteByLogic(goodsSpu);
		addMessage(redirectAttributes, "删除商品管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/spu/goodsSpu/?repage";
	}

	/**
	 * 批量逻辑删除商品管理
	 */
	@RequiresPermissions("goods:spu:goodsSpu:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			goodsSpuService.deleteByLogic(goodsSpuService.get(id));
		}
		addMessage(redirectAttributes, "删除商品管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/spu/goodsSpu/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goods:spu:goodsSpu:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(GoodsSpu goodsSpu, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "商品管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<GoodsSpu> page = goodsSpuService.findPage(new Page<GoodsSpu>(request, response, -1), goodsSpu);
			new ExportExcel("商品管理", GoodsSpu.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出商品管理记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/spu/goodsSpu/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("goods:spu:goodsSpu:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GoodsSpu> list = ei.getDataList(GoodsSpu.class);
			for (GoodsSpu goodsSpu : list) {
				try {
					goodsSpuService.save(goodsSpu);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条商品管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条商品管理记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入商品管理失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/spu/goodsSpu/?repage";
	}

	/**
	 * 下载导入商品管理数据模板
	 */
	@RequiresPermissions("goods:spu:goodsSpu:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "商品管理数据导入模板.xlsx";
			List<GoodsSpu> list = Lists.newArrayList();
			new ExportExcel("商品管理数据", GoodsSpu.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/spu/goodsSpu/?repage";
	}

	/**
	 * 选择商品单位
	 */
	@RequestMapping(value = "selectgoodsUnit")
	public String selectgoodsUnit(GoodsUnit goodsUnit, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!"admin".equals(user.getLoginName())) {
			Office company = user.getCompany();
			goodsUnit.setFsponsorid(company);
		}
		Page<GoodsUnit> page = goodsSpuService.findPageBygoodsUnit(new Page<GoodsUnit>(request, response), goodsUnit);
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
		model.addAttribute("obj", goodsUnit);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择商品所属品牌
	 */
	@RequestMapping(value = "selectbrand")
	public String selectbrand(Brand brand, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!"admin".equals(user.getLoginName()) ) {
			Office company = user.getCompany();
			brand.setFsponsorId(company);
		}
		Page<Brand> page = goodsSpuService.findPageBybrand(new Page<Brand>(request, response), brand);
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
		model.addAttribute("obj", brand);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择商品所属分类
	 */
	@RequestMapping(value = "selectcategorys")
	public String selectcategorys(Categorys categorys, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!"admin".equals(user.getLoginName()) ) {
			Office company = user.getCompany();
			categorys.setFsponsorId(company);
		}

		Page<Categorys> page = goodsSpuService.findPageBycategorys(new Page<Categorys>(request, response), categorys);
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
		model.addAttribute("obj", categorys);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

}