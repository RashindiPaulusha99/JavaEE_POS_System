package DAO;

public interface CrudDAO<T, ID> extends SuperDAO{
    boolean add();
    boolean update();
    boolean delete();
    boolean search();
    boolean getAll();
}
