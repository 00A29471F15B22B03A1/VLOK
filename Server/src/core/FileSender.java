package core;

import core.logging.Console;
import core.networking.Server;
import core.serialization.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileSender {

    private static final int MAX_PACKET_SIZE = 1024 * 10;

    public static void sendFile(int fileID, Server server, int c) {
        FileInfo fileInfo = FileManager.getFile(fileID);
        String path = "storage/" + fileInfo.name;

        byte[] buffer = new byte[MAX_PACKET_SIZE];

        try {
            FileInputStream fis = new FileInputStream(new File(path));

            int size;
            while ((size = fis.read(buffer)) != -1) {

                byte[] sendBuffer = trim(buffer, size);

                sendDB(sendBuffer, fileInfo.name, server, c);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            fis.close();

            sendFinished(fileInfo.name, server, c);

        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to read file " + path);
        }
    }

    private static void sendFinished(String fileName, Server server, int c) {
        VLOKDatabase db = new VLOKDatabase("file_transfer");
        VLOKObject data = new VLOKObject("data");
        data.addString(VLOKString.Create("name", fileName));
        data.addField(VLOKField.Boolean("finished", true));
        db.addObject(data);

        server.send(db, c);
    }

    private static void sendDB(byte[] bytes, String fileName, Server server, int c) {
        VLOKDatabase db = new VLOKDatabase("file_transfer");
        VLOKObject data = new VLOKObject("data");
        data.addString(VLOKString.Create("name", fileName));
        data.addArray(VLOKArray.Byte("data", bytes));
        db.addObject(data);

        server.send(db, c);
    }

    private static byte[] trim(byte[] src, int size) {
        byte[] result = new byte[size];
        System.arraycopy(src, 0, result, 0, size);
        return result;
    }

}
