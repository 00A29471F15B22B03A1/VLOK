package core;

import core.localization.Localization;
import core.logging.Logger;
import core.packets.FileTransferPacket;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSender {

    public static void sendFile(FileInfo fileInfo, File file, String sessionKey, NetworkClient client) {
        if (file.length() > Math.pow(10, 9)) {
            JOptionPane.showMessageDialog(null, Localization.getString("error.file_too_big"), Localization.getString("error.error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        Logger.info("Started uploading " + fileInfo.name);

        try {
            byte[] buffer = new byte[FileTransferPacket.MAX_PACKET_SIZE];
            InputStream inputStream = new FileInputStream(file);

            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                byte[] sendData = compact(buffer, read);

                if (sendData.length == 0) continue;

                sendFilePacket(fileInfo, sendData, sessionKey, client);

                Thread.sleep(1);
            }

            inputStream.close();

            FileTransferPacket finishedPacket = new FileTransferPacket();
            finishedPacket.sessionKey = sessionKey;
            finishedPacket.fileInfo = fileInfo;
            finishedPacket.finished = true;
            client.sendTCP(finishedPacket);

            Logger.info("Finished uploading " + fileInfo.name);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Logger.err("Failed to upload " + fileInfo.name);
        }
    }

    private static void sendFilePacket(FileInfo fileInfo, byte[] data, String sessionKey, NetworkClient client) {
        FileTransferPacket packet = new FileTransferPacket();
        packet.sessionKey = sessionKey;
        packet.fileInfo = fileInfo;
        packet.data = data;
        client.sendTCP(packet);
    }

    private static byte[] compact(byte[] data, int size) {
        byte[] result = new byte[size];

        System.arraycopy(data, 0, result, 0, size);

        return result;
    }

}
