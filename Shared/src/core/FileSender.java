package core;

import com.esotericsoftware.kryonet.Connection;
import core.logging.Logger;
import core.packets.FileTransferPacket;

import java.io.*;

public class FileSender {

    public static void sendFile(final NetworkInterface ni, final Connection connection, final FileInfo fileInfo) {
        final InputStream inputStream = getInputStream(new File("storage/" + fileInfo.name));
        if (inputStream == null)
            return;

        final Thread thread = new Thread(() -> {
            try {
                byte[] buffer = new byte[FileTransferPacket.MAX_PACKET_SIZE];

                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    byte[] sendData = compact(buffer, read);

                    sendFilePacket(fileInfo, sendData, ni, connection);

                    Thread.sleep(1);
                }

                inputStream.close();

                FileTransferPacket finishedPacket = new FileTransferPacket();
                finishedPacket.fileInfo = fileInfo;
                finishedPacket.finished = true;
                ni.sendTCP(finishedPacket, connection.getID());

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Logger.err("Failed to read file " + fileInfo.name);
            }
        });
        thread.start();
    }

    private static InputStream getInputStream(File f) {
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.err("Failed to create new input stream for " + f.getName());
        }
        return null;
    }


    private static void sendFilePacket(FileInfo fileInfo, byte[] data, NetworkInterface ni, Connection connection) {
        FileTransferPacket packet = new FileTransferPacket();
        packet.fileInfo = fileInfo;
        packet.data = data;
        ni.sendTCP(packet, connection.getID());
    }


    private static byte[] compact(byte[] data, int size) {
        byte[] result = new byte[size];

        System.arraycopy(data, 0, result, 0, size);

        return result;
    }

}
