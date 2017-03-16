package core.packets;

import core.FileInfo;

public class FileUploadPacket {

    //TODO: move to better location
    public static final int MAX_PACKET_SIZE = 50000;

    public boolean finished = false;

    public FileInfo fileInfo;
    public byte[] data;

}
