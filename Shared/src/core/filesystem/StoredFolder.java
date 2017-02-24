package core.filesystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a folder in memory
 */
public class StoredFolder extends StoredFile {

    private List<StoredFile> containingFiles;

    public StoredFolder(String name, String path) {
        super(name, path);

        containingFiles = new ArrayList<>();
    }

    /**
     * Gets a subfolder
     *
     * @param name of the folder
     * @return the subfolder
     */
    public StoredFolder getSubfolder(String name) {
        for (StoredFile f : containingFiles) {

            if (f instanceof StoredFolder) {

                if (f.getName().equals(name))
                    return (StoredFolder) f;
            }
        }

        return null;
    }

    /**
     * Gets a subfile
     *
     * @param name of the file
     * @return the file
     */
    public StoredFile getFile(String name) {
        for (StoredFile f : containingFiles) {
            if (f.getName().equals(name))
                return f;

        }

        return null;
    }

    /**
     * Adds a file
     *
     * @param file to add
     */
    public void addFile(StoredFile file) {
        if (getFile(file.getName()) != null) {
            System.err.println("Folder " + getName() + " already has a file with name " + file.getName());
            return;
        }

        containingFiles.add(file);

        file.setParentDir(this);
    }

    /**
     * Returns a list of contained files
     *
     * @return list of files
     */
    public List<StoredFile> getContainingFiles() {
        return containingFiles;
    }
}
