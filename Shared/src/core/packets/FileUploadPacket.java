package core.packets;

public class FileUploadPacket {

    public static final int MAX_PACKET_SIZE = 50000;

    public String name;
    public String path;
    public byte[] data;

}