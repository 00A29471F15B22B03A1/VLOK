package core.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public final FileTreePanel fileTreePanel;

    private final JPanel sidePanel;

    public MainWindow() {
        setSize(700, 400);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        fileTreePanel = new FileTreePanel();
        add(fileTreePanel, BorderLayout.CENTER);

        sidePanel = new SidePanel(new FileInfoPanel(fileTreePanel), new FolderInfoPanel(fileTreePanel), fileTreePanel);
        add(sidePanel, BorderLayout.EAST);

        setVisible(true);
    }
}
