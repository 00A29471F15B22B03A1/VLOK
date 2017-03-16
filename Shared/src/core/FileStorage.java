package core;

import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    private List<FileInfo> files;

    public FileStorage() {
        files = new ArrayList<>();
    }

    public void addFile(FileInfo file) {
        files.add(file);
    }

    public void removeFile(String name) {
        files.removeIf(file -> file.getName().equals(name));
    }

    public int getFileAmount() {
        return files.size();
    }

    public FileInfo getFile(String name) {
        for (FileInfo fileInfo : files)
            if (fileInfo.getName().equals(name))
                return fileInfo;
        return null;
    }

    public List<FileInfo> getFiles() {
        return files;
    }
}
