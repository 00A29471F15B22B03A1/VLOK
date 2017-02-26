package core.database;

import core.filesystem.StoredFile;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to communicate with the database file
 */
public class FileDatabase {

    private Connection connection;

    public FileDatabase(String databaseFile) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
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

    public void addFile(StoredFile file) {
        execute("INSERT INTO files(name, path) VALUES ('" + file.getName() + "', '" + file.getPath() + "');");
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

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
