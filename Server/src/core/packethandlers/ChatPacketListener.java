package core.packethandlers;

import core.PacketListener;
import core.networking.Server;
import core.logging.Console;
import core.serialization.VLOKDatabase;

public class ChatPacketListener extends PacketListener {

    public ChatPacketListener() {
        super("chat_message");
    }

    @Override
    public void packetReceived(VLOKDatabase db, int c, Server server) {
        Console.info("Received chat message from " + c);
        server.send(db);
    }
}
