package controller.user;

import controller.Photos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import model.AlbumEntry;
import model.Photo;

import utilities.Utilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    static AlbumEntry selectedEntry;
    static Photo selectedPhoto;
    static TableView staticPhotoTable;

    List<Photo> photos = new ArrayList<>();
    public static final String ALBUM_PATH_FORMAT = "src/resources/USERS/%s/%s.txt";

    private void setPhotos() {
        String albumPath = String.format(ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
        List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(albumPath);
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        captionColumn.setCellValueFactory(new PropertyValueFactory<>("caption"));
        dateTakenColumn.setCellValueFactory(new PropertyValueFactory<>("dateTaken"));
        for (Photo p : photosInAlbum) {
            AlbumEntry albumEntry = new AlbumEntry(p.getLocation(), p.getCaption(), p.getDateTaken(), p);
            photoTable.getItems().add(albumEntry);
        }
        staticPhotoTable = photoTable;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPhotos();
    }

    @FXML
    public void updateSelected(MouseEvent mouseEvent) {
        AlbumEntry newSelectedEntry = (AlbumEntry) photoTable.getSelectionModel().getSelectedItem();
        if (newSelectedEntry != null) {
            selectedEntry = (AlbumEntry) photoTable.getSelectionModel().getSelectedItem();
            selectedPhoto = selectedEntry.getAssociatedPhoto();
        }
    }

    @FXML
    public void addPhoto(MouseEvent mouseEvent) {
        Utilities.displayView("user/AddPhotoView.fxml");
        photoTable.refresh();
    }

    @FXML
    public void removePhoto(MouseEvent mouseEvent) {

    }

    // TODO doesn't work, finish
    @FXML
    public void modifyCaption(MouseEvent mouseEvent) {
        if (selectedEntry == null) Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
        else Utilities.displayView("user/ModifyCaptionView.fxml");
    }

    @FXML
    public void displayPhoto(MouseEvent mouseEvent) {
        if (selectedEntry == null) Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
        else Utilities.displayView("user/DisplayPhotoView.fxml");
    }

    @FXML
    public void addTag(MouseEvent mouseEvent) {

    }

    @FXML
    public void removeTag(MouseEvent mouseEvent) {

    }

    @FXML
    public void copyToNewAlbum(MouseEvent mouseEvent) {

    }

    @FXML
    public void moveToNewAlbum(MouseEvent mouseEvent) {

    }

    @FXML
    public void viewSlideshow(MouseEvent mouseEvent) {
        Utilities.displayView("user/ViewSlideshowView.fxml");
    }

}
