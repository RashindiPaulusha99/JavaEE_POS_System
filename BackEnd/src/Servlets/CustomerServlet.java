package Servlets;

import DAO.CustomerDAOImpl;
import DTO.CustomerDTO;
import Entity.Customer;

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

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String cusId = req.getParameter("cusId");
        String option = req.getParameter("option");

        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            switch (option){
                case  "SEARCH":

                    ResultSet resultSet = connection.prepareStatement("SELECT * FROM Customer WHERE customerId='" + cusId + "'").executeQuery();

                    while (resultSet.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id",resultSet.getString(1));
                        objectBuilder.add("name",resultSet.getString(2));
                        objectBuilder.add("gender",resultSet.getString(3));
                        objectBuilder.add("contact",resultSet.getString(4));
                        objectBuilder.add("nic",resultSet.getString(5));
                        objectBuilder.add("address",resultSet.getString(6));
                        objectBuilder.add("email",resultSet.getString(7));

                        writer.write(String.valueOf(objectBuilder.build()));

                    }

                 break;

                case "GETALL":

                    ResultSet rst = connection.prepareStatement("SELECT * FROM Customer").executeQuery();

                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (rst.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id",rst.getString(1));
                        objectBuilder.add("name",rst.getString(2));
                        objectBuilder.add("gender",rst.getString(3));
                        objectBuilder.add("contact",rst.getString(4));
                        objectBuilder.add("nic",rst.getString(5));
                        objectBuilder.add("address",rst.getString(6));
                        objectBuilder.add("email",rst.getString(7));
                        arrayBuilder.add(objectBuilder.build());

                    }
                    writer.write(String.valueOf(arrayBuilder.build()));

                    break;

                case "COUNT":

                    ResultSet rsts = connection.prepareStatement("SELECT COUNT(*) FROM Customer").executeQuery();
                    while (rsts.next()){
                        writer.print(rsts.getInt(1));
                    }

                    break;

                case "GETIDS":

                    ResultSet rset = connection.prepareStatement("SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1").executeQuery();
                    while (rset.next()){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("customerId",rset.getString(1));
                        writer.print(objectBuilder.build());
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

        try {
            Connection connection = dataSource.getConnection();

            Customer customer = new Customer(
                    jsonObject.getString("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("gender"),
                    jsonObject.getString("contact"),
                    jsonObject.getString("nic"),
                    jsonObject.getString("address"),
                    jsonObject.getString("email")
            );

            if (new CustomerDAOImpl().addCustomer(customer,connection)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message", "Customer Successfully Added.");
                objectBuilder.add("status", resp.getStatus());
                writer.print(objectBuilder.build());
            }
            connection.close();

        } catch (SQLException throwables) {
            resp.setStatus(HttpServletResponse.SC_OK);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("data",throwables.getLocalizedMessage());
            objectBuilder.add("message","Error");
            objectBuilder.add("status",resp.getStatus());
            writer.print(objectBuilder.build());

            throwables.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/jason");
        PrintWriter writer = resp.getWriter();

        String cusId = req.getParameter("cusId");

        try {

            Connection connection = dataSource.getConnection();

            if (new CustomerDAOImpl().deleteCustomer(cusId,connection)) {

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("message","Customer Successfully Deleted.");
                objectBuilder.add("status",resp.getStatus());
                writer.print(objectBuilder.build());

            }else {

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message","Wrong Id Inserted.");
                objectBuilder.add("status",400);
                writer.print(objectBuilder.build());

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
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        try {
            Connection connection = dataSource.getConnection();

            Customer customer = new Customer(
                    jsonObject.getString("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("gender"),
                    jsonObject.getString("contact"),
                    jsonObject.getString("nic"),
                    jsonObject.getString("address"),
                    jsonObject.getString("email")
            );

            if (new CustomerDAOImpl().updateCustomer(customer,connection)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message","Customer Successfully Updated.");
                objectBuilder.add("status",resp.getStatus());
                writer.print(objectBuilder.build());

            }else{
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message","Update Failed.");
                objectBuilder.add("status",400);
                writer.print(objectBuilder.build());

            }
            connection.close();

        } catch (SQLException e) {

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
