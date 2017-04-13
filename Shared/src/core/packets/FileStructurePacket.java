package core.packets;

import core.FileStructure;

public class FileStructurePacket extends Packet {

    public FileStructure fileStructure;

    public FileStructurePacket() {
    }

    public FileStructurePacket(FileStructure fileStructure) {
        this.fileStructure = fileStructure;
    }
}
