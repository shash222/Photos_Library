package controller.user;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import constants.Constants;
import controller.Photos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Photo;
import model.PhotoEntry;
import utilities.Utilities;

public class SearchResultsController implements Initializable {
	
	@FXML
	TableView resultsTable;
	@FXML
	private TableColumn imageColumn;
	@FXML
	private TableColumn captionColumn;
	@FXML
	private TableColumn dateTakenColumn;


	static TableView staticResultsTable;

	List<Photo> photosInAlbum = new ArrayList<>(); 


	private void setPhotos() {

		resultsTable.getItems().remove(0, resultsTable.getItems().size());
		if(SearchPhotoByTagController.tags) {
			photosInAlbum = (List<Photo>)SearchPhotoByTagController.finalList; 
		} else {
			photosInAlbum = SearchPhotoByDateController.searchResult;
			// SET photosInAlbum TO EQUAL THE LIST FROM SearchPhotoByDateController
		}
		imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
		captionColumn.setCellValueFactory(new PropertyValueFactory<>("caption"));
		dateTakenColumn.setCellValueFactory(new PropertyValueFactory<>("dateTaken"));
		for (Photo p : photosInAlbum) {
			PhotoEntry photoEntry = new PhotoEntry(p.getLocation(), p.getCaption(), p.getDateTaken(), p);
			resultsTable.getItems().add(photoEntry);
		}
		staticResultsTable = resultsTable;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setPhotos();
	}
	
	@FXML
	public void createAlbumDialog(MouseEvent mouseEvent) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Create Album With Search Results");
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
		HashSet<Photo> photosSet = new HashSet<Photo>(photosInAlbum); 
		Photos.users.get(Photos.currentUser).put(albumName, photosSet);
		String filePath = String.format("%s/%s.txt",  Utilities.getUserPath(Photos.currentUser), albumName);
	//	 String userAlbumListPath = String.format(Constants.USER_ALBUM_LIST_PATH_FORMAT, Photos.currentUser, Photos.currentUser);
		Utilities.createFile(filePath);
		Utilities.writeSerializedObjectToFile(photosInAlbum, filePath);
		System.out.println("Creating file");
	//	Utilities.writeToFile(userAlbumListPath, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()));
		Utilities.displayAlert(AlertType.CONFIRMATION, "Album will be added after closing this box");
		// UserController.updateTableView();
//		Utilities.updateListView(albumList, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()), userPath);
	}

}
