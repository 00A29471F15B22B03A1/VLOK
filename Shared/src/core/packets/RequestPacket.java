package core.packets;

public class RequestPacket extends Packet {

    public enum Type {
        FILE_STRUCTURE,
        FILE_DOWNLOAD
    }

    public Type type;
    public String argument;

}
