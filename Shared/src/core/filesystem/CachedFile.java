package core.filesystem;

/**
 * Class to store a single cached file
 */
public class CachedFile {

    private String path;
    private byte[] data;

    public CachedFile(String path, byte[] data) {
        this.path = path;
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public byte[] getData() {
        return data;
    }

    public long getByteSize() {
        return data.length;
    }
}
