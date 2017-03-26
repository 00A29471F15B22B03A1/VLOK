package core;

import core.localization.Language;
import core.localization.Localization;
import core.logging.Logger;
import core.ui.login.LoginWindow;
import core.ui.mainwindow.MainWindow;

import javax.swing.*;

public class ClientMain {

    public static void main(String[] args) {
        Localization.setLanguage(Language.English);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            Logger.err("Failed to change ui look and feel");
        }

        VLOKManager.init();

        LoginWindow loginWindow = new LoginWindow();

        loginWindow.addLoginListener(sessionKey -> {
            VLOKManager.setSessionKey(sessionKey);
            new MainWindow();
        });

    }

}