package DAO;

import Entity.Customer;
import Entity.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO{

    @Override
    public boolean add(Order order, Connection connection) throws SQLException {
        return false;
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
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE orderId=?",
                connection,
                id
        );
        if (rst.next()){
            return new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getDouble(5)
            );
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<Order> getAll(Connection connection) throws SQLException {
        throw new UnsupportedOperationException("No Supported Yet.");
    }
}
