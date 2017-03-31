package ui;

import core.VLOKManager;
import core.localization.Localization;
import core.packets.RequestPacket;
import core.ui.mainwindow.FileTreePanel;
import core.ui.mainwindow.SelectedFile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PendingInfoPanel extends JPanel {

    private FileTreePanel fileTreePanel;

    private SelectedFile selectedFile;

    public PendingInfoPanel(FileTreePanel fileTreePanel) {
        this.fileTreePanel = fileTreePanel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(200, 100));

        TitledBorder titledBorder = new TitledBorder(new LineBorder(Color.black), Localization.getString("ui.pending"));

        setBorder(titledBorder);

        JTextField pathField = new JTextField("");
        add(pathField);

        add(Box.createVerticalStrut(10));

        JButton acceptButton = new JButton(Localization.getString("ui.accept"));

        acceptButton.addActionListener(e -> {
            if (selectedFile == null)
                return;

            VLOKManager.sendRequest(RequestPacket.Type.FILE_UNPEND, selectedFile.fileInfo.id + "|" + pathField.getText());
        });

        fileTreePanel.addFileClickListener(selectedFile -> this.selectedFile = selectedFile);

        add(acceptButton);

        Localization.addLanguageListener(newLanguage -> {
            titledBorder.setTitle(Localization.getString("ui.pending"));
            repaint();
        });
    }
}
