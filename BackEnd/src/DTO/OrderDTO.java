package DTO;

import java.util.ArrayList;

public class OrderDTO {
    private String orderId;
    private String customerId;
    private String orderDate;
    private double grossTotal;
    private double netTotal;
    private ArrayList<OrderDetailDTO> items;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String customerId, String orderDate, double grossTotal, double netTotal) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.grossTotal = grossTotal;
        this.netTotal = netTotal;
    }

    public OrderDTO(String orderId, String customerId, String orderDate, double grossTotal, double netTotal, ArrayList<OrderDetailDTO> items) {
        this.setOrderId(orderId);
        this.setCustomerId(customerId);
        this.setOrderDate(orderDate);
        this.setGrossTotal(grossTotal);
        this.setNetTotal(netTotal);
        this.setItems(items);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(double grossTotal) {
        this.grossTotal = grossTotal;
    }

    public double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
    }

    public ArrayList<OrderDetailDTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderDetailDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", grossTotal=" + grossTotal +
                ", netTotal=" + netTotal +
                ", items=" + items +
                '}';
    }
}
