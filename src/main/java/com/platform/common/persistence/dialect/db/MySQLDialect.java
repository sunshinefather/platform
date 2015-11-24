package com.platform.common.persistence.dialect.db;

import com.platform.common.persistence.dialect.Dialect;
/**
 * Mysql分页
 * @ClassName:  MySQLDialect   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月23日 下午5:36:10
 */
public class MySQLDialect implements Dialect {


    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset),
                Integer.toString(limit));
    }

    public boolean supportsLimit() {
        return true;
    }

    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        StringBuilder stringBuilder = new StringBuilder(sql);
		if (stringBuilder.lastIndexOf(";") == stringBuilder.length() - 1) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
        stringBuilder.append(" limit ");
        if (offset > 0) {
            stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
        } else {
            stringBuilder.append(limitPlaceholder);
        }
        return stringBuilder.toString();
    }

}
