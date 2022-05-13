function Count() {
    $("#customerCount").text(customerDB.length);
    $("#itemCount").text(itemDB.length);
    $("#orderCount").text(orderDB.length);

    var income=0;
    for (var i = 0; i <orderDB.length ; i++) {
        var total=parseInt(orderDB[i].getNetTotal());
        income+=total;
    }
    $("#income").text(income);
}