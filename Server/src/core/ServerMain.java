package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.packets.FileInfoPacket;
import core.packets.FileStoragePacket;
import core.packets.RequestPacket;

public class ServerMain {

    private static NetworkServer server;

    private static FileStorage fileStorage;

    public static void main(String[] args) {
        fileStorage = FileDatabase.getAllFiles();

        server = new NetworkServer();

        server.addListener(new FileReceiver(fileStorage));

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof RequestPacket)
                    handleRequestPacket((RequestPacket) o, connection);
            }
        });

        server.start();
    }

    private static void handleRequestPacket(RequestPacket packet, Connection connection) {
        switch (packet.type) {
            case FILE_STRUCTURE:
                FileStoragePacket fileStoragePacket = new FileStoragePacket();

                fileStoragePacket.fileStorage = fileStorage;

                server.sendTCP(connection.getID(), fileStoragePacket);

                break;
            case FILE_INFO:
                FileInfoPacket fileInfoPacket = new FileInfoPacket();

                FileInfo file = fileStorage.getFile(packet.argument);

                if (file == null) {
                    System.err.println("Failed to find fileInfo " + packet.argument);
                    return;
                }

                fileInfoPacket.fileInfo = file;

                server.sendTCP(connection.getID(), fileInfoPacket);

                break;
            case FILE_DOWNLOAD:

                break;
        }
    }
}