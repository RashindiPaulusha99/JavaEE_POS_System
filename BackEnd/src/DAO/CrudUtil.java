package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {

    private static PreparedStatement repeatStatement(String query, Connection connection, Object... args) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(query);
        for (int i = 0; i < args.length ; i++) {
            pstm.setObject(i+1, args[i]);
        }
        return pstm;
    }

    public static boolean executeUpdate(String query, Connection connection, Object... args) throws SQLException {
        PreparedStatement pstm = repeatStatement(query, connection, args);
        return pstm.executeUpdate()>0;
    }

    public static ResultSet executeQuery(String query, Connection connection, Object... args) throws SQLException {
        PreparedStatement pstm = repeatStatement(query, connection, args);
        return pstm.executeQuery();
    }
}
