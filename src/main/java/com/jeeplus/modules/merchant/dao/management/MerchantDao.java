/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.merchant.dao.management;

import com.jeeplus.modules.sys.entity.Area;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.merchant.entity.management.Merchant;

/**
 * 商户管理DAO接口
 * @author diqiang
 * @version 2017-06-23
 */
@MyBatisDao
public interface MerchantDao extends CrudDao<Merchant> {

	public List<Area> findListByarea(Area area);
	
}