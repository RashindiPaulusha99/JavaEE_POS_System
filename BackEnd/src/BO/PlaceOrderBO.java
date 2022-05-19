package BO;

import DTO.OrderDTO;
import DTO.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PlaceOrderBO extends SuperBO{
    boolean placeOrder(OrderDTO orderDTO,Connection connection);
    boolean saveOrderDetail(OrderDTO orderDTO, Connection connection) throws SQLException;
    boolean updateQtyOnHand(String code,int qty, Connection connection) throws SQLException;
    String getOrderId(Connection connection) throws SQLException;
    int countOrders(Connection connection) throws SQLException;
    double findNetTotal(Connection connection) throws SQLException;
    ArrayList<OrderDetailDTO> searchOrderDetail(String id, Connection connection) throws SQLException;
    OrderDTO searchOrder(String id, Connection connection) throws SQLException;
    int countQtyOnHand(String id, Connection connection) throws SQLException;

}
