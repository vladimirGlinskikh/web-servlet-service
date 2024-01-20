package kz.zhelezyaka.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static HikariDataSource dataSource;

    private ConnectionPool() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }

    private static void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://servletdb:5432/postgres");
        config.setUsername("admin");
        config.setPassword("root");
        config.setDriverClassName("org.postgresql.Driver");
        config.setConnectionTimeout(30000);
        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);

        dataSource = new HikariDataSource(config);
    }
}