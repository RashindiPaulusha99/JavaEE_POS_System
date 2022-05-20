package DAO;

import Entity.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO{

    @Override
    public boolean add(Order order, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate(
                "INSERT INTO `Order` VALUES(?,?,?,?,?)",
                connection,
                order.getOrderId(),order.getCustomerId(),order.getOrderDate(),order.getGrossTotal(),order.getNetTotal()
                );
    }

    @Override
    public boolean update(Order order, Connection connection) throws SQLException {
        throw new UnsupportedOperationException("No Supported Yet.");
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException {
        throw new UnsupportedOperationException("No Supported Yet.");
    }

    @Override
    public Order search(String id, Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery(
                "SELECT * FROM `Order` WHERE orderId=?",
                connection,
                id
        );
        if (rst.next()){
            Order order = new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getDouble(5)
            );
            System.out.println(order);
            return order;
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<Order> getAll(Connection connection) throws SQLException {
        throw new UnsupportedOperationException("No Supported Yet.");
    }

    @Override
    public String getOrderId(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1",
                connection
        );
        if (rst.next()){
            return rst.getString(1);
        }else {
            return null;
        }
    }

    @Override
    public int countOrders(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT COUNT(*) FROM `Order`",
                connection
        );
        if (rst.next()){
            return rst.getInt(1);
        }else {
            return 0;
        }
    }

    @Override
    public double findNetTotal(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT SUM(netTotal) FROM `Order`",
                connection
        );
        if (rst.next()){
            return rst.getDouble(1);
        }else {
            return 0;
        }
    }

}
