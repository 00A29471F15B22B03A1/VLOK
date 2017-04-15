package core.ui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class Popup {

    private static Dialog<Void> loadingDialog;

    public static void init(Stage primaryStage) {
        loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(primaryStage);//stage here is the stage of your webview
        loadingDialog.initStyle(StageStyle.TRANSPARENT);

        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = loadingDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        Label loader = new Label("LOADING");
        loader.setContentDisplay(ContentDisplay.BOTTOM);
        loader.setGraphic(new ProgressIndicator());
        loadingDialog.getDialogPane().setGraphic(loader);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3);
        ds.setOffsetY(1.3);
        ds.setColor(Color.DARKGRAY);
        loadingDialog.getDialogPane().setEffect(ds);
    }

    public static void enableLoading() {
        loadingDialog.show();
    }

    //TODO: fix
    public static void disableLoading() {
        loadingDialog.setResult(null);
        loadingDialog.close();
    }

    public static void alert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.showAndWait();
        });
    }

    public static void info(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.showAndWait();
        });
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.map(buttonType -> buttonType == ButtonType.OK).orElse(false);
    }

    public static String input(String title, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    public static String input(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

}
