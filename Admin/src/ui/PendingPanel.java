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

public class PendingPanel extends JPanel {

    private FileTreePanel fileTreePanel;

    public PendingPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        fileTreePanel = new FileTreePanel();

        splitPane.add(fileTreePanel);
        splitPane.add(new PendingInfoPanel(fileTreePanel));

        setLayout(new BorderLayout());
        add(splitPane);

        VLOKManager.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileStructurePacket) {
                    fileTreePanel.updateTree(((FileStructurePacket) o).fileStructure);
                    Logger.info("Received FileStructure");
                }
            }
        });
        VLOKManager.sendRequest(RequestPacket.Type.FILE_STRUCTURE_PENDING, "");

        Logger.info("Created pending panel");
    }
}
