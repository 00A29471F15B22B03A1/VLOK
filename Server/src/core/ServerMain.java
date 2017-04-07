package core;

import com.esotericsoftware.kryonet.Connection;
import core.logging.Console;
import core.packethandlers.LoginPacketHandler;
import core.packethandlers.RequestPacketHandler;
import core.packets.FileStructurePacket;

public class ServerMain {

    public static final float NEWEST_VERSION = 0.1f;
    public static final String DOWNLOAD_URL = "https://drive.google.com/uc?export=download&id=0B--FUaYGCO3DVzhzVm1obE9vbUE";

    private static NetworkServer server;

    public static void main(String[] args) {
        Utils.setNativeLookAndFeel();

        server = new NetworkServer();

        server.addListener(new FileReceiver());

        server.addPacketHandler(new LoginPacketHandler());
        server.addPacketHandler(new RequestPacketHandler());

        server.start();
    }


    public static void sendFileStructure(Connection connection, FileStructure fileStructure) {
        FileStructurePacket fileStructurePacket = new FileStructurePacket();

        fileStructurePacket.fileStructure = fileStructure;

        if (connection == null)
            server.sendTCP(fileStructurePacket);
        else
            server.sendTCP(fileStructurePacket, connection.getID());

        Console.info("Sent file structure to " + (connection == null ? "all" : connection.getRemoteAddressTCP().toString()));
    }

}
