package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.database.FileDatabase;
import core.filesystem.StoredFile;
import core.packets.FileCompletedPacket;
import core.packets.FileUploadPacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerMain {

    public static void main(String[] args) {
        NetworkServer server = new NetworkServer();

        server.start();

        FileDatabase fileDatabase = new FileDatabase("files.sqlite");

        Map<Connection, List<byte[]>> receivingFiles = new HashMap<>();

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("Connected " + connection.getRemoteAddressTCP());
                receivingFiles.put(connection, new ArrayList<>());
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println("Disconnected " + connection.getID());
                receivingFiles.remove(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileUploadPacket) {
                    FileUploadPacket packet = (FileUploadPacket) o;
                    System.out.println("Received file: " + packet.name + " path: " + packet.path);

                    receivingFiles.get(connection).add(packet.data);
                } else if (o instanceof FileCompletedPacket) {
                    FileCompletedPacket packet = (FileCompletedPacket) o;

                    System.out.println("Fully received " + packet.name);

                    byte[] fullData = storeInByteArray(receivingFiles.get(connection));

                    fileDatabase.addFile(new StoredFile(packet.name, packet.path));
                    storeFile(packet.name.replaceAll("'", ""), packet.path, fullData);

                    receivingFiles.remove(connection);
                }
            }
        });
    }

    private static byte[] storeInByteArray(List<byte[]> data) {
        int byteLength = 0;
        for (byte[] array : data)
            byteLength += array.length;

        byte[] fullData = new byte[byteLength];

        int bytePointer = 0;
        for (byte[] array : data)
            for (byte b : array) {
                fullData[bytePointer++] = b;
            }

        return fullData;
    }

    public static void storeFile(String name, String path, byte[] data) {
        try {
            String fullPath = "storage/" + path + "/" + name;

            File folder = new File("storage/" + path);
            folder.mkdirs();

            File file = new File(fullPath);

            if (!file.exists())
                file.createNewFile();

            FileOutputStream fos = new FileOutputStream(fullPath);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}