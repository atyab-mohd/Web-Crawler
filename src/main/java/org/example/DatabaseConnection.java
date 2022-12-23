package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collections;

public class DatabaseConnection {
    static Connection connection = null;
    public static Connection getConnection(){
        if(connection != null){
            return connection;
        }
        String db = "searchaccio";
        String user = "root";
        String pas = "rootpassword";
        return getConnection(db, user, pas);
    }
    private static Connection getConnection(String db, String user, String pas){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?user="+user+"&password="+pas);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return connection;
    }
}
