package core.packets;

public class UpdatePacket extends Packet {

    public String downloadURL;

    public UpdatePacket() {
    }

    public UpdatePacket(String downloadURL) {
        this.downloadURL = downloadURL;
    }
}
