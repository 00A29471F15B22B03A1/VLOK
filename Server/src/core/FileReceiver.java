package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.logging.Console;
import core.packets.FileTransferPacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReceiver extends Listener {

    private Map<Integer, FileOutputStream> receivingCache;

    public FileReceiver() {
        receivingCache = new HashMap<>();
    }

    @Override
    public void disconnected(Connection connection) {
        try {
            FileOutputStream fos = receivingCache.get(connection.getID());

            if (fos == null)
                return;

            fos.close();

            receivingCache.remove(connection.getID());
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to stop input stream for connection " + connection.getID());
        }
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof FileTransferPacket) {
            FileTransferPacket p = (FileTransferPacket) o;

            if (!UserManager.checkSessionKey(c.getID(), p.sessionKey))
                return;

            //Started receiving fileInfo
            if (!receivingCache.containsKey(c.getID())) {
                FileOutputStream fos = createOutputStream(p.fileInfo);
                receivingCache.put(c.getID(), fos);
            }

            //Finished receiving fileInfo
            if (p.finished) {
                try {
                    receivingCache.get(c.getID()).close();
                    receivingCache.remove(c.getID());

                    FileManager.addFile(p.fileInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                    Console.err("Failed to stop input stream for connection " + c.getID());
                }

                return;
            }

            try {
                receivingCache.get(c.getID()).write(p.data);
            } catch (IOException e) {
                e.printStackTrace();
                Console.err("Failed to write to output stream for " + p.fileInfo.name);
            }
        }
    }

    private static FileOutputStream createOutputStream(FileInfo fileInfo) {
        try {
            String fullPath = "storage/" + fileInfo.path + "/" + fileInfo.name;

            File folder = new File("storage/" + fileInfo.path);

            if (!folder.mkdirs())
                Console.info("Failed to create folder or folder already exists for fileInfo " + fileInfo.name);

            File file = new File(fullPath);
            if (!file.exists() && !file.createNewFile()) {
                Console.info("Failed to create fileInfo " + fileInfo.name);
                return null;
            }

            return new FileOutputStream(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to create output stream for " + fileInfo.name);
        }
        return null;
    }

}
