package core.packethandlers;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.logging.Console;
import core.packets.ChatMessagePacket;
import core.packets.Packet;

public class ChatPacketHandler extends PacketHandler {

    public ChatPacketHandler() {
        super(ChatMessagePacket.class);
    }

    @Override
    public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
        Console.info("Received chat message from " + c.getID());
        ni.sendTCP(p);


    }
}
