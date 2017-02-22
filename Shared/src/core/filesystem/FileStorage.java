package core.filesystem;

public class FileStorage {

    private StoredFolder rootFolder;

    public FileStorage() {
        rootFolder = new StoredFolder("root");
        rootFolder.getContainingFiles().add(new StoredFile("Test.txt"));
        StoredFolder f = new StoredFolder("test");
        f.getContainingFiles().add(new StoredFile("test2.png"));
        rootFolder.getContainingFiles().add(f);

        for (int i = 0; i < rootFolder.getContainingFiles().size(); i++) {

            StoredFile file = rootFolder.getContainingFiles().get(i);
            System.out.println(file.getName());

            if (file instanceof StoredFolder) {
                StoredFolder folder = (StoredFolder) file;

                for (int j = 0; j < folder.getContainingFiles().size(); j++) {
                    System.out.println("   -" + folder.getContainingFiles().get(j).getName());
                }
            }
        }
    }
}
