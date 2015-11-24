package com.platform.common.persistence.dialect;

/**
 * Dialect
 * @ClassName:  Dialect   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月23日 下午5:21:11
 */
public interface Dialect {

    /**
     * 支持当前的分页查询方式
     * @return 是否支持
     */
    public boolean supportsLimit();

    /**
     * 将sql转换为分页SQL
     * @param sql    SQL语句
     * @param offset 开始条数
     * @param limit  每页显示多少纪录条数
     * @return 分页查询的sql
     */
    public String getLimitString(String sql, int offset, int limit);

}
