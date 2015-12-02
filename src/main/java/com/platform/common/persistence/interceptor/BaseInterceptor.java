package com.platform.common.persistence.interceptor;

import java.io.Serializable;
import java.util.Properties;
import org.apache.ibatis.plugin.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.platform.common.config.Global;
import com.platform.common.persistence.Page;
import com.platform.common.persistence.dialect.Dialect;
import com.platform.common.persistence.dialect.db.DB2Dialect;
import com.platform.common.persistence.dialect.db.H2Dialect;
import com.platform.common.persistence.dialect.db.MySQLDialect;
import com.platform.common.persistence.dialect.db.OracleDialect;
import com.platform.common.persistence.dialect.db.PostgreSQLDialect;
import com.platform.common.persistence.dialect.db.SQLServer2005Dialect;
import com.platform.common.utils.reflection.ReflectionUtils;

/**
 * Mybatis分页拦截器基类
 * @ClassName:  BaseInterceptor   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月23日 下午5:45:16
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {
	
	private static final long serialVersionUID = 1L;

    protected static final String PAGE = "page";
    
    protected static final String DELEGATE = "delegate";

    protected static final String MAPPED_STATEMENT = "mappedStatement";

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected Dialect DIALECT;
    
    /**
     * 对参数进行转换和检查
     * @param parameterObject 参数对象
     * @param page            分页对象
     * @return 分页对象
     * @throws NoSuchFieldException 无法找到参数
     */
    @SuppressWarnings("unchecked")
	protected static Page<Object> convertParameter(Object parameterObject, Page<Object> page) {
    	try{
            if (parameterObject instanceof Page) {
                return (Page<Object>) parameterObject;
            } else {
                return (Page<Object>)ReflectionUtils.getFieldValue(parameterObject, PAGE);
            }
    	}catch (Exception e) {
			return null;
		}
    }

    protected void initProperties(Properties p) {
    	Dialect dialect = null;
        String dbType = Global.getConfig("jdbc.type");
        if ("db2".equals(dbType)){
        	dialect = new DB2Dialect();
        }else if("h2".equals(dbType)){
        	dialect = new H2Dialect();
        }else if("mysql".equals(dbType)){
        	dialect = new MySQLDialect();
        }else if("oracle".equals(dbType)){
        	dialect = new OracleDialect();
        }else if("postgre".equals(dbType)){
        	dialect = new PostgreSQLDialect();
        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
        	dialect = new SQLServer2005Dialect();
        }
        if (dialect == null) {
            throw new RuntimeException("mybatis dialect error.");
        }
        DIALECT = dialect;
    }
}
