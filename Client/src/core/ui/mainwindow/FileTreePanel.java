package core.ui.mainwindow;

import core.FileInfo;
import core.FileStructure;
import core.logging.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class FileTreePanel extends JPanel {

    private final JTree fileTree;
    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

    private final List<FileClickListener> fileClickListeners = new ArrayList<>();

    public FileTreePanel() {
        setLayout(new BorderLayout());
        fileTree = new JTree(root);
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();

            if (node == null)
                return;

            if (node.getUserObject() instanceof FileInfo)
                fireFileClickEvent(new SelectedFile((FileInfo) node.getUserObject()));
            else
                fireFileClickEvent(new SelectedFile(joinPath(node.getPath())));
        });

        JScrollPane scrollPane = new JScrollPane(fileTree);
        add(scrollPane, BorderLayout.CENTER);

        Logger.info("Created FileTreePanel");
    }

    public void updateTree(FileStructure fileStructure) {
        root.removeAllChildren();
        for (FileInfo fileInfo : fileStructure.getFiles())
            addFile(fileInfo);

        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();

        model.reload(root);
        model.nodeStructureChanged(root);
        fileTree.treeDidChange();

        Logger.info("Updated file tree view");
    }

    private void addFile(FileInfo fileInfo) {
        DefaultMutableTreeNode current = root;
        String fullPath = (fileInfo.getPath() + fileInfo.getName());
        String[] splitPath = fullPath.split("/");

        for (int i = 0; i < splitPath.length; i++) {
            String folder = splitPath[i];

            DefaultMutableTreeNode child = getChild(current, folder);

            if (child == null) {
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(folder);

                if (i == splitPath.length - 1) {
                    newChild = new DefaultMutableTreeNode(fileInfo);
                }

                current.add(newChild);
                current = newChild;
            } else
                current = child;
        }
    }

    private DefaultMutableTreeNode getChild(DefaultMutableTreeNode treeNode, String name) {
        Enumeration enumeration = treeNode.children();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            String fileInfo = "";

            if (node.getUserObject() instanceof String)
                fileInfo = (String) node.getUserObject();
            else if (node.getUserObject() instanceof FileInfo)
                fileInfo = ((FileInfo) node.getUserObject()).getName();

            if (fileInfo.equals(name))
                return node;
        }
        return null;
    }

    public void addFileClickListener(FileClickListener fileClickListener) {
        fileClickListeners.add(fileClickListener);
    }

    public void removeFileClickListener(FileClickListener fileClickListener) {
        fileClickListeners.remove(fileClickListener);
    }

    private void fireFileClickEvent(SelectedFile selectedFile) {
        for (FileClickListener fileClickListener : fileClickListeners)
            fileClickListener.onFileClick(selectedFile);
    }

    private String joinPath(TreeNode[] path) {
        String result = "";
        for (TreeNode n : path)
            result += n.toString() + "/";
        return result;
    }
}
