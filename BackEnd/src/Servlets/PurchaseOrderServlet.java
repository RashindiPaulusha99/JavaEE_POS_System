package Servlets;

import DAO.OrderDAOImpl;
import DAO.OrderDetailDAOImpl;
import Entity.Order;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane;

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

                    ResultSet rSet = connection.prepareStatement("SELECT * FROM `Order Detail` WHERE oId='" + orderId + "'").executeQuery();
                    JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();

                    while (rSet.next()){
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("oId",rSet.getString(1));
                        ob.add("itemId",rSet.getString(2));
                        ob.add("itemKind",rSet.getString(3));
                        ob.add("itemName",rSet.getString(4));
                        ob.add("sellQty",rSet.getString(5));
                        ob.add("unitPrice",rSet.getString(6));
                        ob.add("itemDiscount",rSet.getString(7));
                        ob.add("total",rSet.getString(8));
                        arrayBuilder1.add(ob.build());
                    }
                    writer.write(String.valueOf(arrayBuilder1.build()));

                    break;

                case "GETALL":

                    ResultSet rset = connection.prepareStatement("SELECT * FROM `Order`").executeQuery();

                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (rset.next()){
                        JsonObjectBuilder obje = Json.createObjectBuilder();
                        obje.add("orderId",rset.getString(1));
                        obje.add("cusId",rset.getString(2));
                        obje.add("orderDate",rset.getString(3));
                        obje.add("grossTotal",rset.getString(4));
                        obje.add("netTotal",rset.getString(5));
                        arrayBuilder.add(obje.build());

                    }
                    writer.write(String.valueOf(arrayBuilder.build()));

                    break;

                case "GETIDS":

                    ResultSet rst = connection.prepareStatement("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1").executeQuery();
                    while (rst.next()){
                        JsonObjectBuilder obj = Json.createObjectBuilder();
                        obj.add("orderId",rst.getString(1));
                        writer.print(obj.build());
                    }

                    break;

                case "COUNT":

                    ResultSet rsts = connection.prepareStatement("SELECT COUNT(*) FROM `Order`").executeQuery();
                    while (rsts.next()){
                        writer.print(rsts.getInt(1));
                    }

                    break;

                case "TOTAL":

                    ResultSet set = connection.prepareStatement(" SELECT SUM(netTotal) FROM `Order`").executeQuery();
                    while (set.next()){
                        writer.print(set.getInt(1));
                    }

                    break;

                case "COUNTQTY":

                    ResultSet rts = connection.prepareStatement("SELECT SUM(sellQty) FROM `Order Detail` WHERE oId='"+ orderId +"'").executeQuery();
                    while (rts.next()){
                        writer.print(rts.getInt(1));
                    }

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
