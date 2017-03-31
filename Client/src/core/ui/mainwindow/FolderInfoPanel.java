package core.ui.mainwindow;

import core.localization.Localization;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FolderInfoPanel extends JPanel {

    private final JLabel folderPath;

    public FolderInfoPanel(FileTreePanel fileTreePanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(200, 100));

        TitledBorder titledBorder = new TitledBorder(new LineBorder(Color.black), Localization.getString("ui.folder_properties"));

        setBorder(titledBorder);

        folderPath = new JLabel();
        add(folderPath);

        add(Box.createVerticalStrut(10));

        fileTreePanel.addFileClickListener(selectedFile -> {
            if (!selectedFile.isFolder)
                return;
            folderPath.setText(selectedFile.path);
        });

        Localization.addLanguageListener(newLanguage -> {
            titledBorder.setTitle(Localization.getString("ui.folder_properties"));
            repaint();
        });
    }

}
