package controller.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Photo;
import model.PhotoEntry;
import utilities.Utilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import constants.Constants;
import controller.Photos;

public class DisplayPhotoController implements Initializable {
	
	@FXML
	ListView<String> tagList; 
    @FXML
    private Label captionField;
    @FXML
    private TextField dateTakenField;
    @FXML
    private ImageView imageContainer;
    
    public static String currentTag; 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (OpenAlbumController.selectedPhoto == null) {
            Utilities.displayAlert(AlertType.ERROR, "No image selected");
        } else {
        	tagList.setItems(FXCollections.observableList(new ArrayList(OpenAlbumController.selectedPhoto.getTags().keySet())));
            this.captionField.setText(OpenAlbumController.selectedPhoto.getCaption());
            this.dateTakenField.setText(OpenAlbumController.selectedPhoto.getDateTaken().toString());
            this.imageContainer.setImage(Utilities.getImage(OpenAlbumController.selectedPhoto.getLocation()));  
        }
    }
    
    private void updateSerilization() {
    	tagList.setItems(FXCollections.observableList(new ArrayList(OpenAlbumController.selectedPhoto.getTags().keySet())));
    	ObservableList items = OpenAlbumController.staticPhotoTable.getItems();
		List<Photo> photosInAlbum = new ArrayList();

		for (Object item : items) {
			photosInAlbum.add(((PhotoEntry) item).getAssociatedPhoto());
		}
		String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
		Utilities.writeSerializedObjectToFile(photosInAlbum, albumPath);
		OpenAlbumController.staticPhotoTable.refresh();
    }
    
    public void addTagView(MouseEvent mouseEvent) {
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("Add a TagName");
    	dialog.setHeaderText("Add a TagName");
    	dialog.setContentText("New TagName:");

    	// Traditional way to get the response value.
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    		OpenAlbumController.selectedPhoto.addTag(result.get());
    		Utilities.updateSerilization(tagList, OpenAlbumController.selectedPhoto.getTags().keySet(), OpenAlbumController.staticPhotoTable.getItems(), OpenAlbumController.staticPhotoTable);
    	}
    }
    
    public void deleteTag(MouseEvent mouseEvent) {
        String selectedTag = tagList.getSelectionModel().getSelectedItem();
        currentTag = selectedTag; 
        if (selectedTag == null) {
            Utilities.displayAlert(AlertType.ERROR, "No Tag Selected");
        } else {
        	OpenAlbumController.selectedPhoto.deleteTag(selectedTag);
    		Utilities.updateSerilization(tagList, OpenAlbumController.selectedPhoto.getTags().keySet(), OpenAlbumController.staticPhotoTable.getItems(), OpenAlbumController.staticPhotoTable);
        }
    }
    
    public void editTagView(MouseEvent mouseEvent) {
    	String selectedTag = tagList.getSelectionModel().getSelectedItem();
        currentTag = selectedTag; 
        if (selectedTag == null)
			Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else
			Utilities.displayView("user/AddTagView.fxml");
    }
    
    
}
