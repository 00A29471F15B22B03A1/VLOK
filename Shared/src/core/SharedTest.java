package core;

import core.filesystem.SupportedFileTypes;
import core.filesystem.caching.FileCache;

public class SharedTest {

    public static void main(String[] args) {
        cacheTest();
        supportedFileTest();
    }

    private static void supportedFileTest() {
        System.out.println("SupportedFile Test: " + SupportedFileTypes.isValidExtension("png"));
        System.out.println("SupportedFile Test: " + !SupportedFileTypes.isValidExtension("exe"));
    }

    private static void cacheTest() {
        byte[] testData = new byte[100];

        FileCache fileCache = new FileCache();

        for (int i = 0; i < 100; i++)
            fileCache.addFile(new core.filesystem.caching.CachedFile("file " + i, testData));

        System.out.println("FileCache Test: " + (fileCache.getFileAmount() == 100));
        System.out.println("FileCache Test: " + (fileCache.getCurrentCacheSize() == 100 * testData.length));

    }

}
