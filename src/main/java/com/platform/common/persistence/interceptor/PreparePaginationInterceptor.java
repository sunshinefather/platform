package com.platform.common.persistence.interceptor;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import com.platform.common.persistence.Page;
import com.platform.common.utils.reflection.ReflectionUtils;
import java.sql.Connection;
import java.util.Properties;
/**
 * Mybatis数据库分页插件
 * @ClassName:  PreparePaginationInterceptor   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月24日 上午10:39:44
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PreparePaginationInterceptor extends BaseInterceptor {
    
    private static final long serialVersionUID = 1L;

    public PreparePaginationInterceptor() {
        super();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget().getClass().isAssignableFrom(RoutingStatementHandler.class)) {
            final RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
            final BaseStatementHandler delegate = (BaseStatementHandler) ReflectionUtils.getFieldValue(statementHandler, DELEGATE);
            final MappedStatement mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(delegate, MAPPED_STATEMENT);

                BoundSql boundSql = delegate.getBoundSql();

                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject == null) {
                    log.error("参数未实例化");
                    throw new NullPointerException("parameterObject尚未实例化！");
                } else {
                    Page<Object> page = null;
                    page = convertParameter(parameterObject, page);
                    if(page!=null && page.getPageSize() != -1){
                        final String sql = boundSql.getSql();
                        final int count = SQLHelper.getCount(sql, mappedStatement, parameterObject, boundSql, log);
                        page.setCount(count);
                        String pagingSql = SQLHelper.generatePageSql(sql, page, DIALECT);
                        if (log.isDebugEnabled()) {
                            log.debug("PAGE SQL:" + pagingSql);
                        }
                        ReflectionUtils.setFieldValue(boundSql, "sql", pagingSql);
                    }
                }
                
            }
        return invocation.proceed();
    }


    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        super.initProperties(properties);
    }
}