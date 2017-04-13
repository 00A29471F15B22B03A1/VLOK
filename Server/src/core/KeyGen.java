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
        String result = "";

        result += randomInt();
        result += randomChar();
        for (int i = 0; i < 4; i++)
            result += randomInt();
        result += randomChar();
        for (int i = 0; i < 4; i++)
            result += randomInt();
        result += randomChar();
        result += randomInt();
        result += randomChar();
        result += randomInt();
        result += randomChar();

        return result;
    }

    private static int randomInt() {
        return new Random().nextInt(10);
    }

    private static char randomChar() {
        return (char) (new Random().nextInt(26) + 'A');
    }

}
