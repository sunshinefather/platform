package com.platform.modules.test.dao;

import com.platform.common.persistence.TreeDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.test.bean.TestTree;

/**
 * 树结构生成DAO接口 
 * @author sunshine
 * @date 2015-04-06
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}