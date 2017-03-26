package core.ui.mainwindow;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.VLOKManager;
import core.logging.Logger;
import core.packets.FileStructurePacket;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public FileTreePanel fileTreePanel;

    public MainWindow() {
        setSize(700, 400);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        fileTreePanel = new FileTreePanel();
        add(fileTreePanel, BorderLayout.CENTER);

        JPanel sidePanel = new SidePanel(new FileInfoPanel(fileTreePanel), new FolderInfoPanel(fileTreePanel), fileTreePanel);
        add(sidePanel, BorderLayout.EAST);

        setVisible(true);

        Logger.info("Created MainWindow");

        VLOKManager.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileStructurePacket) {
                    fileTreePanel.updateTree(((FileStructurePacket) o).fileStructure);
                    Logger.info("Received FileStructure");
                }
            }
        });

        VLOKManager.sendFileStructureRequest();
    }
}
