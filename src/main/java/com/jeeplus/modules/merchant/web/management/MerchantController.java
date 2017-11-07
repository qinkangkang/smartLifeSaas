/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.merchant.web.management;

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

import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.service.AreaService;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.merchant.entity.management.Merchant;
import com.jeeplus.modules.merchant.service.management.MerchantService;

/**
 * 商户管理Controller
 * @author diqiang
 * @version 2017-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/merchant/management/merchant")
public class MerchantController extends BaseController {

	@Autowired
	private MerchantService merchantService;
	
	//区域service
	@Autowired
	private AreaService areaService;
	
	@ModelAttribute
	public Merchant get(@RequestParam(required=false) String id) {
		Merchant entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = merchantService.get(id);
		}
		if (entity == null){
			entity = new Merchant();
		}
		return entity;
	}
	
	/**
	 * 商户管理列表页面
	 */
	@RequiresPermissions("merchant:management:merchant:list")
	@RequestMapping(value = {"list", ""})
	public String list(Merchant merchant, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Merchant> page = merchantService.findPage(new Page<Merchant>(request, response), merchant); 
		model.addAttribute("page", page);
		return "modules/merchant/management/merchantList";
	}

	/**
	 * 查看，增加，编辑商户管理表单页面
	 */
	@RequiresPermissions(value={"merchant:management:merchant:view","merchant:management:merchant:add","merchant:management:merchant:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Merchant merchant, Model model) {
		model.addAttribute("merchant", merchant);
		return "modules/merchant/management/merchantForm";
	}

	/**
	 * 保存商户管理
	 */
	@RequiresPermissions(value={"merchant:management:merchant:add","merchant:management:merchant:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Merchant merchant, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, merchant)){
			return form(merchant, model);
		}
		if(!merchant.getIsNewRecord()){//编辑表单保存
			Merchant t = merchantService.get(merchant.getId());//从数据库取出记录的值
			
			try {
				Merchant findUniqueByProperty = merchantService.findUniqueByProperty("fname", merchant.getFname());
				String id1 = t.getId();
				String id2 = findUniqueByProperty.getId();
				if(!id1.equals(id2)){
					throw new Exception();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				addMessage(redirectAttributes, "修改商户失败，商户名重复！！！");
				return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
			}
			
			MyBeanUtils.copyBeanNotNull2Bean(merchant, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			merchantService.save(t);//保存
		}else{//新增表单保存
			//获取区域id并设置区域编号
			Area area = merchant.getArea();
			Area area2 = areaService.get(area.getId());
			if(area2!=null){
				
				merchant.setCode(area2.getCode());
			}
			
			try {
				//通过商户名判断商户是否已存在
				String fname = merchant.getFname();
				Merchant findUniqueByProperty = merchantService.findUniqueByProperty("fname", fname);
				if(findUniqueByProperty!=null){
					addMessage(redirectAttributes, "保存商户失败，该商户已存在！！！");
					return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				addMessage(redirectAttributes, "保存商户失败，未知错误！！！");
				return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
			}
			
			merchantService.save(merchant);//保存
		}
		addMessage(redirectAttributes, "保存商户成功");
		return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
	}
	
	/**
	 * 逻辑删除商户管理
	 */
	@RequiresPermissions("merchant:management:merchant:del")
	@RequestMapping(value = "delete")
	public String delete(Merchant merchant, RedirectAttributes redirectAttributes) {
		merchantService.deleteByLogic(merchant);
		addMessage(redirectAttributes, "删除商户管理成功");
		return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
	}
	
	/**
	 * 批量逻辑删除商户管理
	 */
	@RequiresPermissions("merchant:management:merchant:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			merchantService.deleteByLogic(merchantService.get(id));
		}
		addMessage(redirectAttributes, "删除商户管理成功");
		return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("merchant:management:merchant:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Merchant merchant, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "商户管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Merchant> page = merchantService.findPage(new Page<Merchant>(request, response, -1), merchant);
    		new ExportExcel("商户管理", Merchant.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出商户管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("merchant:management:merchant:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Merchant> list = ei.getDataList(Merchant.class);
			for (Merchant merchant : list){
				try{
					merchantService.save(merchant);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商户管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条商户管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入商户管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
    }
	
	/**
	 * 下载导入商户管理数据模板
	 */
	@RequiresPermissions("merchant:management:merchant:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "商户管理数据导入模板.xlsx";
    		List<Merchant> list = Lists.newArrayList(); 
    		new ExportExcel("商户管理数据", Merchant.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/merchant/management/merchant/?repage";
    }
	
	
	/**
	 * 选择所属区域
	 */
	@RequestMapping(value = "selectarea")
	public String selectarea(Area area, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Area> page = merchantService.findPageByarea(new Page<Area>(request, response),  area);
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
		model.addAttribute("obj", area);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}