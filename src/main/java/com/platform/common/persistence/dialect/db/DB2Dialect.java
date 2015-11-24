package com.platform.common.persistence.dialect.db;

import com.platform.common.persistence.dialect.Dialect;
/**
 * DB2分页
 * @ClassName:  DB2Dialect   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月23日 下午5:24:33
 */
public class DB2Dialect implements Dialect {
    @Override
    public boolean supportsLimit() {
        return true;
    }

    private static String getRowNumber(String sql) {
        StringBuilder rownumber = new StringBuilder(50)
                .append("rownumber() over(");

        int orderByIndex = sql.toLowerCase().indexOf("order by");

        if (orderByIndex > 0 && !hasDistinct(sql)) {
            rownumber.append(sql.substring(orderByIndex));
        }

        rownumber.append(") as rownumber_,");

        return rownumber.toString();
    }

    private static boolean hasDistinct(String sql) {
        return sql.toLowerCase().contains("select distinct");
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
    }
    
    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        int startOfSelect = sql.toLowerCase().indexOf("select");

        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100)
                .append(sql.substring(0, startOfSelect)) 
                .append("select * from ( select ")
                .append(getRowNumber(sql)); 

        if (hasDistinct(sql)) {
            pagingSelect.append(" row_.* from ( ") 
                    .append(sql.substring(startOfSelect))
                    .append(" ) as row_");
        } else {
            pagingSelect.append(sql.substring(startOfSelect + 6));
        }

        pagingSelect.append(" ) as temp_ where rownumber_ ");

        if (offset > 0) {

            String endString = offsetPlaceholder + "+" + limitPlaceholder;
            pagingSelect.append("between ").append(offsetPlaceholder)
                    .append("+1 and ").append(endString);
        } else {
            pagingSelect.append("<= ").append(limitPlaceholder);
        }

        return pagingSelect.toString();
    }
}
