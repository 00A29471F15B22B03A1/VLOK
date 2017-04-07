package core.packets;

public class ErrorPacket extends Packet {

    public String error;

    public ErrorPacket() {
    }

    public ErrorPacket(String error) {
        this.error = error;
    }
}
