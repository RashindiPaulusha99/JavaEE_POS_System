package Servlets;

import BO.BOFactory;
import BO.custom.ItemBO;
import DTO.ItemDTO;

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

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    private ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String itemCode = req.getParameter("itemCode");
        String option = req.getParameter("option");

        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            switch (option) {
                case "SEARCH":

                    ItemDTO item = itemBO.searchItem(itemCode, connection);

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("code", item.getItemCode());
                    objectBuilder.add("kind", item.getKind());
                    objectBuilder.add("itemName", item.getItemName());
                    objectBuilder.add("qtyOnHand", item.getQtyOnHand());
                    objectBuilder.add("unitPrice", item.getUnitPrice());

                    writer.write(String.valueOf(objectBuilder.build()));

                    break;

                case "GETALL":
                    ArrayList<ItemDTO> all = itemBO.getAllItems(connection);
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    for (ItemDTO i : all) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("code", i.getItemCode());
                        ob.add("kind", i.getKind());
                        ob.add("itemName", i.getItemName());
                        ob.add("qtyOnHand", i.getQtyOnHand());
                        ob.add("unitPrice", i.getUnitPrice());
                        arrayBuilder.add(ob.build());
                    }
                    writer.write(String.valueOf(arrayBuilder.build()));

                    break;

                case "COUNT":

                    writer.print(itemBO.countItem(connection));

                    break;

                case "GETIDS":

                    List<String> codes = itemBO.getItemCodes(connection);
                    for (String code : codes) {
                        JsonObjectBuilder obj = Json.createObjectBuilder();
                        obj.add("itemCode", code);
                        writer.print(obj.build());
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

            ItemDTO item = new ItemDTO(
                    jsonObject.getString("code"),
                    jsonObject.getString("kind"),
                    jsonObject.getString("itemName"),
                    Integer.parseInt(jsonObject.getString("qtyOnHand")),
                    Double.parseDouble(jsonObject.getString("unitPrice"))
            );

            if (itemBO.saveItem(item, connection)){
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message", "Item Successfully Added.");
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

        String itemCode = req.getParameter("itemCode");

        try {
            Connection connection = dataSource.getConnection();

            if (itemBO.deleteItem(itemCode,connection)) {

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

            ItemDTO item = new ItemDTO(
                    jsonObject.getString("code"),
                    jsonObject.getString("kind"),
                    jsonObject.getString("itemName"),
                    Integer.parseInt(jsonObject.getString("qtyOnHand")),
                    Double.parseDouble(jsonObject.getString("unitPrice"))
            );

            if (itemBO.updateItem(item, connection)) {
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
