package core.packets;

public class RequestPacket {

    public String sessionKey;

    public enum Type {
        FILE_STRUCTURE,
        FILE_DOWNLOAD
    }

    public Type type;
    public String argument;

}
