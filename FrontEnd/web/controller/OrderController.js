function disableFields() {
    $("#orderCusName").prop('disabled', true);
    $("#orderId").prop('disabled', true);
    $("#orderCusContact").prop('disabled', true);
    $("#orderCusId").prop('disabled', true);
    $("#orderCusAddress").prop('disabled', true);
    $("#orderCusNIC").prop('disabled', true);
    $("#orderItemName").prop('disabled', true);
    $("#orderItemCode").prop('disabled', true);
    $("#orderKind").prop('disabled', true);
    $("#orderPrice").prop('disabled', true);
    $("#orderQty").prop('disabled', true);
    $("#gross").prop('disabled', true);
    $("#net").prop('disabled', true);
}

var now = new Date();

var day = ("0" + now.getDate()).slice(-2);
var month = ("0" + (now.getMonth() + 1)).slice(-2);
var today = now.getFullYear() + "-" + (month) + "-" + (day);
$('#orderDate').val(today);

function generateOrderId() {

    $("#orderId").val("O00-0001");

    $.ajax({
        url: "http://localhost:8080/backend/purchaseOrder?option=GETIDS",
        method: "GET",
        success: function (response) {
            var orderId = response.orderId;
            var tempId = parseInt(orderId.split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                $("#orderId").val("O00-000" + tempId);
            } else if (tempId <= 99) {
                $("#orderId").val("O00-00" + tempId);
            } else if (tempId <= 999) {
                $("#orderId").val("O00-0" + tempId);
            } else {
                $("#orderId").val("O00-" + tempId);
            }
        },
        error: function (ob, statusText, error) {

        }
    });

}

$("#btnNew").click(function () {
    netAmount = 0;
    grossAmount = 0;

    generateOrderId();

    $("#orderItemName").val("");
    $("#orderItemCode").val("");
    $("#orderKind").val("");
    $("#orderQty").val("");
    $("#orderPrice").val("");
    $("#sellQty").val("");
    $("#itemDiscount").val("");
    $("#orderCusName").val("");
    $("#orderCusId").val("");
    $("#orderCusAddress").val("");
    $("#orderCusNIC").val("");
    $("#orderCusContact").val("");
    $("#searchOrder").val("");
    $("#gross").val("");
    $("#net").val("");
    $("#cash").val("");
    $("#discount").val("");
    $("#balance").val("");
    $("#tblOrder tbody").empty();

    $("#sellQty").css('border', '2px solid transparent');
    $("#itemDiscount").css('border', '2px solid transparent');
    $("#discount").css('border', '2px solid transparent');
    $("#cash").css('border', '2px solid transparent');
});

/*--------------------------*/

$("#btnSearchOrder").click(function () {
    $("#tblOrder tbody tr").empty();
    var oid = $.trim($("#searchOrder").val());
    searchOrder(oid);
    searchOrderDetails(oid);
});

function searchOrder(oid) {
    $.ajax({
        url: "http://localhost:8080/backend/purchaseOrder?option=SEARCH&orderId=" + oid,
        method: "GET",
        success: function (response) {
            $("#orderCusId").val(response.cusId);
            $("#orderId").val(response.orderId);
            $("#orderDate").val(response.orderDate);
            $("#net").val(response.grossTotal);
            $("#gross").val(response.netTotal);

            searchCustomerDetail(response.cusId);
        },
        error: function (ob, statusText, error) {
            alert("No Such Order");
        }
    });
}

function searchCustomerDetail(cusId) {
    $.ajax({
        url: "http://localhost:8080/backend/customer?option=SEARCH&cusId=" + cusId,
        method: "GET",
        success: function (response) {
            $("#orderCusId").val(response.id);
            $("#orderCusName").val(response.name);
            $("#orderCusAddress").val(response.address);
            $("#orderCusContact").val(response.contact);
            $("#orderCusNIC").val(response.nic);
        },
        error: function (ob, statusText, error) {
            alert("No Such Customer");
        }
    });
}

