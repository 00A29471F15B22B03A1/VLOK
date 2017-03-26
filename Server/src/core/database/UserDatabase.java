package core.database;

import core.logging.Logger;

import java.sql.*;

public class UserDatabase {

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + "users.sqlite");
            Logger.info("Opened file database successfully");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to connect to user database");
        }
    }

    public static void addUser(String name, String key, String code) {
        String sql = "INSERT INTO users(name,key,code,keyType,permissionLevel,os) VALUES(?,?,?,null,null,null)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, key);
            pstmt.setString(3, code);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to add user " + name);
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
            Logger.err("Failed to get code info for " + key);
        }

        return false;
    }

}
