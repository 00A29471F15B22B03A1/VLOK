package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.packets.FileStructurePacket;
import core.ui.MainWindow;

import javax.swing.*;

public class ClientMain {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        final MainWindow mainWindow = new MainWindow();

        VLOKManager.init();

        VLOKManager.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileStructurePacket) {
                    mainWindow.fileTreePanel.updateTree(((FileStructurePacket) o).fileStructure);
                }
            }
        });
    }

}