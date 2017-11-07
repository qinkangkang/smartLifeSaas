/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.dao.spu;

import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;

/**
 * 商品管理DAO接口
 * @author maxiao
 * @version 2017-06-13
 */
@MyBatisDao
public interface GoodsSpuDao extends CrudDao<GoodsSpu> {

	public List<GoodsUnit> findListBygoodsUnit(GoodsUnit goodsUnit);
	public List<Brand> findListBybrand(Brand brand);
	public List<Categorys> findListBycategorys(Categorys categorys);
	
}