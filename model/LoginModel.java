package model;

import java.sql.*;
import model.database.ConnectDB;

public class LoginModel {
    Connection connection;
    public void processLogin(String userId) throws Exception {
        connection = ConnectDB.connect();
        String getUser = "select * from user where name =" + "'" + userId + "'";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(getUser);
        if (!resultSet.next()) {
            String query = "INSERT INTO user(name) VALUES ('" + userId + "')";
            int result = stmt.executeUpdate(query);
            connection.commit();
        }
        else {
            return;
        }
    }
}
