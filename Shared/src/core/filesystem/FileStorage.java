package core.filesystem;

public class FileStorage {

    public StoredFolder rootFolder;

    public FileStorage() {
        rootFolder = new StoredFolder("root", "");

        //addFile("test/test2/pindakaas/");
        //addFile("twat/twinkies.txt");

        //rootFolder.getSubfolder("test").addFile(new StoredFile("hehe.txt"));
        //rootFolder.getSubfolder("test").getSubfolder("test2").addFile(new StoredFile("hehe2.png"));

    }

    public StoredFolder addFolder(String path) {
        String[] splitPath = path.split("/");

        StoredFolder currentDir = rootFolder;

        for (String f : splitPath) {
            StoredFolder subFolder = currentDir.getSubfolder(f);

            if (subFolder == null) {
                StoredFolder newFolder = new StoredFolder(f, currentDir.getPath());
                currentDir.addFile(newFolder);
                currentDir = newFolder;
            } else {
                currentDir = subFolder;
            }
        }

        return currentDir;
    }

    public void addFile(String folder, String file) {
        StoredFolder f = addFolder(folder);
        f.addFile(new StoredFile(file, folder));
    }
}
