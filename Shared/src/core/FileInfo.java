package core;

public class FileInfo {

    public int id;
    public String name;
    public String path;
    public String description;
    public int minPermissionLevel = PermissionLevels.PEASAN;
    public boolean pending;
    public String uploadDate;

    //TODO: add these properties
    private int size;
    private String uploader;

    /**
     * Empty constructor for serialization
     */
    public FileInfo() {
    }

    public FileInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public FileInfo(String name, String path, String description, int minPermissionLevel, boolean pending) {
        this.name = name;
        this.path = path;
        this.description = description;
        this.minPermissionLevel = minPermissionLevel;
        this.pending = pending;
    }

    public FileInfo(int id, String name, String path, String description, int minPermissionLevel, boolean pending) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.description = description;
        this.minPermissionLevel = minPermissionLevel;
        this.pending = pending;
    }

    @Override
    public String toString() {
        return name;
    }
}
