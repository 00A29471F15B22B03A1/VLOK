package core;

import core.database.UserDatabase;

import java.util.Random;

public class KeyGen {

    public static void main(String[] args) {
        String username = "";
        String key = getNewKey();
        String hash = Utils.hash("");
        UserDatabase.addUser(username, key, hash, PermissionLevels.ADMIN);
        System.out.println("New user: " + username);
        System.out.println(key);
        System.out.println(hash);
        System.exit(0);
    }

    private static String getNewKey() {
        StringBuilder result = new StringBuilder();

        result.append(randomInt());
        result.append(randomChar());
        for (int i = 0; i < 4; i++)
            result.append(randomInt());
        result.append(randomChar());
        for (int i = 0; i < 4; i++)
            result.append(randomInt());
        result.append(randomChar());
        result.append(randomInt());
        result.append(randomChar());
        result.append(randomInt());
        result.append(randomChar());

        return result.toString();
    }

    private static int randomInt() {
        return new Random().nextInt(10);
    }

    private static char randomChar() {
        return (char) (new Random().nextInt(26) + 'A');
    }

}
