package kz.zhelezyaka.dao;

import kz.zhelezyaka.connection.ConnectionPool;
import kz.zhelezyaka.entity.Purchase;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {
    public List<Purchase> getAllPurchases() {
        String sql = "select * from purchases";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<Purchase> result = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Purchase purchase = new Purchase();
                purchase.setId(resultSet.getInt("id"));
                purchase.setUserId(resultSet.getInt("user_id"));
                purchase.setPetId(resultSet.getInt("pet_id"));

                result.add(purchase);
            }

            return result;
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting all purchases", e);
        }
    }

    public Purchase getPurchaseById(int id) {
        String sql = "select * from purchases where id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Purchase purchase = new Purchase();
            if (resultSet.next()) {
                purchase.setId(resultSet.getInt("id"));
                purchase.setUserId(resultSet.getInt("user_id"));
                purchase.setPetId(resultSet.getInt("pet_id"));
            }
            return purchase;

        } catch (SQLException | NamingException e) {
            throw new RuntimeException();
        }
    }
}
