package main.java.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static volatile DBConnection instance;
    private static final String URL = "jdbc:postgresql://localhost:5432/Wallet";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";

    private DBConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
        }
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) instance = new DBConnection();
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
