package core.packethandlers;

import com.esotericsoftware.kryonet.Connection;
import core.*;
import core.database.FileDatabase;
import core.database.UserDatabase;
import core.packets.Packet;
import core.packets.RequestPacket;

public class RequestPacketHandler extends PacketHandler {

    public RequestPacketHandler() {
        super(RequestPacket.class);
    }

    @Override
    public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
        RequestPacket packet = (RequestPacket) p;

        if (!UserManager.checkSessionKey(c.getID(), packet.sessionKey))
            return;

        boolean admin = UserDatabase.isAdmin(UserManager.getLoginKey(packet.sessionKey));

        switch (packet.type) {
            case FILE_STRUCTURE:
                ServerMain.sendFileStructure(c, FileManager.getNonPendingFiles());
                break;

            case FILE_STRUCTURE_ALL:
                if (admin)
                    ServerMain.sendFileStructure(c, FileManager.getAllFiles());
                break;

            case FILE_STRUCTURE_PENDING:
                if (admin)
                    ServerMain.sendFileStructure(c, FileManager.getPendingFiles());
                break;

            case FILE_DOWNLOAD:
                int fileId = Integer.parseInt(((RequestPacket) p).argument);
                FileSender.sendFile(ni, c, FileManager.getFile(fileId));
                break;

            case FILE_UNPEND:
                if (admin) {
                    int id = Integer.parseInt(packet.argument.split("|")[0]);
                    String path = packet.argument.split("|")[1];
                    FileDatabase.unpend(id, path);
                }
                break;
        }
    }

}
