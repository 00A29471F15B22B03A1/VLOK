package core.database;

import core.FileInfo;
import core.FileStructure;
import core.PermissionLevels;
import core.logging.Console;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileDatabase {

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + "Server/dbs/files.sqlite");
            Console.info("Opened file database successfully");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Console.err("Failed to connect to file database");
        }
    }

    public static void execute(String query) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to execute sqlite query " + query);
        }
    }

    public static void updateFile(FileInfo fileInfo) {
        String sql = "UPDATE files SET name=?, path=?, description=?, minPermissions=?, uploadDate=? WHERE id=?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, fileInfo.name);
            pstmt.setString(2, fileInfo.path);
            pstmt.setString(3, fileInfo.description);
            pstmt.setInt(4, fileInfo.minPermissionLevel);
            pstmt.setInt(5, fileInfo.id);
            pstmt.setString(6, fileInfo.uploadDate);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to update file info for " + fileInfo.name);
        }
    }

    public static void addFile(String name, String description) {
        String sql = "INSERT INTO files(id, name, path, description, minPermissions, pending, uploadDate) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(2, name);
            pstmt.setString(4, description);
            pstmt.setInt(5, PermissionLevels.PEASAN);
            pstmt.setString(6, "true");
            pstmt.setString(7, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to add file info for " + name);
        }
    }

    public static void unpend(int id, String path) {
        String sql = "UPDATE files SET pending=?, path=? WHERE id=?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "false");
            pstmt.setString(2, path);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to update file info for file" + id);
        }
    }

    public static FileStructure getAllFiles() {
        FileStructure files = new FileStructure();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM files");

            while (resultSet.next()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.id = resultSet.getInt("id");
                fileInfo.name = resultSet.getString("name");
                fileInfo.path = resultSet.getString("path");
                fileInfo.description = resultSet.getString("description");
                fileInfo.minPermissionLevel = resultSet.getInt("minPermissions");
                fileInfo.uploadDate = resultSet.getString("uploadDate");
                fileInfo.pending = Boolean.parseBoolean(resultSet.getString("pending"));
                files.addFile(fileInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to fetch files from database");
        }

        return files;
    }

    public static void removeFile(int id) {
        execute("REMOVE FROM files WHERE id = " + id);
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Console.err("Failed to close database connection");
        }
    }

}
