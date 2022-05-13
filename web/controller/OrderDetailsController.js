function disableOrderFields() {
    $("#OID").prop('disabled',true);
    $("#ODate").prop('disabled',true);
    $("#cusId").prop('disabled',true);
    $("#itemQty").prop('disabled',true);
    $("#grossAmount").prop('disabled',true);
    $("#netAmount").prop('disabled',true);
}

$("#btnSearchOrders").click(function () {

    $("#tblOrderDetail tbody tr").empty();
    var trimid=$.trim($("#searchOrderId").val());

    var ifExists = false;
    for (var i = 0; i <orderDB.length ; i++) {
        if (trimid == orderDB[i].getOrderId()){
            ifExists = true;
        }
    }

    if (ifExists == true) {
        for (var i = 0; i < orderDB.length; i++) {
            if (trimid == orderDB[i].getOrderId()) {
                $("#cusId").val(orderDB[i].getOrderCusId());
                $("#OID").val(trimid);
                $("#ODate").val(orderDB[i].getOrderDate());
                $("#netAmount").val(orderDB[i].getNetTotal());
                $("#grossAmount").val(orderDB[i].getGrossTotal());
            }
        }

        for (var i = 0; i < orderDetailsDB.length; i++) {
            if (trimid == orderDetailsDB[i].getOrderDetailId()) {
                let raw = `<tr><td> ${orderDetailsDB[i].getOrderItemCode()} </td><td> ${orderDetailsDB[i].getOrderItemKind()} </td><td> ${orderDetailsDB[i].getOrderItemName()} </td><td> ${orderDetailsDB[i].getSellQty()} </td><td> ${orderDetailsDB[i].getOrderUnitPrice()} </td><td> ${orderDetailsDB[i].getDiscount()} </td><td> ${orderDetailsDB[i].getTotal()} </td></tr>`;
                $("#tblOrderDetail tbody").append(raw);
            }
        }

        $("#itemQty").val($("#tblOrderDetail tbody tr").length);

    }else if (ifExists == false){
        alert("No Such Order");
    }

});

$("#btnClear").click(function () {
    $("#tblOrderDetail tbody").empty();
    $("#cusId").val("");
    $("#OID").val("");
    $("#ODate").val("");
    $("#netAmount").val("");
    $("#grossAmount").val("");
    $("#itemQty").val("");
    $("#searchOrderId").val("");
});