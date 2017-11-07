/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.customer.web.basic;


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
import com.jeeplus.modules.customer.entity.basic.CustomerBasic;
import com.jeeplus.modules.customer.entity.category.CustomerCate;
import com.jeeplus.modules.customer.entity.info.CustomerInfo;
import com.jeeplus.modules.customer.service.basic.CustomerBasicService;
import com.jeeplus.modules.customer.service.category.CustomerCateService;
import com.jeeplus.modules.customer.service.info.CustomerInfoService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 客户基本信息Controller
 * @author diqiang
 * @version 2017-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/customer/basic/customerBasic")
public class CustomerBasicController extends BaseController {
	/**
	 * 客户信息service
	 */
	@Autowired
	private CustomerBasicService customerBasicService;
	/**
	 * 客户分类service
	 */
	@Autowired
	private CustomerCateService customerCateService;
	/**
	 * 系统管理service
	 */
	@Autowired
	private SystemService systemService;
	
	/**
	 * 线上用户service
	 */
	@Autowired
	private CustomerInfoService customerInfoService;
	
	
	@ModelAttribute
	public CustomerBasic get(@RequestParam(required=false) String id) {
		CustomerBasic entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerBasicService.get(id);
		}
		if (entity == null){
			entity = new CustomerBasic();
		}
		return entity;
	}
	
	/**
	 * 客户基本信息列表页面
	 */
	@RequiresPermissions("customer:basic:customerBasic:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerBasic customerBasic, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerBasic> page = customerBasicService.findPage(new Page<CustomerBasic>(request, response), customerBasic); 
		
		List<CustomerBasic> list = page.getList();
		
		if(list!=null&&list.size()>0){
			
			for(CustomerBasic basic:list){
				CustomerCate cate = customerCateService.get(basic.getFcategoryId());
				if(cate!=null){
					basic.setFcatename(cate.getFname());
				}
				
				
				basic.setCreateBy(systemService.getUser(basic.getCreateBy().getId()));
				
				basic.setUpdateBy(systemService.getUser(basic.getUpdateBy().getId()));
			}
			
			
		}
		
		CustomerCate cate=new CustomerCate();
		List<CustomerCate> customerCateList = customerCateService.findAllList(cate);
		model.addAttribute("customerCateList",customerCateList);//客户所有分类数据传到前段
		
		model.addAttribute("page", page);
		return "modules/customer/basic/customerBasicList";
	}

	/**
	 * 查看，增加，编辑客户基本信息表单页面
	 */
	@RequiresPermissions(value={"customer:basic:customerBasic:add","customer:basic:customerBasic:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CustomerBasic customerBasic, Model model) {
		
		CustomerCate cate=new CustomerCate();
		List<CustomerCate> customerCateList = customerCateService.findAllList(cate);
		
		model.addAttribute("customerCateList",customerCateList); //客户所有分类数据传到前段
		
		model.addAttribute("customerBasic", customerBasic);
		return "modules/customer/basic/customerBasicForm";
	}

	/**
	 * 保存客户基本信息
	 */
	@RequiresPermissions(value={"customer:basic:customerBasic:add","customer:basic:customerBasic:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CustomerBasic customerBasic, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		if (!beanValidator(model, customerBasic)){
			return form(customerBasic, model);
		}
		//验证手机格式
		String cellphone = customerBasic.getFcellphone();
		
		boolean matches = cellphone.matches("^1[3|7|5|8][0-9]\\d{4,8}$");
		if(!matches){
			addMessage(redirectAttributes, "保存客户基本信息失败，手机号格式不正确！！！");
			return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
		}
		
		if(!customerBasic.getIsNewRecord()){//编辑表单保存
			CustomerBasic t = customerBasicService.get(customerBasic.getId());//从数据库取出记录的值
			
			String getfCellphone2 = t.getFcellphone();
			String getfCellphone3 = customerBasic.getFcellphone();
			//如果手机号变更则查询手机号是否已被注册
			if(!getfCellphone2.equals(getfCellphone3)){
				//创建线上信息对象
				CustomerInfo info2=new CustomerInfo();
				
				CustomerBasic findByCellphone = customerBasicService.findByCellphone(customerBasic);//通过手机号查询信息
				
				if(findByCellphone!=null){
					addMessage(redirectAttributes, "修改信息失败，该手机号已经存在！！！");//如果手机号已存在则客户信息已存储过
					return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
				}
				//通过原有手机号查询线上信息对象
				info2.setFcellphone(getfCellphone2);
				CustomerInfo customerInfo =null;
				try {
					customerInfo=customerInfoService.findByCellphone(info2);
					//解除信息关联
					if(customerInfo!=null){
						customerInfo.setFisrelate(0);
						customerInfo.setFcustomerbasicid(null);
//						customerInfo.setCustomerBasic(null);
						customerInfoService.save(customerInfo);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			
			MyBeanUtils.copyBeanNotNull2Bean(customerBasic, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			
			CustomerCate cate = customerCateService.get(customerBasic.getFcategoryId());
			
			if(cate!=null){
				t.setFcatename(cate.getFname());
				t.setFdiscount(cate.getFdiscount()); //设置对应分类折扣
			}
			//获取修改后手机号
			String getfCellphone = t.getFcellphone();
			//通过手机号查询线上用户
			CustomerInfo info=new CustomerInfo();
			info.setFcellphone(getfCellphone);
			CustomerInfo infoByCellphone = customerInfoService.findByCellphone(info);
			//如果线上用户存在则建立关联信息
			if(infoByCellphone!=null){
				t.setFisrelate(1);
				t.setFcustomerinfo(infoByCellphone.getId());
				infoByCellphone.setFisrelate(1);
				infoByCellphone.setFcustomerbasicid(t.getId());
				customerInfoService.save(infoByCellphone);
			}else{
				t.setFisrelate(0);
				t.setFcustomerinfo(null);
			}
			
			customerBasicService.save(t);//保存
		}else{//新增表单保存
			CustomerBasic findByCellphone = customerBasicService.findByCellphone(customerBasic);//通过手机号查询信息
			
						if(findByCellphone!=null){
							addMessage(redirectAttributes, "保存客户基本信息失败，该用户手机已经存在！！！");//如果手机号已存在则客户信息已存储过
							return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
						}

			//通过分类ID查询是否有分类
			CustomerCate cate = customerCateService.get(customerBasic.getFcategoryId());
			//设置对应分类折扣
			if(cate!=null){
				customerBasic.setFdiscount(cate.getFdiscount()); 
				customerBasic.setFcatename(cate.getFname());
			}
			//获取手机号
			String getfCellphone = customerBasic.getFcellphone();
			//通过手机号查询线上用户
			CustomerInfo info=new CustomerInfo();
			info.setFcellphone(getfCellphone);
			CustomerInfo infoByCellphone = customerInfoService.findByCellphone(info);
			//如果线上用户存在则建立关联信息
			if(infoByCellphone!=null){
				customerBasic.setFisrelate(1);
				customerBasic.setFcustomerinfo(infoByCellphone.getId());
//				customerBasic.setCustomerInfo(infoByCellphone);
				
			}else{
				customerBasic.setFisrelate(0);
				customerBasic.setFcustomerinfo(null);
//				customerBasic.setCustomerInfo(null);
			}
			//获取当前登录用户
			//并获取当前用户所属商户，设置给对象
			User user = UserUtils.getUser();
			Office company = user.getCompany();
			Office office = user.getOffice();
			customerBasic.setFsponsor(company);
			customerBasic.setFstore(office);
	
			customerBasicService.save(customerBasic);//保存
			
			if(infoByCellphone!=null&&customerBasic.getFisrelate()==1){
				infoByCellphone.setFisrelate(1);
				infoByCellphone.setFcustomerbasicid(customerBasic.getId());
				customerInfoService.save(infoByCellphone);
			}
		}
		addMessage(redirectAttributes, "保存客户基本信息成功");
		return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
	}
	
	/**
	 * 删除客户基本信息
	 */
	@RequiresPermissions("customer:basic:customerBasic:del")
	@RequestMapping(value = "delete")
	public String delete(CustomerBasic customerBasic, RedirectAttributes redirectAttributes) {
		CustomerInfo info = findInfo(customerBasic);
		customerBasicService.delete(customerBasic);
		/**
		 * 修改关联账号
		 */
		CustomerInfo infoByCellphone = customerInfoService.findByCellphone(info);
		if(infoByCellphone!=null){
			infoByCellphone.setFisrelate(0);
			infoByCellphone.setFcustomerbasicid(null);
			customerInfoService.save(infoByCellphone);
		}
		
		addMessage(redirectAttributes, "删除客户基本信息成功");
		return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
	}
	
	/**
	 * 批量删除客户基本信息
	 */
	@RequiresPermissions("customer:basic:customerBasic:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			CustomerBasic basic = customerBasicService.get(id);
			CustomerInfo info = findInfo(basic);
			customerBasicService.delete(basic);
			/**
			 * 修改关联账号
			 */
			CustomerInfo infoByCellphone = customerInfoService.findByCellphone(info);
			if(infoByCellphone!=null){
				infoByCellphone.setFisrelate(0);
				infoByCellphone.setFcustomerbasicid(null);
				customerInfoService.save(infoByCellphone);
			}
		}
		addMessage(redirectAttributes, "删除客户基本信息成功");
		return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("customer:basic:customerBasic:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(CustomerBasic customerBasic, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerBasic> page = customerBasicService.findPage(new Page<CustomerBasic>(request, response, -1), customerBasic);
    		new ExportExcel("客户信息", CustomerBasic.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出客户基本信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("customer:basic:customerBasic:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerBasic> list = ei.getDataList(CustomerBasic.class);
			StringBuilder builder=new StringBuilder();
			for (CustomerBasic customerBasic : list){
				try{
					
					
					CustomerBasic basic = customerBasicService.findUniqueByProperty("fcellphone", customerBasic.getFcellphone());
					if(basic!=null){
						
						builder.append(basic.getFcellphone()+";");
						
						failureNum++;
					}else {
						customerBasicService.save(customerBasic);
						successNum++;	
					}
					
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户基本信息记录,请检查"+builder+"手机号是否已存在。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户基本信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户基本信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
    }
	
	/**
	 * 下载导入客户基本信息数据模板
	 */
	@RequiresPermissions("customer:basic:customerBasic:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户基本信息数据导入模板.xlsx";
    		List<CustomerBasic> list = Lists.newArrayList(); 
    		new ExportExcel("客户基本信息数据", CustomerBasic.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/customer/basic/customerBasic/?repage";
    }
	
	
	
	/**
	 * 查看客户基本信息表单页面
	 */
	@RequiresPermissions(value={"customer:basic:customerBasic:view"},logical=Logical.OR)
	@RequestMapping(value = "view")
	public String view(CustomerBasic customerBasic, Model model) {
		
		CustomerCate cate=new CustomerCate();
		List<CustomerCate> customerCateList = customerCateService.findAllList(cate);
		
		model.addAttribute("customerCateList",customerCateList); //客户所有分类数据传到前段
//		String cellphone = customerBasic.getFCellphone();
//		String customerinfoId = customerBasic.getCustomerinfoId();
//		CustomerInfo info=new CustomerInfo();
//		info.setFcellphone(cellphone);
//		CustomerInfo customerInfo = customerInfoService.findByCellphone(info);
//		if(customerInfo!=null&&customerInfo.getId().equals(customerinfoId)){
//			customerBasic.setCustomerInfo(info);
//			customerBasic.setCustomerInfoName(info.getFusername());
//		}
		model.addAttribute("customerBasic", customerBasic);
		return "modules/customer/basic/customerBasicView";
	}
	
	/**
	 * 通过手机获取线上用户
	 * @param customerBasic
	 * @return
	 */
	public CustomerInfo findInfo(CustomerBasic customerBasic){
		CustomerBasic findByCellphone = customerBasicService.findByCellphone(customerBasic);
		CustomerInfo info=null;
		if(findByCellphone!=null){
			info=new CustomerInfo();
			info.setFcellphone(findByCellphone.getFcellphone());
			return info;
		}else{
			return new CustomerInfo();
		}
		
	}
	

}