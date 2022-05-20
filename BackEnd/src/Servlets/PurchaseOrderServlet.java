package Servlets;

import BO.BOFactory;
import BO.custom.PlaceOrderBO;
import DTO.OrderDTO;
import DTO.OrderDetailDTO;

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

    private PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACEORDER);

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

                    OrderDTO order = placeOrderBO.searchOrder(orderId, connection);

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("orderId",order.getOrderId());
                    objectBuilder.add("cusId",order.getCustomerId());
                    objectBuilder.add("orderDate",order.getOrderDate());
                    objectBuilder.add("grossTotal",order.getGrossTotal());
                    objectBuilder.add("netTotal",order.getNetTotal());

                    writer.write(String.valueOf(objectBuilder.build()));

                    break;

                case "SEARCHDETAILS":

                    ArrayList<OrderDetailDTO> orderDetails = placeOrderBO.searchOrderDetail(orderId, connection);

                    JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();

                    for (OrderDetailDTO orderDetail : orderDetails) {
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
                    obj.add("orderId",placeOrderBO.getOrderId(connection));
                    writer.print(obj.build());

                    break;

                case "COUNT":

                    writer.print(placeOrderBO.countOrders(connection));

                    break;

                case "TOTAL":

                    writer.print(placeOrderBO.findNetTotal(connection));

                    break;

                case "COUNTQTY":

                    writer.print(placeOrderBO.countQtyOnHand(orderId,connection));

                    break;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray items = jsonObject.getJsonArray("items");

        try {

          Connection  connection = dataSource.getConnection();

            ArrayList<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
            for (JsonValue item : items) {
                JsonObject jo = item.asJsonObject();
                orderDetailDTOS.add(new OrderDetailDTO(
                        jo.getString("oId"),
                        jo.getString("itemId"),
                        jo.getString("itemKind"),
                        jo.getString("itemName"),
                        Integer.parseInt(jo.getString("sellQty")),
                        Double.parseDouble(jo.getString("unitPrice")),
                        Integer.parseInt(jo.getString("itemDiscount")),
                        Double.parseDouble(jo.getString("total"))
                ));
            }

            OrderDTO orderDTO = new OrderDTO(
                    jsonObject.getString("orderId"),
                    jsonObject.getString("cusId"),
                    jsonObject.getString("orderDate"),
                    Double.parseDouble(jsonObject.getString("grossTotal")),
                    Double.parseDouble(jsonObject.getString("netTotal")),
                    orderDetailDTOS
            );

            if (placeOrderBO.placeOrder(orderDTO,connection)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("message", "Successfully Purchased Order.");
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

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
