package main.java;

import main.java.utils.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection is working fine!");
            } else {
                System.out.println(" Connection failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
