package core.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private Connection connection;

    public Database(String file) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            System.out.println("Opened database successfully");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void execute(String query) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getFiles() {
        String sql = "SELECT * FROM files";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            Map<String, String> result = new HashMap<>();

            while (rs.next()) {
                String name = rs.getString("name");
                String path = rs.getString("path");

                result.put(name, path);
            }

            rs.close();

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getPath(String fileName) {
        String sql = "SELECT path FROM files WHERE name LIKE ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, fileName);

            ResultSet rs = pstmt.executeQuery();

            String path = rs.getString("path");

            rs.close();

            return path;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
