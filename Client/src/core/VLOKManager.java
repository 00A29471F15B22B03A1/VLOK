package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.localization.Localization;
import core.logging.Console;
import core.packethandlers.ErrorPacketHandler;
import core.packethandlers.FileTransferPacketHandler;
import core.packets.LoginPacket;
import core.packets.RequestPacket;
import core.packets.UpdatePacket;
import core.ui.Popup;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;

public class VLOKManager {

    public static NetworkClient client;

    public static void init() {
        client = new NetworkClient("vlok.dynu.com");

        client.start();

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof UpdatePacket)
                    System.out.println("UPDATE");
            }
        });

        client.addPacketHandler(new FileTransferPacketHandler());
        client.addPacketHandler(new ErrorPacketHandler());

        Console.info("Initialized VLOK");
    }


    //TODO: fix localization
    public static void sendFile() {
        Platform.runLater(() -> {
            Stage window = new Stage();

            window.initModality(Modality.APPLICATION_MODAL);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Localization.get("ui.choose_file"));
            File file = fileChooser.showOpenDialog(window);

            if (file == null)
                return;

            String name = JOptionPane.showInputDialog("Give the file a name");

            if (name.isEmpty())
                return;

            String description = JOptionPane.showInputDialog("Give the file a description");

            int choice = JOptionPane.showConfirmDialog(null, "Filename: " + name + ", description: " + description);

            if (choice == JOptionPane.YES_OPTION) {
                FileSender.sendFile(name, description, file, CurrentUser.sessionKey, client);
                Popup.info("File upload", "File upload completed!");
            }
        });
    }

    public static void sendDownloadRequest(FileInfo fileInfo) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.sessionKey = CurrentUser.sessionKey;
        requestPacket.type = RequestPacket.Type.FILE_DOWNLOAD;
        requestPacket.argument = fileInfo.id + "";

        client.sendTCP(requestPacket);
        Console.info("Sent file download request");
    }

    public static void sendLogin(String key, String code, String os) {
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.fullKey = key + "¡" + Utils.hash(code) + "¡" + os;
        loginPacket.version = ClientMain.VERSION;

        client.sendTCP(loginPacket);
    }

    public static void sendRequest(RequestPacket.Type type, String argument) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.sessionKey = CurrentUser.sessionKey;
        requestPacket.type = type;
        requestPacket.argument = argument;
        client.sendTCP(requestPacket);
    }

}
