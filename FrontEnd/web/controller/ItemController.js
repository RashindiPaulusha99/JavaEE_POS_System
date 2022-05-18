var regExKind = /^[A-Z|a-z\s]{3,20}$/;
var regExItemName = /^[A-Z|a-z\s]{3,20}$/;
var regExQuantity = /^[0-9]{2,20}$/;
var regExUnitPrice = /^[0-9]{1,10}(.)[0-9]{2}$/;

$("#itemCode").prop('disabled', true);

$("#kind").keyup(function (event) {

    let kind = $("#kind").val();
    if (regExKind.test(kind)) {
        $("#kind").css('border', '2px solid blue');
        $("#errorKind").text("");
        if (event.key == "Enter") {
            $("#nameOfItem").focus();
        }
    } else {
        $("#kind").css('border', '2px solid red');
        $("#errorKind").text("Kind is a required field: Min 5");
    }
});

$("#nameOfItem").keyup(function (event) {

    let itemName = $("#nameOfItem").val();
    if (regExItemName.test(itemName)) {
        $("#nameOfItem").css('border', '2px solid blue');
        $("#errorItemName").text("");
        if (event.key == "Enter") {
            $("#qty").focus();
        }
    } else {
        $("#nameOfItem").css('border', '2px solid red');
        $("#errorItemName").text("Item Name is a required field: Min 3");
    }
});

$("#qty").keyup(function (event) {

    let qty = $("#qty").val();
    if (regExQuantity.test(qty)) {
        $("#qty").css('border', '2px solid blue');
        $("#errorQty").text("");
        if (event.key == "Enter") {
            $("#unitPrice").focus();
        }
    } else {
        $("#qty").css('border', '2px solid red');
        $("#errorQty").text("Quantity is a required field: Pattern 00");
    }
});

$("#unitPrice").keyup(function (event) {

    $("#tblItem tbody > tr").off("click");
    $("#tblItem tbody > tr").off("dblclick");

    let unitPrice = $("#unitPrice").val();
    if (regExUnitPrice.test(unitPrice)) {
        $("#unitPrice").css('border', '2px solid blue');
        $("#errorPrice").text("");

        if (event.key == "Enter") {
            saveItem();
        }
    } else {
        $("#unitPrice").css('border', '2px solid red');
        $("#errorPrice").text("Unit Price is a required field: Pattern 00.00");
    }
});

$('#itemCode,#kind,#nameOfItem,#qty,#unitPrice').keydown(function (e) {
    if (e.key == "Tab") {
        e.preventDefault();
    }
});

function generateItemCodes() {
    $("#itemCode").val("I00-0001");

    $.ajax({
        url: "http://localhost:8080/backend/item?option=GETIDS",
        method: "GET",
        success: function (response) {
            var itemCode = response.itemCode;
            var tempId = parseInt(itemCode.split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                $("#itemCode").val("I00-000" + tempId);
            } else if (tempId <= 99) {
                $("#itemCode").val("I00-00" + tempId);
            } else if (tempId <= 999) {
                $("#itemCode").val("I00-0" + tempId);
            } else {
                $("#itemCode").val("I00-" + tempId);
            }
        },
        error: function (ob, statusText, error) {
        }

    });
}

var tblItemRow;

$("#btnSaveItem").click(function () {

    $("#tblItem tbody > tr").off("click");
    $("#tblItem tbody > tr").off("dblclick");

    if ($("#errorCode").text() != "" || $("#errorKind").text() != "" || $("#errorItemName").text() != "" || $("#errorQty").text() != "" || $("#errorPrice").text() != "" ||
        $("#itemCode").val() == "" || $("#nameOfItem").val() == "" || $("#kind").val() == "" || $("#qty").val() == "" || $("#unitPrice").val() == "") {
        $("#btnSaveItem").disable();
    } else {
        saveItem();
    }

});

function saveItem() {

    let text = "Do you really want to save this Item?";

    if (confirm(text) == true) {

        addItemToDB();

    } else {

    }
}

function addItemToDB() {
    var itemDetails = {
        code: $("#itemCode").val(),
        kind: $("#kind").val(),
        itemName: $("#nameOfItem").val(),
        qtyOnHand: $("#qty").val(),
        unitPrice: $("#unitPrice").val()
    }

    $.ajax({
        url: "http://localhost:8080/backend/item",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(itemDetails),
        success: function (response) {
            if (response.status == 200) {
                if (response.message == "Item Successfully Added.") {
                    alert($("#itemCode").val() + " " + response.message);
                } else if (response.message == "Error") {
                    alert(response.data + " " + "Already Exists.");
                }
            } else if (response.status == "400") {
                alert(response.data);
            }
            loadItemDetails();
        },
        error: function (ob, statusText, error) {
            alert(statusText);
            loadItemDetails();
        }
    });
}

function loadItemDetails() {
    $.ajax({
        url: "http://localhost:8080/backend/item?option=GETALL",
        method: "GET",
        success: function (response) {

            $("#tblItem tbody").empty();
            for (var responseKey of response) {
                let raw = `<tr><td> ${responseKey.code} </td><td> ${responseKey.kind} </td><td> ${responseKey.itemName} </td><td> ${responseKey.qtyOnHand} </td><td> ${responseKey.unitPrice} </td></tr>`;
                $("#tblItem tbody").append(raw);
            }
            clearItems();
            clickEventForItem();
        },
        error: function (ob, statusText, error) {
            alert(statusText);
        }
    });
}

