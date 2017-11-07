/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service.sku;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.dao.sku.GoodsSkuDao;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 商品详情Service
 * 
 * @author maxiao
 * @version 2017-06-07
 */
@Service
@Transactional(readOnly = true)
public class GoodsSkuService extends CrudService<GoodsSkuDao, GoodsSku> {

	public GoodsSku get(String id) {
		return super.get(id);
	}

	public List<GoodsSku> findList(GoodsSku goodsSku) {
		return super.findList(goodsSku);
	}

	public Page<GoodsSku> findPage(Page<GoodsSku> page, GoodsSku goodsSku) {
		return super.findPage(page, goodsSku);
	}

	@Transactional(readOnly = false)
	public void save(GoodsSku goodsSku) {
		super.save(goodsSku);
	}

	@Transactional(readOnly = false)
	public void delete(GoodsSku goodsSku) {
		super.deleteByLogic(goodsSku);
	}

	public Page<Colors> findPageBycolors(Page<Colors> page, Colors colors) {
		colors.setPage(page);
		page.setList(dao.findListBycolors(colors));
		return page;
	}

	public Page<Size> findPageBysize(Page<Size> page, Size size) {
		size.setPage(page);
		page.setList(dao.findListBysize(size));
		return page;
	}

	/**
	 * 查找无fbarcode条形码的商品详情列表
	 * 
	 * @return
	 */
	public List<GoodsSku> findBarcodeIsNullCount() {
		return dao.findBarcodeIsNullCount();
	}

	/**
	 * 为无条码商品插入条形码
	 * 
	 * @param goodsSku
	 */
	@Transactional(readOnly = false)
	public void updateBarcode(GoodsSku goodsSku) {
		dao.updateBarcode(goodsSku);
	}

	public List<GoodsSku> findBarcodeIsNullCountByCompany(String id) {
		// TODO Auto-generated method stub
		return dao.findBarcodeIsNullCountByCompany(id);
	}

}