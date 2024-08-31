package umg.progra2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DateBaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_telebot";
    private static final String USER = "root";
    private static final String PASSWORD = "123Pk2..";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
