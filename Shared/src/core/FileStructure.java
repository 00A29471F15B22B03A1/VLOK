package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileStructure {

    private List<FileInfo> files;

    public FileStructure() {
        files = new ArrayList<>();
    }

    public void addFile(FileInfo file) {
        files.add(file);
    }

    public void removeFile(String name) {
        Iterator<FileInfo> iterator = files.iterator();
        while (iterator.hasNext())
            if (iterator.next().getName().equals(name))
                iterator.remove();
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
