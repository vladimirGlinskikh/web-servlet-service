package kz.zhelezyaka.dao;

import kz.zhelezyaka.connection.ConnectionPool;
import kz.zhelezyaka.entity.Pet;
import kz.zhelezyaka.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {
    public List<Pet> getAllPets() {
        String sql = "SELECT * FROM pets";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<Pet> result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Pet pets = new Pet();
                setPetParameters(pets, resultSet);
                result.add(pets);
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error while getting all pets", e);
        }
    }

    public Pet getPetById(int id) {
        String sql = "SELECT * FROM pets WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Pet pet = new Pet();
            if (resultSet.next()) {
                setPetParameters(pet, resultSet);
            }
            return pet;
        } catch (SQLException e) {
            throw new DataAccessException("Error while getting pets by id", e);
        }
    }

    public Pet savePet(Pet pet) {
        String sql = "INSERT INTO pets(name) VALUES (?)";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, pet.getName());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        pet.setId(generatedId);
                    } else {
                        throw new DataAccessException("Failed to get generated key.");
                    }
                }
            } else {
                throw new SQLException("Failed to insert pet. No rows affected.");
            }
            return pet;
        } catch (SQLException e) {
            throw new DataAccessException("Error while saving pet.", e);
        }
    }

    public Pet updatePet(Pet pet) {
        String sql = "UPDATE pets SET name = ? WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            statement.setString(1, pet.getName());
            statement.setInt(2, pet.getId());
            statement.executeUpdate();
            return pet;
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating pet", e);
        }
    }

    public boolean removePet(int id) {
        String sql = "DELETE FROM pets WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int result = statement.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error while removing pet", e);
        }
    }

    private static void setPetParameters(Pet pet, ResultSet resultSet) throws SQLException {
        pet.setId(resultSet.getInt(1));
        pet.setName(resultSet.getString(2));
    }
}
