package DAO;

import Entity.Order;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order, String, Connection>{
    String getOrderId(Connection connection) throws SQLException;
    int countOrders(Connection connection) throws SQLException;
    double findNetTotal(Connection connection) throws SQLException;
}
