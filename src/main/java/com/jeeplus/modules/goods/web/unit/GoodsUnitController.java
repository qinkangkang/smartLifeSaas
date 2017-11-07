/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.unit;

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
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.service.unit.GoodsUnitService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 单位管理Controller
 * 
 * @author maxiao
 * @version 2017-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/unit/goodsUnit")
public class GoodsUnitController extends BaseController {

	@Autowired
	private GoodsUnitService goodsUnitService;

	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public GoodsUnit get(@RequestParam(required = false) String id) {
		GoodsUnit entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = goodsUnitService.get(id);
		}
		if (entity == null) {
			entity = new GoodsUnit();
		}
		return entity;
	}

	/**
	 * 单位管理列表页面
	 */
	@RequiresPermissions("goods:unit:goodsUnit:list")
	@RequestMapping(value = { "list", "" })
	public String list(GoodsUnit goodsUnit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsUnit> page = goodsUnitService.findPage(new Page<GoodsUnit>(request, response), goodsUnit);
		List<GoodsUnit> list = page.getList();
		for (GoodsUnit goodsUnit2 : list) {
			goodsUnit2.setCreateBy(systemService.getUser(goodsUnit2.getCreateBy().getId()));
			goodsUnit2.setUpdateBy(systemService.getUser(goodsUnit2.getUpdateBy().getId()));
		}
		model.addAttribute("page", page);
		return "modules/goods/unit/goodsUnitList";
	}

	/**
	 * 查看，增加，编辑单位管理表单页面
	 */
	@RequiresPermissions(value = { "goods:unit:goodsUnit:view", "goods:unit:goodsUnit:add",
			"goods:unit:goodsUnit:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(GoodsUnit goodsUnit, Model model) {
		model.addAttribute("goodsUnit", goodsUnit);
		return "modules/goods/unit/goodsUnitForm";
	}

	/**
	 * 保存单位管理
	 */
	@RequiresPermissions(value = { "goods:unit:goodsUnit:add", "goods:unit:goodsUnit:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(GoodsUnit goodsUnit, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, goodsUnit)) {
			return form(goodsUnit, model);
		}
		if (!goodsUnit.getIsNewRecord()) {// 编辑表单保存
			GoodsUnit t = goodsUnitService.get(goodsUnit.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(goodsUnit, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			goodsUnitService.save(t);// 保存
		} else {// 新增表单保存
			// 当前登录用户
			User currentuser = UserUtils.getUser();
			// 部门
			Office office = currentuser.getOffice();
			goodsUnit.setOffice(office);
			// 公司:公司的员工可以看到该公司的产品
			Office company = currentuser.getCompany();
			goodsUnit.setFsponsorid(company);
			goodsUnitService.save(goodsUnit);// 保存
		}
		addMessage(redirectAttributes, "保存单位管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/unit/goodsUnit/?repage";
	}

	/**
	 * 逻辑删除单位管理
	 */
	@RequiresPermissions("goods:unit:goodsUnit:del")
	@RequestMapping(value = "delete")
	public String delete(GoodsUnit goodsUnit, RedirectAttributes redirectAttributes) {
		goodsUnitService.deleteByLogic(goodsUnit);
		addMessage(redirectAttributes, "删除单位管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/unit/goodsUnit/?repage";
	}

	/**
	 * 批量逻辑删除单位管理
	 */
	@RequiresPermissions("goods:unit:goodsUnit:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			goodsUnitService.deleteByLogic(goodsUnitService.get(id));
		}
		addMessage(redirectAttributes, "删除单位管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/unit/goodsUnit/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goods:unit:goodsUnit:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(GoodsUnit goodsUnit, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "单位管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<GoodsUnit> page = goodsUnitService.findPage(new Page<GoodsUnit>(request, response, -1), goodsUnit);
			new ExportExcel("单位管理", GoodsUnit.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出单位管理记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/unit/goodsUnit/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("goods:unit:goodsUnit:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GoodsUnit> list = ei.getDataList(GoodsUnit.class);
			for (GoodsUnit goodsUnit : list) {
				try {
					goodsUnitService.save(goodsUnit);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条单位管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条单位管理记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入单位管理失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/unit/goodsUnit/?repage";
	}

	/**
	 * 下载导入单位管理数据模板
	 */
	@RequiresPermissions("goods:unit:goodsUnit:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "单位管理数据导入模板.xlsx";
			List<GoodsUnit> list = Lists.newArrayList();
			new ExportExcel("单位管理数据", GoodsUnit.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/unit/goodsUnit/?repage";
	}

}