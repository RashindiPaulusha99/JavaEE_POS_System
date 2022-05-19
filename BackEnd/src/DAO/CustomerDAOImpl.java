package DAO;

import Entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Customer searchCustomer(String id,Connection connection) throws SQLException {
        ResultSet rst = connection.prepareStatement("SELECT * FROM Customer WHERE customerId='" + id + "'").executeQuery();

        if (rst.next()){
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        }else {
            return null;
        }
    }

    public ArrayList<Customer> getAllCustomer(Connection connection) throws SQLException {
        ResultSet rst = connection.prepareStatement("SELECT * FROM Customer").executeQuery();
        ArrayList<Customer> customers = new ArrayList<>();
        while (rst.next()){
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return customers;
    }

    public int countCustomer(Connection connection) throws SQLException {
        ResultSet rsts = connection.prepareStatement("SELECT COUNT(*) FROM Customer").executeQuery();
        int count = 0;
        while (rsts.next()){
            count = rsts.getInt(1);
        }

        return count;
    }

    public List<String> getIds(Connection connection) throws SQLException {
        ResultSet rst = connection.prepareStatement("SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }

        return ids;
    }
}
