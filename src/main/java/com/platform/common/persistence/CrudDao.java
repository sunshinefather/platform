package com.platform.common.persistence;

import java.util.List;
/**
 * DAO层接口
 * @ClassName:  CrudDao   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月26日 上午11:59:56
 * @param <T>
 */
public interface CrudDao<T> {

	/**
	 * 获取单条数据
	 * @param id
	 */
	public T get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 */
	public T get(T entity);
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
	 * @param entity
	 */
	public List<T> findList(T entity);
	
	/**
	 * 插入数据
	 * @param entity
	 */
	public int insert(T entity);
	
	/**
	 * 更新数据
	 * @param entity
	 */
	public int update(T entity);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param id
	 * @see public int delete(T entity)
	 * @return
	 */
	@Deprecated
	public int delete(String id);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param entity
	 */
	public int delete(T entity);
	
}