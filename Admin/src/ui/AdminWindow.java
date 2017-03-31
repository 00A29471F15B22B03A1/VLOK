package ui;

import core.User;
import core.Utils;
import core.VLOKManager;
import core.localization.Localization;
import core.logging.Logger;
import core.ui.login.LoginWindow;

import javax.swing.*;

public class AdminWindow extends JFrame {

    public FilePanel filePanel;
    public PendingPanel pendingPanel;

    public AdminWindow() {
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JTabbedPane tabbedPane = new JTabbedPane();
        filePanel = new FilePanel();
        tabbedPane.addTab(Localization.getString("ui.files"), filePanel);
        pendingPanel = new PendingPanel();
        tabbedPane.addTab(Localization.getString("ui.pending"), pendingPanel);

        add(tabbedPane);

        setVisible(true);

        Logger.info("Created main window");
    }

    public static void main(String[] args) {
        Utils.setNativeLookAndFeel();

        VLOKManager.init();

        LoginWindow loginWindow = new LoginWindow();

        loginWindow.addLoginListener((sessionKey) -> {
            User.sessionKey = sessionKey;
            new AdminWindow();
        });
    }
}
