package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.filesystem.SupportedFileTypes;
import core.packets.FileCompletedPacket;
import core.packets.FileUploadPacket;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientMain {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        NetworkClient client = new NetworkClient();
        client.connect("192.168.1.27");

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("Connected " + connection.getRemoteAddressTCP());
            }
        });

        System.out.println("Successfully connected to server");

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        String[] supportedFiles = new String[SupportedFileTypes.values().length];

        int i = 0;
        for (SupportedFileTypes file : SupportedFileTypes.values())
            supportedFiles[i++] = file.toString().toLowerCase();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Upload Files", supportedFiles);
        chooser.setFileFilter(filter);

        while (true) {
            int returnValue = chooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();

                String path = JOptionPane.showInputDialog("Please gimme path:");

                for (File file : files) {

                    sendFile(path, file, client);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void sendFilePacket(String fileName, String filePath, byte[] data, NetworkClient client) {
        FileUploadPacket packet = new FileUploadPacket();
        packet.name = fileName.replaceAll("'", "");
        packet.path = filePath;
        packet.data = data;
        client.sendTCP(packet);
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

    public static byte[] compact(byte[] data, int size) {
        byte[] result = new byte[size];

        for (int i = 0; i < size; i++)
            result[i] = data[i];

        return result;
    }

    public static byte[][] divideArray(byte[] source) {
        int chunkSize = FileUploadPacket.MAX_PACKET_SIZE;
        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) chunkSize)][chunkSize];

        int start = 0;

        for (int i = 0; i < ret.length; i++) {
            if (start + chunkSize > source.length) {
                System.arraycopy(source, start, ret[i], 0, source.length - start);
            } else {
                System.arraycopy(source, start, ret[i], 0, chunkSize);
            }
            start += chunkSize;
        }

        return ret;
    }

    private static byte[] read(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
