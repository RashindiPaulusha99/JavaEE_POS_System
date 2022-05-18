$("#customerName").css('display','none');
$("#customerSec").css('display','none');
$("#itemName").css('display','none');
$("#itemSec").css('display','none');
$("#orderName").css('display','none');
$("#orderSec").css('display','none');
$("#orderDetailName").css('display','none');
$("#orderDetailSec").css('display','none');

count();
function count() {
    countCustomers();
    countItems();
    countOrders();
    income();
}

function countCustomers() {
    $.ajax({
        url:"http://localhost:8080/backend/customer?option=COUNT",
        method:"GET",
        success:function (response) {
            $("#customerCount").text(response);
        },
        error:function (ob, statusText, error) {
        }
    });
}

function countItems() {
    $.ajax({
        url:"http://localhost:8080/backend/item?option=COUNT",
        method:"GET",
        success:function (response) {
            $("#itemCount").text(response);
        },
        error:function (ob, statusText, error) {
        }
    });
}

function countOrders() {
    $.ajax({
        url:"http://localhost:8080/backend/purchaseOrder?option=COUNT",
        method:"GET",
        success:function (response) {
            $("#orderCount").text(response);
        },
        error:function (ob, statusText, error) {
        }
    });
}

function income(){
    $.ajax({
        url:"http://localhost:8080/backend/purchaseOrder?option=TOTAL",
        method:"GET",
        success:function (response) {
            $("#income").text(response+"/=");
        },
        error:function (ob, statusText, error) {
        }
    });
}

$("#customer").click(function () {
    $("#customerName").css('display','block');
    $("#customerSec").css('display','block');
    $("#itemName").css('display','none');
    $("#itemSec").css('display','none');
    $("#orderName").css('display','none');
    $("#orderSec").css('display','none');
    $("#homeSec").css('display','none');
    $("#name").css('display','none');
    $("#orderDetailName").css('display','none');
    $("#orderDetailSec").css('display','none');

    generateCustomerIds();
    $("#nameOfCustomer").focus();
});

$("#item").click(function () {
    $("#customerName").css('display','none');
    $("#customerSec").css('display','none');
    $("#itemName").css('display','block');
    $("#itemSec").css('display','block');
    $("#orderName").css('display','none');
    $("#orderSec").css('display','none');
    $("#homeSec").css('display','none');
    $("#name").css('display','none');
    $("#orderDetailName").css('display','none');
    $("#orderDetailSec").css('display','none');

    loadItemDetails();
    $("#kind").focus();
    generateItemCodes();
});

$("#home").click(function () {
    $("#customerName").css('display','none');
    $("#customerSec").css('display','none');
    $("#itemName").css('display','none');
    $("#itemSec").css('display','none');
    $("#orderName").css('display','none');
    $("#orderSec").css('display','none');
    $("#homeSec").css('display','block');
    $("#name").css('display','block');
    $("#orderDetailName").css('display','none');
    $("#orderDetailSec").css('display','none');

    count();
});

$("#order").click(function () {
    $("#customerName").css('display','none');
    $("#customerSec").css('display','none');
    $("#itemName").css('display','none');
    $("#itemSec").css('display','none');
    $("#orderName").css('display','block');
    $("#orderSec").css('display','block');
    $("#homeSec").css('display','none');
    $("#name").css('display','none');
    $("#orderDetailName").css('display','none');
    $("#orderDetailSec").css('display','none');

    loadCustomerIds();
    loadItemCodes();
    disableFields();
    generateOrderId();
});

$("#orderDetails").click(function () {
    $("#customerName").css('display','none');
    $("#customerSec").css('display','none');
    $("#itemName").css('display','none');
    $("#itemSec").css('display','none');
    $("#orderName").css('display','none');
    $("#orderSec").css('display','none');
    $("#homeSec").css('display','none');
    $("#name").css('display','none');
    $("#orderDetailName").css('display','block');
    $("#orderDetailSec").css('display','block');

    disableOrderFields();

});