package core.ui;

import core.database.Database;
import core.filesystem.FileStorage;
import core.filesystem.StoredFile;
import core.filesystem.StoredFolder;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Map;

/**
 * Class for showing a filesystem in a tree view
 */
public class FileTree extends JPanel {

    private JTree tree;

    public FileTree(StoredFolder rootFolder) {
        setLayout(new BorderLayout());

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootFolder);
        tree = new JTree(top);

        addFiles(top, rootFolder);

        tree.expandRow(0);

        JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);
        add(BorderLayout.CENTER, scrollpane);
    }

    /**
     * Adds a listener to the treeview
     *
     * @param listener to add
     */
    public void addActionListener(TreeSelectionListener listener) {
        tree.addTreeSelectionListener(listener);
    }

    /**
     * Adds all the files to the treeview
     *
     * @param treeNode node to add the files to
     * @param folder   to get the files from
     */
    private void addFiles(DefaultMutableTreeNode treeNode, StoredFolder folder) {
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("FileTree");
        frame.setForeground(Color.black);
        frame.setBackground(Color.lightGray);
        Container cp = frame.getContentPane();

        FileStorage fileStorage = new FileStorage();

        Database db = new Database("test.sqlite");

        Map<String, String> files = db.getFiles();

        for (Map.Entry<String, String> entry : files.entrySet())
            fileStorage.addFile(entry.getValue(), entry.getKey());

        cp.add(new FileTree(fileStorage.rootFolder));

        frame.pack();
        frame.setSize(400, 500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