function searchOrderDetails(oid) {
    $.ajax({
        url: "http://localhost:8080/backend/purchaseOrder?option=SEARCHDETAILS&orderId=" + oid,
        method: "GET",
        success: function (response) {
            $("#tblOrder tbody").empty()
            for (var oDetails of response) {
                let raw = `<tr><td> ${oDetails.itemId} </td><td> ${oDetails.itemKind} </td><td> ${oDetails.itemName} </td><td> ${oDetails.sellQty} </td><td> ${oDetails.unitPrice} </td><td> ${oDetails.itemDiscount} </td><td> ${oDetails.total} </td><td> <input id='btnEdit' class='btn btn-success btn-sm' value='Update' style="width: 75px"/> </td><td> <input id='btnDelete' class='btn btn-danger btn-sm' value='Delete' style="width: 75px"/> </td></tr>`;
                $("#tblOrder tbody").append(raw);
            }
        },
        error: function (ob, statusText, error) {
            alert("No Such Order Details.");
        }
    });
}

/*-------------------Customer Sec-----------------------*/

function loadCustomerIds() {
    $("#ids").empty();
    $("#ids").append($("<option></option>").attr("value", 0).text("Select ID"));

    var countCustomerIds = 1;

    $.ajax({
        url: "http://localhost:8080/backend/customer?option=GETALL",
        method: "GET",
        success: function (response) {
            for (var ids of response) {
                $("#ids").append($("<option></option>").attr("value", countCustomerIds).text(ids.id));
                countCustomerIds++;
            }
        },
        error: function (ob, statusText, error) {
        }
    });
}

$("#ids").click(function () {
    $.ajax({
        url: "http://localhost:8080/backend/customer?option=SEARCH&cusId=" + $("#ids option:selected").text(),
        method: "GET",
        success: function (response) {
            $("#orderCusName").val(response.name);
            $("#orderCusId").val(response.id);
            $("#orderCusContact").val(response.contact);
            $("#orderCusNIC").val(response.nic);
            $("#orderCusAddress").val(response.address);
        },
        error: function (ob, statusText, error) {
        }
    });
});

/*-------------------Item Sec-----------------------*/

var regExSellQuantity = /^[0-9]{1,20}$/;
var regExDiscount = /^[0-9]{1,2}$/;

function loadItemCodes() {
    $("#codes").empty();
    $("#codes").append($("<option></option>").attr("value", 0).text("Select Code"));

    var countItemCodes = 1;

    $.ajax({
        url: "http://localhost:8080/backend/item?option=GETALL",
        method: "GET",
        success: function (response) {
            for (var codes of response) {
                $("#codes").append($("<option></option>").attr("value", countItemCodes).text(codes.code));
                countItemCodes++;
            }
        },
        error: function (ob, statusText, error) {
        }
    });
}

$("#codes").click(function () {
    clickCodes($("#codes option:selected").text());
});

function clickCodes(code) {
    $.ajax({
        url: "http://localhost:8080/backend/item?option=SEARCH&itemCode=" + code,
        method: "GET",
        success: function (response) {
            $("#orderItemCode").val(response.code);
            $("#orderKind").val(response.kind);
            $("#orderItemName").val(response.itemName);

            if ($("#tblOrder tbody tr").length == 0){
                $("#orderQty").val(response.qtyOnHand);
            }else {
                for (var i = 0; i < $("#tblOrder tbody tr").length; i++) {
                    if ($("#tblOrder tbody tr").children(':nth-child(1)')[i].innerText == code){
                        $("#orderQty").val(response.qtyOnHand - parseInt($("#tblOrder tbody tr").children(':nth-child(4)')[i].innerText))
                    }else {
                        $("#orderQty").val(response.qtyOnHand);
                    }
                }
            }

            $("#orderPrice").val(response.unitPrice);
        },
        error: function (ob, statusText, error) {
        }
    });
}

$("#sellQty").keyup(function (event) {

    let sellQty = $("#sellQty").val();
    if (regExSellQuantity.test(sellQty)) {
        $("#sellQty").css('border', '2px solid blue');
        $("#errorSellQty").text("");
        if (event.key == "Enter") {
            $("#itemDiscount").focus();
        }
    } else {
        $("#sellQty").css('border', '2px solid red');
        $("#errorSellQty").text("Quantity is a required field: Pattern 00");
    }
});

