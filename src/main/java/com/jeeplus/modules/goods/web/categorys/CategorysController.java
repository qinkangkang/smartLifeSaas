/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.categorys;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.service.categorys.CategorysService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 商品的分类Controller
 * 
 * @author maxiao
 * @version 2017-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/categorys/categorys")
public class CategorysController extends BaseController {

	@Autowired
	private CategorysService categorysService;

	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public Categorys get(@RequestParam(required = false) String id) {
		Categorys entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = categorysService.get(id);
		}
		if (entity == null) {
			entity = new Categorys();
		}
		return entity;
	}

	/**
	 * 商品分类的管理列表页面
	 */
	@RequiresPermissions("goods:categorys:categorys:list")
	@RequestMapping(value = { "list", "" })
	public String list(Categorys categorys, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Categorys> list = categorysService.findList(categorys);
		for (Categorys categorys2 : list) {
			categorys2.setCreateBy(systemService.getUser(categorys2.getCreateBy().getId()));
			categorys2.setUpdateBy(systemService.getUser(categorys2.getUpdateBy().getId()));
		}
		model.addAttribute("list", list);
		return "modules/goods/categorys/categorysList";
	}

	/**
	 * 查看，增加，编辑商品分类的管理表单页面
	 */
	@RequiresPermissions(value = { "goods:categorys:categorys:view", "goods:categorys:categorys:add",
			"goods:categorys:categorys:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Categorys categorys, Model model) {
		if (categorys.getParent() != null && StringUtils.isNotBlank(categorys.getParent().getId())) {
			categorys.setParent(categorysService.get(categorys.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(categorys.getId())) {
				Categorys categorysChild = new Categorys();
				categorysChild.setParent(new Categorys(categorys.getParent().getId()));
				List<Categorys> list = categorysService.findList(categorys);
				if (list.size() > 0) {
					categorys.setSort(list.get(list.size() - 1).getSort());
					if (categorys.getSort() != null) {
						categorys.setSort(categorys.getSort() + 30);
					}
				}
			}
		}
		if (categorys.getSort() == null) {
			categorys.setSort(30);
		}
		model.addAttribute("categorys", categorys);
		return "modules/goods/categorys/categorysForm";
	}

	/**
	 * 保存商品分类的管理
	 */
	@RequiresPermissions(value = { "goods:categorys:categorys:add",
			"goods:categorys:categorys:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Categorys categorys, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, categorys)) {
			return form(categorys, model);
		}
		if (!categorys.getIsNewRecord()) {// 编辑表单保存
			Categorys t = categorysService.get(categorys.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(categorys, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			categorysService.save(t);// 保存
		} else {// 新增表单保存
			User currentuser = UserUtils.getUser();
			// 部门
		if(currentuser.getLoginName()!="admin")	{
			Office office = currentuser.getOffice();
			categorys.setOffice(office);
			// 公司:公司的员工可以看到该公司的
			Office company = currentuser.getCompany();
			categorys.setFsponsorId(company);
		}
			categorysService.save(categorys);// 保存
		}
		addMessage(redirectAttributes, "保存商品分类的管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/categorys/categorys/?repage";
	}

	/**
	 * 删除商品分类的管理
	 */
	@RequiresPermissions("goods:categorys:categorys:del")
	@RequestMapping(value = "delete")
	public String delete(Categorys categorys, RedirectAttributes redirectAttributes) {
		categorysService.deleteByLogic(categorys);
		addMessage(redirectAttributes, "删除商品分类的管理成功");
		return "redirect:" + Global.getAdminPath() + "/goods/categorys/categorys/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Categorys> list = categorysService.findList(new Categorys());
		for (int i = 0; i < list.size(); i++) {
			Categorys e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

}