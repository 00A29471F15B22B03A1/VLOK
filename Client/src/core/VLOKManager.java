package core;

import core.localization.Localization;
import core.logging.Console;
import core.packethandlers.ErrorPacketHandler;
import core.packets.LoginPacket;
import core.packets.RequestPacket;
import core.ui.Popup;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class VLOKManager {

    public static NetworkClient client;

    public static void init() {
        client = new NetworkClient("vlok.dynu.com");

        client.start();

        client.addPacketHandler(new FileTransferPacketHandler(Utils.getDownloadPath(), (file, fileInfo) -> Utils.selectFile(file.getPath())));

        client.addPacketHandler(new ErrorPacketHandler());

        Console.info("Initialized VLOK");
    }

    //TODO: fix localization
    public static void sendFile() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Localization.get("ui.choose_file"));
        File file = fileChooser.showOpenDialog(window);

        if (file == null)
            return;

        String name = Popup.input("File Name", "Give the file a name", file.getName());

        if (name.isEmpty()) {
            Popup.info("Canceling upload", "Canceling upload because there is no name");
            return;
        }

        String description = Popup.input("Give Description", "Give the file a description");

        if (Popup.confirm("Confirm?", "Filename: " + name + ", description: " + description)) {
            FileSender.sendFile(new FileInfo(name, description), file, CurrentUser.sessionKey, packet -> client.sendTCP(packet));
            Popup.info("File upload", "File upload completed!");
        }
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
