package BO;

import DAO.DAOFactory;
import DAO.ItemDAO;
import DAO.OrderDAO;
import DAO.OrderDetailDAO;
import DTO.OrderDTO;
import DTO.OrderDetailDTO;
import Entity.Order;
import Entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO{

    private OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAIL);
    private OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean placeOrder(OrderDTO orderDTO, Connection connection) {
        try {
            connection.setAutoCommit(false);

            boolean ifSaveOrder = orderDAO.add(new Order(
                            orderDTO.getOrderId(),
                            orderDTO.getCustomerId(),
                            orderDTO.getOrderDate(),
                            orderDTO.getGrossTotal(),
                            orderDTO.getNetTotal()),
                    connection
            );

            if (ifSaveOrder){
                if (saveOrderDetail(orderDTO,connection)){
                    connection.commit();
                    return true;
                }else {
                    connection.rollback();
                    return false;
                }
            }else {
                connection.rollback();
                return false;
            }
        } catch (SQLException throwables) {
        } finally {
            try {

                connection.setAutoCommit(true);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean saveOrderDetail(OrderDTO orderDTO, Connection connection) throws SQLException {
        for (OrderDetailDTO item : orderDTO.getItems()) {
            boolean ifOrderDetailSaved = orderDetailDAO.add(new OrderDetail(
                            item.getOrderId(), item.getItemCode(), item.getKind(), item.getItemName(), item.getSellQty(), item.getUnitPrice(), item.getItemDiscount(), item.getTotal()),
                    connection
            );
            if (ifOrderDetailSaved){
                if (updateQtyOnHand(item.getItemCode(),item.getSellQty(),connection)){

                }else {
                    return false;
                }
            }else {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean updateQtyOnHand(String code, int qty, Connection connection) throws SQLException {
        return itemDAO.updateQtyOnHand(code,qty,connection);
    }

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
    public OrderDTO searchOrder(String id, Connection connection) throws SQLException {
        Order search = orderDAO.search(id, connection);
        return new OrderDTO(
                search.getOrderId(),
                search.getCustomerId(),
                search.getOrderDate(),
                search.getGrossTotal(),
                search.getNetTotal()
        );
    }

    @Override
    public int countQtyOnHand(String code, Connection connection) throws SQLException {
        return orderDetailDAO.countQtyOnHand(code,connection);
    }
}
