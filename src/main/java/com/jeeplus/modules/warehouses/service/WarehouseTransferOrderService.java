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
import com.jeeplus.modules.warehouses.entity.WarehouseTransferOrder;
import com.jeeplus.modules.warehouses.dao.WarehouseTransferOrderDao;

import com.jeeplus.modules.warehouses.entity.Warehouse;
import com.jeeplus.modules.warehouses.entity.WarehouseGoodsInfo;
import com.jeeplus.modules.warehouses.entity.WarehouseRecord;
import com.jeeplus.modules.warehouses.entity.WarehouseTransferGoods;
import com.jeeplus.modules.goods.entity.brand.Brand;
import com.jeeplus.modules.goods.entity.categorys.Categorys;
import com.jeeplus.modules.goods.entity.unit.GoodsUnit;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.warehouses.dao.WarehouseTransferGoodsDao;

/**
 * 仓库调拨Service
 * 
 * @author hxting
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class WarehouseTransferOrderService extends CrudService<WarehouseTransferOrderDao, WarehouseTransferOrder> {

	@Autowired
	private WarehouseTransferGoodsDao warehouseTransferGoodsDao;

	@Autowired
	private WarehouseGoodsInfoService warehouseGoodsInfoService;
	
	@Autowired
	private WarehouseRecordService warehouseRecordService;
	
	@Autowired
	private WarehouseService warehouseService;
	

	public WarehouseTransferOrder get(String id) {
		WarehouseTransferOrder warehouseTransferOrder = super.get(id);
		warehouseTransferOrder.setWarehouseTransferGoodsList(
				warehouseTransferGoodsDao.findList(new WarehouseTransferGoods(warehouseTransferOrder)));
		return warehouseTransferOrder;
	}

	public List<WarehouseTransferOrder> findList(WarehouseTransferOrder warehouseTransferOrder) {
		return super.findList(warehouseTransferOrder);
	}

	public Page<WarehouseTransferOrder> findPage(Page<WarehouseTransferOrder> page,
			WarehouseTransferOrder warehouseTransferOrder) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		warehouseTransferOrder.getSqlMap().put("dsf", dataScopeFilter(warehouseTransferOrder.getCurrentUser(), "o", "createby"));
		return super.findPage(page, warehouseTransferOrder);
	}

	@Transactional(readOnly = false)
	public void save(WarehouseTransferOrder warehouseTransferOrder) {
		User currentUser = warehouseTransferOrder.getCurrentUser();
		if(!currentUser.isAdmin()){
			Office office = currentUser.getOffice();
			Office company = currentUser.getCompany();
			warehouseTransferOrder.setCompany(company);
			warehouseTransferOrder.setOffice(office);
		}
		
		super.save(warehouseTransferOrder);
		for (WarehouseTransferGoods wtg : warehouseTransferOrder.getWarehouseTransferGoodsList()) {
			if (wtg.getId() == null) {
				continue;
			}
			if (WarehouseTransferGoods.DEL_FLAG_NORMAL.equals(wtg.getDelFlag())) {
				if (StringUtils.isBlank(wtg.getId())) {
					wtg.setTransferOrder(warehouseTransferOrder);
					wtg.preInsert();
					warehouseTransferGoodsDao.insert(wtg);
				} else {
					wtg.preUpdate();
					warehouseTransferGoodsDao.update(wtg);
				}
				//调拨数量
				Integer transferNum = wtg.getTransferNum();
				
				
				if(WarehouseTransferOrder.TRANSFER_STATUS_DRAFT.equals(warehouseTransferOrder.getFstatus())){
					//调出仓库商品信息
					if(wtg.getWarehouseOutGoods() != null){
						WarehouseGoodsInfo warehouseOutGoods = wtg.getWarehouseOutGoods();
						warehouseOutGoods.setFinventory(warehouseOutGoods.getFinventory() - transferNum);
						warehouseOutGoods.setFlockinventory(transferNum);
						warehouseOutGoods.setFtotalinventory(warehouseOutGoods.getFinventory());
						warehouseGoodsInfoService.save(warehouseOutGoods);
					}
				}
				
				if(WarehouseTransferOrder.TRANSFER_STATUS_TRANSFER.equals(warehouseTransferOrder.getFstatus())){
					//调出仓库商品信息
					if(wtg.getWarehouseOutGoods() != null){
						WarehouseGoodsInfo warehouseOutGoods = wtg.getWarehouseOutGoods();
						warehouseOutGoods.setFlockinventory(warehouseOutGoods.getFlockinventory()-transferNum);
						warehouseGoodsInfoService.save(warehouseOutGoods);
						
						WarehouseRecord wr = new WarehouseRecord();
						wr.setWarehouse(warehouseTransferOrder.getWarehouseOut());
						wr.setGoodsSpu(warehouseOutGoods.getGoodsSpu());
						wr.setGoodsSku(warehouseOutGoods.getGoodsSku());
						wr.setBusinessTime(warehouseTransferOrder.getFtransferDate());
						wr.setBusinessType(WarehouseRecord.BUSINESS_TYPE_TRANSFER);
						wr.setBusinessorderNumber(warehouseTransferOrder.getFtransferOrder());
						wr.setChangeNum(transferNum);
						wr.setRemainingNum(warehouseOutGoods.getFtotalinventory() - transferNum);	
						warehouseRecordService.save(wr);
						
					}
					//调入仓库商品信息
					if(wtg.getWarehouseInGoods() != null){
						WarehouseGoodsInfo warehouseInGoods = wtg.getWarehouseInGoods();
						warehouseInGoods.setFinventory(warehouseInGoods.getFinventory() + transferNum);
						warehouseInGoods.setFtotalinventory(warehouseInGoods.getFtotalinventory() + transferNum);
						warehouseGoodsInfoService.save(warehouseInGoods);
						
						WarehouseRecord wr = new WarehouseRecord();
						wr.setWarehouse(warehouseTransferOrder.getFwarehouseIn());
						wr.setGoodsSpu(warehouseInGoods.getGoodsSpu());
						wr.setGoodsSku(warehouseInGoods.getGoodsSku());
						wr.setBusinessTime(warehouseTransferOrder.getFtransferDate());
						wr.setBusinessType(WarehouseRecord.BUSINESS_TYPE_TRANSFER);
						wr.setBusinessorderNumber(warehouseTransferOrder.getFtransferOrder());
						wr.setChangeNum(transferNum);
						wr.setRemainingNum(warehouseInGoods.getFtotalinventory() + transferNum);	
						warehouseRecordService.save(wr);
					}else{
						if(wtg.getWarehouseOutGoods() != null){
							WarehouseGoodsInfo warehouseInGoods = wtg.getWarehouseOutGoods();
							warehouseInGoods.setId("");
							warehouseInGoods.setFinventory(transferNum);
							warehouseInGoods.setFtotalinventory(transferNum);
							warehouseInGoods.setWarehouse(warehouseTransferOrder.getFwarehouseIn());
							warehouseGoodsInfoService.save(warehouseInGoods);
							
							WarehouseRecord wr = new WarehouseRecord();
							wr.setWarehouse(warehouseTransferOrder.getFwarehouseIn());
							wr.setGoodsSpu(warehouseInGoods.getGoodsSpu());
							wr.setGoodsSku(warehouseInGoods.getGoodsSku());
							wr.setBusinessTime(warehouseTransferOrder.getFtransferDate());
							wr.setBusinessType(WarehouseRecord.BUSINESS_TYPE_TRANSFER);
							wr.setBusinessorderNumber(warehouseTransferOrder.getFtransferOrder());
							wr.setChangeNum(transferNum);
							wr.setRemainingNum(warehouseInGoods.getFtotalinventory() + transferNum);	
							warehouseRecordService.save(wr);
						}
					}
				}
			} else {
				warehouseTransferGoodsDao.delete(wtg);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(WarehouseTransferOrder warehouseTransferOrder) {
		super.delete(warehouseTransferOrder);
		warehouseTransferGoodsDao.delete(new WarehouseTransferGoods(warehouseTransferOrder));
	}

	public Page<Warehouse> findPageBywarehouseOut(Page<Warehouse> page, Warehouse warehouseOut) {
		return warehouseService.findPage(page, warehouseOut);
	}

	public Page<Warehouse> findPageByfwarehouseIn(Page<Warehouse> page, Warehouse fwarehouseIn) {
		
		return warehouseService.findPage(page, fwarehouseIn);
	}

	public int findCountByType() {
		
		return dao.findCountByType();
	}

	@Transactional(readOnly = false)
	public void revoke(WarehouseTransferOrder warehouseTransferOrder) {
		warehouseTransferOrder.setFstatus(WarehouseTransferOrder.TRANSFER_STATUS_BACKOUT);
		if(warehouseTransferOrder.getId() != null){
			WarehouseTransferGoods entity = new WarehouseTransferGoods();
			entity.setTransferOrder(warehouseTransferOrder);
			List<WarehouseTransferGoods> list = warehouseTransferGoodsDao.findList(entity);
			if(list != null && list.size() > 0){
				for (WarehouseTransferGoods wtg : list) {
					Integer transferNum = wtg.getTransferNum();
				}
				
			}
		}
		
		super.save(warehouseTransferOrder);
		
		
	}

}