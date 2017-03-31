package ui;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.VLOKManager;
import core.logging.Logger;
import core.packets.FileStructurePacket;
import core.packets.RequestPacket;
import core.ui.mainwindow.FileTreePanel;

import javax.swing.*;
import java.awt.*;

public class FilePanel extends JPanel {

    public FileTreePanel fileTreePanel;

    public FilePanel() {
        setLayout(new BorderLayout());
        fileTreePanel = new FileTreePanel();

        add(fileTreePanel, BorderLayout.CENTER);

        VLOKManager.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileStructurePacket) {
                    fileTreePanel.updateTree(((FileStructurePacket) o).fileStructure);
                    Logger.info("Received FileStructure");
                }
            }
        });
        VLOKManager.sendRequest(RequestPacket.Type.FILE_STRUCTURE_ALL, "");

        Logger.info("Created file panel");
    }
}
