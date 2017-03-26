package core;

import core.logging.Logger;

import java.sql.*;

public class FileDatabase {

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + "files.sqlite");
            Logger.info("Opened file database successfully");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to connect to file database");
        }
    }

    public static void execute(String query) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to execute sqlite query " + query);
        }
    }

    public static FileInfo getFile(String name) {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM files WHERE name = '" + name + "'");
            FileInfo file = null;

            while (resultSet.next() && file == null) {
                String description = resultSet.getString("description");
                String path = resultSet.getString("path");
                int minPermissions = resultSet.getInt("minPermissions");
                file = new FileInfo(name, path, description, minPermissions);
            }

            return file;
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to get file info for " + name);
        }

        return null;
    }

    public static void updateFile(FileInfo fileInfo) {
        String sql = "UPDATE files SET path=?, description=?, minPermissions=? WHERE name=?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, fileInfo.getPath());
            pstmt.setString(2, fileInfo.getDescription());
            pstmt.setInt(3, fileInfo.getMinPermissionLevel());
            pstmt.setString(4, fileInfo.getName());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to update file info for " + fileInfo.getName());
        }
    }

    public static void addFile(FileInfo fileInfo) {
        String sql = "INSERT INTO files(name, path, description, minPermissions) VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, fileInfo.getName());
            pstmt.setString(2, fileInfo.getPath());
            pstmt.setString(3, fileInfo.getDescription());
            pstmt.setInt(4, fileInfo.getMinPermissionLevel());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to add file info for " + fileInfo.getName());
        }
    }

    public static FileStructure getAllFiles() {
        FileStructure files = new FileStructure();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM files");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                String description = resultSet.getString("description");
                int minPermissions = resultSet.getInt("minPermissions");
                files.addFile(new FileInfo(name, path, description, minPermissions));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to fetch files from database");
        }

        return files;
    }

    public static void removeFile(String filename) {
        execute("REMOVE FROM files WHERE name = " + filename);
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.err("Failed to close database connection");
        }
    }

}
