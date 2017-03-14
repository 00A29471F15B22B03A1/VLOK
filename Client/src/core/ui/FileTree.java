package core.ui;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.NetworkClient;
import core.filesystem.FileStorage;
import core.filesystem.StoredFile;
import core.filesystem.StoredFolder;
import core.packets.FileStructurePacket;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Class for showing a filesystem in a tree view
 */
public class FileTree extends JPanel {

    private FilePropertiesPanel propertiesPanel;

    private static NetworkClient client;

    private static FileStorage fileStorage;

    private static JTree tree;
    private static DefaultMutableTreeNode top;

    public FileTree(StoredFolder rootFolder) {
        propertiesPanel = new FilePropertiesPanel(client, fileStorage);

        setLayout(new BorderLayout());

        top = new DefaultMutableTreeNode(rootFolder);
        tree = new JTree(top);

        updateTreeView();

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            Object file = selectedNode.getUserObject();
            if (file != null)
                propertiesPanel.setFile((StoredFile) file);
        });

        JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);
        add(BorderLayout.CENTER, scrollpane);

        add(BorderLayout.EAST, propertiesPanel);
    }

    /**
     * Adds all the files to the treeview
     *
     * @param treeNode node to add the files to
     * @param folder   to get the files from
     */
    private static void addFiles(DefaultMutableTreeNode treeNode, StoredFolder folder) {
        for (StoredFile file : folder.getContainingFiles()) {

            if (file instanceof StoredFolder) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);
                addFiles(newNode, (StoredFolder) file);
                treeNode.add(newNode);
            } else {
                treeNode.add(new DefaultMutableTreeNode(file));
            }
        }

    }

    public static void updateTreeView() {
        top.removeAllChildren();

        addFiles(top, fileStorage.rootFolder);

        tree.expandRow(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        client = new NetworkClient();
        client.connect("192.168.1.18");

        JFrame frame = new JFrame("FileTree");
        Container cp = frame.getContentPane();

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                System.out.println(o);
                if (o instanceof FileStructurePacket) {
                    fileStorage = new FileStorage();
                    cp.add(new FileTree(fileStorage.rootFolder));
                }
            }
        });


        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() / 2), (int) (screenSize.getHeight() / 2));
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
