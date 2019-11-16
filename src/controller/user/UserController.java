package controller.user;

import static constants.Constants.USERS_FILE_PATH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
		albumList.setItems(FXCollections.observableList(new ArrayList(Photos.users.get(Photos.currentUser))));

	}

	public void readAlbums() throws IOException {

	}

	public void createAlbumDialog(MouseEvent mouseEvent) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Create New Album");
		dialog.setHeaderText("Create Album");
		dialog.setContentText("Album Name: ");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if ((Photos.users.get(Photos.currentUser).contains(result.get()))) {
				Utilities.displayAlert(AlertType.ERROR, "Album already exists");
			} else {
				createAlbum(result.get());
			}
		}

	}

	private void createAlbum(String albumName) {
		System.out.println(albumName); 
		Photos.users.get(Photos.currentUser).add(albumName);
		String filePath = "src/resources/ALBUM" + Photos.currentUser + albumName + ".txt";
		File file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utilities.displayAlert(AlertType.CONFIRMATION, "Album will be added after closing this box");
		Utilities.updateListView(albumList, new ArrayList<>(Photos.users.get(Photos.currentUser)), userPath);


	}

	@FXML
	public void deleteAlbumDialog(MouseEvent mouseEvent) {
		String selected = albumList.getSelectionModel().getSelectedItem();
		if (selected == null) {
			Utilities.displayAlert(AlertType.ERROR, "No Album selected");
		} else {
			Photos.users.get(Photos.currentUser).remove(selected);
			deleteAlbumFile("src/resources/ALBUM" + Photos.currentUser + selected + ".txt"); 
		}
	}
	
	private void deleteAlbumFile(String path) {
		File file = new File(path);
		System.out.println(path); 
		if(file.delete()) {
			System.out.println("file Deleted"); 
		} else {
			System.out.println("Failed to Deleted"); 

		}
		Utilities.updateListView(albumList, new ArrayList<>(Photos.users.get(Photos.currentUser)), userPath);
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
			if ((Photos.users.get(Photos.currentUser).contains(result.get()))) {
				
			} else {
				// add file and transfer info
				createAlbum(result.get()); 
				Utilities.transferAlbumContent("src/resources/ALBUM" + Photos.currentUser + selected + ".txt", "src/resources/ALBUM" + Photos.currentUser + result.get() + ".txt");
				// delete old file
				Photos.users.get(Photos.currentUser).remove(selected);
				deleteAlbumFile("src/resources/ALBUM" + Photos.currentUser + selected + ".txt"); // /Photos/src/resources/ALBUMuser1rest.txt
				// /Photos/src/resources/ALBUMuser1hi.txt
				// /Photos/src/resources/ALBUMuser1hi.txt
			}
			
		}
	}

}