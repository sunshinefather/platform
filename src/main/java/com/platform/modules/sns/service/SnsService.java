package com.platform.modules.sns.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.platform.common.persistence.Page;
import com.platform.common.service.CrudService;
import com.platform.modules.sns.bean.Sns;
import com.platform.modules.sns.dao.SnsDao;

/**
 * 圈子Service
 * @author sunshine
 * @date 2016-01-04 17:58:14
 */
@Service
@Transactional(readOnly = true)
public class SnsService extends CrudService<SnsDao, Sns> {

	public Sns get(String id) {
		return super.get(id);
	}
	
	public List<Sns> findList(Sns sns) {
		return super.findList(sns);
	}
	
	public Page<Sns> findPage(Page<Sns> page, Sns sns) {
		return super.findPage(page, sns);
	}
	
	@Transactional(readOnly = false)
	public void save(Sns sns) {
		super.save(sns);
	}
	
	@Transactional(readOnly = false)
	public void delete(Sns sns) {
		super.delete(sns);
	}
	
}