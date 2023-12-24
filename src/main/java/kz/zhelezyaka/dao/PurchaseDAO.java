package kz.zhelezyaka.dao;

import kz.zhelezyaka.connection.ConnectionPool;
import kz.zhelezyaka.entity.Purchase;
import kz.zhelezyaka.exception.DataAccessException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PurchaseDAO {
    public List<Purchase> getAllPurchases() {
        String sql = "SELECT * FROM purchases";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<Purchase> result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Purchase purchase = new Purchase();
                setPurchasePropertiesFromResultSet(purchase, resultSet);

                result.add(purchase);
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all purchase", e);
        }
    }

    public Purchase getPurchaseById(int id) {
        String sql = "SELECT * FROM purchases WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Purchase purchase = new Purchase();
            if (resultSet.next()) {
                setPurchasePropertiesFromResultSet(purchase, resultSet);
            }
            return purchase;

        } catch (SQLException e) {
            log.error("Error while getting purchases by id", e);
            throw new DataAccessException("Error retrieving purchase by id: " + id, e);
        }
    }

    private static void setPurchasePropertiesFromResultSet(Purchase purchase, ResultSet resultSet) throws SQLException {
        purchase.setId(resultSet.getInt("id"));
        purchase.setUserId(resultSet.getInt("user_id"));
        purchase.setPetId(resultSet.getInt("pet_id"));
    }
}
