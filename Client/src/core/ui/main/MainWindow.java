package core.ui.main;

import com.esotericsoftware.kryonet.Connection;
import core.*;
import core.localization.Localization;
import core.packets.FileStructurePacket;
import core.packets.Packet;
import core.packets.RequestPacket;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainWindow {

    Scene scene;

    FileInfo selectedFile;

    SettingsWindow settingsWindow;

    public MainWindow(Stage primaryStage) {
        settingsWindow = new SettingsWindow();
        scene = new Scene(createPane(), 600, 400);
        primaryStage.setTitle("");
    }

    private BorderPane createPane() {
        BorderPane borderPane = new BorderPane();

        //<editor-fold desc="FileInfo">
        GridPane infoPane = new GridPane();

        infoPane.setPadding(new Insets(10, 10, 10, 10));
        infoPane.setVgap(8);
        infoPane.setHgap(10);
        infoPane.setPrefWidth(250);

        Label fileNameLabel = new Label(Localization.get("ui.name") + ": ");
        GridPane.setConstraints(fileNameLabel, 0, 0);
        Label fileName = new Label();
        GridPane.setConstraints(fileName, 1, 0);

        Label fileDescriptionLabel = new Label(Localization.get("ui.description") + ": ");
        GridPane.setConstraints(fileDescriptionLabel, 0, 1);
        Label fileDescription = new Label();
        GridPane.setConstraints(fileDescription, 1, 1);

        Label filePathLabel = new Label(Localization.get("ui.path") + ": ");
        GridPane.setConstraints(filePathLabel, 0, 2);
        Label filePath = new Label();
        GridPane.setConstraints(filePath, 1, 2);

        Button downloadButton = new Button(Localization.get("ui.download"));
        downloadButton.setOnAction(event -> {
            if (selectedFile != null)
                VLOKManager.sendDownloadRequest(selectedFile);
        });
        GridPane.setConstraints(downloadButton, 0, 3);

        infoPane.getChildren().addAll(fileNameLabel, fileName, fileDescriptionLabel, fileDescription, filePathLabel, filePath, downloadButton);

        borderPane.setRight(infoPane);
        //</editor-fold>

        //<editor-fold desc="FileTree">
        TreeItem<FileInfo> root = new TreeItem<>();

        VLOKManager.sendRequest(RequestPacket.Type.FILE_STRUCTURE, "");

        VLOKManager.client.addPacketHandler(new PacketHandler(FileStructurePacket.class) {
            @Override
            public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
                if (p instanceof FileStructurePacket) {
                    FileStructure fileStructure = ((FileStructurePacket) p).fileStructure;
                    root.getChildren().clear();
                    for (FileInfo f : fileStructure.getFiles())
                        root.getChildren().add(new TreeItem<>(f));
                }
            }
        });

        TreeView<FileInfo> files = new TreeView<>(root);
        files.setShowRoot(false);
        files.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            FileInfo selectedInfo = newValue.getValue();
            fileName.setText(selectedInfo.name);
            fileDescription.setText(selectedInfo.description);
            filePath.setText(selectedInfo.path);
            selectedFile = selectedInfo;
        });

        borderPane.setCenter(files);
        //</editor-fold>

        //<editor-fold desc="MenuBar">
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("_" + Localization.get("ui.file"));

        MenuItem uploadItem = new MenuItem(Localization.get("ui.upload"));
        uploadItem.setOnAction(event -> VLOKManager.sendFile());

        MenuItem settingsItem = new MenuItem(Localization.get("ui.settings"));
        settingsItem.setOnAction(event -> settingsWindow.show());

        MenuItem creditsItem = new MenuItem(Localization.get("ui.credits"));
        CreditsWindow creditsWindow = new CreditsWindow();
        creditsItem.setOnAction(event -> creditsWindow.show());

        MenuItem exitItem = new MenuItem(Localization.get("ui.exit"));
        exitItem.setOnAction(event -> System.exit(0));

        fileMenu.getItems().addAll(uploadItem, settingsItem, creditsItem, exitItem);

        Menu helpMenu = new Menu("_" + Localization.get("ui.help"));

        MenuItem documentationItem = new MenuItem(Localization.get("ui.documentation"));
        DocumentationWindow documentationWindow = new DocumentationWindow();
        documentationItem.setOnAction(event -> documentationWindow.show());

        helpMenu.getItems().addAll(documentationItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        borderPane.setTop(menuBar);
        //</editor-fold>

        Localization.addLanguageListener(newLanguage -> {
            fileNameLabel.setText(Localization.get("ui.name"));
            fileDescriptionLabel.setText(Localization.get("ui.description"));
            filePathLabel.setText(Localization.get("ui.path"));
            downloadButton.setText(Localization.get("ui.download"));
            fileMenu.setText("_" + Localization.get("ui.file"));
            settingsItem.setText(Localization.get("ui.settings"));
            exitItem.setText(Localization.get("ui.exit"));
        });

        return borderPane;
    }

    public Scene getScene() {
        return scene;
    }
}