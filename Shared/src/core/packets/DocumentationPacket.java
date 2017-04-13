package core.packets;

public class DocumentationPacket extends Packet {

    public String name;
    public String text;

    public DocumentationPacket() {
    }

    public DocumentationPacket(String name, String text) {
        this.name = name;
        this.text = text;
    }
}
