package BO.custom.impl;

import BO.custom.CustomerBO;
import DAO.custom.CustomerDAO;
import DAO.DAOFactory;
import DTO.CustomerDTO;
import Entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {

    private CustomerDAO customerDAO = (CustomerDAO)DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDTO customerDTO, Connection connection) throws SQLException {
        return customerDAO.add(new Customer(
                customerDTO.getCustomerId(),
                customerDTO.getCustomerName(),
                customerDTO.getGender(),
                customerDTO.getContact(),
                customerDTO.getNic(),
                customerDTO.getAddress(),
                customerDTO.getEmail()),
                connection
        );
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO, Connection connection) throws SQLException {
        return customerDAO.update(new Customer(
                        customerDTO.getCustomerId(),
                        customerDTO.getCustomerName(),
                        customerDTO.getGender(),
                        customerDTO.getContact(),
                        customerDTO.getNic(),
                        customerDTO.getAddress(),
                        customerDTO.getEmail()),
                connection
        );
    }

    @Override
    public boolean deleteCustomer(String id, Connection connection) throws SQLException {
        return customerDAO.delete(id, connection);
    }

    @Override
    public CustomerDTO searchCustomer(String id, Connection connection) throws SQLException {
        Customer search = customerDAO.search(id, connection);
        if (search == null){
            return null;
        }else {
            return new CustomerDTO(
                    search.getCustomerId(),
                    search.getCustomerName(),
                    search.getGender(),
                    search.getContact(),
                    search.getNic(),
                    search.getAddress(),
                    search.getEmail()
            );
        }
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers(Connection connection) throws SQLException {
        ArrayList<Customer> all = customerDAO.getAll(connection);
        ArrayList<CustomerDTO> allCustomer = new ArrayList<>();
        for (Customer customer : all) {
            allCustomer.add(new CustomerDTO(
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    customer.getGender(),
                    customer.getContact(),
                    customer.getNic(),
                    customer.getAddress(),
                    customer.getEmail()
            ));
        }
        return allCustomer;
    }

    @Override
    public int countCustomer(Connection connection) throws SQLException {
        return customerDAO.countCustomer(connection);
    }

    @Override
    public List<String> getCustomerIds(Connection connection) throws SQLException {
        return customerDAO.getIds(connection);
    }
}
