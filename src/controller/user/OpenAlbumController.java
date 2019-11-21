package controller.user;

import constants.Constants;
import controller.Photos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import model.PhotoEntry;
import model.Photo;

import utilities.Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller that handles album modifications
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class OpenAlbumController implements Initializable {
	@FXML
	TableView photoTable;
	@FXML
	private TableColumn imageColumn;
	@FXML
	private TableColumn captionColumn;
	@FXML
	private TableColumn dateTakenColumn;

	static PhotoEntry selectedEntry;
	static Photo selectedPhoto;
	static TableView staticPhotoTable;

	List<Photo> photos = new ArrayList<>();

	/**
	 * updates tableview whenever a modification is made to the album
	 */
	private void setPhotos() {

		photoTable.getItems().remove(0, photoTable.getItems().size());
		String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
		List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(albumPath);
		imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
		captionColumn.setCellValueFactory(new PropertyValueFactory<>("caption"));
		dateTakenColumn.setCellValueFactory(new PropertyValueFactory<>("dateTaken"));
		for (Photo p : photosInAlbum) {
			PhotoEntry photoEntry = new PhotoEntry(p.getLocation(), p.getCaption(), p.getDateTaken(), p);
			photoTable.getItems().add(photoEntry);
		}
		staticPhotoTable = photoTable;
	}

	/**
	 * Runs when controller is triggered
	 * @param location default param
	 * @param resources default param
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("There");
		setPhotos();
		System.out.println("There");
	}

	/**
	 * updates static selectedEntry and selecctedPhoto values whenever tableview is clicked
	 * @param mouseEvent response to mouse click
	 */
	@FXML
	public void updateSelected(MouseEvent mouseEvent) {
		PhotoEntry newSelectedEntry = (PhotoEntry) photoTable.getSelectionModel().getSelectedItem();
		if (newSelectedEntry != null) {
			selectedEntry = (PhotoEntry) photoTable.getSelectionModel().getSelectedItem();
			selectedPhoto = selectedEntry.getAssociatedPhoto();
		}
	}

	/**
	 * Adds photo to album
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void addPhoto(MouseEvent mouseEvent) {
		Utilities.displayView("user/AddPhotoView.fxml");
		// For some reason updateTableView() doesn't work here only

		// updateTableView();
		setPhotos();
		photoTable.refresh();
	}

	/**
	 * Removes photo from album
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void removePhoto(MouseEvent mouseEvent) {
		if (selectedEntry == null) Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else {
			photoTable.getItems().remove(selectedEntry);
			updateTableView();
		}
	}

	/**
	 * Updates tableview after modifying album
	 */
	private void updateTableView() {
		String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
		List<Photo> photosInAlbum = new ArrayList();
		for (Object item : photoTable.getItems()) {
			photosInAlbum.add(((PhotoEntry) item).getAssociatedPhoto());
		}
		Utilities.writeSerializedObjectToFile(photosInAlbum, albumPath);
		setPhotos();
		photoTable.refresh();
	}

	/**
	 * Modifies caption on image
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void modifyCaption(MouseEvent mouseEvent) {
		if (selectedEntry == null)
			Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Rename Caption" + selectedEntry.getCaption());
			dialog.setHeaderText("Rename " + selectedEntry.getCaption());
			dialog.setContentText("New Caption:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
//				String ALBUM_PATH_FORMAT = "src/resources/USERS/%s/%s.txt";
//
//				String albumPath = String.format(ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);

				selectedEntry.setCaption(result.get());
				updateTableView();
//				List<Photo> photosInAlbum = new ArrayList();
//
//				ObservableList items = photoTable.getItems();
//				for (Object item : items) {
//					photosInAlbum.add(((AlbumEntry) item).getAssociatedPhoto());
//				}
//				Utilities.writeSerializedObjectToFile(photosInAlbum, albumPath);
//				setPhotos();
//				photoTable.refresh();

			}
		}
	}

	/**
	 * Displays photo with details
	 * @param mouseEvent response to clicking button
	 * @throws IOException thrown if fxml view file can't be found
	 */
	@FXML
	public void displayPhoto(MouseEvent mouseEvent) throws IOException {
		if (selectedEntry == null)
			Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else
			Utilities.displayView("user/DisplayPhotoView.fxml");
	}


	/**
	 * Copies photo to new album
	 * @param title title of album
	 * @param header header
	 * @param text text
	 */
	private void copyPhoto(String title, String header, String text) {
		if (selectedEntry == null) Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle(title);
			dialog.setHeaderText(header);
			dialog.setContentText(text);
			Optional<String> results = dialog.showAndWait();
			if (results.isPresent()) {
				String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, results.get());
				List<Photo> photosInTargetAlbum = Utilities.readSerializedObjectFromFile(albumPath);
				photosInTargetAlbum.add(selectedPhoto);
				Utilities.writeSerializedObjectToFile(photosInTargetAlbum, albumPath);
			}
		}
	}

	/**
	 * Copies photo to new album
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void copyToNewAlbum(MouseEvent mouseEvent) {
		copyPhoto("Copy Photo", "Rename", "Album to copy to: ");
	}

	/**
	 * Copies  photo to new album and removes  from current
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void moveToNewAlbum(MouseEvent mouseEvent) {
		copyPhoto("Move photo", "Move", "Album to move to: ");
		photoTable.getItems().remove(selectedEntry);
		updateTableView();
	}

	/**
	 * Displays slideshow
	 * @param mouseEvent response to clicking button
	 */
	@FXML
	public void viewSlideshow(MouseEvent mouseEvent) {
		if (photoTable.getItems().size() == 0) Utilities.displayAlert(Alert.AlertType.ERROR, "No photos in album");
		else Utilities.displayView("user/ViewSlideshowView.fxml");
	}
}