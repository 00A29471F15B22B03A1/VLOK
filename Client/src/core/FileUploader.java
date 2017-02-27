package core;

import core.packets.FileCompletedPacket;
import core.packets.FileUploadPacket;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUploader {

    public static void sendFiles(String path, File[] files, NetworkClient client) {
        for (File file : files) {

            sendFile(path, file, client);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendFile(String path, File file, NetworkClient client) {
        if (file.length() > 1000000000) {
            JOptionPane.showMessageDialog(null, "File " + file.getName() + " is toooo big", "File toooo big", JOptionPane.ERROR_MESSAGE);
            return;
        }
        InputStream ios;
        try {
            byte[] buffer = new byte[FileUploadPacket.MAX_PACKET_SIZE];
            ios = new FileInputStream(file);

            int read;

            while ((read = ios.read(buffer)) != -1) {
                byte[] sendData = compact(buffer, read);
                System.out.println(file.getName() + ": " + sendData.length);
                sendFilePacket(file.getName(), path, sendData, client);

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            FileCompletedPacket completedPacket = new FileCompletedPacket();
            completedPacket.name = file.getName();
            completedPacket.path = path;

            client.sendTCP(completedPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFilePacket(String fileName, String filePath, byte[] data, NetworkClient client) {
        FileUploadPacket packet = new FileUploadPacket();
        packet.name = fileName.replaceAll("'", "");
        packet.path = filePath;
        packet.data = data;
        client.sendTCP(packet);
    }

    public static byte[] compact(byte[] data, int size) {
        byte[] result = new byte[size];

        System.arraycopy(data, 0, result, 0, size);

        return result;
    }

}
