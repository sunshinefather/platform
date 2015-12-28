package com.platform.modules.cms.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.cms.bean.Site;

/**
 * 站点DAO接口 
 * @author sunshine
 * @date 2013-8-23
 */
@MyBatisDao
public interface SiteDao extends CrudDao<Site> {

}
