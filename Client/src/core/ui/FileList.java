package core.ui;

import core.database.Database;
import core.filesystem.FileStorage;
import core.filesystem.StoredFile;
import core.filesystem.StoredFolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class FileList extends JPanel {

    private JList<StoredFile> list;
    private DefaultListModel<StoredFile> listModel;

    private StoredFolder currentDir;

    public FileList(StoredFolder rootFolder) {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();

        addFiles(listModel, rootFolder);

        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<StoredFile> list = (JList<StoredFile>) evt.getSource();

                if (evt.getClickCount() == 2) {
                    StoredFile selectedFile = list.getSelectedValue();

                    if (selectedFile instanceof StoredFolder) {
                        addFiles(listModel, (StoredFolder) selectedFile);
                    }

                }
            }
        });

        JButton backButton = new JButton("Back");
        Action backAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                StoredFolder parentDir = currentDir.getParentDir();
                if (parentDir == null)
                    return;
                addFiles(listModel, parentDir);
            }
        };
        backButton.setAction(backAction);
        add(BorderLayout.NORTH, backButton);


        JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(list);
        add(BorderLayout.CENTER, scrollpane);
    }

    private void addFiles(DefaultListModel<StoredFile> listModel, StoredFolder folder) {
        currentDir = folder;

        listModel.clear();

        for (StoredFile file : folder.getContainingFiles())
            listModel.addElement(file);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FileList");
        frame.setForeground(Color.black);
        frame.setBackground(Color.lightGray);
        Container cp = frame.getContentPane();

        FileStorage fileStorage = new FileStorage();

        Database db = new Database("test.sqlite");

        Map<String, String> files = db.getFiles();

        for (Map.Entry<String, String> entry : files.entrySet())
            fileStorage.addFile(entry.getValue(), entry.getKey());

        cp.add(new FileList(fileStorage.rootFolder));

        frame.pack();
        frame.setSize(400, 500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
