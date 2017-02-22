package core.filesystem;

import java.util.ArrayList;
import java.util.List;

public class StoredFolder extends StoredFile {

    private List<StoredFile> containingFiles;

    public StoredFolder(String name) {
        super(name);

        containingFiles = new ArrayList<>();
    }

    public List<StoredFile> getContainingFiles() {
        return containingFiles;
    }
}
