package Servlets;

import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/purchaseOrder")
public class PurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        String option = req.getParameter("option");
        String orderId = req.getParameter("orderId");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pos", "root", "1234");

            switch (option){
                case "SEARCH":

                    ResultSet resultSet = connection.prepareStatement("SELECT * FROM `Order` WHERE orderId='" + orderId + "'").executeQuery();

                    while (resultSet.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("orderId",resultSet.getString(1));
                        objectBuilder.add("cusId",resultSet.getString(2));
                        objectBuilder.add("orderDate",resultSet.getString(3));
                        objectBuilder.add("grossTotal",resultSet.getString(4));
                        objectBuilder.add("netTotal",resultSet.getString(5));

                        writer.write(String.valueOf(objectBuilder.build()));

                    }

                    break;

                case "SEARCHDETAILS":

                    ResultSet rSet = connection.prepareStatement("SELECT * FROM `Order Detail` WHERE oId='" + orderId + "'").executeQuery();

                    while (rSet.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("oId",rSet.getString(1));
                        objectBuilder.add("itemId",rSet.getString(2));
                        objectBuilder.add("itemKind",rSet.getString(3));
                        objectBuilder.add("itemName",rSet.getString(4));
                        objectBuilder.add("sellQty",rSet.getString(5));
                        objectBuilder.add("unitPrice",rSet.getString(6));
                        objectBuilder.add("itemDiscount",rSet.getString(7));
                        objectBuilder.add("total",rSet.getString(8));

                        writer.write(String.valueOf(objectBuilder.build()));

                    }

                    break;

                case "GETALL":

                    ResultSet rset = connection.prepareStatement("SELECT * FROM Customer").executeQuery();

                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (rset.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("orderId",rset.getString(1));
                        objectBuilder.add("cusId",rset.getString(2));
                        objectBuilder.add("orderDate",rset.getString(3));
                        objectBuilder.add("grossTotal",rset.getString(4));
                        objectBuilder.add("netTotal",rset.getString(5));
                        arrayBuilder.add(objectBuilder.build());

                    }
                    writer.write(String.valueOf(arrayBuilder.build()));

                    break;

                case "GETIDS":

                    ResultSet rst = connection.prepareStatement("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1").executeQuery();
                    while (rst.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("orderId",rst.getString(1));
                        writer.print(objectBuilder.build());
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
            }


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String option = req.getParameter("option");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        Connection connection = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pos","root","1234");
            connection.setAutoCommit(false);

            switch (option){
                case "Order":

                    PreparedStatement pstm = connection.prepareStatement("INSERT INTO `Order` VALUES(?,?,?,?,?)");
                    pstm.setObject(1,jsonObject.getString("orderId"));
                    pstm.setObject(2,jsonObject.getString("cusId"));
                    pstm.setObject(3,jsonObject.getString("orderDate"));
                    pstm.setObject(4,jsonObject.getString("grossTotal"));
                    pstm.setObject(5,jsonObject.getString("netTotal"));
                    System.out.println(jsonObject.getString("orderId"));
                    System.out.println("order");

                    if (pstm.executeUpdate()>0) {
                        connection.commit();

                        resp.setStatus(HttpServletResponse.SC_OK);
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("message", "Successfully Purchased Order.");
                        objectBuilder.add("status", resp.getStatus());
                        writer.print(objectBuilder.build());

                    }else {
                        connection.rollback();
                    }

                    break;
                case "OrderDetail" :

                    PreparedStatement stm = connection.prepareStatement("INSERT INTO `Order Detail` VALUES(?,?,?,?,?,?,?,?)");
                    stm.setObject(1,jsonObject.getString("oId"));
                    stm.setObject(2,jsonObject.getString("itemId"));
                    stm.setObject(3,jsonObject.getString("itemKind"));
                    stm.setObject(4,jsonObject.getString("itemName"));
                    stm.setObject(5,jsonObject.getString("sellQty"));
                    stm.setObject(6,jsonObject.getString("unitPrice"));
                    stm.setObject(7,jsonObject.getString("itemDiscount"));
                    stm.setObject(8,jsonObject.getString("total"));

                    if (stm.executeUpdate()>0){
                        connection.commit();

                        resp.setStatus(HttpServletResponse.SC_OK);
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("message", "Successfully Purchased Order.");
                        objectBuilder.add("status", resp.getStatus());
                        writer.print(objectBuilder.build());

                    }else {
                        connection.rollback();
                    }

                    break;

            }


        } catch (ClassNotFoundException | SQLException e) {

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
