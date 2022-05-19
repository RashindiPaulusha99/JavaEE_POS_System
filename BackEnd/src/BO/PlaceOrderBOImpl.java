package BO;

import DTO.OrderDTO;
import DTO.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO{
    @Override
    public boolean placeOrder(OrderDTO orderDTO, Connection connection) {
        return false;
    }

    @Override
    public boolean saveOrderDetail(OrderDTO orderDTO, Connection connection) {
        return false;
    }

    @Override
    public boolean updateQtyOnHand(String code, int qty, Connection connection) {
        return false;
    }

    @Override
    public String getOrderId(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public int countOrders(Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public double findNetTotal(Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<OrderDetailDTO> searchOrderDetail(String id, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public int countQtyOnHand(String id, Connection connection) throws SQLException {
        return 0;
    }
}
