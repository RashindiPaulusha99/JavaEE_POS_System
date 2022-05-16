function disableOrderFields() {
    $("#OID").prop('disabled',true);
    $("#ODate").prop('disabled',true);
    $("#cusId").prop('disabled',true);
    $("#itemQty").prop('disabled',true);
    $("#grossAmount").prop('disabled',true);
    $("#netAmount").prop('disabled',true);
}

$("#btnSearchOrders").click(function () {

    $("#tblOrderDetail tbody").empty();
    var oid = $.trim($("#searchOrderId").val());

    searchORDER(oid);
    searchDetailsForOrder(oid);

});

function searchORDER(oid) {
    $.ajax({
        url: "purchaseOrder?option=SEARCH&orderId=" + oid,
        method: "GET",
        success: function (response) {
            console.log("search");
            $("#cusId").val(response.cusId);
            $("#OID").val(response.orderId);
            $("#ODate").val(response.orderDate);
            $("#grossAmount").val(response.grossTotal);
            $("#netAmount").val(response.netTotal);

            searchItemQty(oid);
        },
        error: function (ob, statusText, error) {
            alert("No Such Order");
        }
    });
}

function searchItemQty(oid) {
    $.ajax({
        url: "purchaseOrder?option=COUNTQTY&orderId=" + oid,
        method: "GET",
        success: function (response) {
            console.log("search");
            $("#itemQty").val(response);
        },
        error: function (ob, statusText, error) {
            alert("No Quantity.");
        }
    });
}

function searchDetailsForOrder(oid) {
    $.ajax({
        url: "purchaseOrder?option=SEARCHDETAILS&orderId=" + oid,
        method: "GET",
        success: function (response) {
            console.log(response);
            $("#tblOrderDetail tbody").empty()
            for (var oDetails of response) {
                let raw = `<tr><td> ${oDetails.itemId} </td><td> ${oDetails.itemKind} </td><td> ${oDetails.itemName} </td><td> ${oDetails.sellQty} </td><td> ${oDetails.unitPrice} </td><td> ${oDetails.itemDiscount} </td><td> ${oDetails.total} </td></tr>`;
                $("#tblOrderDetail tbody").append(raw);
            }
        },
        error: function (ob, statusText, error) {
            alert("No Such Order Details.");
        }
    });
}


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