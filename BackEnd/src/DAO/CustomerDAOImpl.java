package DAO;

import Entity.Customer;
import Servlets.CustomerServlet;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl {

    public boolean addCustomer(Customer c,Connection connection) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES(?,?,?,?,?,?,?)");
        pstm.setObject(1,c.getCustomerId());
        pstm.setObject(2,c.getCustomerName());
        pstm.setObject(3,c.getGender());
        pstm.setObject(4,c.getContact());
        pstm.setObject(5,c.getNic());
        pstm.setObject(6,c.getAddress());
        pstm.setObject(7,c.getEmail());

        if (pstm.executeUpdate()>0) {
            return true;
        }
        return false;
    }

    public boolean updateCustomer(Customer c, Connection connection) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET customerName=?,Gender=?,contact=?,NIC=?,address=?,email=? WHERE customerId=?");
        pstm.setObject(1,c.getCustomerName());
        pstm.setObject(2,c.getGender());
        pstm.setObject(3,c.getContact());
        pstm.setObject(4,c.getNic());
        pstm.setObject(5,c.getAddress());
        pstm.setObject(6,c.getEmail());
        pstm.setObject(7,c.getCustomerId());

        if (pstm.executeUpdate()>0) {
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(String id,Connection connection) throws SQLException {
        if (connection.prepareStatement("DELETE FROM Customer WHERE customerId='"+ id +"'").executeUpdate()>0) {
            return true;
        }
        return false;
    }

    public Customer searchCustomer(String id){

        return new Customer();
    }

    public ArrayList<Customer> getAllCustomer(){

        return new ArrayList<>();
    }

    public int countCustomer(){

        return 0;
    }

    /*public List<String> getIds(){

        return new List<>();
    }*/
}
