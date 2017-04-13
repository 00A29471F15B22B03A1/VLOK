package core.ui.main;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.VLOKManager;
import core.localization.Localization;
import core.packets.DocumentationPacket;
import core.packets.Packet;
import core.packets.RequestPacket;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DocumentationWindow {

    private Stage dialog;

    private TabPane tabPane;

    public void show() {

        if (dialog == null)
            createWindow();
        dialog.show();
    }

    private void createWindow() {
        dialog = new Stage();
        dialog.setTitle(Localization.get("ui.documentation"));
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setWidth(500);
        dialog.setHeight(400);

        BorderPane layout = new BorderPane();
        tabPane = getTabPane();
        layout.setCenter(tabPane);

        Scene dialogScene = new Scene(layout, 300, 200);
        dialog.setScene(dialogScene);

        VLOKManager.client.addPacketHandler(new DocumentationPacketHandler());

        VLOKManager.sendRequest(RequestPacket.Type.DOCUMENTATION, "");
    }

    private TabPane getTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.TOP);

        return tabPane;
    }

    private Tab addTab(String name, String text) {
        Tab tab = new Tab(name);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));
        TextArea textArea = new TextArea(text);
        textArea.setEditable(false);
        borderPane.setCenter(textArea);

        tab.setContent(borderPane);
        return tab;
    }

    private class DocumentationPacketHandler extends PacketHandler {

        public DocumentationPacketHandler() {
            super(DocumentationPacket.class);
        }

        @Override
        public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
            DocumentationPacket packet = (DocumentationPacket) p;
            Platform.runLater(() -> tabPane.getTabs().add(addTab(packet.name, packet.text)));
        }
    }
}