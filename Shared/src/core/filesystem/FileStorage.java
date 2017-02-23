package core.filesystem;

public class FileStorage {

    private StoredFolder rootFolder;

    public FileStorage() {
        rootFolder = new StoredFolder("root");

        addFolder("test/test2/pindakaas");
        addFolder("test/test2/pindakaas");

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

    public void addFolder(String path) {
        String[] splitPath = path.split("/");
        StoredFolder currentFolder = rootFolder;

        for (String f : splitPath) {

            StoredFolder subfolder = currentFolder.getSubfolder(f);

            if (subfolder == null) {
                subfolder = new StoredFolder(f);
                currentFolder.addFile(subfolder);
                currentFolder = subfolder;

            } else {
                currentFolder = subfolder;
            }

        }
    }
}
