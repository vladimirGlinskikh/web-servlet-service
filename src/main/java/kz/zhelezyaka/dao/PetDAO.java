package kz.zhelezyaka.dao;

import kz.zhelezyaka.connection.ConnectionPool;
import kz.zhelezyaka.entity.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public List<Pet> getAllPets() {
        String sql = "select * from pets";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<Pet> result = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Pet pets = new Pet();
                pets.setId(resultSet.getInt(1));
                pets.setName(resultSet.getString(2));

                result.add(pets);
            }

            return result;
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting all pets", e);
        }
    }

    public Pet getPetById(int id) {
        String sql = "select * from pets where id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Pet pets = new Pet();
            if (resultSet.next()) {
                pets.setId(resultSet.getInt(1));
                pets.setName(resultSet.getString(2));
            }
            return pets;

        } catch (SQLException | NamingException e) {
            throw new RuntimeException();
        }
    }

    public Pet savePet(Pet pet) {
        String sql = "insert into pets(name) values(?)";
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);  // Установите автоматический коммит в false для управления транзакцией

            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, pet.getName());
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 1) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            pet.setId(generatedId);
                            logger.info("Pet saved successfully. Generated ID: {}", generatedId);
                        } else {
                            throw new SQLException("Failed to get generated key.");
                        }
                    }
                } else {
                    throw new SQLException("Failed to insert pet. No rows affected.");
                }
            }

            connection.commit();  // Если не было исключений, коммитим транзакцию

            return pet;
        } catch (SQLException | NamingException e) {
            try {
                if (connection != null) {
                    connection.rollback();  // Если возникло исключение, откатываем транзакцию
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction.", ex);
            }
            throw new RuntimeException("Error while saving pet.", e);
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

    public Pet updatePet(Pet pet) {
        String sql = "update pets set name = ? where id = ?";
        try (Connection connection = ConnectionPool.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, pet.getName());
                statement.setInt(2, pet.getId());
                statement.executeUpdate();
                connection.commit();
                return pet;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removePet(int id) {
        String sql = "delete from pets where id = ?";
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
        } catch (SQLException | NamingException e) {
            logger.error("Error while removing pet", e);
            throw new RuntimeException(e);
        }
    }
}
