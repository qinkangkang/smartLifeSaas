/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.size;

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
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.service.size.SizeService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 尺寸设置Controller
 * 
 * @author maxiao
 * @version 2017-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/size/size")
public class SizeController extends BaseController {

	@Autowired
	private SizeService sizeService;

	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public Size get(@RequestParam(required = false) String id) {
		Size entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = sizeService.get(id);
		}
		if (entity == null) {
			entity = new Size();
		}
		return entity;
	}

	/**
	 * 尺寸管理列表页面
	 */
	@RequiresPermissions("goods:size:size:list")
	@RequestMapping(value = { "list", "" })
	public String list(Size size, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Size> page = sizeService.findPage(new Page<Size>(request, response), size);
		List<Size> list = page.getList();
		for (Size size2 : list) {
			size2.setCreateBy(systemService.getUser(size2.getCreateBy().getId()));
			size2.setUpdateBy(systemService.getUser(size2.getUpdateBy().getId()));
		}
		model.addAttribute("page", page);
		return "modules/goods/size/sizeList";
	}

	/**
	 * 查看，增加，编辑尺寸管理表单页面
	 */
	@RequiresPermissions(value = { "goods:size:size:view", "goods:size:size:add",
			"goods:size:size:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Size size, Model model) {
		model.addAttribute("size", size);
		return "modules/goods/size/sizeForm";
	}

	/**
	 * 保存尺寸管理
	 */
	@RequiresPermissions(value = { "goods:size:size:add", "goods:size:size:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Size size, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, size)) {
			return form(size, model);
		}
		if (!size.getIsNewRecord()) {// 编辑表单保存
			Size t = sizeService.get(size.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(size, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			sizeService.save(t);// 保存
		} else {// 新增表单保存
			
			// 当前登录用户
			User currentuser = UserUtils.getUser();
			if(currentuser.getLoginName()!="admin"){
				// 部门
				Office office = currentuser.getOffice();
				size.setOffice(office);
				// 公司:公司的员工可以看到该公司的产品
				Office company = currentuser.getCompany();
				size.setFsponsorid(company);	
			}
			
			sizeService.save(size);// 保存
		}
		addMessage(redirectAttributes, "保存尺寸管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/size/size/?repage";
	}

	/**
	 * 逻辑删除尺寸管理
	 */
	@RequiresPermissions("goods:size:size:del")
	@RequestMapping(value = "delete")
	public String delete(Size size, RedirectAttributes redirectAttributes) {
		sizeService.deleteByLogic(size);
		addMessage(redirectAttributes, "删除尺寸管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/size/size/?repage";
	}

	/**
	 * 批量逻辑删除尺寸管理
	 */
	@RequiresPermissions("goods:size:size:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			sizeService.deleteByLogic(sizeService.get(id));
		}
		addMessage(redirectAttributes, "删除尺寸管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/size/size/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goods:size:size:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Size size, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "尺寸管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Size> page = sizeService.findPage(new Page<Size>(request, response, -1), size);
			new ExportExcel("尺寸管理", Size.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出尺寸管理记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/size/size/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("goods:size:size:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Size> list = ei.getDataList(Size.class);
			for (Size size : list) {
				try {
					sizeService.save(size);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条尺寸管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条尺寸管理记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入尺寸管理失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/size/size/?repage";
	}

	/**
	 * 下载导入尺寸管理数据模板
	 */
	@RequiresPermissions("goods:size:size:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "尺寸管理数据导入模板.xlsx";
			List<Size> list = Lists.newArrayList();
			new ExportExcel("尺寸管理数据", Size.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/size/size/?repage";
	}

}