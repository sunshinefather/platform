package com.platform.common.persistence.dialect.db;

import com.platform.common.persistence.dialect.Dialect;
/**
 * H2分页
 * @ClassName:  H2Dialect   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月23日 下午5:38:29
 */
public class H2Dialect implements Dialect {

    public boolean supportsLimit() {
        return true;
    }

    private String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
        return sql + ((offset > 0) ? " limit " + limitPlaceholder + " offset "
                + offsetPlaceholder : " limit " + limitPlaceholder);
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
    }
}