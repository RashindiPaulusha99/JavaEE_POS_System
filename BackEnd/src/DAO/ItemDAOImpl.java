package DAO;

import Entity.Customer;
import Entity.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public boolean add(Item item, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate(
                "INSERT INTO Item VALUES(?,?,?,?,?)",
                connection,
                item.getItemCode(), item.getKind(), item.getItemName(), item.getQtyOnHand(), item.getUnitPrice()
        );
    }

    @Override
    public boolean update(Item item, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate(
                "UPDATE Item SET kind=?,itemName=?,qtyOnHand=?,unitPrice=? WHERE itemCode=?",
                connection,
                item.getKind(), item.getItemName(), item.getQtyOnHand(), item.getUnitPrice(),item.getItemCode()
        );
    }

    @Override
    public boolean delete(String id, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate("DELETE FROM Item WHERE itemCode=?",
                connection,
                id
        );
    }

    @Override
    public Item search(String id, Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Item WHERE itemCode=?",
                connection,
                id
        );
        if (rst.next()){
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getDouble(5)
            );
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<Item> getAll(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Item",
                connection
        );
        ArrayList<Item> items = new ArrayList<>();
        while (rst.next()){
            items.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getDouble(5)
            ));
        }
        return items;
    }

    @Override
    public int countItems(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT COUNT(*) FROM Item",
                connection
        );
        int count = 0;
        while (rst.next()){
            count = rst.getInt(1);
        }
        return count;
    }

    @Override
    public List<String> getCodes(Connection connection) throws SQLException {
        ResultSet rst = CrudUtil.executeQuery("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1",
                connection
        );
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public boolean updateQtyOnHand(String code, int qty, Connection connection) throws SQLException {
        return CrudUtil.executeUpdate("UPDATE Item SET qtyOnHand=(qtyOnHand - " + qty + " )  WHERE itemCode=?",
                connection,
                code
        );
    }
}
