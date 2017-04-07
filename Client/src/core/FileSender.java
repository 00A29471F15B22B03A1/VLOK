package core;

import core.localization.Localization;
import core.logging.Console;
import core.packets.FileTransferPacket;
import core.ui.Popup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSender {

    //TODO: make on file sender for server and client
    public static void sendFile(String name, String description, File file, String sessionKey, NetworkInterface ni) {
        if (file.length() > Math.pow(10, 9)) {
            Popup.alert(Localization.get("error.error"), Localization.get("error.file_too_big"));
            return;
        }

        FileInfo fileInfo = new FileInfo(name, "", description, 0, false);

        Console.info("Started uploading " + name);

        try {
            byte[] buffer = new byte[FileTransferPacket.MAX_PACKET_SIZE];
            InputStream inputStream = new FileInputStream(file);

            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                byte[] sendData = compact(buffer, read);

                if (sendData.length == 0) continue;

                sendFilePacket(fileInfo, sendData, sessionKey, ni);

                Thread.sleep(1);
            }

            inputStream.close();

            FileTransferPacket finishedPacket = new FileTransferPacket();
            finishedPacket.sessionKey = sessionKey;
            finishedPacket.fileInfo = fileInfo;
            finishedPacket.finished = true;
            ni.sendTCP(finishedPacket);

            Console.info("Finished uploading " + fileInfo.name);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Console.err("Failed to upload " + fileInfo.name);
        }
    }

    private static void sendFilePacket(FileInfo fileInfo, byte[] data, String sessionKey, NetworkInterface ni) {
        FileTransferPacket packet = new FileTransferPacket();
        packet.sessionKey = sessionKey;
        packet.fileInfo = fileInfo;
        packet.data = data;
        ni.sendTCP(packet);
    }

    private static byte[] compact(byte[] data, int size) {
        byte[] result = new byte[size];

        System.arraycopy(data, 0, result, 0, size);

        return result;
    }

}
