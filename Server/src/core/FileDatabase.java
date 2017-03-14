package core;

import core.filesystem.FileStorage;
import core.filesystem.StoredFile;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to communicate with the database file
 */
public final class FileDatabase {

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + "files.sqlite");
            System.out.println("Opened database successfully");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public static void execute(String query) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeFile(String filename) {
        execute("REMOVE FROM files WHERE name = " + filename);
    }

    public static void addFile(StoredFile file) {
        execute("INSERT INTO files(name, path) VALUES ('" + file.getName() + "', '" + file.getPath() + "');");
    }

    public static Map<String, String> getFiles() {
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

    public static FileStorage getFileStorage() {
        Map<String, String> files = getFiles();

        FileStorage fileStorage = new FileStorage();

        for (Map.Entry<String, String> f : files.entrySet())
            fileStorage.addFile(f.getValue(), f.getKey());

        return fileStorage;
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
