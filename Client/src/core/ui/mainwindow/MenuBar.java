package core.ui.mainwindow;

import core.FileInfo;
import core.PermissionLevels;
import core.VLOKManager;
import core.localization.Localization;
import core.ui.SettingsWindow;

import javax.swing.*;
import java.io.File;

public class MenuBar extends JMenuBar {

    public MenuBar() {
        JMenu fileMenu = new JMenu(Localization.getString("ui.file"));
        add(fileMenu);

        JMenuItem uploadItem = new JMenuItem(Localization.getString("ui.upload"));

        uploadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                FileInfo fileInfo = new FileInfo(selectedFile.getName(), "", "", PermissionLevels.PEASAN, true);

                VLOKManager.sendFile(selectedFile, fileInfo);
            }
        });

        fileMenu.add(uploadItem);

        final SettingsWindow settingsWindow = new SettingsWindow();

        JMenuItem settingsItem = new JMenuItem(Localization.getString("ui.settings"));
        settingsItem.addActionListener(e -> settingsWindow.show());
        fileMenu.add(settingsItem);

        Localization.addLanguageListener(newLanguage -> {
            fileMenu.setText(Localization.getString("ui.file"));
            uploadItem.setText(Localization.getString("ui.upload"));
            settingsItem.setText(Localization.getString("ui.settings"));
        });
    }
}
