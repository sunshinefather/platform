package com.platform.common.persistence;

import java.util.List;
/**
 * TreeDAO
 * @ClassName:  TreeDao   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月26日 下午2:07:06
 * @param <T>
 */
public interface TreeDao<T extends TreeEntity<T>> extends CrudDao<T> {

	/**
	 * 找到所有子节点
	 * @param entity
	 * @return
	 */
	public List<T> findByParentIdsLike(T entity);

	/**
	 * 更新所有父节点字段
	 * @param entity
	 * @return
	 */
	public int updateParentIds(T entity);
}