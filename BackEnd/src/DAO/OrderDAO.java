package DAO;

import Entity.Order;

import java.sql.Connection;

public interface OrderDAO extends CrudDAO<Order, String, Connection>{
}