$("#itemDiscount").keyup(function () {

    let itemDiscount = $("#itemDiscount").val();
    if (regExDiscount.test(itemDiscount)) {
        $("#itemDiscount").css('border', '2px solid blue');
        $("#errordiscount").text("");
    } else {
        $("#itemDiscount").css('border', '2px solid red');
        $("#errordiscount").text("ItemDiscount is a required field: Pattern 0");
    }
});

/*-------------------Order Table-----------------------*/

/* if add new row , qtyOnHand changes */
function manageAddQty(qty) {
    var qtyOnHand = parseInt($("#orderQty").val());
    qtyOnHand -= parseInt(qty);
    $("#orderQty").val(qtyOnHand);
}

/* if update previous row , qtyOnHand changes */
function manageQty(qty) {
    var nowQty = parseInt(qty);
    var qtyOnHand = parseInt($("#orderQty").val());
    qtyOnHand -= nowQty;
    $("#orderQty").val(qtyOnHand);
}

/* if delete a row , qtyOnHand changes */
function manageReduceQty(qty) {
    var qtyOnHand = parseInt($("#orderQty").val());
    qtyOnHand += parseInt(qty);
    $("#orderQty").val(qtyOnHand);
}

/*----------gross amount----------*/

/* if add new gross*/
var grossAmount = 0;

function calculateGrossAmount(gross) {
    grossAmount += gross;
    $("#gross").val(grossAmount);
}

/* if update new gross*/
function updateGrossAmount(nowGross) {
    grossAmount += nowGross;
    $("#gross").val(grossAmount);
}

/* if delete gross*/
function deleteGrossAmount(gross) {
    grossAmount -= gross;
    $("#gross").val(grossAmount);
}

/*--------net amount-----------*/

/* if add new net*/
var netAmount = 0;

function calculateNetAmount(net) {
    netAmount += net;
    $("#net").val(netAmount);
}

/* if update new net*/
function updateNetAmount(nowNet) {
    netAmount += nowNet;
    $("#net").val(netAmount);
}

/* if delete net*/
function deleteNetAmount(net) {
    netAmount -= net;
    $("#net").val(netAmount);
}

/*-----------------------------*/

var tblOrderRow;
var click = "not clicked";

$("#btnAddCart").click(function () {

    $("#tblOrder tbody > tr").off("click");
    $("#tblOrder tbody").off("click", '#btnDelete');

    if ($("#errorSellQty").text() != "" || $("#errorOrderId").text() != "" || $("#errordiscount").text() != "" || $("#ids option:selected").val() == "" ||
        $("#codes option:selected").val() == "" || $("#sellQty").val() == "" || $("#orderId").val() == "" || $("#orderDate").val() == "") {
        $("#btnAddCart").disable();
    } else {

        let text = "Do you really want to add to cart this Item?";

        if (confirm(text) == true) {

            let itemCode = $("#orderItemCode").val();
            let kind = $("#orderKind").val();
            let itemName = $("#orderItemName").val();
            let unitPrice = $("#orderPrice").val();
            var sellQty = $("#sellQty").val();
            var gross = sellQty * unitPrice;
            let discount = $("#itemDiscount").val();
            var net = gross - (gross * discount / 100);

            var ifDuplicate = false;

            for (var i = 0; i < $("#tblOrder tbody tr").length; i++) {
                if ($("#orderItemCode").val() == $("#tblOrder tbody tr").children(':nth-child(1)')[i].innerText) {
                    ifDuplicate = true;
                }
            }

            if (ifDuplicate != true) { /*that means new item*/

                if (parseInt($("#orderQty").val()) >= sellQty || parseInt($("#orderQty").val()) < 0) {

                    manageAddQty(sellQty);
                    calculateGrossAmount(gross);
                    calculateNetAmount(net);

                    let raw = `<tr><td> ${itemCode} </td><td> ${kind} </td><td> ${itemName} </td><td> ${sellQty} </td><td> ${unitPrice} </td><td> ${discount} </td><td> ${net} </td><td> <input id='btnEdit' class='btn btn-success btn-sm' value='Update' style="width: 75px"/> </td><td> <input id='btnDelete' class='btn btn-danger btn-sm' value='Delete' style="width: 75px"/> </td></tr>`;
                    $("#tblOrder tbody").append(raw);

                } else if (parseInt($("#orderQty").val()) < sellQty || parseInt($("#orderQty").val()) < 0) {
                    alert("No Enough Quantity.");
                }

                clearFieldsWhenAddItem();
                deleteRow();

            } else if (ifDuplicate == true) {

                if (click == "clicked") {

                    if (parseInt($("#orderQty").val()) >= sellQty || parseInt($("#orderQty").val()) < 0) {

                        let newVar = parseInt(sellQty) + parseInt($(tblOrderRow).children(':nth-child(4)').text());
                        let previousGross = parseInt($(tblOrderRow).children(':nth-child(4)').text()) * unitPrice;
                        let netNew = (previousGross + parseInt(net));
                        manageQty(sellQty);
                        updateGrossAmount(gross);
                        updateNetAmount(net);

                        $(tblOrderRow).children(':nth-child(4)').text(parseInt(newVar));
                        $(tblOrderRow).children(':nth-child(7)').text(parseInt(netNew));

                    } else if (parseInt($("#orderQty").val()) < sellQty || parseInt($("#orderQty").val()) < 0) {
                        alert("No Enough Quantity");
                    }

                    clearFieldsWhenAddItem();
                    deleteRow();

                } else if (click == "not clicked") {
                    alert("Please Select A Row.");
                }
            }
        } else {

        }
    }

    clickRow();

});

