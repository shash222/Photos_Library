package controller;


import constants.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utilities.Utilities;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameInput;


    // TODO: Refactor into Utilities???
    private Scene createScene(String path, int width, int height) throws IOException {
        return new Scene(FXMLLoader.load(getClass().getResource(path)), width, height);
    }

    @FXML
    public void quitApp(MouseEvent mouseEvent) {
        Utilities.quitApp();
    }

    /**
     * Validates login if login meets one of the following criteria:
     *   - login equals "admin" -> replace stage with AdminView
     *   - login equals an entry in Photos.users -> replace stage with UserView
     * Otherwise login is unsuccessful, and user is alerted accordingly
     * @param mouseEvent mouseEvent
     * @throws IOException if resource cannot be loaded
     */
    @FXML
    public void validateLogin(MouseEvent mouseEvent) throws IOException {
        String usernameInputText = usernameInput.getText();
        boolean changeStage = false;
        Scene rootScene = Photos.primaryStage.getScene();
        Photos.primaryStage.setScene(null);
        if (usernameInputText.equalsIgnoreCase("admin")) {
            rootScene = createScene("../view/admin/AdminView.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
        } else if (Photos.users.contains(usernameInputText)) {
            rootScene = createScene("../view/user/UserView.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
        } else {
            Utilities.displayAlert(Alert.AlertType.ERROR, "Error: invalid login");
        }
        Photos.primaryStage.setScene(rootScene);
    }
}