package core.ui.login;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.Utils;
import core.VLOKManager;
import core.localization.Localization;
import core.logging.Logger;
import core.packets.LoginPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class LoginWindow extends JFrame {

    private static List<LoginListener> loginListeners = new ArrayList<>();

    public LoginWindow() {
        setSize(300, 130);
        setLocationRelativeTo(null);
        setTitle(Localization.getString("ui.login"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(3, 2, 10, 10));


        JTextField keyField = new JTextField(10);
        JLabel keyLabel = new JLabel(Localization.getString("ui.key"));
        keyLabel.setLabelFor(keyField);

        panel.add(keyLabel);
        panel.add(keyField);


        JPasswordField codeField = new JPasswordField(10);
        JLabel codeLabel = new JLabel(Localization.getString("ui.code"));
        codeLabel.setLabelFor(codeField);

        codeField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    VLOKManager.sendLogin(keyField.getText(), Utils.hash(codeField.getText()), getOS());
                }
            }
        });

        panel.add(codeLabel);
        panel.add(codeField);


        JButton continueButton = new JButton(Localization.getString("ui.continue"));
        panel.add(continueButton);

        VLOKManager.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof LoginPacket) {
                    LoginPacket packet = (LoginPacket) o;

                    if (packet.sessionKey == null || packet.sessionKey.isEmpty()) {
                        Logger.err("Failed to login");
                        codeField.setText("");
                        JOptionPane.showMessageDialog(null, Localization.getString("error.login"), Localization.getString("error.error"), JOptionPane.ERROR_MESSAGE);
                    } else {
                        Logger.info("Logged in");
                        fireLoginEvent(packet.sessionKey);
                        dispose();
                    }
                }
            }
        });

        continueButton.addActionListener(e -> {
            VLOKManager.sendLogin(keyField.getText(), Utils.hash(codeField.getText()), getOS());
        });

        add(panel);
        setVisible(true);
    }


    private void fireLoginEvent(String sessionKey) {
        for (LoginListener loginListener : loginListeners)
            loginListener.onLogin(sessionKey);
    }

    public void addLoginListener(LoginListener loginListener) {
        loginListeners.add(loginListener);
    }

    public void removeLoginListener(LoginListener loginListener) {
        loginListeners.add(loginListener);
    }

    public String getOS() {
        return System.getProperty("os.name");
    }

}
