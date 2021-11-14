package model.database;

import java.sql.Connection;

public class ConnectDB {

    public static Connection connect(){
        ConnectionTest contest = new ConnectionTest();
        Connection conn = contest.connect();
        return conn;
    }

}
