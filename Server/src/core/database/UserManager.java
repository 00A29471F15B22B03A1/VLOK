package core.database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private static Map<Integer, String> users = new HashMap<>();

    public static void newUser(int id, String key) {
        users.put(id, key);
    }

    public static boolean checkKey(int id, String key) {
        return users.get(id).equals(key);
    }

    public static void removeUser(int id) {
        users.remove(id);
    }

    public static String newKey() {
        return UUID.randomUUID().toString();
    }
}
