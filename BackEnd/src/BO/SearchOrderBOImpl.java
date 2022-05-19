package BO;

import DAO.DAOFactory;
import DAO.ItemDAO;
import DAO.OrderDAO;
import DAO.OrderDetailDAO;
import DTO.OrderDetailDTO;
import Entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchOrderBOImpl implements SearchOrderBO {

    private OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAIL);
    private OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public String getOrderId(Connection connection) throws SQLException {
        return orderDAO.getOrderId(connection);
    }

    @Override
    public int countOrders(Connection connection) throws SQLException {
        return orderDAO.countOrders(connection);
    }

    @Override
    public double findNetTotal(Connection connection) throws SQLException {
        return orderDAO.findNetTotal(connection);
    }

    @Override
    public ArrayList<OrderDetailDTO> searchOrderDetail(String id, Connection connection) throws SQLException {
        ArrayList<OrderDetail> orderDetails = orderDetailDAO.searchOrderDetail(id, connection);
        ArrayList<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailDTOS.add(new OrderDetailDTO(
                    orderDetail.getOrderId(),
                    orderDetail.getItemCode(),
                    orderDetail.getKind(),
                    orderDetail.getItemName(),
                    orderDetail.getSellQty(),
                    orderDetail.getUnitPrice(),
                    orderDetail.getItemDiscount(),
                    orderDetail.getTotal()
            ));
        }
        return orderDetailDTOS;
    }

    @Override
    public int countQtyOnHand(String code, Connection connection) throws SQLException {
        return orderDetailDAO.countQtyOnHand(code,connection);
    }
}
