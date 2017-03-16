package core;

import core.packets.FileUploadPacket;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUploader {

    public static void sendFile(FileInfo fileInfo, File file, NetworkClient client) {
        if (file.length() > Math.pow(10, 9)) {
            JOptionPane.showMessageDialog(null, "File " + file.getName() + " is toooo big", "File toooo big", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            byte[] buffer = new byte[FileUploadPacket.MAX_PACKET_SIZE];
            InputStream inputStream = new FileInputStream(file);

            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                byte[] sendData = compact(buffer, read);

                sendFilePacket(fileInfo, sendData, client);

                Thread.sleep(2);
            }

            inputStream.close();

            FileUploadPacket finishedPacket = new FileUploadPacket();
            finishedPacket.fileInfo = fileInfo;
            finishedPacket.finished = true;
            client.sendTCP(finishedPacket);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendFilePacket(FileInfo fileInfo, byte[] data, NetworkClient client) {
        FileUploadPacket packet = new FileUploadPacket();
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
