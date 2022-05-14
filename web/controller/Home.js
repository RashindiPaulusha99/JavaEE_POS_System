function Count() {
    count();

    var income = 0;
    for (var i = 0; i < orderDB.length; i++) {
        var total = parseInt(orderDB[i].getNetTotal());
        income += total;
    }
    $("#income").text(income);
}

