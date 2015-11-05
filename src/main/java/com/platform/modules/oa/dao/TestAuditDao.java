
package com.platform.modules.oa.dao;

import com.platform.common.persistence.CrudDao;
import com.platform.common.persistence.annotation.MyBatisDao;
import com.platform.modules.oa.entity.TestAudit;

/**
 * 审批DAO接口 * @author sunshine
 * @version 2014-05-16
 */
@MyBatisDao
public interface TestAuditDao extends CrudDao<TestAudit> {

	public TestAudit getByProcInsId(String procInsId);
	
	public int updateInsId(TestAudit testAudit);
	
	public int updateHrText(TestAudit testAudit);
	
	public int updateLeadText(TestAudit testAudit);
	
	public int updateMainLeadText(TestAudit testAudit);
	
}
