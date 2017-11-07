/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warehouses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.warehouses.entity.WarehouseCheckOrder;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.dao.WarehouseCheckOrderDao;

import com.jeeplus.modules.warehouses.entity.Warehouse;

import com.jeeplus.modules.warehouses.entity.WarehouseCheckGoods;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.warehouses.dao.WarehouseCheckGoodsDao;

/**
 * 仓库盘点Service
 * 
 * @author hxting
 * @version 2017-06-19
 */
@Service
@Transactional(readOnly = true)
public class WarehouseCheckOrderService extends CrudService<WarehouseCheckOrderDao, WarehouseCheckOrder> {

	@Autowired
	private WarehouseCheckGoodsDao warehouseCheckGoodsDao;

	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;
	@Autowired
	private WarehouseRecordService warehouseRecordService;
	@Autowired
	private WarehouseService warehouseService;

	public WarehouseCheckOrder get(String id) {
		WarehouseCheckOrder warehouseCheckOrder = super.get(id);
		warehouseCheckOrder.setWarehouseCheckGoodsList(
				warehouseCheckGoodsDao.findList(new WarehouseCheckGoods(warehouseCheckOrder)));
		return warehouseCheckOrder;
	}

	public List<WarehouseCheckOrder> findList(WarehouseCheckOrder warehouseCheckOrder) {
		return super.findList(warehouseCheckOrder);
	}

	public Page<WarehouseCheckOrder> findPage(Page<WarehouseCheckOrder> page, WarehouseCheckOrder warehouseCheckOrder) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		warehouseCheckOrder.getSqlMap().put("dsf",
				dataScopeFilter(warehouseCheckOrder.getCurrentUser(), "o", "createby"));
		return super.findPage(page, warehouseCheckOrder);
	}

	@Transactional(readOnly = false)
	public void save(WarehouseCheckOrder warehouseCheckOrder) {
		User currentUser = warehouseCheckOrder.getCurrentUser();
		if (!currentUser.isAdmin()) {
			Office office = currentUser.getOffice();
			Office company = currentUser.getCompany();
			warehouseCheckOrder.setCompany(company);
			warehouseCheckOrder.setOffice(office);
		}
		super.save(warehouseCheckOrder);
		for (WarehouseCheckGoods wcg : warehouseCheckOrder.getWarehouseCheckGoodsList()) {
			if (WarehouseCheckGoods.DEL_FLAG_NORMAL.equals(wcg.getDelFlag())) {
				if (StringUtils.isBlank(wcg.getId())) {
					wcg.setCheckOrder(warehouseCheckOrder);
					wcg.preInsert();
					warehouseCheckGoodsDao.insert(wcg);
				} else {
					wcg.preUpdate();
					warehouseCheckGoodsDao.update(wcg);
				}
				// 盘点状态
				if (WarehouseCheckOrder.CHECK_STATUS_CHECK.equals(warehouseCheckOrder.getFstatus())) {
					if (wcg.getGoodsSpu() != null && wcg.getGoodsSku() != null) {
						WarehouseGoodsInfo w = new WarehouseGoodsInfo();
						w.setGoodsSpu(wcg.getGoodsSpu());
						w.setGoodsSku(wcg.getGoodsSku());
						w.setWarehouse(warehouseCheckOrder.getWarehouse());
						List<WarehouseGoodsInfo> findList = warehouseGoodsInfoService.findList(w);
						if (findList != null && findList.size() > 0) {
							WarehouseGoodsInfo warehouseGoodsInfo = findList.get(0);
							// 盘点数量
							Integer checkNum = wcg.getCheckNum();
							// 商品锁定数量
							Integer flockinventory = warehouseGoodsInfo.getFlockinventory();
							// 盘点前商品总量
							Integer ftotalinventory = warehouseGoodsInfo.getFtotalinventory();

							// 盘点后可用商品数量
							warehouseGoodsInfo.setFinventory(checkNum - flockinventory);
							// 盘点后商品总量
							warehouseGoodsInfo.setFtotalinventory(checkNum);

							warehouseGoodsInfo.setFtotalinventory(checkNum);
							warehouseGoodsInfoService.save(warehouseGoodsInfo);
							// 添加流水
							WarehouseRecord wr = new WarehouseRecord();
							wr.setWarehouse(warehouseCheckOrder.getWarehouse());
							wr.setGoodsSpu(wcg.getGoodsSpu());
							wr.setGoodsSku(wcg.getGoodsSku());
							wr.setBusinessTime(warehouseCheckOrder.getCheckDate());
							wr.setBusinessType(WarehouseRecord.BUSINESS_TYPE_CHECK);
							wr.setBusinessorderNumber(warehouseCheckOrder.getFcheckOrder());
							wr.setChangeNum(ftotalinventory - checkNum);
							wr.setRemainingNum(checkNum);
							warehouseRecordService.save(wr);

						}

					}
				}

			} else {
				warehouseCheckGoodsDao.delete(wcg);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(WarehouseCheckOrder warehouseCheckOrder) {
		super.delete(warehouseCheckOrder);
		warehouseCheckGoodsDao.delete(new WarehouseCheckGoods(warehouseCheckOrder));
	}

	public Page<Warehouse> findPageBywarehouse(Page<Warehouse> page, Warehouse warehouse) {
		return warehouseService.findPage(page, warehouse);
	}

	/**
	 * 逻辑删除数据
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void deleteByLogic(WarehouseCheckOrder warehouseCheckOrder) {
		super.deleteByLogic(warehouseCheckOrder);
		warehouseCheckGoodsDao.deleteByLogic(new WarehouseCheckGoods(warehouseCheckOrder));
	}

}