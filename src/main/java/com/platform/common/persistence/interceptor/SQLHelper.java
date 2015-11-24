package com.platform.common.persistence.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;

import com.platform.common.config.Global;
import com.platform.common.persistence.Page;
import com.platform.common.persistence.dialect.Dialect;
import com.platform.common.utils.Reflections;
import com.platform.common.utils.StringUtils;
/**
 * SQL工具类
 * @ClassName:  SQLHelper   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月24日 上午10:34:15
 */
public class SQLHelper {
	
    /**
     * 对SQL预编译参数赋值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     * @Title: setParameters
     * @Description: TODO  
     * @param: @param ps
     * @param: @param mappedStatement
     * @param: @param boundSql
     * @param: @param parameterObject
     * @param: @throws SQLException      
     * @return: void
     * @author: sunshine  
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null :
                    configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    @SuppressWarnings("rawtypes")
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }


    /**
     * 查询总纪录数
     * @Title: getCount
     * @Description: TODO  
     * @param: @param sql
     * @param: @param mappedStatement
     * @param: @param parameterObject
     * @param: @param boundSql
     * @param: @param log
     * @param: @return
     * @param: @throws SQLException      
     * @return: int
     * @author: sunshine  
     * @throws
     */
    public static int getCount(final String sql,final MappedStatement mappedStatement, final Object parameterObject,
    							final BoundSql boundSql, Logger log) throws SQLException {
    	String dbName = Global.getConfig("jdbc.type");
		final String countSql;
		if("oracle".equals(dbName)){
			countSql = "select count(1) from (" + sql + ") tmp_count";
		}else{
			countSql = "select count(1) from (" + removeOrders(sql) + ") tmp_count";
		}
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	if (log.isDebugEnabled()) {
                log.debug("COUNT SQL: " + StringUtils.replaceEach(countSql, new String[]{"\n","\t"}, new String[]{" "," "}));
            }
        	conn = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
        	ps = conn.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), parameterObject);
			if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
				MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
				Reflections.setFieldValue(countBS, "metaParameters", mo);
			}
            SQLHelper.setParameters(ps, mappedStatement, countBS, parameterObject);
            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
            	ps.close();
            }
            if (conn != null) {
            	conn.close();
            }
        }
    }
    /**
     * 生成分页sql
     * @Title: generatePageSql
     * @Description: TODO  
     * @param: @param sql
     * @param: @param page
     * @param: @param dialect
     * @param: @return      
     * @return: String
     * @author: sunshine  
     * @throws
     */
    public static String generatePageSql(String sql, Page<Object> page, Dialect dialect) {
        if (dialect.supportsLimit()) {
            return dialect.getLimitString(sql, page.getFirstResult(), page.getMaxResults());
        } else {
            return sql;
        }
    }
    
    /** 
     * 去除sql的select子句。 
     * @param sql 
     * @return 
     */  
    @SuppressWarnings("unused")
	private static String removeSelect(String sql){  
        int beginPos = sql.toLowerCase().indexOf("from");  
        return sql.substring(beginPos);  
    }  
      
    /** 
     * 去除orderBy子句。 
     * @param sql 
     * @return 
     */
	private static String removeOrders(String sql) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(sql);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    }
    
}