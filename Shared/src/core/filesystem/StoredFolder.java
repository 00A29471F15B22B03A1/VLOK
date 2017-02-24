package core.filesystem;

import java.util.ArrayList;
import java.util.List;

public class StoredFolder extends StoredFile {

    private List<StoredFile> containingFiles;

    public StoredFolder(String name, String path) {
        super(name, path);

        containingFiles = new ArrayList<>();
    }

    public StoredFolder getSubfolder(String name) {
        for (StoredFile f : containingFiles) {

            if (f instanceof StoredFolder) {

                if (f.getName().equals(name))
                    return (StoredFolder) f;
            }
        }

        return null;
    }

    public StoredFile getFile(String name) {
        for (StoredFile f : containingFiles) {
            if (f.getName().equals(name))
                return f;

        }

        return null;
    }

    public void addFile(StoredFile file) {
        if (getFile(file.getName()) != null) {
            System.err.println("Folder " + getName() + " already has a file with name " + file.getName());
            return;
        }

        containingFiles.add(file);
    }

    public List<StoredFile> getContainingFiles() {
        return containingFiles;
    }
}
