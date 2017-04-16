package core.packets;


public class ChatLoginPacket extends Packet {

    public String username;

    public ChatLoginPacket() {
    }

    public ChatLoginPacket(String username) {
        this.username = username;
    }

}

