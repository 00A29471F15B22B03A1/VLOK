package core.ui;

import core.FileUploader;
import core.NetworkClient;
import core.filesystem.FileStorage;
import core.filesystem.StoredFile;
import core.filesystem.StoredFolder;
import core.filesystem.SupportedFileTypes;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class FilePropertiesPanel extends JPanel {

    private JLabel nameLabel = new JLabel("TEST.txt");
    private JLabel pathLabel = new JLabel("AIDS/WEKE SJANKER/");

    private JButton uploadButton = new JButton("Upload...");

    private StoredFile currentFile;

    public FilePropertiesPanel(NetworkClient client, FileStorage fileStorage) {
        setPreferredSize(new Dimension(400, 500));

        GridLayout layout = new GridLayout(0, 2);

        setLayout(layout);

        add(new JLabel("name: "));

        add(nameLabel);
        add(new JLabel("path: "));
        add(pathLabel);

        uploadButton.addActionListener(e -> {
            File[] filesToUpload = getFiles();

            if (filesToUpload == null)
                return;

            FileUploader.sendFiles(currentFile.getPath(), filesToUpload, client);

            for (File f : filesToUpload) {
                fileStorage.addFile(currentFile.getPath(), f.getName());
                FileTree.updateTreeView();
                System.out.println("added " + f.getName());
            }
        });
    }

    public File[] getFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        String[] supportedFiles = new String[SupportedFileTypes.values().length];

        int i = 0;
        for (SupportedFileTypes file : SupportedFileTypes.values())
            supportedFiles[i++] = file.toString().toLowerCase();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Upload Files", supportedFiles);
        chooser.setFileFilter(filter);

        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFiles();
        return null;
    }

    public void setFile(StoredFile file) {
        currentFile = file;

        nameLabel.setText(file.getName());
        pathLabel.setText(file.getPath());

        remove(uploadButton);

        if (file instanceof StoredFolder) {
            add(uploadButton);
        }
    }

}
