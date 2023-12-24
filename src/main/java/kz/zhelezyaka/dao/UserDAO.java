package kz.zhelezyaka.dao;

import kz.zhelezyaka.connection.ConnectionPool;
import kz.zhelezyaka.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<User> result = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User users = new User();
                users.setId(resultSet.getInt(1));
                users.setName(resultSet.getString(2));

                result.add(users);
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting all users", e);
        }
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
            }
            return user;

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public User saveUser(User user) {
        String sql = "INSERT INTO users(name) VALUES (?)";
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);  // Установите автоматический коммит в false для управления транзакцией

            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getName());
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 1) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            user.setId(generatedId);
                            logger.info("User saved successfully. Generated ID: {}", generatedId);
                        } else {
                            throw new SQLException("Failed to get generated key.");
                        }
                    }
                } else {
                    throw new SQLException("Failed to insert user. No rows affected.");
                }
            }

            connection.commit();  // Если не было исключений, коммитим транзакцию

            return user;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();  // Если возникло исключение, откатываем транзакцию
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction.", ex);
            }
            throw new RuntimeException("Error while saving user.", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);  // Возвращаем автоматический коммит в исходное состояние
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing connection.", e);
            }
        }
    }

    public User updateUser(User user) {
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getName());
                statement.setInt(2, user.getId());
                statement.executeUpdate();
                connection.commit();
                return user;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                int result = statement.executeUpdate();

                if (result > 0) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error("Error while removing user", e);
            throw new RuntimeException(e);
        }
    }
}
