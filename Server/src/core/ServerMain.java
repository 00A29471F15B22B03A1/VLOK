package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.packets.FileStructurePacket;
import core.packets.FileTransferPacket;
import core.packets.RequestPacket;

public class ServerMain {

    private static NetworkServer server;

    private static FileStructure fileStructure;

    public static void main(String[] args) {
        fileStructure = FileDatabase.getAllFiles();

        server = new NetworkServer();

        server.addListener(new FileReceiver(fileStructure));

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                sendFileStructure(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                System.out.println(o.getClass().getSimpleName());

                if (o instanceof FileTransferPacket) {
                    FileTransferPacket packet = (FileTransferPacket) o;

                    if (packet.finished)
                        sendFileStructure(null);

                } else if (o instanceof RequestPacket) {
                    RequestPacket packet = (RequestPacket) o;

                    if (packet.type == RequestPacket.Type.FILE_STRUCTURE)
                        sendFileStructure(connection);
                    else if (packet.type == RequestPacket.Type.FILE_DOWNLOAD)
                        FileSender.sendFile(server, connection, fileStructure.getFile(packet.argument));
                }
            }
        });

        server.start();
    }

    public static void sendFileStructure(Connection connection) {
        FileStructurePacket fileStructurePacket = new FileStructurePacket();

        fileStructurePacket.fileStructure = fileStructure;

        if (connection == null)
            server.sendToAllTCP(fileStructurePacket);
        else
            server.sendTCP(connection.getID(), fileStructurePacket);
    }
}