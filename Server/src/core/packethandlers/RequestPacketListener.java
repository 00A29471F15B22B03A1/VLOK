package core.packethandlers;

import core.*;
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

                break;

            case RequestType.FILE_STRUCTURE_ALL:

                break;

            case RequestType.FILE_DOWNLOAD:

                break;

            case RequestType.DOCUMENTATION:
                DocumentationLoader.sendDocumentation(s, c);
                break;
        }
    }
}
