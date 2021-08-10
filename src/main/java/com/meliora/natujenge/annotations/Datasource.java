package com.meliora.natujenge.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Datasource {
    String id();

    String driverClassName();

    String jdbcUrl();

    String username();

    String password();

    boolean cachePrepStmts() default true;

    int prepStmtCacheSize() default 250;

    int prepStmtCacheSqlLimit() default 2048;

    boolean useSSL() default false;

    int minimumIdle() default 30;

    int maximumPoolSize() default 250;

    boolean isAutoCommit() default true;

    int connectTimeout() default 2000;

    int idleTimeout() default 30000;

    boolean isReadOnly() default false;
}
