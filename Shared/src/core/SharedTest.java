package core;

import core.filesystem.CachedFile;
import core.filesystem.FileCache;
import core.filesystem.SupportedCacheFileTypes;

public class SharedTest {

    public static void main(String[] args) {
        cacheTest();
        supportedFileTest();
    }

    private static void supportedFileTest() {
        System.out.println("SupportedFile Test: " + SupportedCacheFileTypes.isValidExtension("png"));
        System.out.println("SupportedFile Test: " + !SupportedCacheFileTypes.isValidExtension("exe"));
    }

    private static void cacheTest() {
        byte[] testData = new byte[100];

        FileCache fileCache = new FileCache();

        for (int i = 0; i < 100; i++)
            fileCache.addFile(new CachedFile("file " + i, testData));

        System.out.println("FileSystem Test: " + (fileCache.getFileAmount() == 100));
        System.out.println("FileSystem Test: " + (fileCache.getCurrentCacheSize() == 100 * 100));

    }

}