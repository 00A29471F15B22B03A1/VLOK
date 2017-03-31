package core.ui.mainwindow;

import core.FileInfo;
import core.VLOKManager;
import core.localization.Localization;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FileInfoPanel extends JPanel {

    private final JLabel nameLabel;
    private final JLabel pathLabel;
    private final JButton downloadButton;

    private FileInfo currentFile;

    public FileInfoPanel(FileTreePanel fileTreePanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        TitledBorder titledBorder = new TitledBorder(new LineBorder(Color.black), Localization.getString("ui.file_properties"));

        setBorder(titledBorder);

        nameLabel = new JLabel();
        add(nameLabel);

        add(Box.createVerticalStrut(10));

        pathLabel = new JLabel();
        add(pathLabel);

        add(Box.createVerticalStrut(10));

        downloadButton = new JButton(Localization.getString("ui.download"));
        add(downloadButton);

        fileTreePanel.addFileClickListener(selectedFile -> {
            if (selectedFile.isFolder) {
                currentFile = null;
                return;
            }

            nameLabel.setText(selectedFile.fileInfo.name);
            pathLabel.setText(selectedFile.fileInfo.path);
            downloadButton.setEnabled(true);
            currentFile = selectedFile.fileInfo;
        });

        downloadButton.addActionListener(e -> VLOKManager.sendDownloadRequest(currentFile));

        Localization.addLanguageListener(newLanguage -> {
            titledBorder.setTitle(Localization.getString("ui.file_properties"));
            downloadButton.setText(Localization.getString("ui.download"));
            repaint();
        });
    }
}
