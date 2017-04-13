package core;

import core.database.FileDatabase;

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

}
