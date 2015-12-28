package com.platform.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.common.service.CrudService;
import com.platform.modules.cms.bean.ArticleData;
import com.platform.modules.cms.dao.ArticleDataDao;

/**
 * 站点Service 
 * @author sunshine
 * @date 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {

}
