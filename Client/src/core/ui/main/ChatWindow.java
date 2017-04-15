package core.ui.main;

import core.Client;
import core.ClientMain;
import core.packetlisteners.PacketListener;
import core.VLOKManager;
import core.localization.Localization;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKObject;
import core.ui.Popup;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

//TODO: fix localization
public class ChatWindow {

    private Stage window;

    private TextArea chat;

    String username;

    public ChatWindow() {
        createWindow();
        VLOKManager.client.addPacketListener(new ChatPacketHandler());
    }

    public void show() {
        if (username == null) {
            username = Popup.input("Chat", "Give a username please");
        }
        window.show();
    }


    private void createWindow() {
        window = new Stage();
        window.setTitle(Localization.get("ui.chat"));
        window.getIcons().add(ClientMain.icon);
        window.initModality(Modality.NONE);

        BorderPane layout = new BorderPane();

        chat = new TextArea();
        chat.setEditable(false);
        TextField input = new TextField();
        input.setPromptText("Type here to chat...");
        input.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                if (input.getText().isEmpty())
                    return;

                VLOKManager.sentChatMessage(username, input.getText().trim());
                input.setText("");
            }
        });

        layout.setCenter(chat);
        layout.setBottom(input);

        Scene scene = new Scene(layout, 500, 400);
        window.setScene(scene);

        input.requestFocus();

    }

    private class ChatPacketHandler extends PacketListener {

        public ChatPacketHandler() {
            super("chat_message");
        }

        @Override
        public void packetReceived(VLOKDatabase db, Client c) {
            VLOKObject dataObject = db.findObject("data");
            String username = dataObject.findString("username").getString();
            String text = dataObject.findString("text").getString();

            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            chat.appendText("[" + date + "] " + username + ": " + text + "\n");
        }

    }

}
