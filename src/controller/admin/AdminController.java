package controller.admin;

import constants.Constants;
import controller.Photos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilities.Utilities;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private ListView<String> userList;

    public void initialize(URL location, ResourceBundle resources) {
        userList.setItems(FXCollections.observableList(new ArrayList(Photos.users.keySet())));
    }

    @FXML
    public void logout(MouseEvent mouseEvent) throws IOException {
        Utilities.logout();
    }

    // TODO: refactor to Utilities??? Change to displayView???
    @FXML
    public void displayCreateUserView(MouseEvent mouseEvent) throws IOException {
//        Stage createUserStage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("../../view/admin/CreateUserView.fxml"));
//        createUserStage.setScene(new Scene(root, Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT));
//        createUserStage.showAndWait();
        Utilities.displayView("admin/CreateUserView.fxml");
        Utilities.updateListView(userList, new ArrayList<>(Photos.users.keySet()), constants.Constants.USERS_FILE_PATH);
    }

    @FXML
    public void deleteUser(MouseEvent mouseEvent) {
        String selected = userList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Utilities.displayAlert(AlertType.ERROR, "No user selected");
        } else {
            // User's folder
            String folderPath = Utilities.getUserPath(selected);
        	Utilities.deleteFolder(folderPath);
        	/*
            HashMap<String, HashMap<String, HashSet<String>>> albums = Photos.users.get(selected);
            for(String album : albums.keySet()){
            	   Utilities.deleteFile(String.format("%s/%s.txt", Utilities.getAlbumPath(selected), album));
            }
            */ 
            Photos.users.remove(selected);
            Utilities.updateListView(userList, new ArrayList<>(Photos.users.keySet()), constants.Constants.USERS_FILE_PATH);
        }
    }
}