package controller.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import model.Photo;
import utilities.Utilities;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayPhotoController implements Initializable {
    @FXML
    private Label captionField;
    @FXML
    private TextField dateTakenField;
    @FXML
    private ImageView imageContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Photo selectedPhoto = OpenAlbumController.selectedPhoto;
        if (selectedPhoto == null) {
            Utilities.displayAlert(AlertType.ERROR, "No image selected");
        } else {
            this.captionField.setText(selectedPhoto.getCaption());
            this.dateTakenField.setText(selectedPhoto.getDateTaken().toString());
            this.imageContainer.setImage(Utilities.getImage(selectedPhoto.getLocation()));
        }
    }
}
