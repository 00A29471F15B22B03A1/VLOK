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
            if (iterator.next().name.equals(name))
                iterator.remove();
    }

    public int getFileAmount() {
        return files.size();
    }

    public FileInfo getFile(String name) {
        for (FileInfo fileInfo : files)
            if (fileInfo.name.equals(name))
                return fileInfo;
        return null;
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public FileStructure getNonPending() {
        FileStructure fileStructure = new FileStructure();

        for (FileInfo fi : files)
            if (!fi.pending)
                fileStructure.addFile(fi);

        return fileStructure;
    }

    public FileStructure getPending() {
        FileStructure fileStructure = new FileStructure();

        for (FileInfo fi : files)
            if (fi.pending)
                fileStructure.addFile(fi);

        return fileStructure;
    }
}
