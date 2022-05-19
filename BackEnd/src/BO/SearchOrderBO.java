package BO;

import DTO.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SearchOrderBO extends SuperBO{
    String getOrderId(Connection connection) throws SQLException;
    int countOrders(Connection connection) throws SQLException;
    double findNetTotal(Connection connection) throws SQLException;
    ArrayList<OrderDetailDTO> searchOrderDetail(String id, Connection connection) throws SQLException;
    int countQtyOnHand(String id, Connection connection) throws SQLException;
}
