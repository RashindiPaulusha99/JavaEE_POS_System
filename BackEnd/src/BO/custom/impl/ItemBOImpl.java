package BO.custom.impl;

import BO.custom.ItemBO;
import DAO.DAOFactory;
import DAO.custom.ItemDAO;
import DTO.ItemDTO;
import Entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {

    private ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean saveItem(ItemDTO itemDTO, Connection connection) throws SQLException {
        return itemDAO.add(new Item(
                itemDTO.getItemCode(),
                itemDTO.getKind(),
                itemDTO.getItemName(),
                itemDTO.getQtyOnHand(),
                itemDTO.getUnitPrice()),
                connection
        );
    }

    @Override
    public boolean updateItem(ItemDTO itemDTO, Connection connection) throws SQLException {
        return itemDAO.update(new Item(
                        itemDTO.getItemCode(),
                        itemDTO.getKind(),
                        itemDTO.getItemName(),
                        itemDTO.getQtyOnHand(),
                        itemDTO.getUnitPrice()),
                connection
        );
    }

    @Override
    public boolean deleteItem(String id, Connection connection) throws SQLException {
        return itemDAO.delete(id,connection);
    }

    @Override
    public ItemDTO searchItem(String id, Connection connection) throws SQLException {
        Item search = itemDAO.search(id, connection);
        if (search == null){
            return null;
        }else {
            return new ItemDTO(
                    search.getItemCode(),
                    search.getKind(),
                    search.getItemName(),
                    search.getQtyOnHand(),
                    search.getUnitPrice()
            );
        }
    }

    @Override
    public ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException {
        ArrayList<Item> all = itemDAO.getAll(connection);
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        for (Item item : all) {
            allItems.add(new ItemDTO(
                    item.getItemCode(),
                    item.getKind(),
                    item.getItemName(),
                    item.getQtyOnHand(),
                    item.getUnitPrice()
            ));
        }
        return allItems;
    }

    @Override
    public int countItem(Connection connection) throws SQLException {
        return itemDAO.countItems(connection);
    }

    @Override
    public List<String> getItemCodes(Connection connection) throws SQLException {
        return itemDAO.getCodes(connection);
    }
}
