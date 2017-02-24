package core.filesystem;

public class FileStorage {

    private StoredFolder rootFolder;

    public FileStorage() {
        rootFolder = new StoredFolder("root");

        //addFile("test/test2/pindakaas/");
        //addFile("twat/twinkies.txt");

        //rootFolder.getSubfolder("test").addFile(new StoredFile("hehe.txt"));
        //rootFolder.getSubfolder("test").getSubfolder("test2").addFile(new StoredFile("hehe2.png"));

    }

    public void print() {
        rootFolder.print(0);
    }

    //TODO: fix file and folder difference
    public void addFile(String path) {
        String[] splitPath = path.split("/");
        String fileName = splitPath[splitPath.length - 1];

        int count = splitPath.length - 1;

        if (path.endsWith("/"))
            count++;

        StoredFolder currentFolder = rootFolder;

        for (int i = 0; i < count; i++) {

            String f = splitPath[i];

            StoredFolder subfolder = currentFolder.getSubfolder(f);

            if (subfolder == null) {
                subfolder = new StoredFolder(f);
                currentFolder.addFile(subfolder);
                currentFolder = subfolder;

            } else {
                currentFolder = subfolder;
            }

        }

        if (!path.endsWith("/"))
            currentFolder.addFile(new StoredFile(fileName));
    }
}
