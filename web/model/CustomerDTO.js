function CustomerDTO(cusId, cusName, gender, contact, nic, address, email) {
    var __id = cusId;
    var __name = cusName;
    var __gender = gender;
    var __contact = contact;
    var __nic = nic;
    var __address = address;
    var __email = email;

    this.setCustomerId = function (e) {
        __id = e;
    }
    this.getCustomerId = function () {
        return __id;
    }

    this.setCustomerName = function (e) {
        __name = e;
    }
    this.getCustomerName = function () {
        return __name;
    }

    this.setGender = function (e) {
        __gender = e;
    }
    this.getGender = function () {
        return __gender;
    }

    this.setContact = function (e) {
        __contact = e;
    }
    this.getContact = function () {
        return __contact;
    }

    this.setNIC = function (e) {
        __nic = e;
    }
    this.getNIC = function () {
        return __nic;
    }

    this.setAddress = function (e) {
        __address = e;
    }
    this.getAddress = function () {
        return __address;
    }

    this.setEmail = function (e) {
        __email = e;
    }
    this.getEmail = function () {
        return __email;
    }

}