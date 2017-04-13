package core.ui.login;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.VLOKManager;
import core.localization.Localization;
import core.logging.Console;
import core.packets.LoginPacket;
import core.prefs.Prefs;
import core.prefs.PrefsValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LoginWindow {

    private Scene scene;

    private List<LoginListener> loginListeners = new ArrayList<>();

    public LoginWindow(Stage primaryStage) {
        scene = new Scene(createPane(), 215, 115);
        primaryStage.setTitle(Localization.get("ui.login"));
    }

    private GridPane createPane() {
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        Label keyLabel = new Label(Localization.get("ui.key"));
        GridPane.setConstraints(keyLabel, 0, 0);

        TextField keyField = new TextField(Prefs.SETTINGS.doesValueExist("last_key") ? Prefs.SETTINGS.getString("last_key") : "");
        GridPane.setConstraints(keyField, 1, 0);

        Label codeLabel = new Label(Localization.get("ui.code"));
        GridPane.setConstraints(keyLabel, 0, 1);

        PasswordField codeField = new PasswordField();
        codeField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
                login(keyField, codeField);
        });
        GridPane.setConstraints(codeField, 1, 1);

        Button loginButton = new Button(Localization.get("ui.login"));
        GridPane.setConstraints(loginButton, 1, 2);
        loginButton.setOnAction(event -> login(keyField, codeField));

        gridPane.getChildren().addAll(keyLabel, keyField, codeLabel, codeField, loginButton);

        VLOKManager.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof LoginPacket) {
                    LoginPacket packet = (LoginPacket) o;

                    if (packet.sessionKey != null && !packet.sessionKey.isEmpty()) {
                        Console.info("Logged in");
                        fireLoginEvent(packet.sessionKey);
                    }
                }
            }
        });

        return gridPane;
    }

    private void login(TextField keyField, TextField codeField) {
        VLOKManager.sendLogin(keyField.getText(), codeField.getText(), System.getProperty("os.name"));
        Prefs.SETTINGS.updateValue("last_key", keyField.getText(), PrefsValue.Type.STRING);
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

    public Scene getScene() {
        return scene;
    }
}
