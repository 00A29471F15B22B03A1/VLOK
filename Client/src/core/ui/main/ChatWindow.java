package core.ui.main;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.VLOKManager;
import core.localization.Localization;
import core.packets.ChatMessagePacket;
import core.packets.Packet;
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
        VLOKManager.client.addPacketHandler(new ChatPacketHandler());
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

    private class ChatPacketHandler extends PacketHandler {

        public ChatPacketHandler() {
            super(ChatMessagePacket.class);
        }

        @Override
        public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
            ChatMessagePacket packet = (ChatMessagePacket) p;
            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            chat.appendText("[" + date + "] " + packet.username + ": " + packet.message + "\n");

        }
    }

}
