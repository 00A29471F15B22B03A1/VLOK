package core;

import core.logging.Console;
import core.packets.FileTransferPacket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSender {

    public static void sendFile(FileInfo fi, File file, String sk, SendAction sa) {
        Console.info("Started transfer of " + fi.name);

        try {
            InputStream is = new FileInputStream(file);

            byte[] buffer = new byte[FileTransferPacket.MAX_PACKET_SIZE];

            int read;

            while ((read = is.read(buffer)) != -1) {
                byte[] sendData = compact(buffer, read);

                if (sendData.length == 0) continue;

                sa.send(new FileTransferPacket(fi, sendData, false, sk));
            }

            is.close();

            sa.send(new FileTransferPacket(fi, null, true, sk));

            Console.info("Finished transfer of " + fi.name);
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to transfer " + fi.name);
        }
    }

    public interface SendAction {
        void send(FileTransferPacket packet);
    }

    private static byte[] compact(byte[] data, int size) {
        byte[] result = new byte[size];

        System.arraycopy(data, 0, result, 0, size);

        return result;
    }
}
