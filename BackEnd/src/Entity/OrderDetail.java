package Entity;

public class OrderDetail {
    private String orderId;
    private String itemCode;
    private String kind;
    private String itemName;
    private int sellQty;
    private double unitPrice;
    private int itemDiscount;
    private double total;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String itemCode, String kind, String itemName, int sellQty, double unitPrice, int itemDiscount, double total) {
        this.setOrderId(orderId);
        this.setItemCode(itemCode);
        this.setKind(kind);
        this.setItemName(itemName);
        this.setSellQty(sellQty);
        this.setUnitPrice(unitPrice);
        this.setItemDiscount(itemDiscount);
        this.setTotal(total);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSellQty() {
        return sellQty;
    }

    public void setSellQty(int sellQty) {
        this.sellQty = sellQty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(int itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId='" + orderId + '\'' +
                "itemCode='" + itemCode + '\'' +
                ", kind='" + kind + '\'' +
                ", itemName='" + itemName + '\'' +
                ", sellQty=" + sellQty +
                ", unitPrice=" + unitPrice +
                ", itemDiscount=" + itemDiscount +
                ", total=" + total +
                '}';
    }
}
