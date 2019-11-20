package controller.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.PhotoEntry;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewSlideshowController implements Initializable {
    @FXML
    private ImageView imageContainer;
    @FXML
    private Button previousImageButton;
    @FXML
    private Button nextImageButton;

    private Object[] entries;
    private int currentIndex = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entries = OpenAlbumController.staticPhotoTable.getItems().toArray();
        setImage(0);
    }

    @FXML
    public void nextImage(MouseEvent mouseEvent) {
        setImage(1);
    }

    @FXML
    public void previousImage(MouseEvent mouseEvent) {
        setImage(-1);
    }

    public void setImage(int incrementValue) {
        currentIndex += incrementValue;
        if (currentIndex == -1) currentIndex = entries.length - 1;
        if (currentIndex == entries.length) currentIndex = 0;
        imageContainer.setImage(((PhotoEntry)entries[currentIndex]).getImage().getImage());
    }
}
