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
}

function countCustomers() {
    $.ajax({
        url:"customer?option=COUNT",
        method:"GET",
        success:function (response) {
            $("#customerCount").text(response);
        },
        error:function (ob, statusText, error) {
            alert(statusText);
        }
    });
}

function countItems() {
    $.ajax({
        url:"item?option=COUNT",
        method:"GET",
        success:function (response) {
            $("#itemCount").text(response);
        },
        error:function (ob, statusText, error) {
            alert(statusText);
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

    $("#customerId").focus();
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
    $("#itemCode").focus();
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