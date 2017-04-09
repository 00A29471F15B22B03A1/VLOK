package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.logging.Console;
import core.packets.UpdatePacket;
import core.ui.UpdateWindow;
import core.ui.login.LoginWindow;
import core.ui.main.MainWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static final float VERSION = 0.1f;
    public static String DOWNLOAD_URL = "";

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Login");

        LoginWindow loginWindow = new LoginWindow(primaryStage);

        loginWindow.addLoginListener((sessionKey) -> {
            CurrentUser.sessionKey = sessionKey;
            Platform.runLater(() -> primaryStage.setScene(new MainWindow(primaryStage).getScene()));
        });

        VLOKManager.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof UpdatePacket) {
                    DOWNLOAD_URL = ((UpdatePacket) o).downloadURL;
                    Platform.runLater(() -> primaryStage.setScene(new UpdateWindow(primaryStage).getScene()));
                }
            }
        });

        primaryStage.setOnCloseRequest(event -> Console.close());

        primaryStage.setScene(loginWindow.getScene());
        primaryStage.show();
        primaryStage.requestFocus();
    }

    public static void main(String[] args) {
        VLOKManager.init();


        launch(args);
    }

}