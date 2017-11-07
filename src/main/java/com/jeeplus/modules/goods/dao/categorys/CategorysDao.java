/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.dao.categorys;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goods.entity.categorys.Categorys;

/**
 * 商品的分类DAO接口
 * @author maxiao
 * @version 2017-06-07
 */
@MyBatisDao
public interface CategorysDao extends TreeDao<Categorys> {
	
}