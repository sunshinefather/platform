
package com.platform.modules.test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.common.service.CrudService;
import com.platform.modules.test.entity.Test;
import com.platform.modules.test.dao.TestDao;

/**
 * 测试Service * @author sunshine
 * @version 2013-10-17
 */
@Service
@Transactional(readOnly = true)
public class TestService extends CrudService<TestDao, Test> {

}
