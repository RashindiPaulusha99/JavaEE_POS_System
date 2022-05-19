package DAO;

import Entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailDAO extends CrudDAO<OrderDetail, String, Connection>{
    ArrayList<OrderDetail> searchOrderDetail(String id, Connection connection) throws SQLException;
}
