package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
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
    public void received(Connection connection, Object o) {
        if (o instanceof FileTransferPacket) {
            FileTransferPacket p = (FileTransferPacket) o;

            //Started receiving fileInfo
            if (!receivingCache.containsKey(connection.getID())) {
                FileOutputStream fos = getFileOutputStream(p.fileInfo);
                receivingCache.put(connection.getID(), fos);
            }

            //Finished receiving fileInfo
            if (p.finished) {
                try {
                    receivingCache.get(connection.getID()).close();
                    receivingCache.remove(connection.getID());

                    fileStructure.addFile(p.fileInfo);
                    FileDatabase.addFile(p.fileInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.err("Failed to close input stream for connection " + connection.getID());
                }

                return;
            }

            try {
                receivingCache.get(connection.getID()).write(p.data);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.err("Failed to write to output stream for " + p.fileInfo.getName());
            }
        }

    }

    private static FileOutputStream getFileOutputStream(FileInfo fileInfo) {

        try {
            String fullPath = "storage/" + fileInfo.getPath() + "/" + fileInfo.getName();

            File folder = new File("storage/" + fileInfo.getPath());

            if (!folder.mkdirs())
                System.err.println("Failed to create folder or folder already exists for fileInfo " + fileInfo.getName());

            File file = new File(fullPath);
            if (!file.exists() && !file.createNewFile()) {
                System.err.println("Failed to create fileInfo " + fileInfo.getName());
                return null;
            }

            return new FileOutputStream(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.err("Failed to create output stream for " + fileInfo.getName());
        }
        return null;
    }

}
