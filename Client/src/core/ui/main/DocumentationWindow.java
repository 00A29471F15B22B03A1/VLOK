package core.ui.main;

import core.localization.Localization;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DocumentationWindow {

    private Stage dialog;

    public void show() {
        if (dialog == null)
            createWindow();
        dialog.show();
    }

    private void createWindow() {
        dialog = new Stage();
        dialog.setTitle(Localization.get("ui.documentation"));
        dialog.initModality(Modality.APPLICATION_MODAL);

        BorderPane layout = new BorderPane();

        Label label = new Label("Not implemented yet");

        layout.setCenter(label);

        Scene dialogScene = new Scene(layout, 300, 200);
        dialog.setScene(dialogScene);
    }

}
