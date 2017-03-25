package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.packets.FileTransferPacket;
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

    public static void init() {
        outputStreams = new HashMap<>();

        client = new NetworkClient();
        client.connect("vlok.dynu.com");

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileTransferPacket) {
                    FileTransferPacket packet = (FileTransferPacket) o;

                    if (!outputStreams.containsKey(connection)) {
                        OutputStream outputStream = createOutputStream(packet.fileInfo.getName());

                        if (outputStream == null) {
                            System.err.println("Failed to create OutputStream");
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

                            File file = new File(tempPath + "/" + packet.fileInfo.getName());
                            Desktop.getDesktop().open(file);

                            System.out.println("Finished " + packet.fileInfo.getName());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        outputStreams.get(connection).write(packet.data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static OutputStream createOutputStream(String name) {
        try {
            System.out.println("Downloading to " + (File.createTempFile("temp-file", "tmp").getParent() + "/" + name));
            return new FileOutputStream(new File(File.createTempFile("temp-file", "tmp").getParent() + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendDownloadRequest(FileInfo fileInfo) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.type = RequestPacket.Type.FILE_DOWNLOAD;
        requestPacket.argument = fileInfo.getName();
        client.sendTCP(requestPacket);
    }

}
