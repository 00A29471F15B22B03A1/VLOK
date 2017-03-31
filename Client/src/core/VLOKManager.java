package core;

import core.logging.Logger;
import core.packethandlers.FileTransferPacketHandler;
import core.packets.LoginPacket;
import core.packets.RequestPacket;

import java.io.File;

public class VLOKManager {

    public static NetworkClient client;


    public static void init() {
        client = new NetworkClient("vlok.dynu.com");

        client.start();

        client.addPacketHandler(new FileTransferPacketHandler());

        Logger.info("Initialized VLOK");
    }


    public static void sendFile(File file, FileInfo fileInfo) {
        FileSender.sendFile(fileInfo, file, User.sessionKey, client);
    }

    public static void sendDownloadRequest(FileInfo fileInfo) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.sessionKey = User.sessionKey;
        requestPacket.type = RequestPacket.Type.FILE_DOWNLOAD;
        requestPacket.argument = fileInfo.name;

        client.sendTCP(requestPacket);
        Logger.info("Sent file download request");
    }

    public static void sendLogin(String key, String hashCode, String os) {
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.fullKey = key + "¡" + hashCode + "¡" + os;

        client.sendTCP(loginPacket);
    }

    public static void sendRequest(RequestPacket.Type type, String argument) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.sessionKey = User.sessionKey;
        requestPacket.type = type;
        requestPacket.argument = argument;
        client.sendTCP(requestPacket);
    }

}
