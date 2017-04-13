package core;

import com.esotericsoftware.kryonet.Connection;
import core.logging.Console;
import core.packets.FileTransferPacket;
import core.packets.Packet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTransferPacketHandler extends PacketHandler {

    private final Map<Integer, FileOutputStream> outputStreams;
    private final String outputFolder;
    private final TransferFinished transferFinished;

    public FileTransferPacketHandler(String outputFolder, TransferFinished transferFinished) {
        super(FileTransferPacket.class);
        this.outputStreams = new HashMap<>();
        this.outputFolder = outputFolder;
        this.transferFinished = transferFinished;
    }

    @Override
    public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
        FileTransferPacket packet = (FileTransferPacket) p;

        if (!outputStreams.containsKey(c.getID())) {
            FileOutputStream fos = createOutputStream(outputFolder, packet.fileInfo.name);
            outputStreams.put(c.getID(), fos);
        }

        if (packet.finished) {
            try {
                outputStreams.get(c.getID()).close();
                outputStreams.remove(c.getID());

                File resultFile = new File(outputFolder + "/" + packet.fileInfo.name);
                transferFinished.onTransferFinished(resultFile, packet.fileInfo);
            } catch (IOException e) {
                e.printStackTrace();
                Console.err("Failed to close output stream for connection " + c.getID());
            }
            return;
        }

        try {
            outputStreams.get(c.getID()).write(packet.data);
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
