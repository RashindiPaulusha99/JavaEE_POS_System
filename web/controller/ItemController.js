function loadItemDetails() {
    $("#tblItem tbody").empty();

    for (var i = 0; i < itemDB.length; i++) {
        let raw = `<tr><td> ${itemDB[i].getItemCode()} </td><td> ${itemDB[i].getKind()} </td><td> ${itemDB[i].getItemName()} </td><td> ${itemDB[i].getQtyOnHand()} </td><td> ${itemDB[i].getUnitPrice()} </td></tr>`;
        $("#tblItem tbody").append(raw);
    }
}

var regExItemID=/^(I00-)[0-9]{3,4}$/;
var regExKind=/^[A-Z|a-z\s]{3,20}$/;
var regExItemName=/^[A-Z|a-z\s]{3,20}$/;
var regExQuantity=/^[0-9]{2,20}$/;
var regExUnitPrice=/^[0-9]{1,10}(.)[0-9]{2}$/;

$("#itemCode").keyup(function (event) {

    let itemCode = $("#itemCode").val();
    if (regExItemID.test(itemCode)){
        $("#itemCode").css('border','2px solid blue');
        $("#errorCode").text("");
        if (event.key=="Enter"){
            $("#kind").focus();
        }
    }else {
        $("#itemCode").css('border','2px solid red');
        $("#errorCode").text("Item Code is a required field: Pattern I00-000");
    }
});

$("#kind").keyup(function (event) {

    let kind = $("#kind").val();
    if (regExKind.test(kind)){
        $("#kind").css('border','2px solid blue');
        $("#errorKind").text("");
        if (event.key=="Enter"){
            $("#nameOfItem").focus();
        }
    }else {
        $("#kind").css('border','2px solid red');
        $("#errorKind").text("Kind is a required field: Min 5");
    }
});

$("#nameOfItem").keyup(function (event) {

    let itemName = $("#nameOfItem").val();
    if (regExItemName.test(itemName)){
        $("#nameOfItem").css('border','2px solid blue');
        $("#errorItemName").text("");
        if (event.key=="Enter"){
            $("#qty").focus();
        }
    }else {
        $("#nameOfItem").css('border','2px solid red');
        $("#errorItemName").text("Item Name is a required field: Min 3");
    }
});

$("#qty").keyup(function (event) {

    let qty = $("#qty").val();
    if (regExQuantity.test(qty)){
        $("#qty").css('border','2px solid blue');
        $("#errorQty").text("");
        if (event.key=="Enter") {
            $("#unitPrice").focus();
        }
    }else {
        $("#qty").css('border','2px solid red');
        $("#errorQty").text("Quantity is a required field: Pattern 00");
    }
});

