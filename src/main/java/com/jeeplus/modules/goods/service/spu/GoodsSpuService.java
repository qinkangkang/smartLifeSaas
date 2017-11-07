/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service.spu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.OddNumbers;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.goods.dao.sku.GoodsSkuDao;
import com.jeeplus.modules.goods.dao.spu.GoodsSpuDao;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 商品管理Service
 * 
 * @author maxiao
 * @version 2017-06-13
 */
@Service
@Transactional(readOnly = true)
public class GoodsSpuService extends CrudService<GoodsSpuDao, GoodsSpu> {

	@Autowired
	private GoodsSkuDao goodsSkuDao;

	public GoodsSpu get(String id) {
		GoodsSpu goodsSpu = super.get(id);
		goodsSpu.setGoodsSkuList(goodsSkuDao.findList(new GoodsSku(goodsSpu)));
		return goodsSpu;
	}

	public List<GoodsSpu> findList(GoodsSpu goodsSpu) {
		return super.findList(goodsSpu);
	}

	public Page<GoodsSpu> findPage(Page<GoodsSpu> page, GoodsSpu goodsSpu) {
		goodsSpu.getSqlMap().put("dsf", dataScopeFilter(goodsSpu.getCurrentUser(),"c","createby"));
		User currentuser = UserUtils.getUser();
		if(!"admin".equals(currentuser.getLoginName())){
			// 公司:公司的员工可以看到该公司的产品
			Office company = currentuser.getCompany();
			goodsSpu.setCompany(company);	
		}
		goodsSpu.setPage(page);
		page.setList(dao.findList(goodsSpu));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(GoodsSpu goodsSpu) {
		super.save(goodsSpu);
		for (GoodsSku goodsSku : goodsSpu.getGoodsSkuList()) {
			if (goodsSku.getId() == null) {
				continue;
			}
			if (GoodsSku.DEL_FLAG_NORMAL.equals(goodsSku.getDelFlag())) {
				if (StringUtils.isBlank(goodsSku.getId())) {
					goodsSku.setGoodsSpu(goodsSpu);
					goodsSku.preInsert();
					goodsSku.setFgoodsnumber("HH"+OddNumbers.getOrderNo());
					goodsSkuDao.insert(goodsSku);
				} else {
					goodsSku.preUpdate();
					goodsSkuDao.update(goodsSku);
				}
			} else {
				goodsSkuDao.delete(goodsSku);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(GoodsSpu goodsSpu) {
		super.delete(goodsSpu);
		goodsSkuDao.delete(new GoodsSku(goodsSpu));
	}

	public Page<GoodsUnit> findPageBygoodsUnit(Page<GoodsUnit> page, GoodsUnit goodsUnit) {
		goodsUnit.setPage(page);
		page.setList(dao.findListBygoodsUnit(goodsUnit));
		return page;
	}

	public Page<Brand> findPageBybrand(Page<Brand> page, Brand brand) {
		brand.setPage(page);
		page.setList(dao.findListBybrand(brand));
		return page;
	}

	public Page<Categorys> findPageBycategorys(Page<Categorys> page, Categorys categorys) {
		categorys.setPage(page);
		page.setList(dao.findListBycategorys(categorys));
		return page;
	}

}