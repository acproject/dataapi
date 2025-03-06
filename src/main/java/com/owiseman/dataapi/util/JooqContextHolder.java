package com.owiseman.dataapi.util;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JooqContextHolder {
    private static volatile DataSource dataSource;
    private static volatile DSLContext dslContext;

    // 初始化数据源（需在应用启动时调用）
    public static synchronized void initDataSource(DataSource ds) {
        if (dataSource == null) {
            dataSource = ds;
            // 测试连接是否有效
            try (Connection conn = dataSource.getConnection()) {
                dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
                System.out.println("JOOQ DSLContext initialized successfully");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to initialize JOOQ context", e);
            }
        }
    }

    // 获取 DSLContext 实例
    public static DSLContext getDslContext() {
        if (dslContext == null) {
            throw new IllegalStateException("JOOQ DSLContext not initialized. Call initDataSource() first.");
        }
        return dslContext;
    }

    // 手动获取新连接（适用于需要精细控制事务的场景）
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


}