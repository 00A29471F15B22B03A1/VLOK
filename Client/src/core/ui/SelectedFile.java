package core.ui;

import core.FileInfo;

public class SelectedFile {

    public FileInfo fileInfo;

    public final boolean isFolder;

    public String path;

    public SelectedFile(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        this.isFolder = false;
    }

    public SelectedFile(String path) {
        this.path = path;
        this.isFolder = true;
    }
}
