package controller.user;

import static constants.Constants.USERS_FILE_PATH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import constants.Constants;
import controller.Photos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilities.Utilities;

public class UserController implements Initializable {
	@FXML
	private ListView<String> albumList;

	private String userPath = "src/resources/USER" + Photos.currentUser + ".txt";

	public void initialize(URL location, ResourceBundle resources) {
		albumList.setItems(FXCollections.observableList(new ArrayList(Photos.users.get(Photos.currentUser).keySet())));

	}

	public void readAlbums() throws IOException {

	}

	public void createAlbumDialog(MouseEvent mouseEvent) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Create New Album");
		dialog.setHeaderText("Create Album");
		dialog.setContentText("Album Name: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if ((Photos.users.get(Photos.currentUser).containsKey(result.get()))) {
				Utilities.displayAlert(AlertType.ERROR, "Album already exists");
			} else {
				createAlbum(result.get());
			}
		}

	}

	private void createAlbum(String albumName) {
		System.out.println(albumName); 
		Photos.users.get(Photos.currentUser).put(albumName, new HashMap<String, HashSet<String>>()); 
		String filePath = Utilities.getAlbumPath(Photos.currentUser, albumName);
		Utilities.createFile(filePath);
		Utilities.displayAlert(AlertType.CONFIRMATION, "Album will be added after closing this box");
		Utilities.updateListView(albumList, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()), userPath);
	}

	@FXML
	public void deleteAlbumDialog(MouseEvent mouseEvent) {
		String selected = albumList.getSelectionModel().getSelectedItem();
		if (selected == null) {
			Utilities.displayAlert(AlertType.ERROR, "No Album selected");
		} else {
			Photos.users.get(Photos.currentUser).remove(selected);
			deleteAlbumFile(Utilities.getAlbumPath(Photos.currentUser, selected)); 
			
		}
	}
	
	private void deleteAlbumFile(String path) {
		Utilities.deleteFile(path);
		Utilities.updateListView(albumList, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()), userPath);
	}

	public void renameAlbum(MouseEvent mouseEvent) throws IOException {
		String selected = albumList.getSelectionModel().getSelectedItem();
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Rename Album");
		dialog.setHeaderText("Rename " + selected);
		dialog.setContentText("New Name: ");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// add new file
			if ((Photos.users.get(Photos.currentUser).containsKey(result.get()))) {
				
			} else {
				// add file and transfer info
				createAlbum(result.get()); 
				Utilities.transferAlbumContent(Utilities.getAlbumPath(Photos.currentUser, selected), Utilities.getAlbumPath(Photos.currentUser, result.get()));
				
				// delete old file 
				Photos.users.get(Photos.currentUser).remove(selected);
				deleteAlbumFile(Utilities.getAlbumPath(Photos.currentUser, selected)); 
			}
			
		}
	}

}