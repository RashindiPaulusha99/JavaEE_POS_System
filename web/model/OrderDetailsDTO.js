function OrderDetailDTO(code, kind, iName, sellQty, price, discount, total) {
    var __code = code;
    var __kind = kind;
    var __ItemName = iName;
    var __sellQty = sellQty;
    var __unitPrice = price;
    var __itemDiscount = discount;
    var __total = total;

    this.setOrderItemCode = function (e) {
        __code = e;
    }
    this.getOrderItemCode = function () {
        return __code;
    }

    this.setOrderItemName = function (e) {
        __ItemName = e;
    }
    this.getOrderItemName = function () {
        return __ItemName;
    }

    this.setOrderItemKind = function (e) {
        __kind = e;
    }
    this.getOrderItemKind = function () {
        return __kind;
    }

    this.setSellQty = function (e) {
        __sellQty = e;
    }
    this.getSellQty = function () {
        return __sellQty;
    }

    this.setOrderUnitPrice = function (e) {
        __unitPrice = e;
    }
    this.getOrderUnitPrice = function () {
        return __unitPrice;
    }

    this.setDiscount = function (e) {
        __itemDiscount = e;
    }
    this.getDiscount = function () {
        return __itemDiscount;
    }

    this.setTotal = function (e) {
        __total = e;
    }
    this.getTotal = function () {
        return __total;
    }

}