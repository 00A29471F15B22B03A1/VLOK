package core.packets;

public class RequestPacket extends Packet {

    public String sessionKey;

    public enum Type {
        FILE_STRUCTURE,
        FILE_STRUCTURE_ALL,
        FILE_STRUCTURE_PENDING,
        FILE_DOWNLOAD,
        FILE_UNPEND
    }

    public Type type;
    public String argument;

}
