package com.platform.modules.gen.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.gen.bean.GenTable;

/**
 * 业务表DAO接口 
 * @author sunshine
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTableDao extends CrudDao<GenTable> {
	
}