$("#unitPrice").keyup(function (event) {

    $("#tblItem tbody > tr").off("click");
    $("#tblItem tbody > tr").off("dblclick");

    let unitPrice = $("#unitPrice").val();
    if (regExUnitPrice.test(unitPrice)){
        $("#unitPrice").css('border','2px solid blue');
        $("#errorPrice").text("");

        if (event.key=="Enter"){

            let text = "Do you really want to save this Item?";
            if (confirm(text) == true) {
                let itemCode = $("#itemCode").val();
                let kind = $("#kind").val();
                let itemName = $("#nameOfItem").val();
                let qty = $("#qty").val();
                let unitPrice = $("#unitPrice").val();

                var itemDetails = new ItemDTO(
                    itemCode,
                    kind,
                    itemName,
                    qty,
                    unitPrice
                );

                var ifDuplicate=false;

                var code=$("#itemCode").val();
                var trim = $.trim(code);

                for (var j = 0; j < itemDB.length; j++) {
                    if (trim == itemDB[j].getItemCode()){
                        ifDuplicate = true;
                    }else {
                        ifDuplicate = false;
                    }
                }

                if (ifDuplicate == false){
                    itemDB.push(itemDetails);
                    $("#tblItem tbody").empty();

                    for (var i = 0; i < itemDB.length; i++) {
                        let raw = `<tr><td> ${itemDB[i].getItemCode()} </td><td> ${itemDB[i].getKind()} </td><td> ${itemDB[i].getItemName()} </td><td> ${itemDB[i].getQtyOnHand()} </td><td> ${itemDB[i].getUnitPrice()} </td></tr>`;
                        $("#tblItem tbody").append(raw);
                    }

                    $("#itemCode").val("");
                    $("#kind").val("");
                    $("#nameOfItem").val("");
                    $("#qty").val("");
                    $("#unitPrice").val("");

                    $("#itemCode").css('border', '2px solid transparent');
                    $("#kind").css('border', '2px solid transparent');
                    $("#nameOfItem").css('border', '2px solid transparent');
                    $("#qty").css('border', '2px solid transparent');
                    $("#unitPrice").css('border', '2px solid transparent');
                }else {
                    alert("Already Exists");
                }

            } else {

            }
        }
    }else {
        $("#unitPrice").css('border','2px solid red');
        $("#errorPrice").text("Unit Price is a required field: Pattern 00.00");
    }

    $("#tblItem tbody > tr").click(function () {

        tblItemRow=$(this);

        var code=tblItemRow.children(':nth-child(1)').text();
        var trim1 = $.trim(code);
        var kind=tblItemRow.children(':nth-child(2)').text();
        var trim2 = $.trim(kind);
        var iName=tblItemRow.children(':nth-child(3)').text();
        var trim3 = $.trim(iName);
        var qty=tblItemRow.children(':nth-child(4)').text();
        var trim4 = $.trim(qty);
        var price=tblItemRow.children(':nth-child(5)').text();
        var trim5 = $.trim(price);

        $("#itemCode").val(trim1);
        $("#kind").val(trim2);
        $("#nameOfItem").val(trim3);
        $("#qty").val(trim4);
        $("#unitPrice").val(trim5);
    });

    $("#tblItem tbody > tr").dblclick(function () {

        let text = "Are you sure you want to delete this Item?";
        if (confirm(text) == true) {
            tblItemRow.remove();

            var index=-1;
            var code=$("#itemCode").val();
            var trim=$.trim(code);

            for (var i = 0; i < itemDB.length; i++) {
                if (trim == itemDB[i].getItemCode()){
                    index=i;
                }
            }
            itemDB.splice(index,1);

            $("#itemCode").val("");
            $("#kind").val("");
            $("#nameOfItem").val("");
            $("#qty").val("");
            $("#unitPrice").val("");
        } else {

        }
    });
});

$('#itemCode,#kind,#nameOfItem,#qty,#unitPrice').keydown(function (e) {
    if (e.key=="Tab"){
        e.preventDefault();
    }
});

var tblItemRow;

$("#btnSaveItem").click(function () {

    $("#tblItem tbody > tr").off("click");
    $("#tblItem tbody > tr").off("dblclick");

    if($("#errorCode").text()!=""||$("#errorKind").text()!=""||$("#errorItemName").text()!=""||$("#errorQty").text()!=""||$("#errorPrice").text()!=""||
        $("#itemCode").val()==""||$("#nameOfItem").val()==""||$("#kind").val()==""||$("#qty").val()==""||$("#unitPrice").val()==""){
        $("#btnSaveItem").disable();
    }else {

        let text = "Do you really want to save this Item?";

        if (confirm(text) == true) {
            let itemCode = $("#itemCode").val();
            let kind = $("#kind").val();
            let itemName = $("#nameOfItem").val();
            let qty = $("#qty").val();
            let unitPrice = $("#unitPrice").val();

            var itemDetails = new ItemDTO(
                itemCode,
                kind,
                itemName,
                qty,
                unitPrice
            );

            var ifDuplicate=false;

            var code=$("#itemCode").val();
            var trim = $.trim(code);

            for (var j = 0; j < itemDB.length; j++) {
                if (trim == itemDB[j].getItemCode()){
                    ifDuplicate = true;
                }else {
                    ifDuplicate = false;
                }
            }

            if (ifDuplicate == false){
                itemDB.push(itemDetails);
                $("#tblItem tbody").empty();

                for (var i = 0; i < itemDB.length; i++) {
                    let raw = `<tr><td> ${itemDB[i].getItemCode()} </td><td> ${itemDB[i].getKind()} </td><td> ${itemDB[i].getItemName()} </td><td> ${itemDB[i].getQtyOnHand()} </td><td> ${itemDB[i].getUnitPrice()} </td></tr>`;
                    $("#tblItem tbody").append(raw);
                }

                $("#itemCode").val("");
                $("#kind").val("");
                $("#nameOfItem").val("");
                $("#qty").val("");
                $("#unitPrice").val("");

                $("#itemCode").css('border', '2px solid transparent');
                $("#kind").css('border', '2px solid transparent');
                $("#nameOfItem").css('border', '2px solid transparent');
                $("#qty").css('border', '2px solid transparent');
                $("#unitPrice").css('border', '2px solid transparent');
            }else {
                alert("Already Exists");
            }

        } else {

        }
    }

    $("#tblItem tbody > tr").click(function () {

        tblItemRow=$(this);

        var code=tblItemRow.children(':nth-child(1)').text();
        var trim1 = $.trim(code);
        var kind=tblItemRow.children(':nth-child(2)').text();
        var trim2 = $.trim(kind);
        var iName=tblItemRow.children(':nth-child(3)').text();
        var trim3 = $.trim(iName);
        var qty=tblItemRow.children(':nth-child(4)').text();
        var trim4 = $.trim(qty);
        var price=tblItemRow.children(':nth-child(5)').text();
        var trim5 = $.trim(price);

        $("#itemCode").val(trim1);
        $("#kind").val(trim2);
        $("#nameOfItem").val(trim3);
        $("#qty").val(trim4);
        $("#unitPrice").val(trim5);
    });

    $("#tblItem tbody > tr").dblclick(function () {

        let text = "Are you sure you want to delete this Item?";
        if (confirm(text) == true) {
            tblItemRow.remove();

            var index=-1;
            var code=$("#itemCode").val();
            var trim=$.trim(code);

            for (var i = 0; i < itemDB.length; i++) {
                if (trim == itemDB[i].getItemCode()){
                    index=i;
                }
            }
            itemDB.splice(index,1);

            $("#itemCode").val("");
            $("#kind").val("");
            $("#nameOfItem").val("");
            $("#qty").val("");
            $("#unitPrice").val("");
        } else {

        }
    });

});

