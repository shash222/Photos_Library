package controller.user;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

import utilities.Utilities;

/**
 * Class handling user subsystem functionality
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class UserController implements Initializable {
	public class AlbumEntry {
		private String albumName;
		private int numberOfPhotosInAlbum;
		private String dateRange;

		public AlbumEntry(String albumName, int numberOfPhotosInAlbum, Date earliestDate, Date latestDate) {
			this.albumName = albumName;
			this.numberOfPhotosInAlbum = numberOfPhotosInAlbum;
			if (earliestDate != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
				this.dateRange = String.format("%s - %s", dateFormat.format(earliestDate), dateFormat.format(latestDate));
			} else {
				this.dateRange = "No photos in album";
			}
		}

		public String getAlbumName() {
			return this.albumName;
		}

		public int getNumberOfPhotosInAlbum() {
			return this.numberOfPhotosInAlbum;
		}

		public String getDateRange() {
			return this.dateRange;
		}
	}

	@FXML
	private TableView albumsTable;
	@FXML
	private TableColumn albumNameColumn;
	@FXML
	private TableColumn numberOfPhotosColumn;
	@FXML
	private TableColumn dateRangeColumn;

	public static String selectedAlbum;
	public static AlbumEntry selectedAlbumEntry;

	/**
	 * Displays existing albums and details on view
	 */
	private void setup() {
		albumsTable.getItems().remove(0, albumsTable.getItems().size());

		albumNameColumn.setCellValueFactory(new PropertyValueFactory<>("albumName"));
		numberOfPhotosColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPhotosInAlbum"));
		dateRangeColumn.setCellValueFactory(new PropertyValueFactory<>("dateRange"));
		
		String user = Photos.currentUser;
		List<String> userAlbumsList = Utilities.getUserAlbums(String.format(Constants.USER_ALBUM_LIST_PATH_FORMAT, user, user));
		for (String s : userAlbumsList) {
			Date oldestDate = null;
			Date newestDate = null;
			List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(String.format(Constants.ALBUM_PATH_FORMAT, user, s));
			for (Photo p : photosInAlbum) {
				Date dateTaken = p.getDateTaken();
				if (oldestDate == null || oldestDate.compareTo(dateTaken) < 0) {
					oldestDate = dateTaken;
				}
				if (newestDate == null || newestDate.compareTo(dateTaken) > 0) {
					newestDate = dateTaken;
				}
			}
			AlbumEntry albumEntry = new AlbumEntry(s, photosInAlbum.size(), oldestDate, newestDate);
			albumsTable.getItems().add(albumEntry);
		}
//		albumList.setItems(FXCollections.observableList(new ArrayList(Photos.users.get(Photos.currentUser).keySet())));
	}

	/**
	 * Updates table anytime a change is made to an album
	 */
	private void updateTableView() {
		String userAlbumsFilePath = String.format(Constants.USER_ALBUM_LIST_PATH_FORMAT, Photos.currentUser, Photos.currentUser);
		Utilities.writeToFile(userAlbumsFilePath, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()));
		setup();
		albumsTable.refresh();
	}

	/**
	 * Runs as soon as controller is triggered, initializes album data in table view
	 * @param location required parameter
	 * @param resources required parameter
	 */
	public void initialize(URL location, ResourceBundle resources) {
		setup();
	}

	/**
	 * Logs out of account
	 * @param mouseEvent response to clicking button
	 * @throws IOException thrown if view couldn't be found
	 */
	@FXML
	public void logout(MouseEvent mouseEvent) throws IOException {
		Utilities.logout();
	}

	/**
	 * Opens new view with photos in album and allows modification of album
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void openSelectedAlbum(MouseEvent mouseEvent) {
		if (selectedAlbum == null) {
			Utilities.displayAlert(AlertType.ERROR, "No Album selected");
		} else {
//			Stage createUserStage = new Stage();
//			Parent root = FXMLLoader.load(getClass().getResource("../../view/user/OpenAlbumView.fxml"));
//			createUserStage.setScene(new Scene(root, Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT));
//			createUserStage.showAndWait();
			Utilities.displayView("user/OpenAlbumView.fxml");
			updateTableView();
		}
	}

	/**
	 * updates static albumSelected to be used in other classes when tableview is clicked
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void setAlbumSelected(MouseEvent mouseEvent) {
		selectedAlbumEntry = ((AlbumEntry) albumsTable.getSelectionModel().getSelectedItem());
		if (selectedAlbumEntry != null) {
			this.selectedAlbum = selectedAlbumEntry.getAlbumName();
		}
	}

	/**
	 * Displays searchPhotoByTag view
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void searchPhotoByTagView(MouseEvent mouseEvent) {
		Utilities.displayView("user/SearchPhotosByTag.fxml");
		updateTableView();
	}

	/**
	 * Displays search photo by date view
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void searchPhotoByDateView(MouseEvent mouseEvent) {
		Utilities.displayView("user/SearchPhotosByDate.fxml");
		updateTableView(); 

	}

	/**
	 * Displays create album dialog to take in user input and create album if one of the same name doesn't exist
	 * @param mouseEvent response to clicking button
	 * @throws IOException thrown in case view file isn't found
	 */
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

	/**
	 * Creates album  file and adds to list of albums to the user
	 * @param albumName Name of album to create
	 */
	private void createAlbum(String albumName) {
		Photos.users.get(Photos.currentUser).put(albumName, new HashSet<Photo>());
		String filePath = String.format("%s/%s.txt",  Utilities.getUserPath(Photos.currentUser), albumName);
		String userAlbumListPath = String.format(Constants.USER_ALBUM_LIST_PATH_FORMAT, Photos.currentUser, Photos.currentUser);
		Utilities.createFile(filePath);
		System.out.println("Creating file");
//		Utilities.writeToFile(userAlbumListPath, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()));
		Utilities.displayAlert(AlertType.CONFIRMATION, "Album will be added after closing this box");
		updateTableView();
//		Utilities.updateListView(albumList, new ArrayList<>(Photos.users.get(Photos.currentUser).keySet()), userPath);
	}

	/**
	 * Displays dialog that deletes the selected album
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void deleteAlbumDialog(MouseEvent mouseEvent) {
		if (selectedAlbum == null) {
			Utilities.displayAlert(AlertType.ERROR, "No Album selected");
		} else {
			Photos.users.get(Photos.currentUser).remove(selectedAlbum);
			albumsTable.getItems().remove(selectedAlbumEntry);
			deleteAlbumFile(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), selectedAlbum));
			
		}
	}

	/**
	 * Deletes album file from project files
	 * @param path path of file to delete
	 */
	private void deleteAlbumFile(String path) {
		Utilities.deleteFile(path);
		updateTableView();
	}

	/**
	 * Renames album filename and in list of user albums
	 * @param mouseEvent response to clicking button
	 * @throws IOException thrown in case fxml file isn't found
	 */
	public void renameAlbum(MouseEvent mouseEvent) throws IOException {
		if (selectedAlbumEntry == null) Utilities.displayAlert(AlertType.ERROR, "No Album selected");
		else {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Rename Album");
			dialog.setHeaderText("Rename " + selectedAlbum);
			dialog.setContentText("New Name: ");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				// add new file
				if ((Photos.users.get(Photos.currentUser).containsKey(result.get()))) {

				} else {
					// add file and transfer info

					File oldFile = new File(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), selectedAlbum));
					File newFile = new File(String.format("%s/%s.txt", Utilities.getUserPath(Photos.currentUser), result.get()));
					// newFile.createNewFile(); 
				    Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				    // oldFile.delete(); 
				    
					if (oldFile.delete()) {
						System.out.println("Renamed Successfully.");
					} else {
						System.out.println("Failed to rename.");
					}
					
					HashSet<Photo> temp = Photos.users.get(Photos.currentUser).get(selectedAlbum);
					Photos.users.get(Photos.currentUser).remove(selectedAlbum);
					Photos.users.get(Photos.currentUser).put(result.get(), temp);
					updateTableView();
				}
			}
		}
	}
}