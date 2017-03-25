package core.ui;

import core.FileClickListener;
import core.FileInfo;
import core.VLOKManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileInfoPanel extends JPanel {

    private final JLabel nameLabel;
    private final JLabel pathLabel;
    private final JButton downloadButton;

    private FileInfo currentFile;

    public FileInfoPanel(FileTreePanel fileTreePanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBorder(new TitledBorder(new LineBorder(Color.black), "File Properties"));

        nameLabel = new JLabel();
        add(nameLabel);

        add(Box.createVerticalStrut(10));

        pathLabel = new JLabel();
        add(pathLabel);

        add(Box.createVerticalStrut(10));

        downloadButton = new JButton("Download");
        add(downloadButton);

        fileTreePanel.addFileClickListener(new FileClickListener() {
            @Override
            public void onFileClick(SelectedFile selectedFile) {
                if (selectedFile.isFolder) {
                    currentFile = null;
                    return;
                }

                nameLabel.setText(selectedFile.fileInfo.getName());
                pathLabel.setText(selectedFile.fileInfo.getPath());
                downloadButton.setEnabled(true);
                currentFile = selectedFile.fileInfo;
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VLOKManager.sendDownloadRequest(currentFile);
            }
        });
    }
}
