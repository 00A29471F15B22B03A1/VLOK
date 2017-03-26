package core.ui.mainwindow;

import core.FileInfo;
import core.PermissionLevels;
import core.VLOKManager;
import core.localization.Localization;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;

public class FolderInfoPanel extends JPanel {

    private final JLabel folderPath;
    private String selectedPath;

    public FolderInfoPanel(FileTreePanel fileTreePanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(200, 100));

        setBorder(new TitledBorder(new LineBorder(Color.black), Localization.getString("ui.folder_properties")));

        folderPath = new JLabel();
        add(folderPath);

        add(Box.createVerticalStrut(10));

        JButton uploadButton = new JButton(Localization.getString("ui.upload"));
        add(uploadButton);

        fileTreePanel.addFileClickListener(selectedFile -> {
            if (!selectedFile.isFolder) {
                selectedPath = "";
                return;
            }

            selectedPath = selectedFile.path.replaceFirst("//", "");
            folderPath.setText(selectedFile.path);
        });

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                FileInfo fileInfo = new FileInfo(selectedFile.getName(), selectedPath.replaceFirst("/", ""), "", PermissionLevels.PEASAN);

                VLOKManager.sendFile(selectedFile, fileInfo);
            }
        });


    }

}
