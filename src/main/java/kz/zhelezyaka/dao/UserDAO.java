package kz.zhelezyaka.dao;

import kz.zhelezyaka.connection.ConnectionPool;
import kz.zhelezyaka.entity.User;
import kz.zhelezyaka.exception.DataAccessException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDAO {
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<User> result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                setUserParameters(user, resultSet);

                result.add(user);
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error while getting all users", e);
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
                setUserParameters(user, resultSet);
            }
            return user;

        } catch (SQLException e) {
            throw new DataAccessException("Error getting users by id: " + id, e);
        }
    }

    public User saveUser(User user) {
        String sql = "INSERT INTO users(name) VALUES (?)";
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getName());
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 1) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            user.setId(generatedId);
                            log.info("User saved successfully. Generated ID: {}", generatedId);
                        } else {
                            throw new SQLException("Failed to get generated key.");
                        }
                    }
                } else {
                    throw new SQLException("Failed to insert user. No rows affected.");
                }
            }
            connection.commit();
            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Error while saving user.", e);
        }
    }

    public User updateUser(User user) {
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            statement.setString(1, user.getName());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
            connection.commit();
            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating user", e);
        }
    }

    public boolean removeUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            statement.setInt(1, id);
            int result = statement.executeUpdate();

            if (result > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error while removing user", e);
        }
    }

    private static void setUserParameters(User user, ResultSet resultSet) throws SQLException {
        user.setId(resultSet.getInt(1));
        user.setName(resultSet.getString(2));
    }
}
