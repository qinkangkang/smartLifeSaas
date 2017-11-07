/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.store.web.storemanage;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.store.entity.storemanage.Store;
import com.jeeplus.modules.store.service.storemanage.StoreService;

/**
 * 门店管理Controller
 * @author 金圣智
 * @version 2017-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/store/storemanage/store")
public class StoreController extends BaseController {

	@Autowired
	private StoreService storeService;
	
	@ModelAttribute
	public Store get(@RequestParam(required=false) String id) {
		Store entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = storeService.get(id);
		}
		if (entity == null){
			entity = new Store();
		}
		return entity;
	}
	
	/**
	 * 门店信息列表页面
	 */
	@RequiresPermissions("store:storemanage:store:list")
	@RequestMapping(value = {"list", ""})
	public String list(Store store, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Store> page = storeService.findPage(new Page<Store>(request, response), store); 
		model.addAttribute("page", page);
		return "modules/store/storemanage/storeList";
	}

	/**
	 * 查看，增加，编辑门店信息表单页面
	 */
	@RequiresPermissions(value={"store:storemanage:store:view","store:storemanage:store:add","store:storemanage:store:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Store store, Model model) {
		model.addAttribute("store", store);
		return "modules/store/storemanage/storeForm";
	}

	/**
	 * 保存门店信息
	 */
	@RequiresPermissions(value={"store:storemanage:store:add","store:storemanage:store:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Store store, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, store)){
			return form(store, model);
		}
		if(!store.getIsNewRecord()){//编辑表单保存
			Store t = storeService.get(store.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(store, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			storeService.save(t);//保存
		}else{//新增表单保存
			storeService.save(store);//保存
		}
		addMessage(redirectAttributes, "保存门店信息成功");
		return "redirect:"+Global.getAdminPath()+"/store/storemanage/store/?repage";
	}
	
	/**
	 * 逻辑删除门店信息
	 */
	@RequiresPermissions("store:storemanage:store:del")
	@RequestMapping(value = "delete")
	public String delete(Store store, RedirectAttributes redirectAttributes) {
		storeService.deleteByLogic(store);
		addMessage(redirectAttributes, "删除门店信息成功");
		return "redirect:"+Global.getAdminPath()+"/store/storemanage/store/?repage";
	}
	
	/**
	 * 批量逻辑删除门店信息
	 */
	@RequiresPermissions("store:storemanage:store:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			storeService.deleteByLogic(storeService.get(id));
		}
		addMessage(redirectAttributes, "删除门店信息成功");
		return "redirect:"+Global.getAdminPath()+"/store/storemanage/store/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("store:storemanage:store:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Store store, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "门店信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Store> page = storeService.findPage(new Page<Store>(request, response, -1), store);
    		new ExportExcel("门店信息", Store.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出门店信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/storemanage/store/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("store:storemanage:store:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Store> list = ei.getDataList(Store.class);
			for (Store store : list){
				try{
					storeService.save(store);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条门店信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条门店信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入门店信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/storemanage/store/?repage";
    }
	
	/**
	 * 下载导入门店信息数据模板
	 */
	@RequiresPermissions("store:storemanage:store:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "门店信息数据导入模板.xlsx";
    		List<Store> list = Lists.newArrayList(); 
    		new ExportExcel("门店信息数据", Store.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/storemanage/store/?repage";
    }
	
	
	

}