function clearItems() {
    $("#itemCode").val("");
    $("#kind").val("");
    $("#nameOfItem").val("");
    $("#qty").val("");
    $("#unitPrice").val("");
    $("#searchItem").val("");

    $("#itemCode").css('border', '2px solid transparent');
    $("#kind").css('border', '2px solid transparent');
    $("#nameOfItem").css('border', '2px solid transparent');
    $("#qty").css('border', '2px solid transparent');
    $("#unitPrice").css('border', '2px solid transparent');
}

function clickEventForItem() {
    $("#tblItem tbody > tr").click(function () {

        tblItemRow = $(this);

        var code = $.trim(tblItemRow.children(':nth-child(1)').text());
        var kind = $.trim(tblItemRow.children(':nth-child(2)').text());
        var iName = $.trim(tblItemRow.children(':nth-child(3)').text());
        var qty = $.trim(tblItemRow.children(':nth-child(4)').text());
        var price = $.trim(tblItemRow.children(':nth-child(5)').text());

        $("#itemCode").val(code);
        $("#kind").val(kind);
        $("#nameOfItem").val(iName);
        $("#qty").val(qty);
        $("#unitPrice").val(price);
    });

    $("#tblItem tbody > tr").dblclick(function () {

        let text = "Are you sure you want to delete this Item?";
        if (confirm(text) == true) {
            tblItemRow.remove();
            deleteItem();
        } else {

        }
    });
}

$("#btnClearItem").click(function () {
    clearItems();
});

$("#btnDeleteItem").click(function () {

    if ($("#errorCode").text() != "" || $("#errorKind").text() != "" || $("#errorItemName").text() != "" || $("#errorQty").text() != "" || $("#errorPrice").text() != "" ||
        $("#itemCode").val() == "" || $("#nameOfItem").val() == "" || $("#kind").val() == "" || $("#qty").val() == "" || $("#unitPrice").val() == "") {
        $("#btnDeleteItem").disable();
    } else {

        let text = "Are you sure you want to delete this Item?";
        if (confirm(text) == true) {
            tblItemRow.remove();
            deleteItem();
        } else {

        }
    }

});

function deleteItem() {

    searchIfItemAlreadyExists();

    $.ajax({
        url: "http://localhost:8080/backend/item?itemCode=" + $("#itemCode").val(),
        method: "DELETE",
        success: function (resp) {
            if (search == true) {
                alert($("#itemCode").val() + " " + "Item Successfully Deleted.");
                loadItemDetails();
            }
        },
        error: function (ob, statusText, error) {
            alert(statusText);
            loadItemDetails();
        }
    });
}

var search = false;

function searchIfItemAlreadyExists() {
    $.ajax({
        url: "http://localhost:8080/backend/item?option=SEARCH&itemCode=" + $("#itemCode").val(),
        method: "GET",
        success: function (response) {
            if (response.code == $("#itemCode").val()) {
                search = true;
            }
        },
        error: function (ob, statusText, error) {
            search = false;
            alert("No Such Item");
            loadItemDetails();
        }
    });
}

$("#btnEditItem").click(function () {

    if ($("#errorCode").text() != "" || $("#errorKind").text() != "" || $("#errorItemName").text() != "" || $("#errorQty").text() != "" || $("#errorPrice").text() != "" ||
        $("#itemCode").val() == "" || $("#nameOfItem").val() == "" || $("#kind").val() == "" || $("#qty").val() == "" || $("#unitPrice").val() == "") {
        $("#btnEditItem").disable();
    } else {

        let text = "Do you really want to update this Item?";

        if (confirm(text) == true) {

            let itemCode = $("#itemCode").val();
            let kind = $("#kind").val();
            let itemName = $("#nameOfItem").val();
            let qty = $("#qty").val();
            let unitPrice = $("#unitPrice").val();

            $(tblItemRow).children(':nth-child(1)').text(itemCode);
            $(tblItemRow).children(':nth-child(2)').text(kind);
            $(tblItemRow).children(':nth-child(3)').text(itemName);
            $(tblItemRow).children(':nth-child(4)').text(qty);
            $(tblItemRow).children(':nth-child(5)').text(unitPrice);

            updateItem();

        } else {

        }
    }

});

function updateItem() {
    var itemDetails = {
        code: $("#itemCode").val(),
        kind: $("#kind").val(),
        itemName: $("#nameOfItem").val(),
        qtyOnHand: $("#qty").val(),
        unitPrice: $("#unitPrice").val()
    }

    $.ajax({
        url: "http://localhost:8080/backend/item",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(itemDetails),
        success: function (response) {
            console.log(response);
            if (response.status == 200) {
                alert($("#itemCode").val() + " " + response.message);
            } else if (response.status == 400) {
                alert(response.message);
            } else {
                alert(response.data);
            }
            loadItemDetails();
        },
        error: function (ob, statusText, error) {
            alert(statusText);
            loadItemDetails();
        }
    });
}

$("#btnSearchItem").click(function () {
    $.ajax({
        url: "http://localhost:8080/backend/item?option=SEARCH&itemCode=" + $("#searchItem").val(),
        method: "GET",
        success: function (response) {
            $("#itemCode").val(response.code);
            $("#nameOfItem").val(response.itemName);
            $("#kind").val(response.kind);
            $("#qty").val(response.qtyOnHand);
            $("#unitPrice").val(response.unitPrice);
        },
        error: function (ob, statusText, error) {
            alert("No Such Item");
            loadItemDetails();
        }
    });
});

$("#btnViewItem").click(function () {
    loadItemDetails();
});