function deleteRow() {
    $("#tblOrder tbody").on('click', '#btnDelete', function () {

        let text = "Are you sure you want to remove this Item from cart?";

        if (confirm(text) == true) {
            tblOrderRow.remove();

            manageReduceQty(tblOrderRow.children(':nth-child(4)').text());
            var preGross = parseInt($(tblOrderRow).children(':nth-child(4)').text()) * $("#orderPrice").val();
            deleteGrossAmount(preGross);
            deleteNetAmount(parseInt($(tblOrderRow).children(':nth-child(7)').text()));

            clearFieldsWhenAddItem();
        } else {

        }
    });
}

function clickRow() {
    $("#tblOrder tbody > tr").click(function () {
        tblOrderRow = $(this);
        click = "clicked";

        let code = $.trim(tblOrderRow.children(':nth-child(1)').text());
        let kind = $.trim(tblOrderRow.children(':nth-child(2)').text());
        let iName = $.trim(tblOrderRow.children(':nth-child(3)').text());
        let sellqty = $.trim(tblOrderRow.children(':nth-child(4)').text());
        let price = $.trim(tblOrderRow.children(':nth-child(5)').text());
        let discount = $.trim(tblOrderRow.children(':nth-child(6)').text());

        $("#orderItemCode").val(code);
        $("#orderKind").val(kind);
        $("#orderItemName").val(iName);
        $("#sellQty").val(sellqty);
        $("#orderPrice").val(price);
        $("#itemDiscount").val(discount);

        updateQtyOnHandWhenClickRow($("#orderItemCode").val(), sellqty);

    });
}

function updateQtyOnHandWhenClickRow(code, sellqty) {
    $.ajax({
        url: "http://localhost:8080/backend/item?option=SEARCH&itemCode=" + code,
        method: "GET",
        success: function (response) {
            $("#orderQty").val((response.qtyOnHand - sellqty));
        },
        error: function (ob, statusText, error) {
        }
    });
}

$("#btnClearCart").click(function () {
    clearFieldsWhenAddItem()
});

function clearFieldsWhenAddItem() {
    $("#orderItemName").val("");
    $("#orderItemCode").val("");
    $("#orderKind").val("");
    $("#orderQty").val("");
    $("#orderPrice").val("");
    $("#sellQty").val("");
    $("#itemDiscount").val("");

    $("#sellQty").css('border', '2px solid transparent');
    $("#itemDiscount").css('border', '2px solid transparent');
}

/*-------------------Final Total----------------------*/

var regExCash = /^[0-9]{2,10}(.)[0-9]{2}$/;
var regExFinalDiscount = /^[0-9]{1,2}$/;

$("#cash").keyup(function (event) {

    let cash = $("#cash").val();
    if (regExCash.test(cash)) {
        $("#cash").css('border', '2px solid blue');
        $("#errorCash").text("");
        if (event.key == "Enter") {
            $("#discount").focus();
        }
    } else {
        $("#cash").css('border', '2px solid red');
        $("#errorCash").text("Cash is a required field: Pattern 00.00");
    }
});

