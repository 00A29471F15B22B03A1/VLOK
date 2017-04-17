package core.ui.main;

import com.esotericsoftware.kryonet.Connection;
import core.FileInfo;
import core.NetworkInterface;
import core.PacketHandler;
import core.VLOKManager;
import core.localization.Localization;
import core.packets.ChatLoginPacket;
import core.packets.ChatMessagePacket;
import core.packets.Packet;
import core.packets.RequestPacket;
import core.ui.Popup;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//TODO: Fix localization

public class ChatWindow {

    private Stage window;

    private TextArea chat;

    private TextArea online;

    private String username;

    public List<String> onlineU = new ArrayList<>();


    public ChatWindow() {
        createWindow();
        VLOKManager.client.addPacketHandler(new ChatPacketHandler());
        VLOKManager.client.addPacketHandler(new ChatLoginPacketHandler());
    }

    public void show() {
        if (username == null) {
            username = Popup.input(Localization.get("ui.chat"), Localization.get("ui.q_username"));
        }
        VLOKManager.sendChatLogin(username, true);
        window.show();
    }


    private void createWindow() {
        window = new Stage();
        window.setTitle(Localization.get("ui.chat"));
        window.initModality(Modality.NONE);

        BorderPane layout = new BorderPane();

        chat = new TextArea();
        chat.setEditable(false);

        online = new TextArea();
        online.setEditable(false);


        TextField input = new TextField();
        input.setPromptText(Localization.get("ui.s_type_here"));
        input.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String message = input.getText();
                int messageL = message.length();
                if (message.isEmpty())
                    return;

                if (messageL > 100) {
                    chat.appendText("[ERROR] Messages cant be bigger than 100 characters");
                    return;
                }

                VLOKManager.sendChatMessage(username, message.trim());
                input.setText("");
            }
        });


        layout.setCenter(chat);
        layout.setBottom(input);

        online.setPrefWidth(100);
        layout.setRight(online);
        online.appendText("[ONLINE] \n");

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

            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            chat.appendText("[" + date + "] " + packet.username + ": ");

            for (int i = 0; i < words.length; i++) {
                if (words[i].contains("%")) {
                    String fileId = words[i].replace("%", "");
                    int id = Integer.parseInt(fileId);
                    FileInfo fileInfo = VLOKManager.fileStructure.getFile(id);
                    if (fileInfo != null) {
                        chat.appendText("{" + fileInfo.name + "}");
                        boolean result = Popup.confirm("Chat: download", "Would you like to download " + fileInfo + "?");
                        if (result) {
                            VLOKManager.sendRequest(RequestPacket.Type.FILE_DOWNLOAD, fileId);
                        }
                    }

                } else {
                    chat.appendText(words[i]);
                }
                chat.appendText(" ");
            }
            chat.appendText("\n");
        }
    }

    private class ChatLoginPacketHandler extends PacketHandler {

        public ChatLoginPacketHandler() {
            super(ChatLoginPacket.class);
        }

        @Override
        public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
            ChatLoginPacket packet = (ChatLoginPacket) p;

            chat.appendText("--Welcome " + packet.username + " to the chatroom--");
            chat.appendText("\n");

            if (((ChatLoginPacket) p).onlineStatus) {
                onlineU.add(packet.username);
                writeOnline();
            } else {
                onlineU.remove(packet.username);
                writeOnline();
            }

        }

        private void writeOnline() {

            for (int i = 0; onlineU.size() > i; i++)
                online.appendText("-" + onlineU.get(i) + "\n");

        }

    }

}





