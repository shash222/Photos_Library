package controller.user;

import controller.Photos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Photo;
import utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddPhotoController implements Initializable {
    @FXML
    private Label addPhotoLabel;
    @FXML
    private TextField photoPath;
    @FXML
    private TextField captionText;

    private static final String ADD_PHOTO_LABEL_FORMAT = "Add photo to your %s album";
    private static final String ALBUM_PATH_FORMAT = "src/resources/USERS/%s/%s.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addPhotoLabel.setText(String.format(ADD_PHOTO_LABEL_FORMAT, UserController.selectedAlbum));
    }

    @FXML
    public void addPhotoToAlbum(MouseEvent mouseEvent) {
        String photoPath = this.photoPath.getText();
        if (photoPath.isEmpty()) {
            Utilities.displayAlert(AlertType.ERROR, "No filepath selected");
        } else {
            File file = new File(photoPath);
            if (file.isFile()) {
                try {
                    String albumPath = String.format(ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MILLISECOND, 0);
                    Photo photo = new Photo(photoPath, (captionText.getText() == null) ? "" : captionText.getText(), new Date(file.lastModified()));
                    List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(albumPath);
                    photosInAlbum.add(photo);
                    Utilities.writeSerializedObjectToFile(photosInAlbum, albumPath);
                    Utilities.displayAlert(AlertType.CONFIRMATION, "User will be added after closing this box");
                } catch (Exception e) {
                    String msg = "Error writing to file";
                    throw new RuntimeException(msg, e);
                }
            } else {
                Utilities.displayAlert(AlertType.ERROR, "Photo not found");
            }
        }
    }

}
