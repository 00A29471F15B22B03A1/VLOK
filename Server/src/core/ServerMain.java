package core;

import com.esotericsoftware.kryonet.Connection;
import core.logging.Console;
import core.packethandlers.ChatLoginPacketHandler;
import core.packethandlers.ChatPacketHandler;
import core.packethandlers.LoginPacketHandler;
import core.packethandlers.RequestPacketHandler;
import core.packets.FileStructurePacket;

public class ServerMain {

    public static final float NEWEST_VERSION = 0.15f;
    public static final String DOWNLOAD_URL = "https://drive.google.com/uc?export=download&id=0B--FUaYGCO3DVzhzVm1obE9vbUE";

    private static NetworkServer server;

    public static void main(String[] args) {
        Console.close();

        server = new NetworkServer();

        server.addPacketHandler(new LoginPacketHandler());
        server.addPacketHandler(new RequestPacketHandler());
        server.addPacketHandler(new FileTransferPacketHandler("Server/storage/", (file, fileInfo) -> FileManager.addFile(fileInfo)));
        server.addPacketHandler(new ChatPacketHandler());
        server.addPacketHandler(new ChatLoginPacketHandler());

        server.start();
    }

    public static void sendFileStructure(Connection connection, FileStructure fileStructure) {
        FileStructurePacket fileStructurePacket = new FileStructurePacket(fileStructure);

        if (connection == null)
            server.sendTCP(fileStructurePacket);
        else
            server.sendTCP(fileStructurePacket, connection.getID());

        Console.info("Sent file structure to " + (connection == null ? "all" : connection.getRemoteAddressTCP().toString()));
    }

}
