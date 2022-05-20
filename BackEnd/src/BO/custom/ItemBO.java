package BO.custom;

import BO.SuperBO;
import DTO.CustomerDTO;
import DTO.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ItemBO extends SuperBO {
    boolean saveItem(ItemDTO itemDTO, Connection connection) throws SQLException;
    boolean updateItem(ItemDTO itemDTO, Connection connection) throws SQLException;
    boolean deleteItem(String id, Connection connection) throws SQLException;
    ItemDTO searchItem(String id, Connection connection) throws SQLException;
    ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException;
    int countItem(Connection connection) throws SQLException;
    List<String> getItemCodes(Connection connection) throws SQLException;
}
