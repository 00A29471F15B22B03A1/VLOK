package core.packetlisteners;

import core.Client;
import core.Utils;
import core.logging.Console;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKObject;

import java.awt.*;
import java.io.*;

public class FileTransferPacketListener extends PacketListener {

    private OutputStream outputStream;

    public FileTransferPacketListener() {
        super("file_transfer");
    }

    @Override
    public void packetReceived(VLOKDatabase db, Client c) {
        VLOKObject object = db.findObject("data");
        String name = object.findString("name").getString();

        if (outputStream == null)
            createNewFileInputStream(name);

        if (object.findField("finished") != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;

            try {
                Console.info("Opening " + name + "...");
                Desktop.getDesktop().open(new File(Utils.getDownloadPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            outputStream.write(object.findArray("data").data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewFileInputStream(String fileName) {
        try {
            outputStream = new FileOutputStream(new File(Utils.getDownloadPath() + "/" + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
