package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T, ID, C> extends SuperDAO{
    boolean add(T t, C c) throws SQLException;
    boolean update(T t, C c) throws SQLException;
    boolean delete(ID id, C c) throws SQLException;
    T search(ID id, C c) throws SQLException;
    ArrayList<T> getAll(C c) throws SQLException;
}
