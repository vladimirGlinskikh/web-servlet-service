package kz.zhelezyaka.testContainers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
class TestDatabasePostgreSQL {
    private static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>("postgres:alpine");
        container.start();
    }

    @Test
    void shouldBeDatabaseConnection() {
        container.start();

        String jdbcUrl = container.getJdbcUrl();
        String username = container.getUsername();
        String password = container.getPassword();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        DataSource dataSource = new HikariDataSource(config);

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        container.stop();
    }
}
