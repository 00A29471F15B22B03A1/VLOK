package core.packethandlers;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.logging.Console;
import core.packets.ChatLoginPacket;
import core.packets.Packet;

public class ChatLoginPacketHandler extends PacketHandler {

    public ChatLoginPacketHandler() {
        super(ChatLoginPacket.class);
    }

    @Override
    public void handlePacket(Packet p, Connection c, NetworkInterface ni) {

        Console.info("Received chat login from " + c.getID());
        ni.sendTCP(p);


    }
}
