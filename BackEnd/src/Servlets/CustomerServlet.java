package Servlets;

import BO.CustomerBOImpl;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    CustomerBOImpl customerBO = new CustomerBOImpl();

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

                    CustomerDTO customer = customerBO.searchCustomer(cusId, connection);

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("id",customer.getCustomerId());
                    objectBuilder.add("name",customer.getCustomerName());
                    objectBuilder.add("gender",customer.getGender());
                    objectBuilder.add("contact",customer.getContact());
                    objectBuilder.add("nic",customer.getNic());
                    objectBuilder.add("address",customer.getAddress());
                    objectBuilder.add("email",customer.getEmail());

                    writer.write(String.valueOf(objectBuilder.build()));

                 break;

                case "GETALL":

                    ArrayList<CustomerDTO> allCustomer = customerBO.getAllCustomers(connection);
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    for (CustomerDTO c : allCustomer) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("id",c.getCustomerId());
                        ob.add("name",c.getCustomerName());
                        ob.add("gender",c.getGender());
                        ob.add("contact",c.getContact());
                        ob.add("nic",c.getNic());
                        ob.add("address",c.getAddress());
                        ob.add("email",c.getEmail());
                        arrayBuilder.add(ob.build());
                    }
                    writer.write(String.valueOf(arrayBuilder.build()));

                    break;

                case "COUNT":

                    writer.print(customerBO.countCustomer(connection));

                    break;

                case "GETIDS":

                    List<String> ids = customerBO.getCustomerIds(connection);
                    for (String id : ids) {
                        JsonObjectBuilder objectBuilder1 = Json.createObjectBuilder();
                        objectBuilder1.add("customerId",id);
                        writer.print(objectBuilder1.build());
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

            CustomerDTO customer = new CustomerDTO(
                    jsonObject.getString("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("gender"),
                    jsonObject.getString("contact"),
                    jsonObject.getString("nic"),
                    jsonObject.getString("address"),
                    jsonObject.getString("email")
            );

            if (customerBO.saveCustomer(customer,connection)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message", "Customer Successfully Added.");
                objectBuilder.add("status", resp.getStatus());
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/jason");
        PrintWriter writer = resp.getWriter();

        String cusId = req.getParameter("cusId");

        try {

            Connection connection = dataSource.getConnection();

            if (customerBO.deleteCustomer(cusId,connection)) {

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

            CustomerDTO customer = new CustomerDTO(
                    jsonObject.getString("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("gender"),
                    jsonObject.getString("contact"),
                    jsonObject.getString("nic"),
                    jsonObject.getString("address"),
                    jsonObject.getString("email")
            );

            if (customerBO.updateCustomer(customer,connection)) {
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
