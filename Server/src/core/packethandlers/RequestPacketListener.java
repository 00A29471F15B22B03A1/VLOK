package core.packethandlers;

import core.*;
import core.networking.Server;
import core.serialization.VLOKDatabase;

public class RequestPacketListener extends PacketListener {

    public RequestPacketListener() {
        super("request");
    }

    @Override
    public void packetReceived(VLOKDatabase db, int c, Server s) {
        byte type = db.findObject("data").findField("type").getByte();

        switch (type) {
            case RequestType.FILE_STRUCTURE:
                ServerMain.sendFileStructure(c, FileManager.getNonPendingFiles());
                break;

            case RequestType.FILE_STRUCTURE_PENDING:
                ServerMain.sendFileStructure(c, FileManager.getPendingFiles());
                break;

            case RequestType.FILE_STRUCTURE_ALL:
                ServerMain.sendFileStructure(c, FileManager.getAllFiles());
                break;

            case RequestType.FILE_DOWNLOAD:
                FileSender.sendFile(db.findObject("data").findField("argument").getInt(), s, c);
                break;

            case RequestType.DOCUMENTATION:
                DocumentationLoader.sendDocumentation(s, c);
                break;
        }
    }
}
