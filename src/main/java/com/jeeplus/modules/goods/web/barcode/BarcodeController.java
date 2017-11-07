/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web.barcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.goods.entity.barcode.Barcode;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.service.barcode.BarcodeService;
import com.jeeplus.modules.goods.service.sku.GoodsSkuService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 生成条形码Controller
 * 
 * @author maxiao
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/barcode/barcode")
public class BarcodeController extends BaseController {

	@Resource
	private BarcodeService barcodeService;

	@Resource
	private GoodsSkuService skuService;

	@ModelAttribute
	public Barcode get(@RequestParam(required = false) String id) {
		Barcode entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = barcodeService.get(id);
		}
		if (entity == null) {
			entity = new Barcode();
		}
		return entity;
	}

	/**
	 * 跳转到生成条形码表单页面
	 */
	@RequiresPermissions(value = { "goods:barcode:barcode:list" })
	@RequestMapping(value = "list")
	public String form(Barcode barcode, Model model) {
		return "modules/goods/barcode/barcode";
	}

	/**
	 * 為无条形码的商品生成条形码
	 */
	@ResponseBody
	@RequiresPermissions(value = { "goods:barcode:barcode:create" })
	@RequestMapping(value = "create")
	public String create(String fstrCode, String fproductCode, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		String flag="1";//默认1:代表生成商品条码成功;0代表失败,2代表不存在无条码的商品
		try {
			ByteArrayOutputStream outputStream = null;
			BufferedImage bi = null;
			JBarcode productBarcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(),
					EAN13TextPainter.getInstance());
			// 尺寸，面积，大小
			productBarcode.setXDimension(0.4);
			// 高度 10.0 = 1cm 默认1.5cm
			productBarcode.setBarHeight(12.2);

			// 首先当前用户所属商家的所有商品中没有条形码的sku
			User currentuser = UserUtils.getUser();
			List<GoodsSku> skuList =null;
			if(!"admin".equals(currentuser.getLoginName())){
				Office company = currentuser.getCompany();
				skuList= skuService.findBarcodeIsNullCountByCompany(company.getId());
			}else{
				 skuList = skuService.findBarcodeIsNullCount();
			}
			if (skuList == null || skuList.size() <= 0) {
				flag="2";//flag=2代表无没有条码的商品
			} else if (skuList != null && skuList.size() > 0) {
				// for循环,为无条形码的商品插入条形码
				for (GoodsSku goodsSku : skuList) {
					// fproductCode转成整数+j再转成字符串
					int code = Integer.valueOf(fproductCode);
					fproductCode = String.valueOf(code + 1);
					if (fproductCode.length() < 4) {
						for (int k = 0; k <= 4 - fproductCode.length() + 1; k++) {
							fproductCode = "0".concat(fproductCode);
						}
					}
					String jbarCode = fstrCode.concat(fproductCode);
					
					// 条形码的第十三位数字
					// 第一步：取出该数的奇数位的和: 第二步：取出该数的偶数位的和：
					int c1 = 0;// 奇数位的和
					int c2 = 0;// 偶数位的和
					// i=0,2,4,6,8,10 奇数位的值
					// i+1 1,3,5,7,9,11
					// 取奇数、偶数
					for (int i = 0; i < jbarCode.length(); i += 2) {
						// 奇数位值
						char c = jbarCode.charAt(i);
						// 奇数位的和
						c1 = c1 + c - 48;
						// 偶数位的值
						char c3 = jbarCode.charAt(i + 1);
						// 偶数位的和
						c2 = c2 + c3 - 48;
					}
					// 计算奇数位值的和
					int cc = c1 + c2 * 3; // 110
					// 去结果的个位数
					cc %= 10;
					// 用十减去这个个位数
					cc = 10 - cc;
					cc = cc % 10;

					// 条形码=输入的条形码+系统生成的校验码
					String barcode = jbarCode.concat(String.valueOf(cc));
					// 把条形码插入sku中
					goodsSku.setFbarcode(barcode);
					skuService.updateBarcode(goodsSku);

					// 生成条形码 图片
					/*
					 * bi = productBarcode.createBarcode(jbarCode); //
					 * 传入字符串长度为12为，最后一位检验码由系统按照前12位数生成 outputStream = new
					 * ByteArrayOutputStream(); ImageIO.write(bi, "jpg",
					 * outputStream); byte[] b = outputStream.toByteArray();
					 * response.getOutputStream().write(b);
					 */
					// BASE64Encoder encoder = new BASE64Encoder();
					// return encoder.encode(outputStream.toByteArray());

				}
			}

		} catch (Exception e) {
			flag="0";
			e.printStackTrace();
		}
		return  flag;
	}

}