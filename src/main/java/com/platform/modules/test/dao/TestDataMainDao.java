package com.platform.modules.test.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.test.bean.TestDataMain;

/**
 * 主子表生成DAO接口 
 * @author sunshine
 * @date 2015-04-06
 */
@MyBatisDao
public interface TestDataMainDao extends CrudDao<TestDataMain> {
	
}