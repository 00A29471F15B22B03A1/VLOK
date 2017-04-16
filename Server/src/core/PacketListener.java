package core;

import core.networking.Server;
import core.serialization.VLOKDatabase;

public abstract class PacketListener {

    private String packetName;

    public PacketListener(String packetName) {
        this.packetName = packetName;
    }

    public abstract void packetReceived(VLOKDatabase db, int c, Server s);

    public String getPacketName() {
        return packetName;
    }
}