package controller.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import model.Photo;
import utilities.Utilities;

public class AddTagController  implements Initializable {
	@FXML
	ListView<String> valueList; 
	
	@FXML
	Label tagName; 
	
	String currentTag; 
	
	Photo currentPhoto; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if(OpenAlbumController.selectedPhoto == null || DisplayPhotoController.currentTag == null) {
			Utilities.displayAlert(AlertType.ERROR, "Error Displaying Tag Values");
		} else {
			this.currentPhoto = OpenAlbumController.selectedPhoto; 
			this.currentTag = DisplayPhotoController.currentTag; 
			this.tagName.setText(this.currentTag);
			System.out.println(OpenAlbumController.selectedPhoto.getTags().get(currentTag).size()); 
				valueList.setItems(FXCollections.observableList(new ArrayList(OpenAlbumController.selectedPhoto.getTags().get(currentTag))));
			
		}
		
		
	}
	
	public void addValue() {
		TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("Add a TagValue");
    	dialog.setHeaderText("Add a value for " + currentTag);
    	dialog.setContentText("New TagName:");

    	// Traditional way to get the response value.
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    		OpenAlbumController.selectedPhoto.addTag(currentTag, result.get());
			Utilities.updateSerilization(valueList, OpenAlbumController.selectedPhoto.getTags().get(currentTag), OpenAlbumController.staticPhotoTable.getItems(), OpenAlbumController.staticPhotoTable);  		
    	}
	}
	
	public void deleteValue() {
		String selectedTag = valueList.getSelectionModel().getSelectedItem();
        if (selectedTag == null) {
            Utilities.displayAlert(AlertType.ERROR, "No Value Selected");
        } else {
        	OpenAlbumController.selectedPhoto.deleteTagValue(currentTag, selectedTag);
			Utilities.updateSerilization(valueList, OpenAlbumController.selectedPhoto.getTags().get(currentTag), OpenAlbumController.staticPhotoTable.getItems(), OpenAlbumController.staticPhotoTable);  		

        }
		
	}

}
