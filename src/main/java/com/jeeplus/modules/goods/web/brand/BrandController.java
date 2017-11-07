/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.brand;

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
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.service.brand.BrandService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 商品品牌Controller
 * 
 * @author maxiao
 * @version 2017-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/brand/brand")
public class BrandController extends BaseController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public Brand get(@RequestParam(required = false) String id) {
		Brand entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = brandService.get(id);
		}
		if (entity == null) {
			entity = new Brand();
		}
		return entity;
	}

	/**
	 * 品牌管理列表页面
	 */
	@RequiresPermissions("goods:brand:brand:list")
	@RequestMapping(value = { "list", "" })
	public String list(Brand brand, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Brand> page = brandService.findPage(new Page<Brand>(request, response), brand);
		List<Brand> list = page.getList();
		for (Brand brand2 : list) {
			brand2.setCreateBy(systemService.getUser(brand2.getCreateBy().getId()));
			brand2.setUpdateBy(systemService.getUser(brand2.getUpdateBy().getId()));
		}
		model.addAttribute("page", page);
		return "modules/goods/brand/brandList";
	}

	/**
	 * 查看，增加，编辑品牌管理表单页面
	 */
	@RequiresPermissions(value = { "goods:brand:brand:view", "goods:brand:brand:add",
			"goods:brand:brand:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Brand brand, Model model) {
		model.addAttribute("brand", brand);
		return "modules/goods/brand/brandForm";
	}

	/**
	 * 保存品牌管理
	 */
	@RequiresPermissions(value = { "goods:brand:brand:add", "goods:brand:brand:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Brand brand, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, brand)) {
			return form(brand, model);
		}
		if (!brand.getIsNewRecord()) {// 编辑表单保存
			Brand t = brandService.get(brand.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(brand, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			brandService.save(t);// 保存
		} else {// 新增表单保存
			User currentuser = UserUtils.getUser();
			// 部门
			if(currentuser.getLoginName()!="admin"){
				Office office = currentuser.getOffice();
				brand.setOffice(office);
				// 公司:公司的员工可以看到该公司的
				Office company = currentuser.getCompany();
				brand.setFsponsorId(company);
			}
			
			brandService.save(brand);// 保存
		}
		addMessage(redirectAttributes, "保存品牌管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/brand/brand/?repage";
	}

	/**
	 * 逻辑删除品牌管理
	 */
	@RequiresPermissions("goods:brand:brand:del")
	@RequestMapping(value = "delete")
	public String delete(Brand brand, RedirectAttributes redirectAttributes) {
		brandService.deleteByLogic(brand);
		addMessage(redirectAttributes, "删除品牌管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/brand/brand/?repage";
	}

	/**
	 * 批量逻辑删除品牌管理
	 */
	@RequiresPermissions("goods:brand:brand:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			brandService.deleteByLogic(brandService.get(id));
		}
		addMessage(redirectAttributes, "删除品牌管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/brand/brand/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goods:brand:brand:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Brand brand, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "品牌管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Brand> page = brandService.findPage(new Page<Brand>(request, response, -1), brand);
			new ExportExcel("品牌管理", Brand.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出品牌管理记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/brand/brand/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("goods:brand:brand:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Brand> list = ei.getDataList(Brand.class);
			for (Brand brand : list) {
				try {
					brandService.save(brand);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条品牌管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条品牌管理记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入品牌管理失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/brand/brand/?repage";
	}

	/**
	 * 下载导入品牌管理数据模板
	 */
	@RequiresPermissions("goods:brand:brand:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "品牌管理数据导入模板.xlsx";
			List<Brand> list = Lists.newArrayList();
			new ExportExcel("品牌管理数据", Brand.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/brand/brand/?repage";
	}

}