function OrderDTO(oID, oDate, cusId, gross, net) {
    var __orderId = oID;
    var __orderDate = oDate;
    var __cusID = cusId;
    var __grossTotal = gross;
    var __netTotal = net;

    this.setOrderId = function (e) {
        __orderId = e;
    }
    this.getOrderId  = function () {
        return __orderId;
    }

    this.setOrderDate = function (e) {
        __orderDate = e;
    }
    this.getOrderDate = function () {
        return __orderDate;
    }

    this.setOrderCusId = function (e) {
        __cusID = e;
    }
    this.getOrderCusId  = function () {
        return __cusID;
    }

    this.setGrossTotal = function (e) {
        __grossTotal = e;
    }
    this.getGrossTotal = function () {
        return __grossTotal;
    }

    this.setNetTotal = function (e) {
        __netTotal = e;
    }
    this.getNetTotal = function () {
        return __netTotal;
    }

}