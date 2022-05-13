function ItemDTO(code, kind, iName, qty, price) {
    var __code = code;
    var __kind = kind;
    var __ItemName = iName;
    var __qtyOnHand = qty;
    var __unitPrice = price;

    this.setItemCode = function (e) {
        __code = e;
    }
    this.getItemCode = function () {
        return __code;
    }

    this.setItemName = function (e) {
        __ItemName = e;
    }
    this.getItemName = function () {
        return __ItemName;
    }

    this.setKind = function (e) {
        __kind = e;
    }
    this.getKind = function () {
        return __kind;
    }

    this.setQtyOnHand = function (e) {
        __qtyOnHand = e;
    }
    this.getQtyOnHand = function () {
        return __qtyOnHand;
    }

    this.setUnitPrice = function (e) {
        __unitPrice = e;
    }
    this.getUnitPrice = function () {
        return __unitPrice;
    }

}