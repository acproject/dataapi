package com.owiseman.dataapi.config;

import com.owiseman.dataapi.util.JooqContextHolder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;

import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class DataSourceConfig {
    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment env;

    @PostConstruct
    public void printProperties() {
        System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
        System.out.println("Property sources: " + ((AbstractEnvironment) env).getPropertySources());

        System.out.println("spring.datasource.url: " + env.getProperty("spring.datasource.url"));
        System.out.println("All properties: " + ((AbstractEnvironment) env).getPropertySources());
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        // 从环境变量中读取配置
        hikariConfig.setJdbcUrl(env.getProperty("spring.datasource.url"));
        hikariConfig.setUsername(env.getProperty("spring.datasource.username"));
        hikariConfig.setPassword(env.getProperty("spring.datasource.password"));
        hikariConfig.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));

        // 设置其他HikariCP属性
        hikariConfig.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource.hikari.minimum-idle", "5")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.hikari.maximum-pool-size", "20")));
        hikariConfig.setAutoCommit(Boolean.parseBoolean(env.getProperty("spring.datasource.hikari.auto-commit", "true")));
        hikariConfig.setIsolateInternalQueries(Boolean.parseBoolean(env.getProperty("spring.datasource.hikari.isolate-internal-statements", "false")));
        hikariConfig.setTransactionIsolation(env.getProperty("spring.datasource.hikari.transaction-isolation", "TRANSACTION_READ_COMMITTED"));

        // 其他可选配置
        hikariConfig.addDataSourceProperty("cachePrepStmts", env.getProperty("spring.datasource.hikari.cache-prep-stmts", "true"));
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", env.getProperty("spring.datasource.hikari.prep-stmt-cache-size", "250"));
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", env.getProperty("spring.datasource.hikari.prep-stmt-cache-sql-limit", "2048"));
        log.info("hikariConfig: injected!");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        JooqContextHolder.initDataSource(dataSource);
        return dataSource;
    }




    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        log.info("TransactionManager injected!");
        return new DataSourceTransactionManager(dataSource);
    }


}
