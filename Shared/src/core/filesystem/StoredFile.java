package core.filesystem;

public class StoredFile {

    private String name;
    private String path;

    public StoredFile(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
