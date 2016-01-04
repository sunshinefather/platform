package com.platform.modules.sns.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.sns.bean.Sns;

/**
 * @Description 圈子DAO接口
 * @author sunshine
 * @date 2016-01-04 17:58:14
 */
@MyBatisDao
public interface SnsDao extends CrudDao<Sns> {
	
}