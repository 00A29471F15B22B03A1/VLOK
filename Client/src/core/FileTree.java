package core;

import core.database.Database;
import core.filesystem.FileStorage;
import core.filesystem.StoredFile;
import core.filesystem.StoredFolder;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.util.Map;

public class FileTree extends JPanel {

    private JTree tree;
    private DefaultTreeModel treeModel;

    public FileTree(StoredFolder rootFolder) {
        setLayout(new BorderLayout());

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);

        for (StoredFile file : rootFolder.getContainingFiles()) {
            MutableTreeNode node = new DefaultMutableTreeNode(file);
            root.add(node);
        }

        JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);
        add(BorderLayout.CENTER, scrollpane);
    }

    public static void main(String[] args) {
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
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
