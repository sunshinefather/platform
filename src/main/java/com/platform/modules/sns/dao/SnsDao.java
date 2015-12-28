package com.platform.modules.sns.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.sns.bean.Sns;

/**
 * 圈子DAO接口
 * @author sunshine
 * @date 2015-12-25
 */
@MyBatisDao
public interface SnsDao extends CrudDao<Sns> {
	
}