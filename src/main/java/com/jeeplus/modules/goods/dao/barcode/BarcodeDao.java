/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.dao.barcode;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goods.entity.barcode.Barcode;

/**
 * 生成条形码DAO接口
 * @author maxiao
 * @version 2017-06-12
 */
@MyBatisDao
public interface BarcodeDao extends CrudDao<Barcode> {

	
}