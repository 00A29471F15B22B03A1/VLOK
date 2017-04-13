package core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class UserManager {

    private static List<ConnectedClient> clients = new ArrayList<>();

    public static String newUser(String loginKey, int connectionID) {
        String sessionKey = newSessionKey();
        ConnectedClient client = new ConnectedClient(sessionKey, loginKey, connectionID);
        clients.add(client);

        return sessionKey;
    }

    public static boolean checkSessionKey(int connectionID, String clientSessionKey) {
        String serverSessionKey = getSessionKey(connectionID);
        return serverSessionKey != null && serverSessionKey.equals(clientSessionKey);
    }

    private static String getSessionKey(int connectionID) {
        for (ConnectedClient client : clients)
            if (client.connectionID == connectionID)
                return client.sessionKey;
        return null;
    }

    public static void removeUser(int connectionID) {
        clients.removeIf(client -> client.connectionID == connectionID);
    }

    private static String newSessionKey() {
        return UUID.randomUUID().toString();
    }

    public static String getLoginKey(String sessionKey) {
        for (ConnectedClient client : clients)
            if (client.sessionKey.equals(sessionKey))
                return client.key;
        return null;
    }

}