$("#discount").keyup(function () {

    let discount = $("#discount").val();
    if (regExFinalDiscount.test(discount)) {
        $("#discount").css('border', '2px solid blue');
        $("#errorFinalDiscount").text("");
    } else {
        $("#discount").css('border', '2px solid red');
        $("#errorFinalDiscount").text("Discount is a required field: Pattern 0");
    }
});

$("#btnPurchase").click(function () {

    if ($("#errorSellQty").text() != "" || $("#errorOrderId").text() != "" || $("#errordiscount").text() != "" || $("#errorCash").text() != "" ||
        $("#errorFinalDiscount").text() != "" || $("#ids option:selected").val() == "" || $("#orderId").val() == "" || $("#orderDate").val() == "" ||
        $("#gross").val() == "" || $("#net").val() == "" || $("#cash").val() == "" || $("#discount").val() == "") {
        $("#btnPurchase").disable();
    } else {
        let text = "Do you really want to place order?";

        if (confirm(text) == true) {

            searchOrderIdForPurchase();

            manageBalance();

        } else if (confirm(text) == false) {

            generateOrderId();
            netAmount = 0;
            grossAmount = 0;

            clearAll();

        }
    }
});

function manageBalance() {
    let net = parseInt($("#net").val());
    let cash = parseInt($("#cash").val());
    let discount = parseInt($("#discount").val());
    var rest = net - (net * discount / 100);
    var balance = cash - rest;
    $("#balance").val(balance);

    $("#btnAddCart").attr('disabled', false);
}

function searchOrderIdForPurchase() {
    $.ajax({
        url: "http://localhost:8080/backend/purchaseOrder?option=SEARCH&orderId=" + $("#orderId").val(),
        method: "GET",
        success: function (response) {
            alert("Something Wrong.");
        },
        error: function (ob, statusText, error) {
            addDataToOrderDB();
        }
    });
}

function addDataToOrderDB() {

    let details = new Array();
    for (var i = 0; i < $("#tblOrder tbody tr").length; i++) {
        var orderDetail = {
            oId: $("#orderId").val(),
            itemId: $("#tblOrder tbody tr").children(':nth-child(1)')[i].innerText,
            itemKind: $("#tblOrder tbody tr").children(':nth-child(2)')[i].innerText,
            itemName: $("#tblOrder tbody tr").children(':nth-child(3)')[i].innerText,
            sellQty: $("#tblOrder tbody tr").children(':nth-child(4)')[i].innerText,
            unitPrice: $("#tblOrder tbody tr").children(':nth-child(5)')[i].innerText,
            itemDiscount: $("#tblOrder tbody tr").children(':nth-child(6)')[i].innerText,
            total: $("#tblOrder tbody tr").children(':nth-child(7)')[i].innerText
        }
        details.push(orderDetail);
    }

    var order={
        orderId:$("#orderId").val(),
        orderDate:$("#orderDate").val(),
        cusId:$("#orderCusId").val(),
        grossTotal:$("#gross").val(),
        netTotal:$("#net").val(),
        items:details
    }

    $.ajax({
        url:"http://localhost:8080/backend/purchaseOrder?",
        method:"POST",
        contentType:"application/json",
        data: JSON.stringify(order),
        success:function (response) {
            if (response.status == 200){
                if (response.message == "Successfully Purchased Order."){
                    alert(response.message);

                }else if (response.message == "Error"){
                    alert(response.data);
                }
            }else if (response.status == "400"){
                alert(response.data);
            }
        },
        error:function (ob , statusText , error) {
            alert(statusText);
        }
    });
}

function clearAll() {

    clearFieldsWhenAddItem();

    $("#orderCusName").val("");
    $("#orderCusId").val("");
    $("#orderCusAddress").val("");
    $("#orderCusNIC").val("");
    $("#orderCusContact").val("");
    $("#searchOrder").val("");
    $("#gross").val("");
    $("#net").val("");
    $("#cash").val("");
    $("#discount").val("");
    $("#balance").val("");

    $("#tblOrder tbody").empty();

    $("#discount").css('border', '2px solid transparent');
    $("#cash").css('border', '2px solid transparent');
}
