package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    Connection connection;

    private DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement","root","password");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public static DBConnection getInstance(){
        return (dbConnection == null) ?  dbConnection = new DBConnection() : dbConnection;
    }

    public Connection getConnection(){
        return connection;
    }

}
