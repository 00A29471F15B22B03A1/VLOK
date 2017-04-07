package core.packethandlers;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.logging.Console;
import core.packets.FileTransferPacket;
import core.packets.Packet;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileTransferPacketHandler extends PacketHandler {

    private Map<Connection, OutputStream> outputStreams;

    public FileTransferPacketHandler() {
        super(FileTransferPacket.class);
        outputStreams = new HashMap<>();
    }

    @Override
    public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
        FileTransferPacket packet = (FileTransferPacket) p;

        if (!outputStreams.containsKey(c)) {
            OutputStream outputStream = createOutputStream(packet.fileInfo.name);

            if (outputStream == null) {
                Console.err("Failed to create output stream");
                return;
            }

            outputStreams.put(c, outputStream);
        }

        if (packet.finished) {
            try {
                outputStreams.get(c).flush();
                outputStreams.get(c).close();
                outputStreams.remove(c);

                String tempPath = File.createTempFile("temp-file", ".tmp").getParent();

                Console.info("Finished downloading " + packet.fileInfo.name);
                Console.info("Opening " + packet.fileInfo.name);

                File file = new File(tempPath + "/" + packet.fileInfo.name);
                Desktop.getDesktop().open(file);

                return;
            } catch (IOException e) {
                e.printStackTrace();
                Console.err("Failed to finish downloading " + packet.fileInfo.name);
            }
        }

        try {
            outputStreams.get(c).write(packet.data);
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed writing to output steam");
        }
    }

    private OutputStream createOutputStream(String name) {
        try {
            Console.info("Created output stream for " + name);
            return new FileOutputStream(new File(File.createTempFile("temp-file", "tmp").getParent() + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed creating output stream for " + name);
        }
        return null;
    }
}
