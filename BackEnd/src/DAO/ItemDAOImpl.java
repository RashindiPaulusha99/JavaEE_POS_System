package DAO;

import Entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public boolean add(Item item, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Item item, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public Item search(String s, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public ArrayList<Item> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public int countItems(Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public List<String> getCodes(Connection connection) throws SQLException {
        return null;
    }
}