$("#btnClearItem").click(function () {
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
});

$("#btnDeleteItem").click(function () {

    if($("#errorCode").text()!=""||$("#errorKind").text()!=""||$("#errorItemName").text()!=""||$("#errorQty").text()!=""||$("#errorPrice").text()!=""||
        $("#itemCode").val()==""||$("#nameOfItem").val()==""||$("#kind").val()==""||$("#qty").val()==""||$("#unitPrice").val()==""){
        $("#btnDeleteItem").disable();
    }else {

        let text = "Are you sure you want to delete this Item?";
        if (confirm(text) == true) {
            tblItemRow.remove();

            var index=-1;
            var code=$("#itemCode").val();
            var trim=$.trim(code);

            for (var i = 0; i < itemDB.length; i++) {
                if (trim == itemDB[i].getItemCode()){
                    index=i;
                }
            }
            itemDB.splice(index,1);

            $("#itemCode").val("");
            $("#kind").val("");
            $("#nameOfItem").val("");
            $("#qty").val("");
            $("#unitPrice").val("");
        } else {

        }
    }

});

$("#btnEditItem").click(function () {

    if($("#errorCode").text()!=""||$("#errorKind").text()!=""||$("#errorItemName").text()!=""||$("#errorQty").text()!=""||$("#errorPrice").text()!=""||
        $("#itemCode").val()==""||$("#nameOfItem").val()==""||$("#kind").val()==""||$("#qty").val()==""||$("#unitPrice").val()==""){
        $("#btnEditItem").disable();
    }else {

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

            var count=-1;
            for (var i = 0; i < itemDB.length; i++) {

                var code=$("#itemCode").val();
                var trim=$.trim(code);

                if (trim == itemDB[i].getItemCode()){
                    itemDB[i].setItemCode(itemCode);
                    itemDB[i].setKind(kind);
                    itemDB[i].setItemName(itemName);
                    itemDB[i].setQtyOnHand(qty);
                    itemDB[i].setUnitPrice(unitPrice);
                }
            }

            $("#itemCode").val("");
            $("#kind").val("");
            $("#nameOfItem").val("");
            $("#qty").val("");
            $("#unitPrice").val("");

            $("#itemCode").css('border', '2px solid transparent');
            $("#kind").css('border', '2px solid transparent');
            $("#nameOfItem").css('border', '2px solid transparent');
            $("#qty").css('border', '2px solid transparent');
            $("#unitPrice").css('border', '2px solid transparent');

        } else {

        }
    }

});

$("#btnSearchItem").click(function () {
    for (var i = 0; i < itemDB.length; i++) {
        if ($("#searchItem").val()==itemDB[i].getItemCode()){
            $("#itemCode").val(itemDB[i].getItemCode());
            $("#nameOfItem").val(itemDB[i].getItemName());
            $("#kind").val(itemDB[i].getKind());
            $("#qty").val(itemDB[i].getQtyOnHand());
            $("#unitPrice").val(itemDB[i].getUnitPrice());
        }else {
            alert("No Such Item");
        }
    }
});