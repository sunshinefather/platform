package com.platform.test.dao;

import com.platform.common.persistence.TreeDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.test.bean.TestTree;

/**
 * 树结构生成DAO接口 
 * @author sunshine
 * @version 2015-04-06
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}