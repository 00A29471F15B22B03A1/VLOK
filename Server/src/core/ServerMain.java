package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import core.database.FileDatabase;
import core.logging.Logger;
import core.packethandlers.LoginPacketHandler;
import core.packethandlers.RequestPacketHandler;
import core.packets.FileStructurePacket;

public class ServerMain {

    private static NetworkServer server;

    public static FileStructure fileStructure;

    public static void main(String[] args) {
        Utils.setNativeLookAndFeel();

        fileStructure = FileDatabase.getAllFiles();

        Logger.closeWindow();

        server = new NetworkServer();

        server.addListener(new FileReceiver(fileStructure));

        server.addPacketHandler(new LoginPacketHandler());
        server.addPacketHandler(new RequestPacketHandler());
        
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Logger.info("New connection: " + connection.getRemoteAddressTCP());
            }

            @Override
            public void disconnected(Connection connection) {
                UserManager.removeUser(connection.getID());
                Logger.info("Removed connection: " + connection.getID());
            }

            @Override
            public void received(Connection connection, Object o) {
                if (o.getClass() != FrameworkMessage.KeepAlive.class)
                    Logger.info("Received " + o.getClass().getSimpleName());
            }
        });

        server.start();
    }

    public static void sendFileStructure(Connection connection, FileStructure fileStructure) {
        FileStructurePacket fileStructurePacket = new FileStructurePacket();

        fileStructurePacket.fileStructure = fileStructure;

        if (connection == null)
            server.sendTCP(fileStructurePacket);
        else
            server.sendTCP(fileStructurePacket, connection.getID());

        Logger.info("Sent file structure to " + (connection == null ? "all" : connection.getRemoteAddressTCP().toString()));
    }
}
