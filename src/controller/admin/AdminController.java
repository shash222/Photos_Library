package controller.admin;

import controller.Photos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import utilities.Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for admin subsyste
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class AdminController implements Initializable {
    @FXML
    private ListView<String> userList;

    /**
     * Runs as soon as controller is triggered
     * @param location default parameter
     * @param resources default parameter
     */
    public void initialize(URL location, ResourceBundle resources) {
        userList.setItems(FXCollections.observableList(new ArrayList(Photos.users.keySet())));
    }

    /**
     * Logs admin out of program
     * @param mouseEvent response to clicking button
     * @throws IOException thrown in case fxml ui file isn't found
     */
    @FXML
    public void logout(MouseEvent mouseEvent) throws IOException {
        Utilities.logout();
    }

    /**
     * Displays create user view to create new user
     * @param mouseEvent response to clicking button
     */
    @FXML
    public void displayCreateUserView(MouseEvent mouseEvent) {
        Utilities.displayView("admin/CreateUserView.fxml");
        Utilities.updateListView(userList, new ArrayList<>(Photos.users.keySet()), constants.Constants.USERS_FILE_PATH);
    }

    /**
     * Deletes user's files
     * @param mouseEvent response to clicking button
     */
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