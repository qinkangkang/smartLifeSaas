/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.color;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
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
import com.jeeplus.modules.goods.service.color.ColorsService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 颜色管理Controller
 * 
 * @author maxiao
 * @version 2017-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/color/colors")
public class ColorsController extends BaseController {

	@Autowired
	private ColorsService colorsService;

	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public Colors get(@RequestParam(required = false) String id) {
		Colors entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = colorsService.get(id);
		}
		if (entity == null) {
			entity = new Colors();
		}
		return entity;
	}

	/**
	 * 颜色管理列表页面
	 */
	@RequiresPermissions("goods:color:colors:list")
	@RequestMapping(value = { "list", "" })
	public String list(Colors colors, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Colors> page = colorsService.findPage(new Page<Colors>(request, response), colors);
		List<Colors> list = page.getList();
		for (Colors colors2 : list) {
			colors2.setCreateBy(systemService.getUser(colors2.getCreateBy().getId()));
			colors2.setUpdateBy(systemService.getUser(colors2.getUpdateBy().getId()));
		}
		model.addAttribute("page", page);
		return "modules/goods/color/colorsList";
	}

	/**
	 * 查看，增加，编辑颜色管理表单页面
	 */
	@RequiresPermissions(value = { "goods:color:colors:view", "goods:color:colors:add",
			"goods:color:colors:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Colors colors, Model model) {
		model.addAttribute("colors", colors);
		return "modules/goods/color/colorsForm";
	}

	/**
	 * 保存颜色管理
	 */
	@RequiresPermissions(value = { "goods:color:colors:add", "goods:color:colors:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Colors colors, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, colors)) {
			return form(colors, model);
		}
		if (!colors.getIsNewRecord()) {// 编辑表单保存
			Colors t = colorsService.get(colors.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(colors, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			colorsService.save(t);// 保存
		} else {// 新增表单保存
			// 当前登录用户
			User currentuser = UserUtils.getUser();
			if (currentuser.getLoginName() != "admin") {
				// 部门
				Office office = currentuser.getOffice();
				colors.setOffice(office);
				// 公司:公司的员工可以看到该公司的产品
				Office company = currentuser.getCompany();
				colors.setFsponsorid(company);
			}

			colorsService.save(colors);// 保存
		}
		addMessage(redirectAttributes, "保存颜色管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/color/colors/?repage";
	}

	/**
	 * 逻辑删除颜色管理
	 */
	@RequiresPermissions("goods:color:colors:del")
	@RequestMapping(value = "delete")
	public String delete(Colors colors, RedirectAttributes redirectAttributes) {
		colorsService.deleteByLogic(colors);
		addMessage(redirectAttributes, "删除颜色管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/color/colors/?repage";
	}

	/**
	 * 批量逻辑删除颜色管理
	 */
	@RequiresPermissions("goods:color:colors:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			colorsService.deleteByLogic(colorsService.get(id));
		}
		addMessage(redirectAttributes, "删除颜色管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/color/colors/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goods:color:colors:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Colors colors, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "颜色管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";

			// 解决excel的命名乱码问题:需区分不同浏览器的不同编码方式
			String agent = request.getHeader("User-agent");// 获取请求头
			String codeFilename = FileUtils.encodeDownloadFilename(fileName, agent);
			ServletOutputStream outputStream = response.getOutputStream();
			String mimeType = request.getServletContext().getMimeType(fileName);
			response.setContentType(mimeType);
			response.setHeader("content-disposition", "attachment;filename=" + codeFilename);
			Page<Colors> page = colorsService.findPage(new Page<Colors>(request, response, -1), colors);
			ExportExcel excel = new ExportExcel("颜色管理", Colors.class).setDataList(page.getList());
			excel.write(outputStream);
			excel.dispose();

			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出颜色管理记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/color/colors/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("goods:color:colors:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Colors> list = ei.getDataList(Colors.class);
			for (Colors colors : list) {
				try {
					colorsService.save(colors);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条颜色管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条颜色管理记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入颜色管理失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/color/colors/?repage";
	}

	/**
	 * 下载导入颜色管理数据模板
	 */
	@RequiresPermissions("goods:color:colors:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		try {
			String fileName = "颜色管理数据导入模板.xlsx";
			List<Colors> list = Lists.newArrayList();
			// 解决excel的命名乱码问题:需区分不同浏览器的不同编码方式
			String agent = request.getHeader("User-agent");// 获取请求头
			String codeFilename = FileUtils.encodeDownloadFilename(fileName, agent);
			ServletOutputStream outputStream = response.getOutputStream();
			String mimeType = request.getServletContext().getMimeType(fileName);
			response.setContentType(mimeType);
			response.setHeader("content-disposition", "attachment;filename=" + codeFilename);
			ExportExcel excel = new ExportExcel("颜色管理数据", Colors.class).setDataList(list);
			excel.write(outputStream);
			excel.dispose();// 清除临时文件
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/goods/color/colors/?repage";
	}

}