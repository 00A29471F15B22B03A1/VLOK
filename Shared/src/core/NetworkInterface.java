package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public abstract class NetworkInterface {

    private List<PacketHandler> packetHandlers;

    public NetworkInterface() {
        packetHandlers = new ArrayList<>();
    }

    public void addPacketHandler(PacketHandler packetHandler) {
        packetHandlers.add(packetHandler);
    }

    public void handlePacket(Connection connection, Object o) {
        if (!(o instanceof Packet))
            return;

        Packet packet = (Packet) o;
        for (PacketHandler packetHandler : packetHandlers) {
            if (packetHandler.getType().equals(o.getClass()))
                packetHandler.handlePacket(packet, connection, this);
        }
    }

    public abstract void addListener(Listener listener);

    public abstract void sendTCP(Object o);

    public abstract void sendTCP(Object o, int connection);

    public abstract void start();

    public abstract void stop();

}