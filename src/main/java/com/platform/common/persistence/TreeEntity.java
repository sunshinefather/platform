package com.platform.common.persistence;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.platform.common.utils.StringUtils;
import com.platform.common.utils.reflection.ReflectionUtils;

/**
 * 树形Entity类 
 * @author sunshine
 */
public abstract class TreeEntity<T> extends DataEntity<T> {

	private static final long serialVersionUID = 1L;
	
    /**父级编号*/
	protected T parent;
	/**所有父级编号*/
	protected String parentIds;
	/**节点名称*/
	protected String name;
	/**所在层级*/
	protected Integer levelIndex;
	/**排序*/
	protected Integer sort;
	
	public TreeEntity() {
		super();
		this.sort = 30;
	}
	
	public TreeEntity(String id) {
		super(id);
	}
	
	@JsonBackReference
	@NotNull
	public abstract T getParent();

	public abstract void setParent(T parent);

	@Length(min=1, max=2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		String id = null;
		if (parent != null){
			id = (String)ReflectionUtils.getFieldValue(parent, "id");
		}
		return StringUtils.isNotBlank(id) ? id : "0";
	}

	public Integer getLevelIndex() {
		return levelIndex;
	}

	public void setLevelIndex(Integer levelIndex) {
		this.levelIndex = levelIndex;
	}		
}