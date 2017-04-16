package core;

import core.database.FileDatabase;
import core.logging.Console;

public class FileManager {

    private static FileStructure fileStructure;

    static {
        FileManager.fileStructure = FileDatabase.getAllFiles();
    }

    public static void addFile(FileInfo fileInfo) {
        fileStructure.addFile(fileInfo);
        FileDatabase.addFile(fileInfo.name, fileInfo.description);
    }

    public static FileStructure getPendingFiles() {
        return fileStructure.getPending();
    }

    public static FileStructure getNonPendingFiles() {
        return fileStructure.getNonPending();
    }

    public static FileStructure getAllFiles() {
        return fileStructure;
    }

    public static FileInfo getFile(int id) {
        return fileStructure.getFile(id);
    }

    public static FileInfo getFile(String name) {
        for (FileInfo info : fileStructure.getFiles())
            if (info.name.equals(name))
                return info;
        Console.err("Could not file file with name " + name);
        return null;
    }

}
