/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.delivery.dao.express;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.delivery.entity.express.Express;
import com.jeeplus.modules.test.entity.grid.Category;

/**
 * 物流信息DAO接口
 * @author qkk
 * @version 2017-06-01
 */
@MyBatisDao
public interface ExpressDao extends CrudDao<Express> {

	public int deleteByStatus(Express express);
	
}