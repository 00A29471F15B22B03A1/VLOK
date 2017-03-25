package core;

import core.ui.SelectedFile;

import java.util.EventListener;

public interface FileClickListener extends EventListener {

    void onFileClick(SelectedFile selectedFile);

}