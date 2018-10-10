package com.lenovo.iot.devicemanager.util;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * 分页插件
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}), @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class PageWidget<Entity> implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(PageWidget.class);
    private String dialect = "mysql";
    private String pageMatch = ".*Page$";
    private Page page = null;

    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            while (metaObject.hasGetter("h")) {
                Object object = metaObject.getValue("h");
                metaObject = SystemMetaObject.forObject(object);
            }
            while (metaObject.hasGetter("target")) {
                Object object = metaObject.getValue("target");
                metaObject = SystemMetaObject.forObject(object);
            }
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            if (mappedStatement.getId().matches(pageMatch)) {
                BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
                String sql = boundSql.getSql();
                Assert.notNull(metaObject.getValue("delegate.boundSql.parameterObject"), "parameterObject is null");
                try {
                    page = (Page) metaObject.getValue("delegate.boundSql.parameterObject.page");
                } catch (Exception e) {
                    page = null;
                    return invocation.proceed();
                }
                boolean needCount = true;
                boolean needPage = true;
                if (needPage) {
                    String pageSql = buildPageSql(sql, page);
                    metaObject.setValue("delegate.boundSql.sql", pageSql);
                    if (needCount) {
                        Connection conn = (Connection) invocation.getArgs()[0];
                        try {
                            setPageParameters(sql, page, conn, mappedStatement, boundSql);
                            metaObject.setValue("delegate.boundSql.parameterObject.page", page);
                    	} finally {
                            conn.close();
                    	}
                    }
                }
            }
            return invocation.proceed();
        } else if (invocation.getTarget() instanceof ResultSetHandler) {
            Object result = invocation.proceed();
            if(page == null) {
               return result;
            }
            PageImpl<Entity> pageImpl = new PageImpl<Entity>((List<Entity>) result, page.getPageNo(), page.getPageSize(), page.getPageCount(), page.getPageResultCount());
            page = null;	// 只处理本次分页查询的结果
            return pageImpl;
        } else {
            return invocation.proceed();
        }
    }

    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    public void setProperties(Properties properties) {
        this.dialect = properties.getProperty("dialect");
        this.pageMatch = properties.getProperty("pageMatch");
    }

    private String buildPageSql(String sql, Page page) {
        Assert.notNull(page, "pageutil is null");
        return buildPageSqlForDialect(dialect, sql, page).toString();
    }

    private StringBuilder buildPageSqlForDialect(String dialect, String sql, Page page) {
        StringBuilder builder = new StringBuilder();
        if ("mysql".equalsIgnoreCase(dialect)) {
            builder.append(sql);
            builder.append(" LIMIT ").append(page.getPageOffset()).append(",").append(page.getPageSize());
        } else {
            builder.append(sql);
        }
        log.debug("分页sql:{}", builder);
        if(page.isPageShowSql()) {
            System.out.println("page sql:\t" + builder);
        }
        return builder;
    }

    private void setPageParameters(String sql, Page page, Connection connection, MappedStatement mappedStatement, BoundSql boundSql) {
        int from = sql.indexOf("FROM");
        String countSql = "SELECT COUNT(*) " + sql.substring(from);
        if(page.isPageShowSql()) {
            System.out.println("\npage sql count:\t" + countSql);
        }
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(statement, mappedStatement, countBS, boundSql.getParameterObject());
            rs = statement.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            page.setPageResultCount(totalCount);
            page.setPageCount((int) (page.getPageResultCount() % page.getPageSize() == 0 ? page.getPageResultCount() / page.getPageSize() : (page.getPageResultCount() / page.getPageSize()) + 1));
        } catch (SQLException e) {
            log.error("PageWidget error get total count:", e);
        } finally {
            try {
            	if(rs != null) {
            		rs.close();
            	}
            	if(statement != null) {
            		statement.close();
            	}
            } catch (SQLException e) {
                log.error("PageWidget error close rs or stmt:", e);
            }
        }
    }

    private void setParameters(PreparedStatement statement, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(statement);
    }

}
