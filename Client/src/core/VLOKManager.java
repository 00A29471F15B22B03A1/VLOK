package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.logging.Logger;
import core.packets.FileTransferPacket;
import core.packets.LoginPacket;
import core.packets.RequestPacket;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class VLOKManager {

    public static NetworkClient client;

    private static Map<Connection, OutputStream> outputStreams;
    private static String sessionKey;

    public static void init() {
        outputStreams = new HashMap<>();

        client = new NetworkClient();

        if (!client.connect("vlok.dynu.com"))
            System.exit(-1);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileTransferPacket) {
                    FileTransferPacket packet = (FileTransferPacket) o;

                    if (!outputStreams.containsKey(connection)) {
                        OutputStream outputStream = createOutputStream(packet.fileInfo.getName());

                        if (outputStream == null) {
                            Logger.err("Failed to create output stream");
                            return;
                        }

                        outputStreams.put(connection, outputStream);
                    }

                    if (packet.finished) {
                        try {
                            outputStreams.get(connection).flush();
                            outputStreams.get(connection).close();
                            outputStreams.remove(connection);

                            String tempPath = File.createTempFile("temp-file", "tmp").getParent();

                            Logger.info("Finished downloading " + packet.fileInfo.getName());

                            Logger.info("Opening " + packet.fileInfo.getName());

                            File file = new File(tempPath + "/" + packet.fileInfo.getName());
                            Desktop.getDesktop().open(file);

                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Logger.err("Failed to finish downloading " + packet.fileInfo.getName());
                        }
                    }

                    try {
                        outputStreams.get(connection).write(packet.data);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logger.err("Failed writing to output steam");
                    }
                }
            }
        });

        Logger.info("Initialized VLOK");
    }

    private static OutputStream createOutputStream(String name) {
        try {
            Logger.info("Created output stream for " + name);
            return new FileOutputStream(new File(File.createTempFile("temp-file", "tmp").getParent() + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.err("Failed creating output stream for " + name);
        }
        return null;
    }

    public static void sendFile(File file, FileInfo fileInfo) {
        FileSender.sendFile(fileInfo, file, sessionKey, client);
    }

    public static void sendDownloadRequest(FileInfo fileInfo) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.sessionKey = sessionKey;
        requestPacket.type = RequestPacket.Type.FILE_DOWNLOAD;
        requestPacket.argument = fileInfo.getName();

        client.sendTCP(requestPacket);
        Logger.info("Sent file download request");
    }

    public static void sendLogin(String key, String hashCode, String os) {
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.fullKey = key + "¡" + hashCode + "¡" + os;

        client.sendTCP(loginPacket);
    }

    public static void sendFileStructureRequest() {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.sessionKey = sessionKey;
        requestPacket.type = RequestPacket.Type.FILE_STRUCTURE;
        client.sendTCP(requestPacket);
    }

    public static void setSessionKey(String sessionKey) {
        VLOKManager.sessionKey = sessionKey;
    }
}
