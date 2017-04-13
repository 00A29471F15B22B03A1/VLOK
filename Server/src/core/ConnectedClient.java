package core;

public class ConnectedClient {

    public String sessionKey;
    public String key;
    public int connectionID;

    public ConnectedClient(String sessionKey, String key, int connectionID) {
        this.sessionKey = sessionKey;
        this.key = key;
        this.connectionID = connectionID;
    }
}
