package core.packetlisteners;

import core.Client;
import core.serialization.VLOKDatabase;

public abstract class PacketListener {

    private String packetName;

    public PacketListener(String packetName) {
        this.packetName = packetName;
    }

    public abstract void packetReceived(VLOKDatabase db, Client c);

    public String getPacketName() {
        return packetName;
    }
}