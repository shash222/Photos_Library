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

import constants.Constants;

// TODO Fix bug where list doesn't update unless this view is reopened
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("There");
		setPhotos();
		System.out.println("There");
	}

	@FXML
	public void updateSelected(MouseEvent mouseEvent) {
		PhotoEntry newSelectedEntry = (PhotoEntry) photoTable.getSelectionModel().getSelectedItem();
		if (newSelectedEntry != null) {
			selectedEntry = (PhotoEntry) photoTable.getSelectionModel().getSelectedItem();
			selectedPhoto = selectedEntry.getAssociatedPhoto();
		}
	}

	@FXML
	public void addPhoto(MouseEvent mouseEvent) {
		Utilities.displayView("user/AddPhotoView.fxml");
		// For some reason updateTableView() doesn't work here only
		// updateTableView();
		setPhotos();
		photoTable.refresh();
	}

	@FXML
	public void removePhoto(MouseEvent mouseEvent) {
		if (selectedEntry == null) Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else {
			photoTable.getItems().remove(selectedEntry);
			updateTableView();
		}
	}

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

	@FXML
	public void displayPhoto(MouseEvent mouseEvent) throws IOException {
		if (selectedEntry == null)
			Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else
			Utilities.displayView("user/DisplayPhotoView.fxml");
	}

	

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

	@FXML
	public void copyToNewAlbum(MouseEvent mouseEvent) {
		copyPhoto("Copy Photo", "Rename", "Album to copy to: ");
	}

	@FXML
	public void moveToNewAlbum(MouseEvent mouseEvent) {
		copyPhoto("Move photo", "Move", "Album to move to: ");
		photoTable.getItems().remove(selectedEntry);
		updateTableView();
	}

	@FXML
	public void viewSlideshow(MouseEvent mouseEvent) {
		if (photoTable.getItems().size() == 0) Utilities.displayAlert(Alert.AlertType.ERROR, "No photos in album");
		else Utilities.displayView("user/ViewSlideshowView.fxml");
	}
}