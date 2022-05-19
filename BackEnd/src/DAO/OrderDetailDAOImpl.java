package DAO;

import Entity.Order;
import Entity.OrderDetail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO{

    @Override
    public boolean add(OrderDetail orderDetail, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean update(OrderDetail orderDetail, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException {
        throw new UnsupportedOperationException("No Supported Yet.");
    }

    @Override
    public OrderDetail search(String id, Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Order Detail` WHERE oId=?",
                connection,
                id
        );
        if (rst.next()){
            return new OrderDetail(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getInt(7),
                    rst.getDouble(8)
            );
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<OrderDetail> getAll(Connection connection) throws SQLException {
        throw new UnsupportedOperationException("No Supported Yet.");
    }
}
