package Servlets;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String itemCode = req.getParameter("itemCode");
        String option = req.getParameter("option");

        PrintWriter writer = resp.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pos", "root", "1234");

            switch (option){
                case  "SEARCH":

                    ResultSet resultSet = connection.prepareStatement("SELECT * FROM Item WHERE itemCode='" + itemCode + "'").executeQuery();

                    while (resultSet.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("code",resultSet.getString(1));
                        objectBuilder.add("kind",resultSet.getString(2));
                        objectBuilder.add("itemName",resultSet.getString(3));
                        objectBuilder.add("qtyOnHand",resultSet.getString(4));
                        objectBuilder.add("unitPrice",resultSet.getString(5));

                        writer.write(String.valueOf(objectBuilder.build()));

                    }

                    break;

                case "GETALL":

                    ResultSet rst = connection.prepareStatement("SELECT * FROM Item").executeQuery();

                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (rst.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("code",rst.getString(1));
                        objectBuilder.add("kind",rst.getString(2));
                        objectBuilder.add("itemName",rst.getString(3));
                        objectBuilder.add("qtyOnHand",rst.getString(4));
                        objectBuilder.add("unitPrice",rst.getString(5));
                        arrayBuilder.add(objectBuilder.build());

                    }
                    writer.write(String.valueOf(arrayBuilder.build()));

                    break;

                case "COUNT":

                    ResultSet rsts = connection.prepareStatement("SELECT COUNT(*) FROM Item").executeQuery();
                    while (rsts.next()){
                        writer.print(rsts.getInt(1));
                    }

                    break;

                case "GETIDS":

                    ResultSet rset = connection.prepareStatement("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1").executeQuery();
                    while (rset.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("itemCode",rset.getString(1));
                        writer.print(objectBuilder.build());
                    }

                    break;

            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pos","root","1234");
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES(?,?,?,?,?)");
            pstm.setObject(1,jsonObject.getString("code"));
            pstm.setObject(2,jsonObject.getString("kind"));
            pstm.setObject(3,jsonObject.getString("itemName"));
            pstm.setObject(4,jsonObject.getString("qtyOnHand"));
            pstm.setObject(5,jsonObject.getString("unitPrice"));

            if (pstm.executeUpdate()>0) {

                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message", "Item Successfully Added.");
                objectBuilder.add("status", resp.getStatus());
                writer.print(objectBuilder.build());

            }

        } catch (ClassNotFoundException | SQLException e) {

            resp.setStatus(HttpServletResponse.SC_OK);

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("data",e.getLocalizedMessage());
            objectBuilder.add("message","Error");
            objectBuilder.add("status",resp.getStatus());
            writer.print(objectBuilder.build());

            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/jason");
        PrintWriter writer = resp.getWriter();

        String itemCode = req.getParameter("itemCode");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pos", "root", "1234");

            if (connection.prepareStatement("DELETE FROM Item WHERE itemCode='"+ itemCode +"'").executeUpdate()>0) {

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("message","Item Successfully Deleted.");
                objectBuilder.add("status",resp.getStatus());
                writer.print(objectBuilder.build());

            }else {

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message","Wrong Code Inserted.");
                objectBuilder.add("status",400);
                writer.print(objectBuilder.build());

            }

        } catch (ClassNotFoundException | SQLException e) {

            resp.setStatus(HttpServletResponse.SC_OK);

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("data",e.getLocalizedMessage());
            objectBuilder.add("message","Error");
            objectBuilder.add("status",resp.getStatus());
            writer.print(objectBuilder.build());

            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET kind=?,itemName=?,qtyOnHand=?,unitPrice=? WHERE itemCode=?");
            pstm.setObject(1,jsonObject.getString("kind"));
            pstm.setObject(2,jsonObject.getString("itemName"));
            pstm.setObject(3,jsonObject.getString("qtyOnHand"));
            pstm.setObject(4,jsonObject.getString("unitPrice"));
            pstm.setObject(5,jsonObject.getString("code"));

            if (pstm.executeUpdate()>0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message","Item Successfully Updated.");
                objectBuilder.add("status",resp.getStatus());
                writer.print(objectBuilder.build());

            }else{
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message","Update Failed.");
                objectBuilder.add("status",400);
                writer.print(objectBuilder.build());

            }

        } catch (ClassNotFoundException | SQLException e) {

            resp.setStatus(HttpServletResponse.SC_OK);

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("data",e.getLocalizedMessage());
            objectBuilder.add("message","Update Failed.");
            objectBuilder.add("status",resp.getStatus());
            writer.print(objectBuilder.build());

            e.printStackTrace();
        }
    }
}
