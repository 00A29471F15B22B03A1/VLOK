package core.ui.main;

import core.ClientMain;
import core.Debug;
import core.localization.Language;
import core.localization.Localization;
import core.prefs.Prefs;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsWindow {

    private Stage window;

    public void show() {
        if (window == null)
            createWindow();
        window.show();
    }

    private void createWindow() {
        window = new Stage();
        window.setTitle(Localization.get("ui.settings"));
        window.getIcons().add(ClientMain.icon);
        window.initModality(Modality.APPLICATION_MODAL);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(8);
        layout.setHgap(10);

        Label languageLabel = new Label(Localization.get("ui.language") + ": ");
        GridPane.setConstraints(languageLabel, 0, 0);

        ComboBox<Language> languageCombo = new ComboBox<>(FXCollections.observableArrayList(Language.values()));
        languageCombo.getSelectionModel().select(Localization.getCurrentLanguage());
        GridPane.setConstraints(languageCombo, 1, 0);

        Label debugLabel = new Label(Localization.get("ui.debug"));
        GridPane.setConstraints(debugLabel, 0, 1);

        CheckBox debugCheck = new CheckBox();
        debugCheck.setSelected(Prefs.SETTINGS.getBoolean("debug"));
        GridPane.setConstraints(debugCheck, 1, 1);

        Button acceptButton = new Button(Localization.get("ui.accept"));
        acceptButton.setOnAction(event -> {
            Localization.setLanguage(languageCombo.getValue().languageCode);
            Debug.setDebug(debugCheck.isSelected());
            window.close();
        });

        GridPane.setConstraints(acceptButton, 0, 2);

        Button cancelButton = new Button(Localization.get("ui.cancel"));
        cancelButton.setOnAction(event -> window.close());
        GridPane.setConstraints(cancelButton, 1, 2);

        layout.getChildren().addAll(languageLabel, languageCombo, debugLabel, debugCheck, acceptButton, cancelButton);

        Scene dialogScene = new Scene(layout, 300, 200);
        window.setScene(dialogScene);

        Localization.addLanguageListener(newLanguage -> {
            languageLabel.setText(Localization.get("ui.language"));
            debugLabel.setText(Localization.get("ui.debug"));
            acceptButton.setText(Localization.get("ui.accept"));
            cancelButton.setText(Localization.get("ui.cancel"));
        });
    }

}