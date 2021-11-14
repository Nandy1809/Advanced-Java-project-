package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {

    public Connection connect() {
        final String DB_NAME = "testDB";

        try{
            Connection con = getConnection(DB_NAME);
            return con;
        } catch (Exception e) {
            return null;
        }
    }

    public static Connection getConnection(String dbName)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        Connection con = DriverManager.getConnection
                ("jdbc:hsqldb:file:database/" + dbName, "SA", "");
        return con;
    }
}
