package core;

import core.logging.Console;
import core.networking.Server;
import core.packethandlers.ChatPacketListener;
import core.packethandlers.LoginPacketListener;
import core.packethandlers.RequestPacketListener;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKField;
import core.serialization.VLOKObject;
import core.serialization.VLOKString;

public class ServerMain {

    public static final float NEWEST_VERSION = 0.15f;
    public static final String DOWNLOAD_URL = "https://drive.google.com/uc?export=download&id=0B--FUaYGCO3DVzhzVm1obE9vbUE";

    private static Server server;

    public static void main(String[] args) {
        Console.close();

        server = new Server(54555);

        server.start();

        server.addPacketListener(new LoginPacketListener());
        server.addPacketListener(new RequestPacketListener());
        server.addPacketListener(new ChatPacketListener());
    }

    public static void sendFileStructure(int c, FileStructure fileStructure) {
        VLOKDatabase db = new VLOKDatabase("file_structure");

        for (FileInfo fileInfo : fileStructure.getFiles()) {
            VLOKObject file = new VLOKObject("file");
            file.addField(VLOKField.Integer("id", fileInfo.id));
            file.addString(VLOKString.Create("name", fileInfo.name));
            file.addString(VLOKString.Create("path", fileInfo.path));
            file.addString(VLOKString.Create("description", fileInfo.description == null ? "" : fileInfo.description));
            file.addField(VLOKField.Integer("minPermission", fileInfo.minPermissionLevel));
            file.addField(VLOKField.Boolean("pending", fileInfo.pending));
            file.addString(VLOKString.Create("uploadDate", fileInfo.uploadDate == null ? "" : fileInfo.uploadDate));
            db.addObject(file);
        }

        if (c == -1)
            server.send(db);
        else
            server.send(db, c);

        Console.info("Sent file structure to " + (c == -1 ? "all" : c));
    }

}
