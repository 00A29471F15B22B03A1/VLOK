package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.database.FileDatabase;
import core.logging.Logger;
import core.packets.FileTransferPacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReceiver extends Listener {

    private Map<Integer, FileOutputStream> receivingCache;
    private FileStructure fileStructure;

    public FileReceiver(FileStructure fileStructure) {
        receivingCache = new HashMap<>();
        this.fileStructure = fileStructure;
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
            Logger.err("Failed to close input stream for connection " + connection.getID());
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
                FileOutputStream fos = getFileOutputStream(p.fileInfo);
                receivingCache.put(c.getID(), fos);
            }

            //Finished receiving fileInfo
            if (p.finished) {
                try {
                    receivingCache.get(c.getID()).close();
                    receivingCache.remove(c.getID());

                    fileStructure.addFile(p.fileInfo);
                    FileDatabase.addFile(p.fileInfo.name, p.fileInfo.description);
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.err("Failed to close input stream for connection " + c.getID());
                }

                return;
            }

            try {
                receivingCache.get(c.getID()).write(p.data);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.err("Failed to write to output stream for " + p.fileInfo.name);
            }
        }

    }

    private static FileOutputStream getFileOutputStream(FileInfo fileInfo) {

        try {
            String fullPath = "storage/" + fileInfo.path + "/" + fileInfo.name;

            File folder = new File("storage/" + fileInfo.path);

            if (!folder.mkdirs())
                Logger.info("Failed to create folder or folder already exists for fileInfo " + fileInfo.name);

            File file = new File(fullPath);
            if (!file.exists() && !file.createNewFile()) {
                Logger.info("Failed to create fileInfo " + fileInfo.name);
                return null;
            }

            return new FileOutputStream(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.err("Failed to create output stream for " + fileInfo.name);
        }
        return null;
    }

}
