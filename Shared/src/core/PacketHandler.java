package core;

import com.esotericsoftware.kryonet.Connection;
import core.packets.Packet;

public abstract class PacketHandler {

    private final Class<? extends Packet> type;

    public PacketHandler(Class<? extends Packet> type) {
        this.type = type;
    }

    public abstract void handlePacket(Packet p, Connection c, NetworkInterface ni);

    public Class<? extends Packet> getType() {
        return type;
    }

}
