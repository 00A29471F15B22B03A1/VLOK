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


public class ChatWindow {

    private Stage window;

    private TextArea chat;

    private String username;

    public ChatWindow() {
        createWindow();
        VLOKManager.client.addPacketHandler(new ChatPacketHandler());
    }

    public void show() {
        if (username == null) {
            username = Popup.input(Localization.get("ui.chat"), Localization.get("ui.q_username"));
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
        input.setPromptText(Localization.get("ui.s_type_here"));
        input.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                if (input.getText().isEmpty())
                    return;

                VLOKManager.sendChatMessage(username, input.getText().trim());
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

            String[] words = packet.message.split(" ");

            for (int i = 0; i < words.length; i++) {
                System.out.println(words[i]);
                if (words[i].contains("||")) {
                    String fileName = words[i].replaceAll("||", "");
                    //fileName is de naam van de file die door de command wordt gevraagd
                }

            }

            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            chat.appendText("[" + date + "] " + packet.username + ": " + packet.message + "\n");

        }
    }

}
