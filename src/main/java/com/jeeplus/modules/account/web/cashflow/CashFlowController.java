/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web.cashflow;

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
import com.jeeplus.modules.account.entity.cashflow.CashFlow;
import com.jeeplus.modules.account.service.cashflow.CashFlowService;

/**
 * 收款流水(线上)Controller
 * @author 金圣智
 * @version 2017-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/account/cashflow/cashFlow")
public class CashFlowController extends BaseController {

	@Autowired
	private CashFlowService cashFlowService;
	
	@ModelAttribute
	public CashFlow get(@RequestParam(required=false) String id) {
		CashFlow entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cashFlowService.get(id);
		}
		if (entity == null){
			entity = new CashFlow();
		}
		return entity;
	}
	
	/**
	 * 收款流水列表页面
	 */
	@RequiresPermissions("account:cashflow:cashFlow:list")
	@RequestMapping(value = {"list", ""})
	public String list(CashFlow cashFlow, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CashFlow> page = cashFlowService.findPage(new Page<CashFlow>(request, response), cashFlow); 
		model.addAttribute("page", page);
		return "modules/account/cashflow/cashFlowList";
	}

	/**
	 * 查看，增加，编辑收款流水表单页面
	 */
	@RequiresPermissions(value={"account:cashflow:cashFlow:view","account:cashflow:cashFlow:add","account:cashflow:cashFlow:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CashFlow cashFlow, Model model) {
		model.addAttribute("cashFlow", cashFlow);
		return "modules/account/cashflow/cashFlowForm";
	}

	/**
	 * 保存收款流水
	 */
	@RequiresPermissions(value={"account:cashflow:cashFlow:add","account:cashflow:cashFlow:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CashFlow cashFlow, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, cashFlow)){
			return form(cashFlow, model);
		}
		if(!cashFlow.getIsNewRecord()){//编辑表单保存
			CashFlow t = cashFlowService.get(cashFlow.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(cashFlow, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			cashFlowService.save(t);//保存
		}else{//新增表单保存
			cashFlowService.save(cashFlow);//保存
		}
		addMessage(redirectAttributes, "保存收款流水成功");
		return "redirect:"+Global.getAdminPath()+"/account/cashflow/cashFlow/?repage";
	}
	
	/**
	 * 删除收款流水
	 */
	@RequiresPermissions("account:cashflow:cashFlow:del")
	@RequestMapping(value = "delete")
	public String delete(CashFlow cashFlow, RedirectAttributes redirectAttributes) {
		cashFlowService.delete(cashFlow);
		addMessage(redirectAttributes, "删除收款流水成功");
		return "redirect:"+Global.getAdminPath()+"/account/cashflow/cashFlow/?repage";
	}
	
	/**
	 * 批量删除收款流水
	 */
	@RequiresPermissions("account:cashflow:cashFlow:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			cashFlowService.delete(cashFlowService.get(id));
		}
		addMessage(redirectAttributes, "删除收款流水成功");
		return "redirect:"+Global.getAdminPath()+"/account/cashflow/cashFlow/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:cashflow:cashFlow:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(CashFlow cashFlow, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "收款流水"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CashFlow> page = cashFlowService.findPage(new Page<CashFlow>(request, response, -1), cashFlow);
    		new ExportExcel("收款流水", CashFlow.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出收款流水记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/cashflow/cashFlow/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:cashflow:cashFlow:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CashFlow> list = ei.getDataList(CashFlow.class);
			for (CashFlow cashFlow : list){
				try{
					cashFlowService.save(cashFlow);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条收款流水记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条收款流水记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入收款流水失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/cashflow/cashFlow/?repage";
    }
	
	/**
	 * 下载导入收款流水数据模板
	 */
	@RequiresPermissions("account:cashflow:cashFlow:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "收款流水数据导入模板.xlsx";
    		List<CashFlow> list = Lists.newArrayList(); 
    		new ExportExcel("收款流水数据", CashFlow.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/cashflow/cashFlow/?repage";
    }
	
	
	

}