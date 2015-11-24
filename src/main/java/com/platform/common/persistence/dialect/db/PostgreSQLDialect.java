package com.platform.common.persistence.dialect.db;

import com.platform.common.persistence.dialect.Dialect;
/**
 * Postgre Sql分页
 * @ClassName:  PostgreSQLDialect   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月23日 下午5:39:53
 */
public class PostgreSQLDialect implements Dialect {

    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset),
                Integer.toString(limit));
    }
    
    public String getLimitString(String sql, int offset,
                                 String offsetPlaceholder, String limitPlaceholder) {
        StringBuilder pageSql = new StringBuilder().append(sql);
        pageSql = offset <= 0
                ? pageSql.append(" limit ").append(limitPlaceholder) :
                pageSql.append(" limit ").append(limitPlaceholder).append(" offset ").append(offsetPlaceholder);
        return pageSql.toString();
    }
}
