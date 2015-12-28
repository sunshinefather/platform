package com.platform.modules.sys.dao;

import com.platform.common.persistence.TreeDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.sys.bean.Office;

/**
 * 机构DAO接口 
 * @author sunshine
 * @date 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
}
