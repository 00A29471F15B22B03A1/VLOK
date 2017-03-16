package core.packets;

public class RequestPacket {

    public enum Type {
        FILE_STRUCTURE,
        FILE_INFO,
        FILE_DOWNLOAD
    }

    public Type type;
    public String argument;

}
