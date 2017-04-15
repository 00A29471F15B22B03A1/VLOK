package core;

import core.localization.Localization;
import core.logging.Console;
import core.packetlisteners.ErrorPacketListener;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKField;
import core.serialization.VLOKObject;
import core.serialization.VLOKString;
import core.ui.Popup;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class VLOKManager {

    public static Client client;
    public static String sessionKey;

    public static void init() {
        client = new Client("localhost", 54555);

        client.start();

//        client.addPacketHandler(new FileTransferPacketHandler(Utils.getDownloadPath(), (file, fileInfo) -> Utils.selectFile(file.getPath())));

        client.addPacketListener(new ErrorPacketListener());

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
            // FIXME: 15/04/2017 send file
            //FileSender.sendFile(new FileInfo(name, description), file, sessionKey, db -> client.send(db));
            Popup.info(Localization.get("ui.upload"), Localization.get("ui.file_upload_complete"));
        }
    }

    public static void sendLogin(String key, String code, String os) {
        VLOKDatabase db = new VLOKDatabase("login");
        VLOKObject data = new VLOKObject("data");
        data.addField(VLOKField.Float("version", ClientMain.VERSION));
        data.addString(VLOKString.Create("key", key));
        data.addString(VLOKString.Create("code", Utils.hash(code)));
        db.addObject(data);
        client.send(db);
    }

    public static void sendRequest(byte type, String argument) {
        VLOKDatabase db = new VLOKDatabase("request");
        VLOKObject data = new VLOKObject("data");
        data.addField(VLOKField.Byte("type", type));
        data.addString(VLOKString.Create("sessionkey", sessionKey));
        data.addString(VLOKString.Create("argument", argument));
        db.addObject(data);
        client.send(db);
    }

    public static void sentChatMessage(String username, String text) {
        VLOKDatabase db = new VLOKDatabase("chat_message");
        VLOKObject data = new VLOKObject("data");
        data.addString(VLOKString.Create("username", username));
        data.addString(VLOKString.Create("text", text));
        db.addObject(data);
        client.send(db);
    }

}
