package core.ui.mainwindow;

import java.util.EventListener;

public interface FileClickListener extends EventListener {

    void onFileClick(SelectedFile selectedFile);

}
