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

    private Stage dialog;

    public void show() {
        if (dialog == null)
            createDialog();
        dialog.show();
    }

    private void createDialog() {
        dialog = new Stage();
        dialog.setTitle(Localization.get("ui.settings"));
        dialog.initModality(Modality.APPLICATION_MODAL);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(8);
        layout.setHgap(10);

        Label createdByLabel = new Label(Localization.get("ui.created_by"));
        GridPane.setConstraints(createdByLabel, 0, 0);

        Label userName1 = new Label("someone");
        GridPane.setConstraints(userName1, 1, 0);

        Label userName2 = new Label("<\\hakPeer>");
        GridPane.setConstraints(userName2, 1, 1);

        Button closeButton = new Button(Localization.get("ui.close"));
        closeButton.setOnAction(event -> dialog.hide());
        GridPane.setConstraints(closeButton, 1, 2);

        layout.getChildren().addAll(createdByLabel, userName1, userName2, closeButton);

        Scene dialogScene = new Scene(layout, 155, 100);
        dialog.setScene(dialogScene);

        Localization.addLanguageListener(newLanguage -> createdByLabel.setText(Localization.get("ui.created_by")));
    }

}
