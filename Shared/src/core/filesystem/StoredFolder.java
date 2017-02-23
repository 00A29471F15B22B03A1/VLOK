package core.filesystem;

import java.util.ArrayList;
import java.util.List;

public class StoredFolder extends StoredFile {

    private List<StoredFile> containingFiles;

    public StoredFolder(String name) {
        super(name);

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

    public StoredFile hasFile(String name) {
        for (StoredFile f : containingFiles) {
            if (f.getName().equals(name))
                return f;
        }

        return null;
    }

    public void addFile(StoredFile file) {
        if (hasFile(file.getName()) != null) {
            System.err.println("Folder " + getName() + " already has5= a file with name " + file.getName());
            return;
        }

        containingFiles.add(file);
    }

    public List<StoredFile> getContainingFiles() {
        return containingFiles;
    }
}
