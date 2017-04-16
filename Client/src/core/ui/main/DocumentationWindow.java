package core.ui.main;

import core.Client;
import core.ClientMain;
import core.RequestType;
import core.VLOKManager;
import core.localization.Localization;
import core.packetlisteners.PacketListener;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKObject;
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

    private Stage window;

    private TabPane tabPane;

    public DocumentationWindow() {
        createWindow();
    }

    public void show() {
        window.show();
    }

    private void createWindow() {
        window = new Stage();
        window.setTitle(Localization.get("ui.documentation"));
        window.initModality(Modality.APPLICATION_MODAL);
        window.getIcons().add(ClientMain.icon);
        window.setWidth(500);
        window.setHeight(400);

        BorderPane layout = new BorderPane();
        tabPane = getTabPane();
        layout.setCenter(tabPane);

        Scene dialogScene = new Scene(layout, 300, 200);
        window.setScene(dialogScene);

        VLOKManager.client.addPacketListener(new DocumentationPacketHandler());

        VLOKManager.sendRequest(RequestType.DOCUMENTATION, "");
    }

    private TabPane getTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.TOP);

        return tabPane;
    }

    private Tab makeTab(String name, String text) {
        Tab tab = new Tab(name);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));
        TextArea textArea = new TextArea(text);
        textArea.setEditable(false);
        borderPane.setCenter(textArea);

        tab.setContent(borderPane);
        return tab;
    }

    private class DocumentationPacketHandler extends PacketListener {

        public DocumentationPacketHandler() {
            super("documentation");
        }

        @Override
        public void packetReceived(VLOKDatabase db, Client c) {
            VLOKObject dataObject = db.findObject("data");
            Platform.runLater(() -> tabPane.getTabs().add(makeTab(dataObject.findString("name").getString(), dataObject.findString("text").getString())));
        }
    }
}