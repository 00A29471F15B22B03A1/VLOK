package core.packets;

import core.FileInfo;

public class FileTransferPacket extends Packet {

    //TODO: move to better location
    public static final int MAX_PACKET_SIZE = 3000;

    public String sessionKey;

    public boolean finished = false;

    public FileInfo fileInfo;
    public byte[] data;

}
