package core.ui.main;

import core.localization.Localization;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreditsWindow {

    private Stage window;

    public CreditsWindow() {
        createWindow();
    }

    public void show() {
        window.show();
    }

    private void createWindow() {
        window = new Stage();
        window.setTitle(Localization.get("ui.settings"));
        window.initModality(Modality.APPLICATION_MODAL);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(8);
        layout.setHgap(10);

        Label createdByLabel = new Label(Localization.get("ui.created_by"));
        GridPane.setConstraints(createdByLabel, 0, 0);

        Label userName1 = new Label("computr_kint;");
        GridPane.setConstraints(userName1, 1, 0);

        Label userName2 = new Label("<\\hakPeer>");
        GridPane.setConstraints(userName2, 1, 1);

        Button closeButton = new Button(Localization.get("ui.close"));
        closeButton.setOnAction(event -> window.hide());
        GridPane.setConstraints(closeButton, 1, 2);

        layout.getChildren().addAll(createdByLabel, userName1, userName2, closeButton);

        Scene dialogScene = new Scene(layout, 255, 100);
        window.setScene(dialogScene);

        Localization.addLanguageListener(newLanguage -> {
            window.setTitle(Localization.get("ui.settings"));
            createdByLabel.setText(Localization.get("ui.created_by"));
            closeButton.setText(Localization.get("ui.close"));
        });
    }

}
