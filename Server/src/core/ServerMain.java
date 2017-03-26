package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import core.database.UserDatabase;
import core.database.UserManager;
import core.logging.Logger;
import core.packets.FileStructurePacket;
import core.packets.FileTransferPacket;
import core.packets.LoginPacket;
import core.packets.RequestPacket;

import java.util.Arrays;

public class ServerMain {

    private static NetworkServer server;

    private static FileStructure fileStructure;

    public static void main(String[] args) {
        fileStructure = FileDatabase.getAllFiles();

        Logger.closeWindow();

        server = new NetworkServer();

        server.addListener(new FileReceiver(fileStructure));

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Logger.info("New connection: " + connection.getRemoteAddressTCP());
            }

            @Override
            public void received(Connection connection, Object o) {
                if (o.getClass() != FrameworkMessage.KeepAlive.class)
                    Logger.info("Received " + o.getClass().getSimpleName());

                if (o instanceof FileTransferPacket) {
                    FileTransferPacket packet = (FileTransferPacket) o;

                    if (!UserManager.checkKey(connection.getID(), packet.sessionKey))
                        return;

                    if (packet.finished)
                        sendFileStructure(null);

                } else if (o instanceof RequestPacket) {
                    RequestPacket packet = (RequestPacket) o;

                    if (!UserManager.checkKey(connection.getID(), packet.sessionKey))
                        return;

                    if (packet.type == RequestPacket.Type.FILE_STRUCTURE)
                        sendFileStructure(connection);

                    else if (packet.type == RequestPacket.Type.FILE_DOWNLOAD)
                        FileSender.sendFile(server, connection, fileStructure.getFile(packet.argument));

                } else if (o instanceof LoginPacket) {
                    LoginPacket packet = (LoginPacket) o;

                    String[] keys = packet.fullKey.split("¡");

                    System.out.println(Arrays.toString(keys));

                    LoginPacket response = new LoginPacket();


                    if (UserDatabase.isValid(keys[0], keys[1])) {
                        response.sessionKey = UserManager.newKey();
                        UserManager.newUser(connection.getID(), response.sessionKey);
                    }

                    server.sendTCP(connection.getID(), response);
                }
            }
        });

        server.start();
    }

    private static void sendFileStructure(Connection connection) {
        FileStructurePacket fileStructurePacket = new FileStructurePacket();

        fileStructurePacket.fileStructure = fileStructure;

        if (connection == null)
            server.sendToAllTCP(fileStructurePacket);
        else
            server.sendTCP(connection.getID(), fileStructurePacket);

        Logger.info("Sent file structure to " + (connection == null ? "all" : connection.getRemoteAddressTCP().toString()));
    }
}
