package core;

import core.logging.Console;
import core.packetlisteners.PacketListener;
import core.serialization.VLOKDatabase;
import core.ui.UpdateWindow;
import core.ui.login.LoginWindow;
import core.ui.main.MainWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static final float VERSION = 0.15f;
    public static String DOWNLOAD_URL = "";

    public static Image icon = new Image(Class.class.getResourceAsStream("/icon.png"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Login");

        LoginWindow loginWindow = new LoginWindow(primaryStage);

        loginWindow.addLoginListener((sessionKey) -> {
            VLOKManager.sessionKey = sessionKey;
            Platform.runLater(() -> primaryStage.setScene(new MainWindow(primaryStage).getScene()));
        });

        VLOKManager.client.addPacketListener(new PacketListener("update") {
            @Override
            public void packetReceived(VLOKDatabase db, Client c) {
                DOWNLOAD_URL = db.findObject("data").findString("url").getString();
                Platform.runLater(() -> primaryStage.setScene(new UpdateWindow(primaryStage).getScene()));
            }
        });

        primaryStage.setOnCloseRequest(event -> {
            Console.close();
            VLOKManager.client.stop();
        });

        primaryStage.setScene(loginWindow.getScene());
        primaryStage.show();
        primaryStage.getIcons().add(icon);
        primaryStage.requestFocus();
    }

    public static void main(String[] args) {
        VLOKManager.init();

        launch(args);
    }

}
