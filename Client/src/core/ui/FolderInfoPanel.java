package core.ui;

import core.FileClickListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FolderInfoPanel extends JPanel {

    private final JLabel folderPath;

    public FolderInfoPanel(FileTreePanel fileTreePanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(200, 100));

        setBorder(new TitledBorder(new LineBorder(Color.black), "Folder Properties"));

        folderPath = new JLabel();
        add(folderPath);

        add(Box.createVerticalStrut(10));

        JButton uploadButton = new JButton("Upload");
        add(uploadButton);

        fileTreePanel.addFileClickListener(new FileClickListener() {
            @Override
            public void onFileClick(SelectedFile selectedFile) {
                if (!selectedFile.isFolder)
                    return;

                folderPath.setText(selectedFile.path);
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}
