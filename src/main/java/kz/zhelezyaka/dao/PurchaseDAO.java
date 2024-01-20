package kz.zhelezyaka.dao;

import kz.zhelezyaka.connection.ConnectionPool;
import kz.zhelezyaka.entity.Purchase;
import kz.zhelezyaka.entity.User;
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
                User user = new User();
                int userId = resultSet.getInt("user_id");

                user.setId(userId);

                purchase.setUser(user);
                result.add(purchase);
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all purchases", e);
        }
    }
}
