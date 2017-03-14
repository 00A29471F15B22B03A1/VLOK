package core.filesystem;

/**
 * Class that represents a file in memory
 */
public class StoredFile {

    private String name;
    private String path;
    private String description;

    private StoredFolder parentDir;

    public StoredFile(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public StoredFolder getParentDir() {
        return parentDir;
    }

    public void setParentDir(StoredFolder parentDir) {
        this.parentDir = parentDir;
    }

    @Override
    public String toString() {
        return name;
    }

}
