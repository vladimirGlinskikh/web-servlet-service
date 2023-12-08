package kz.zhelezyaka.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static HikariDataSource dataSource;

    private ConnectionPool() {
    }

    public static synchronized Connection getConnection() throws SQLException, NamingException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }

    private static void initializeDataSource() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:comp/env");
        DataSource jndiDataSource = (DataSource) envContext.lookup("jdbc/web-servlet");
        HikariConfig config = new HikariConfig();
        config.setDataSource(jndiDataSource);

        dataSource = new HikariDataSource(config);
    }
}