package DAO;

import Entity.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO{

    @Override
    public boolean add(Customer customer, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate(
                "INSERT INTO Customer VALUES(?,?,?,?,?,?,?)",
                connection,
                customer.getCustomerId(), customer.getCustomerName(), customer.getGender(), customer.getContact(), customer.getNic(), customer.getAddress(), customer.getEmail()
        );
    }

    @Override
    public boolean update(Customer customer, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate("UPDATE Customer SET customerName=?,Gender=?,contact=?,NIC=?,address=?,email=? WHERE customerId=?",
                connection,
                customer.getCustomerName(),customer.getGender(),customer.getContact(),customer.getNic(),customer.getAddress(),customer.getEmail(),customer.getCustomerId()
        );
    }

    @Override
    public boolean delete(String id, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate("DELETE FROM Customer WHERE customerId=?",
                connection,
                id
        );
    }

    @Override
    public Customer search(String id, Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer WHERE customerId=?",
                connection,
                id
        );
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

    @Override
    public ArrayList<Customer> getAll(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer",
                connection
        );
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

    @Override
    public int countCustomer(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT COUNT(*) FROM Customer",
                connection
        );
        int count = 0;
        while (rst.next()){
            count = rst.getInt(1);
        }
        return count;
    }

    @Override
    public List<String> getIds(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1",
                connection
        );
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }
}
