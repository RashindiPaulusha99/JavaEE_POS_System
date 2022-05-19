package DAO;

import DAO.CrudDAO;
import Entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item, String, Connection > {
    int countItems(Connection connection) throws SQLException;
    List<String> getCodes(Connection connection) throws SQLException;

}
