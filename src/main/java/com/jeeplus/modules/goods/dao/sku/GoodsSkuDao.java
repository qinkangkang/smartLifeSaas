/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.dao.sku;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 商品管理DAO接口
 * 
 * @author maxiao
 * @version 2017-06-13
 */
@MyBatisDao
public interface GoodsSkuDao extends CrudDao<GoodsSku> {

	public List<Colors> findListBycolors(Colors colors);

	public List<Size> findListBysize(Size size);

	public List<GoodsSku> findBarcodeIsNullCount();

	public void updateBarcode(GoodsSku goodsSku);

	public List<GoodsSku> findBarcodeIsNullCountByCompany(String id);

}