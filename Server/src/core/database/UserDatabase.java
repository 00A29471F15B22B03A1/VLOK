package core.database;

import core.PermissionLevels;
import core.logging.Console;

import java.sql.*;

public class UserDatabase {

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + "Server/dbs/users.sqlite");
            Console.info("Opened file database successfully");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Console.err("Failed to connect to user database");
        }
    }

    public static void addUser(String name, String key, String code, int permissionLevel) {
        String sql = "INSERT INTO users(name,key,code,keyType,permissionLevel,os) VALUES(?,?,?,null,?,null)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, key);
            pstmt.setString(3, code);
            pstmt.setInt(4, permissionLevel);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to add user " + name);
        }
    }

    public static boolean isValid(String key, String code) {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT code FROM users WHERE key = '" + key + "'");
            String dbCode = null;

            while (resultSet.next() && dbCode == null)
                dbCode = resultSet.getString("code");

            return dbCode != null && dbCode.equals(code);

        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to get code info for " + key);
        }

        return false;
    }

    public static boolean isAdmin(String key) {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT permissionLevel FROM users WHERE key = '" + key + "'");
            int permissionLevel = -1;

            while (resultSet.next() && permissionLevel == -1)
                permissionLevel = resultSet.getInt("permissionLevel");

            return PermissionLevels.isAdmin(permissionLevel);

        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to get code info for " + key);
        }

        return false;
    }

}
