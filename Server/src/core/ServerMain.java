package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.filesystem.StoredFile;
import core.packets.FileCompletedPacket;
import core.packets.FileStructurePacket;
import core.packets.FileUploadPacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerMain {

    private static Map<Connection, List<byte[]>> receivingFiles = new HashMap<>();

    public static void main(String[] args) {
        NetworkServer server = new NetworkServer();

        server.start();

        System.out.println("Started server");

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("Connected " + connection.getRemoteAddressTCP());

                FileStructurePacket fileStructurePacket = new FileStructurePacket();
                fileStructurePacket.fileStorage = FileDatabase.getFiles();

                server.sendTCP(connection.getID(), fileStructurePacket);
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

                    receivingFiles.computeIfAbsent(connection, k -> new ArrayList<>());

                    receivingFiles.get(connection).add(packet.data);
                } else if (o instanceof FileCompletedPacket) {
                    FileCompletedPacket packet = (FileCompletedPacket) o;

                    System.out.println("Fully received " + packet.name);

                    storeFullFile(connection, packet.name, packet.path);
                }
            }
        });
    }

    private static void storeFullFile(Connection connection, String name, String path) {
        byte[] fullData = storeInByteArray(receivingFiles.get(connection));

        FileDatabase.addFile(new StoredFile(name, path));
        saveBytes(name.replaceAll("'", ""), path, fullData);

        receivingFiles.remove(connection);
    }

    private static byte[] storeInByteArray(List<byte[]> data) {
        int byteLength = 0;
        for (byte[] array : data)
            byteLength += array.length;

        byte[] fullData = new byte[byteLength];

        int bytePointer = 0;
        for (byte[] array : data)
            for (byte b : array)
                fullData[bytePointer++] = b;

        return fullData;
    }

    public static void saveBytes(String name, String path, byte[] data) {
        try {
            String fullPath = "storage/" + path + "/" + name;

            File folder = new File("storage/" + path);

            if (!folder.mkdirs()) {
                System.err.println("Failed to create folder for file " + name);
                return;
            }

            File file = new File(fullPath);

            if (!file.exists() && !file.createNewFile()) {
                System.err.println("Failed to create file " + name);
                return;
            }

            FileOutputStream fos = new FileOutputStream(fullPath);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}