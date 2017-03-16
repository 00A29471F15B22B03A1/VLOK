package core;

public class FileInfo {

    private String name;
    private String path;
    private String description = "EMPTY";
    private int minPermissionLevel = PermissionLevels.PEASAN;

    /**
     * Empty constructor for serialization
     */
    public FileInfo() {
    }

    public FileInfo(String name, String path, String description, int minPermissionLevel) {
        this.name = name;
        this.path = path;
        this.description = description;
        this.minPermissionLevel = minPermissionLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinPermissionLevel() {
        return minPermissionLevel;
    }

    public void setMinPermissionLevel(int minPermissionLevel) {
        this.minPermissionLevel = minPermissionLevel;
    }

    public boolean hasPermissions(int permissionLevel) {
        return permissionLevel >= minPermissionLevel;
    }

    @Override
    public String toString() {
        return "File: " + name;
    }
}
