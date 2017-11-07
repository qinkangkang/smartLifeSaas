/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.market.service.ordermarket;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.market.entity.ordermarket.OrderMarket;
import com.jeeplus.modules.goods.entity.spu.GoodsSpu;
import com.jeeplus.modules.goods.entity.color.Colors;
import com.jeeplus.modules.goods.entity.size.Size;
import com.jeeplus.modules.goods.entity.sku.GoodsSku;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.market.entity.ordermarket.OrderMarketDetail;
import com.jeeplus.modules.market.dao.ordermarket.OrderMarketDetailDao;

/**
 * 销售单详细Service
 * @author diqiang
 * @version 2017-06-19
 */
@Service
@Transactional(readOnly = true)
public class OrderMarketDetailService extends CrudService<OrderMarketDetailDao, OrderMarketDetail> {
	
	
	public OrderMarketDetail get(String id) {
		return super.get(id);
	}
	
	public List<OrderMarketDetail> findList(OrderMarketDetail orderMarketDetail) {
		return super.findList(orderMarketDetail);
	}
	
	public Page<OrderMarketDetail> findPage(Page<OrderMarketDetail> page, OrderMarketDetail orderMarketDetail) {
		return super.findPage(page, orderMarketDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderMarketDetail orderMarketDetail) {
		super.save(orderMarketDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderMarketDetail orderMarketDetail) {
		super.deleteByLogic(orderMarketDetail);
	}
	
	public Page<OrderMarket> findPageByorderMarket(Page<OrderMarket> page, OrderMarket orderMarket) {
		//权限过滤
		customDataScopeFilter(orderMarket,"dsf", "id=a.office_id", "id=a.create_by");
//		orderMarket.getSqlMap().put("dsf", dataScopeFilter(orderMarket.getCurrentUser(), "fsponsor", "createby"));
		orderMarket.setPage(page);
		page.setList(dao.findListByorderMarket(orderMarket));
		return page;
	}
	public Page<GoodsSpu> findPageBygoodsSpu(Page<GoodsSpu> page, GoodsSpu goodsSpu) {
		//权限过滤
		customDataScopeFilter(goodsSpu,"dsf", "id=a.office_id", "id=a.create_by");
//		goodsSpu.getSqlMap().put("dsf", dataScopeFilter(goodsSpu.getCurrentUser(), "fsponsor", "createby"));
		goodsSpu.setPage(page);
		page.setList(dao.findListBygoodsSpu(goodsSpu));
		return page;
	}
	public Page<GoodsSku> findPageBygoodsSku(Page<GoodsSku> page, GoodsSku goodsSku) {
		//权限过滤
//		customDataScopeFilter(goodsSku,"dsf", "id=a.office_id", "id=a.create_by");
//		goodsSku.getSqlMap().put("dsf", dataScopeFilter(goodsSku.getCurrentUser(), "fsponsor", "createby"));
		goodsSku.setPage(page);
		page.setList(dao.findListBygoodsSku(goodsSku));
		return page;
	}

	public Page<Colors> findPageBycolors(Page<Colors> page, Colors colors) {
		//权限过滤
//		customDataScopeFilter(colors,"dsf", "id=a.office_id", "id=a.create_by");
//		colors.getSqlMap().put("dsf", dataScopeFilter(colors.getCurrentUser(), "fsponsor", "createby"));
		colors.setPage(page);
		page.setList(dao.findListBycolors(colors));
		return page;
	}

	public Page<Size> findPageBySize(Page<Size> page, Size size) {
		//权限过滤
//		customDataScopeFilter(size,"dsf", "id=a.office_id", "id=a.create_by");
//		size.getSqlMap().put("dsf", dataScopeFilter(size.getCurrentUser(), "fsponsor", "createby"));
		size.setPage(page);
		page.setList(dao.findListBysize(size));
		return page;
	}
	
}