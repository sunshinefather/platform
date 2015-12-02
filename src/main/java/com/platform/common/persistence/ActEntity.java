package com.platform.common.persistence;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platform.modules.act.entity.Act;
/**
 * Activiti Entity
 * @ClassName:  ActEntity   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月26日 下午2:11:28
 * @param <T>
 */
public abstract class ActEntity<T> extends DataEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
    /**流程任务对象*/
	protected Act act;

	public ActEntity() {
		super();
	}
	
	public ActEntity(String id) {
		super(id);
	}
	
	@JsonIgnore
	public Act getAct() {
		if (act == null){
			act = new Act();
		}
		return act;
	}

	public void setAct(Act act) {
		this.act = act;
	}

	/**
	 * 获取流程实例ID
	 * @return
	 */
	public String getProcInsId() {
		return this.getAct().getProcInsId();
	}

	/**
	 * 设置流程实例ID
	 * @param procInsId
	 */
	public void setProcInsId(String procInsId) {
		this.getAct().setProcInsId(procInsId);
	}
}
