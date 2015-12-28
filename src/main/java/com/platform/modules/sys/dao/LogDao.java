package com.platform.modules.sys.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.sys.bean.Log;

/**
 * 日志DAO接口 
 * @author sunshine
 * @date 2014-05-16
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

}
