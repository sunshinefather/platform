package com.platform.modules.cms.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.cms.bean.Comment;

/**
 * 评论DAO接口 
 * @author sunshine
 * @date 2013-8-23
 */
@MyBatisDao
public interface CommentDao extends CrudDao<Comment> {

}
