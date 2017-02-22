package core.filesystem;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class to store all cached files
 */
public class FileCache {

    private static final long MAX_CACHE_SIZE = 1000000000L;

    private long currentCacheSize = 0;

    private Queue<CachedFile> cachedFiles;

    public FileCache() {
        this.cachedFiles = new LinkedList<>();
    }

    /**
     * Add a file to the cache and removes files if cache size is exceeded
     *
     * @param file to add to cache
     */
    public void addFile(CachedFile file) {
        if (file.getByteSize() >= MAX_CACHE_SIZE) {
            System.err.println("File " + file.getPath() + " is bigger than cache size");
            return;
        }

        cachedFiles.add(file);

        currentCacheSize = getSize();

        while (currentCacheSize >= MAX_CACHE_SIZE) {
            CachedFile removedFile = cachedFiles.remove();

            currentCacheSize -= removedFile.getByteSize();
        }
    }

    /**
     * Gets the size in bytes of all files in cache
     *
     * @return size files in bytes
     */
    private long getSize() {
        long totalSize = 0;

        for (CachedFile file : cachedFiles)
            totalSize += file.getByteSize();

        return totalSize;
    }

    /**
     * Clears all current files in the cache
     */
    public void clearCache() {
        cachedFiles.clear();
    }

    public long getCurrentCacheSize() {
        return currentCacheSize;
    }

    public int getFileAmount() {
        return cachedFiles.size();
    }
}
