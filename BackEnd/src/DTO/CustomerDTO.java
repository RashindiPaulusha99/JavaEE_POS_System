package DTO;

import java.util.Objects;

public class CustomerDTO {
    private String customerId;
    private String customerName;
    private String gender;
    private String contact;
    private String nic;
    private String address;
    private String email;

    public CustomerDTO() {
    }

    public CustomerDTO(String customerId, String customerName, String gender, String contact, String nic, String address, String email) {
        this.setCustomerId(customerId);
        this.setCustomerName(customerName);
        this.setGender(gender);
        this.setContact(contact);
        this.setNic(nic);
        this.setAddress(address);
        this.setEmail(email);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", gender='" + gender + '\'' +
                ", contact='" + contact + '\'' +
                ", nic='" + nic + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
