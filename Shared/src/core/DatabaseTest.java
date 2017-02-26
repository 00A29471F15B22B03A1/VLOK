package core;

import core.database.FileDatabase;

public class DatabaseTest {

    public static void main(String[] args) {
        FileDatabase fileDatabase = new FileDatabase("files.sqlite");
        fileDatabase.execute("delete from files where 1=1");
    }

}
