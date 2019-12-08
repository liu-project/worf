package orj.worf.mybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import orj.worf.mybatis.dialect.Dialect;
import orj.worf.mybatis.util.CountHelper;
import orj.worf.mybatis.util.ReflectionUtils;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = StatementHandler.class, method = "prepare", args = { java.sql.Connection.class }) })
public class PaginationInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);
    private static final String CONFIGURATION = "configuration";
    private static final String ROW_BOUNDS = "rowBounds";
    private static final String BOUND_SQL = "boundSql";
    private static final String DELEGATE = "delegate";
    private Dialect dialect;

    public void setDialect(final Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public Object intercept(final Invocation invocation) throws Throwable {
        StatementHandler statementHandler = getStatementHandler(invocation);
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, new DefaultObjectFactory(),
                new DefaultObjectWrapperFactory());
        RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue(ROW_BOUNDS);
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        }
        Connection connection = (Connection) invocation.getArgs()[0];
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue(BOUND_SQL);
        String originalSql = boundSql.getSql();
        String countSql = dialect.getCountSql(originalSql);
        Configuration configuration = (Configuration) metaStatementHandler.getValue(CONFIGURATION);
        CountHelper.getCount(countSql, statementHandler, configuration, boundSql, connection);
        metaStatementHandler.setValue("boundSql.sql",
                this.dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
        metaStatementHandler.setValue("rowBounds.offset", 0);
        metaStatementHandler.setValue("rowBounds.limit", Integer.MAX_VALUE);
        if (logger.isDebugEnabled()) {
            logger.debug("生成分页SQL : {}", statementHandler.getBoundSql().getSql());
        }
        return invocation.proceed();
    }

    protected StatementHandler getStatementHandler(final Invocation invocation) {
        StatementHandler statement = (StatementHandler) invocation.getTarget();
        if (statement instanceof RoutingStatementHandler) {
            statement = (StatementHandler) ReflectionUtils.getFieldValue(statement, DELEGATE);
        }
        return statement;
    }

    @Override
    public Object plugin(final Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(final Properties properties) {
    }
}
