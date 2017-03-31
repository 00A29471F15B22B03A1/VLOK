package core;

import core.ui.login.LoginWindow;
import core.ui.mainwindow.MainWindow;

public class ClientMain {

    public static void main(String[] args) {
        Utils.setNativeLookAndFeel();

        VLOKManager.init();

        LoginWindow loginWindow = new LoginWindow();

        loginWindow.addLoginListener((sessionKey) -> {
            User.sessionKey = sessionKey;
            new MainWindow();
        });

    }

}