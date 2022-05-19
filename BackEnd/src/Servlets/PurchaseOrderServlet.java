package Servlets;

import DAO.OrderDAOImpl;
import DAO.OrderDetailDAOImpl;
import Entity.Order;
import Entity.OrderDetail;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/purchaseOrder")
public class PurchaseOrderServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    OrderDAOImpl orderDAO = new OrderDAOImpl();
    OrderDetailDAOImpl orderDetailDAO = new OrderDetailDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        String option = req.getParameter("option");
        String orderId = req.getParameter("orderId");

        try {
            Connection connection = dataSource.getConnection();

            switch (option){
                case "SEARCH":

                    Order order = orderDAO.search(orderId, connection);

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("orderId",order.getOrderId());
                    objectBuilder.add("cusId",order.getCustomerId());
                    objectBuilder.add("orderDate",order.getOrderDate());
                    objectBuilder.add("grossTotal",order.getGrossTotal());
                    objectBuilder.add("netTotal",order.getNetTotal());

                    writer.write(String.valueOf(objectBuilder.build()));

                    break;

                case "SEARCHDETAILS":

                    ArrayList<OrderDetail> orderDetails = orderDetailDAO.searchOrderDetail(orderId, connection);

                    JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();

                    for (OrderDetail orderDetail : orderDetails) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("oId",orderDetail.getOrderId());
                        ob.add("itemId",orderDetail.getItemCode());
                        ob.add("itemKind",orderDetail.getKind());
                        ob.add("itemName",orderDetail.getItemName());
                        ob.add("sellQty",orderDetail.getSellQty());
                        ob.add("unitPrice",orderDetail.getUnitPrice());
                        ob.add("itemDiscount",orderDetail.getItemDiscount());
                        ob.add("total",orderDetail.getTotal());
                        arrayBuilder1.add(ob.build());
                    }
                    writer.write(String.valueOf(arrayBuilder1.build()));

                    break;

                case "GETIDS":

                    JsonObjectBuilder obj = Json.createObjectBuilder();
                    obj.add("orderId",orderDAO.getOrderId(connection));
                    writer.print(obj.build());

                    break;

                case "COUNT":

                    writer.print(orderDAO.countOrders(connection));

                    break;

                case "TOTAL":

                    writer.print(orderDAO.findNetTotal(connection));

                    break;

                case "COUNTQTY":

                    writer.print(orderDetailDAO.countQtyOnHand(orderId,connection));

                    break;
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray items = jsonObject.getJsonArray("items");

        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            connection.setAutoCommit(false);

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO `Order` VALUES(?,?,?,?,?)");
            pstm.setObject(1,jsonObject.getString("orderId"));
            pstm.setObject(2,jsonObject.getString("cusId"));
            pstm.setObject(3,jsonObject.getString("orderDate"));
            pstm.setObject(4,jsonObject.getString("grossTotal"));
            pstm.setObject(5,jsonObject.getString("netTotal"));
            System.out.println(jsonObject.getString("orderId"));

            if (pstm.executeUpdate()>0) {

                for (JsonValue item : items) {

                    JsonObject jo = item.asJsonObject();
                    PreparedStatement stm = connection.prepareStatement("INSERT INTO `Order Detail` VALUES(?,?,?,?,?,?,?,?)");
                    stm.setObject(1,jo.getString("oId"));
                    stm.setObject(2,jo.getString("itemId"));
                    stm.setObject(3,jo.getString("itemKind"));
                    stm.setObject(4,jo.getString("itemName"));
                    stm.setObject(5,jo.getString("sellQty"));
                    stm.setObject(6,jo.getString("unitPrice"));
                    stm.setObject(7,jo.getString("itemDiscount"));
                    stm.setObject(8,jo.getString("total"));

                    String code = jo.getString("itemId");
                    int qty = Integer.parseInt(jo.getString("sellQty"));

                    if (stm.executeUpdate()>0){

                        PreparedStatement ptm = connection.prepareStatement("UPDATE Item SET qtyOnHand=(qtyOnHand - " + qty + " )  WHERE itemCode='"+ code +"'");

                        if (ptm.executeUpdate()>0){

                        }else {
                            connection.rollback();
                        }
                    }else {
                        connection.rollback();
                    }
                }
                connection.commit();

                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message", "Successfully Purchased Order.");
                objectBuilder.add("status", resp.getStatus());
                writer.print(objectBuilder.build());

            }else {
                connection.rollback();
            }

            connection.close();

        } catch (SQLException e) {

            resp.setStatus(HttpServletResponse.SC_OK);

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("data",e.getLocalizedMessage());
            objectBuilder.add("message","Error");
            objectBuilder.add("status",resp.getStatus());
            writer.print(objectBuilder.build());

            e.printStackTrace();

        }finally {
            try {

                connection.setAutoCommit(true);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
