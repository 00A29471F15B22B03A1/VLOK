package core.ui.mainwindow;

import core.logging.Logger;

import javax.swing.*;
import java.awt.*;


public class SidePanel extends JPanel {

    public SidePanel(FileInfoPanel fileInfoPanel, FolderInfoPanel folderInfoPanel, FileTreePanel fileTreePanel) {
        setMinimumSize(new Dimension(250, 100));
        setPreferredSize(new Dimension(250, 100));
        setLayout(new CardLayout());

        add(fileInfoPanel, FileInfoPanel.class.getSimpleName());

        add(folderInfoPanel, FolderInfoPanel.class.getSimpleName());

        final CardLayout cardLayout = (CardLayout) getLayout();
        final SidePanel sidePanel = this;

        fileTreePanel.addFileClickListener(selectedFile -> {
            if (selectedFile.isFolder)
                cardLayout.show(sidePanel, FolderInfoPanel.class.getSimpleName());
            else
                cardLayout.show(sidePanel, FileInfoPanel.class.getSimpleName());
        });

        Logger.info("Created SidePanel");
    }
}
