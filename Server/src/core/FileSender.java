package core;

import com.esotericsoftware.kryonet.Connection;
import core.packets.FileTransferPacket;

import java.io.*;

public class FileSender {

    public static void sendFile(final NetworkServer server, final Connection connection, final FileInfo fileInfo) {
        final InputStream inputStream = getInputStream(new File("storage/" + fileInfo.getPath() + "/" + fileInfo.getName()));
        if (inputStream == null)
            return;

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buffer = new byte[FileTransferPacket.MAX_PACKET_SIZE];

                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        byte[] sendData = compact(buffer, read);

                        sendFilePacket(fileInfo, sendData, server, connection);

                        Thread.sleep(1);
                    }

                    inputStream.close();

                    FileTransferPacket finishedPacket = new FileTransferPacket();
                    finishedPacket.fileInfo = fileInfo;
                    finishedPacket.finished = true;
                    server.sendTCP(connection.getID(), finishedPacket);

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private static InputStream getInputStream(File f) {
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void sendFilePacket(FileInfo fileInfo, byte[] data, NetworkServer server, Connection connection) {
        FileTransferPacket packet = new FileTransferPacket();
        packet.fileInfo = fileInfo;
        packet.data = data;
        server.sendTCP(connection.getID(), packet);
    }


    private static byte[] compact(byte[] data, int size) {
        byte[] result = new byte[size];

        System.arraycopy(data, 0, result, 0, size);

        return result;
    }

}
