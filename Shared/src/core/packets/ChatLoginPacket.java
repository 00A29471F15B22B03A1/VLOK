package core.packets;


public class ChatLoginPacket extends Packet {

    public String username;
    public boolean onlineStatus;
    public String sessionKey;


    public ChatLoginPacket() {
    }

    public ChatLoginPacket(String username, boolean onlineStatus, String sessionKey) {

        this.username = username;
        this.onlineStatus = onlineStatus;
        this.sessionKey = sessionKey;
    }

}

