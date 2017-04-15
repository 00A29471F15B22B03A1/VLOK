package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.localization.Localization;
import core.logging.Console;
import core.packethandlers.ErrorPacketHandler;
import core.packets.ChatMessagePacket;
import core.packets.FileStructurePacket;
import core.packets.LoginPacket;
import core.packets.RequestPacket;
import core.ui.Popup;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class VLOKManager {

    public static NetworkClient client;
    public static String sessionKey;
    public static FileStructure fileStructure;


    public static void init() {
        client = new NetworkClient("localhost");

        client.start();

        client.addPacketHandler(new FileTransferPacketHandler(Utils.getDownloadPath(), (file, fileInfo) -> Utils.selectFile(file.getPath())));

        client.addPacketHandler(new ErrorPacketHandler());

        VLOKManager.sendRequest(RequestPacket.Type.FILE_STRUCTURE, "");

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof FileStructurePacket) {
                    fileStructure = ((FileStructurePacket) o).fileStructure;
                }
            }
        });

        Console.info("Initialized VLOK");
    }

    public static void sendFile() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Localization.get("ui.choose_file"));
        File file = fileChooser.showOpenDialog(window);

        if (file == null)
            return;

        String name = Popup.input(Localization.get("ui.name"), Localization.get("ui.give_file_name"), file.getName());

        if (name.isEmpty()) {
            Popup.info(Localization.get("ui.canceling_upload"), Localization.get("ui.canceling_upload_no_name"));
            return;
        }

        String description = Popup.input(Localization.get("ui.description"), Localization.get("ui.give_file_description"));

        if (Popup.confirm(Localization.get("ui.confirm"), Localization.get("ui.name") + ": " + name + ", " + Localization.get("ui.description") + ": " + description)) {
            FileSender.sendFile(new FileInfo(name, description), file, sessionKey, packet -> client.sendTCP(packet));
            Popup.info(Localization.get("ui.upload"), Localization.get("ui.file_upload_complete"));
        }
    }

    public static void sendLogin(String key, String code, String os) {
        LoginPacket loginPacket = new LoginPacket(key + "¡" + Utils.hash(code) + "¡" + os, ClientMain.VERSION, "");

        client.sendTCP(loginPacket);
    }

    public static void sendRequest(RequestPacket.Type type, String argument) {
        RequestPacket requestPacket = new RequestPacket(sessionKey, type, argument);
        client.sendTCP(requestPacket);
    }

    public static void sendChatMessage(String username, String text) {
        ChatMessagePacket chatMessage = new ChatMessagePacket(text, VLOKManager.sessionKey, username);
        client.sendTCP(chatMessage);
    }

}
