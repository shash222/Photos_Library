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

/**
 * Controller that handles displaying the photo and its details
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
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

    /**
     * Runs when class is triggered
     * @param location default param
     * @param resources default param
     */
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

    /**
     * Adds tag to photo
     * @param mouseEvent response to mouse click
     */
    public void addTagView(MouseEvent mouseEvent) {
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("Add a TagName");
    	dialog.setHeaderText("Add a TagName");
    	dialog.setContentText("New TagName:");

    	// Traditional way to get the response value.
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    		OpenAlbumController.selectedPhoto.addTag(result.get().toLowerCase());
    		Utilities.updateSerilization(tagList, OpenAlbumController.selectedPhoto.getTags().keySet(), OpenAlbumController.staticPhotoTable.getItems(), OpenAlbumController.staticPhotoTable);
    	}
    }

    /**
     * Deletes tag from photo
     * @param mouseEvent response to clicking button
     */
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

    /**
     * Edits tags in photo
     * @param mouseEvent response to clicking button
     */
    public void editTagView(MouseEvent mouseEvent) {
    	String selectedTag = tagList.getSelectionModel().getSelectedItem();
        currentTag = selectedTag; 
        if (selectedTag == null)
			Utilities.displayAlert(Alert.AlertType.ERROR, "No entry selected");
		else
			Utilities.displayView("user/AddTagView.fxml");
    }
    
    
}
