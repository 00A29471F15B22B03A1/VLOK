package core.packets;

public class LoginPacket extends Packet {

    public String fullKey;
    public float version;

    public String sessionKey;

    public LoginPacket() {
    }

    public LoginPacket(String fullKey, float version, String sessionKey) {
        this.fullKey = fullKey;
        this.version = version;
        this.sessionKey = sessionKey;
    }
}
