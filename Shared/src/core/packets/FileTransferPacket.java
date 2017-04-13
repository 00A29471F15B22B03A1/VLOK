package core.packets;

import core.FileInfo;

public class FileTransferPacket extends Packet {

    //TODO: move to better location
    public static final int MAX_PACKET_SIZE = 3000;

    public FileTransferPacket() {
    }

    public FileTransferPacket(FileInfo fileInfo, byte[] data, boolean finished, String sessionKey) {
        this.fileInfo = fileInfo;
        this.data = data;
        this.finished = finished;
        this.sessionKey = sessionKey;
    }

    public FileInfo fileInfo;
    public byte[] data;
    public boolean finished = false;

    public String sessionKey;
}
