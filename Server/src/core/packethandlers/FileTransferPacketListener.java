package core.packethandlers;

import core.FileInfo;
import core.PacketListener;
import core.Server;
import core.logging.Console;
import core.serialization.VLOKDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTransferPacketListener extends PacketListener {

    private final Map<Integer, FileOutputStream> outputStreams;
    private final String outputFolder;
    private final TransferFinished transferFinished;

    public FileTransferPacketListener(String outputFolder, TransferFinished transferFinished) {
        super("file_transfer");
        this.outputStreams = new HashMap<>();
        this.outputFolder = outputFolder;
        this.transferFinished = transferFinished;
    }

    @Override
    public void packetReceived(VLOKDatabase db, int c, Server server) {
        String fileName = db.findObject("data").findString("name").getString();

        if (!outputStreams.containsKey(c)) {
            FileOutputStream fos = createOutputStream(outputFolder, fileName);
            outputStreams.put(c, fos);
        }

        if (db.findObject("progress").findField("data").getBoolean()) {
            try {
                outputStreams.get(c).close();
                outputStreams.remove(c);

                File resultFile = new File(outputFolder + "/" + fileName);
                //TODO: implement
                //transferFinished.onTransferFinished(resultFile, );
            } catch (IOException e) {
                e.printStackTrace();
                Console.err("Failed to close output stream for connection " + c);
            }
            return;
        }

        try {
            outputStreams.get(c).write(db.findObject("data").findArray("data").data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FileOutputStream createOutputStream(String folderName, String fileName) {
        try {

            String fullPath = folderName + "/" + fileName;

            File folder = new File(folderName);

            if (!folder.mkdirs())
                Console.info("Failed to create folder or folder already exists for fileInfo " + fileName);

            File file = new File(fullPath);
            if (!file.exists() && !file.createNewFile()) {
                Console.info("Failed to create fileInfo " + fileName);
                return null;
            }

            return new FileOutputStream(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to create output stream for " + fileName);
        }
        return null;
    }

    public interface TransferFinished {
        void onTransferFinished(File file, FileInfo fileInfo);
    }

}
