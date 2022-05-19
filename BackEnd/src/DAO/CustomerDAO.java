package DAO;

import Entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer, String, Connection>{
     int countCustomer(Connection connection) throws SQLException;
     List<String> getIds(Connection connection) throws SQLException;

}
