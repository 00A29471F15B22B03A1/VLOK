package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.packets.FileInfoPacket;
import core.packets.FileStoragePacket;

import javax.swing.*;
import java.io.File;

public class ClientMain {

    public static void main(String[] args) throws InterruptedException {
        NetworkClient client = new NetworkClient();

//        client.connect("vlok.dynu.com");
        client.connect("192.168.1.39");

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileInfoPacket) {
                    System.out.println(((FileInfoPacket) o).fileInfo.getPath());
                } else if (o instanceof FileStoragePacket) {
                    System.out.println(((FileStoragePacket) o).fileStorage.getFiles());
                }
            }
        });

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            long startTime = System.currentTimeMillis();
            FileUploader.sendFile(new FileInfo(selectedFile.getName(), "res/test/", "a test fileInfo", PermissionLevels.PEASAN), selectedFile, client);
            System.out.println(System.currentTimeMillis() - startTime);
        }

    }

}