package core;

import core.database.UserDatabase;

public class KeyGen {

    public static void main(String[] args) {
        UserDatabase.addUser("Hector Peeters", "3K5865Y7431S4K9R", Utils.hash("wachtwoord"));
    }

}
