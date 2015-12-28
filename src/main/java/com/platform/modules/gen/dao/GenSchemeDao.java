package com.platform.modules.gen.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.gen.bean.GenScheme;

/**
 * 生成方案DAO接口 
 * @author sunshine
 * @date 2013-10-15
 */
@MyBatisDao
public interface GenSchemeDao extends CrudDao<GenScheme> {
	
}
