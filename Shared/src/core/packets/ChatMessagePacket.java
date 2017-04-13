package core.packets;

//ayylemao

public class ChatMessagePacket extends Packet {

    public String message;
    public String sessionKey;
    public String username;

    public ChatMessagePacket() {
    }

    public ChatMessagePacket(String message, String sessionKey, String username) {
        this.message = message;
        this.sessionKey = sessionKey;
        this.username = username;
    }
}
