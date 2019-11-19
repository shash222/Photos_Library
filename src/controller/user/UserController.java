package controller.user;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Photos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.Photo;
import utilities.Utilities;

public class UserController implements Initializable {
	@FXML
	private ListView<String> albumList;

	private String userPath = Utilities.getUserTxtPath(Photos.currentUser);  
	public static String selectedAlbum;

	public void initialize(URL location, ResourceBundle resources) {
		albumList.setItems(FXCollections.observableList(new ArrayList(Photos.users.get(Photos.currentUser).keySet())));
	}

	@FXML
	public void logout(MouseEvent mouseEvent) throws IOException {
		Utilities.logout();
	}

	public void readAlbums() throws IOException {
	}

	@FXML
	public void openSelectedAlbum(MouseEvent mouseEvent) throws IOException {
		if (selectedAlbum == null) {
			Utilities.displayAlert(AlertType.ERROR, "No Album selected");
		} else {
//			Stage createUserStage = new Stage();
//			Parent root = FXMLLoader.load(getClass().getResource("../../view/user/OpenAlbumView.fxml"));
//			createUserStage.setScene(new Scene(root, Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT));
//			createUserStage.showAndWait();
			Utilities.displayView("user/OpenAlbumView.fxml");
		}
	}

	@FXML
	public void setAlbumSelected(MouseEvent mouseEvent) {
		String selectedAlbum = albumList.getSelectionModel().getSelectedItem();
		if (selectedAlbum != null) {
			this.selectedAlbum = selectedAlbum;
		}
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
		Photos.users.get(Photos.currentUser).put(albumName, new HashSet<Photo>());
		String filePath = String.format("%s/%s.txt",  Utilities.getUserPath(Photos.currentUser), albumName);
		System.out.println("Filepath   " + filePath);
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
			deleteAlbumFile(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), selected));
			
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
				
				File oldFile = new File(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), selected));
				File newFile = new File(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), result.get()));
				if(oldFile.renameTo(newFile)) {
					System.out.println("Renamed Successfully."); 
				} else {
					System.out.println("Failed to rename."); 
				}
				HashSet<Photo> temp = Photos.users.get(Photos.currentUser).get(selected);
				Photos.users.get(Photos.currentUser).remove(selected); 
				Photos.users.get(Photos.currentUser).put(result.get(), temp); 
				Utilities.updateListView(albumList, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()), userPath);

				/*
				createAlbum(result.get()); 
				Utilities.transferAlbumContent(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), selected), String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), result.get()));
				
				// delete old file 
				Photos.users.get(Photos.currentUser).remove(selected);
				deleteAlbumFile(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), selected));
				*/ 
			}
		}
	}
}